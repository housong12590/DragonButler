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
 * 更换手机号码第二步
 * <p/>
 * Created by wanglj on 16/1/15.
 */
public class UpdateUserMobile2Activity extends BaseActivity implements View.OnClickListener {

    private TextView getCodeTextView;
    private TimeCount mTimeCount;
    private String oldVerificationCode;
    private String oldMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_update_user_mobile_2);
        setTitle("更换手机号码", null, NO_RES_ID);

        oldVerificationCode = getIntent().getStringExtra("oldVerificationCode");
        oldMobile = getIntent().getStringExtra("oldMobile");

        getCodeTextView = (TextView) findViewById(R.id.getCodeTextView);
        findViewById(R.id.updateButton).setOnClickListener(this);
        getCodeTextView.setOnClickListener(this);

        mTimeCount = new TimeCount(getCodeTextView, 60000, 1000);


    }

    private EditText getMobileEditText() {
        return (EditText) findViewById(R.id.mobileEditText);
    }

    private EditText getCodeEditText() {
        return (EditText) findViewById(R.id.codeEditText);
    }


    @Override
    public void onClick(View view) {
        String mobile = getMobileEditText().getText().toString();
        switch (view.getId()) {
            case R.id.updateButton:

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

                updateUserInfo(oldMobile,oldVerificationCode,mobile,verificationCode);

                break;
            case R.id.getCodeTextView:

                if (mobile.length() == 0) {
                    Toast.makeText(this, R.string.check_mobile_is_not_null, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Uiutils.isMobile(mobile)) {
                    Toast.makeText(this, R.string.check_please_input_11_number, Toast.LENGTH_SHORT).show();
                    return;
                }
                verificationCode(mobile);
                break;
        }
    }

    private void updateUserInfo(String oldMobile,String oldVericode,String newMobile,String newVericode){
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        Map<String, String> parameter = RequestParameters.updateUserMobile(loginResp.getData().getAccess_token(),oldMobile,oldVericode,newMobile,newVericode);
        new OkHttpRequest.Builder().url(URLManager.USER_MOBILE_UPDATE).params(parameter).tag(this).post(new ResultCallback<LoginResp>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(LoginResp loginResp) {
                if(loginResp.isSuccess(UpdateUserMobile2Activity.this)){
                    //登录成功
//                    Toast.makeText(LoginActivity.this, loginResp.getData().getUser_id(), Toast.LENGTH_SHORT).show();
                    Share.putObject(Share.LOGIN_RESP, loginResp);
                    Share.putLong(Share.LOGIN_TIME, (System.currentTimeMillis() / 100));
                    //设置推送别名
                    JPushInterface.setAlias(UpdateUserMobile2Activity.this, loginResp.getData().getUser_id(), new TagAliasCallback() {
                        @Override
                        public void gotResult(int i, String s, Set<String> set) {

                        }
                    });
                    //检查是否设置过用户信息
                    if (!loginResp.getData().getUser_info().isIs_bound()) {
                        Intent intent = new Intent(UpdateUserMobile2Activity.this, HouseHoldBindActivity.class);
                        startActivity(intent);
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        setResult(RESULT_OK);
                        finish();
                    }

                }
                Toast.makeText(UpdateUserMobile2Activity.this, loginResp.getMsg(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    /**
     * 获取验证码
     *
     * @param mobile
     */
    private void verificationCode(String mobile) {
        Map<String, String> parameter = RequestParameters.verificationCode(mobile);
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
                if (response.isSuccess(UpdateUserMobile2Activity.this)) {
                    Toast.makeText(UpdateUserMobile2Activity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                    mTimeCount.start();
                } else {
                    Toast.makeText(UpdateUserMobile2Activity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
