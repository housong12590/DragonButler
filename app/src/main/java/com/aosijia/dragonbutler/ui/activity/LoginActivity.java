package com.aosijia.dragonbutler.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.model.BaseResp;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.utils.TimeCount;
import com.aosijia.dragonbutler.utils.Uiutils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 登录
 * Created by wanglj on 15/12/15.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private TextView getCodeTextView;
    private TimeCount mTimeCount;



//    public static long getCodeTime = 0;
    private String className;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_login);
        setTitle("登录", null, NO_RES_ID, "取消", NO_RES_ID);
        getCodeTextView = (TextView) findViewById(R.id.getCodeTextView);
        getCodeTextView.setOnClickListener(this);
        findViewById(R.id.loginButton).setOnClickListener(this);
        mTimeCount = new TimeCount(getCodeTextView, 60000, 1000);
        if(getIntent() != null && getIntent().hasExtra("action")){
            className = getIntent().getStringExtra("action");
        }


        setBtnRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_from_top, R.anim.slide_out_to_top);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private EditText getMobileEditText(){
        return (EditText) findViewById(R.id.mobileEditText);
    }

    private EditText getCodeEditText(){
        return (EditText) findViewById(R.id.codeEditText);
    }


    @Override
    public void onClick(View view) {
        String mobile =getMobileEditText().getText().toString();
        switch (view.getId()) {
            case R.id.loginButton:

                String verificationCode = getCodeEditText().getText().toString();
                if(mobile.length() == 0) {
                    Toast.makeText(this, R.string.check_mobile_is_not_null, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!Uiutils.isMobile(mobile)){
                    Toast.makeText(this, R.string.check_please_input_11_number, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(verificationCode.length() == 0){
                    Toast.makeText(this, R.string.check_smscode_not_null, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!Uiutils.isSmsCode(verificationCode)){
                    Toast.makeText(this, R.string.check_please_input_6_number, Toast.LENGTH_SHORT).show();
                    return;
                }

                login(mobile, verificationCode);

                break;
            //获取验证码
            case R.id.getCodeTextView:

                if(mobile.length() == 0) {
                    Toast.makeText(this, R.string.check_mobile_is_not_null, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!Uiutils.isMobile(mobile)){
                    Toast.makeText(this, R.string.check_please_input_11_number, Toast.LENGTH_SHORT).show();
                    return;
                }

                verificationCode(mobile);


                break;
        }
    }

    /**
     * 登录 手机号
     * @param verificationCode 验证码
     */
    private void login(String mobile,String verificationCode){
        Map<String,String> parameter = RequestParameters.login(mobile, verificationCode);
        new OkHttpRequest.Builder().url(URLManager.USER_LOGIN).params(parameter).tag(this).post(new ResultCallback<LoginResp>() {
            @Override
            public void onError(Request request, Exception e) {
                dismissProgressDialog();
                showRequestError();
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                showProgressDialow("正在登陆...");
            }

            @Override
            public void onAfter() {
                super.onAfter();
                dismissProgressDialog();
            }

            @Override
            public void onResponse(LoginResp loginResp) {
                if (loginResp.isSuccess(LoginActivity.this)) {
                    //登录成功
//                    Toast.makeText(LoginActivity.this, loginResp.getData().getUser_id(), Toast.LENGTH_SHORT).show();
                    Share.putObject(Share.LOGIN_RESP, loginResp);
                    Share.putLong(Share.LOGIN_TIME, (System.currentTimeMillis() / 100));
                    //设置推送别名
                    JPushInterface.setAlias(LoginActivity.this, loginResp.getData().getUser_id(), new TagAliasCallback() {
                        @Override
                        public void gotResult(int i, String s, Set<String> set) {

                        }
                    });
                    //检查是否设置过用户信息
                    if (!loginResp.getData().getUser_info().isIs_bound()) {
                        Intent intent = new Intent(LoginActivity.this, HouseHoldBindActivity.class);
                        if (className != null) {
                            intent.putExtra("action", className);
                            if (className.equals("tab")) {
                                intent.putExtra("index", getIntent().getExtras().getInt("index"));
                            }
                            startActivity(intent);
                        }
                        finish();
                    } else {
                        // TODO: 15/12/16
                        Intent it = new Intent();
                        if (className != null) {
                            if (className.equals("tab")) {
                                it.setClassName(getApplicationContext(), MainTabActivity.class.getName());
                                it.putExtra("index", getIntent().getExtras().getInt("index"));
                                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(it);
                            } else {
                                it.setClassName(getApplicationContext(), className);
                                startActivity(it);
                            }
                        }
                        finish();
//                        startActivity(new Intent(LoginActivity.this, HouseHoldBindActivity.class));
                    }
                } else {
                    Toast.makeText(LoginActivity.this, loginResp.getMsg(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }



    /**
     * 获取验证码
     * @param mobile
     */
    private void verificationCode(String mobile){
//        getCodeTime = System.currentTimeMillis();
//        Toast.makeText(LoginActivity.this, "验证码发送成功，请查看短信", Toast.LENGTH_SHORT).show();
//        mTimeCount.start();
        Map<String,String> parameter = RequestParameters.verificationCode(mobile);
        new OkHttpRequest.Builder().url(URLManager.VERIFICATION_CODE).params(parameter).tag(this).post(new ResultCallback<BaseResp>() {
            @Override
            public void onError(Request request, Exception e) {
                dismissProgressDialog();
                showRequestError();
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                showProgressDialow("正在获取验证码...");
            }

            @Override
            public void onAfter() {
                super.onAfter();
                dismissProgressDialog();
            }

            @Override
            public void onResponse(BaseResp response) {
                if (response.isSuccess(LoginActivity.this)) {
//                    getCodeTime = System.currentTimeMillis();
                    Toast.makeText(LoginActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                    mTimeCount.start();
                } else {
                    Toast.makeText(LoginActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
