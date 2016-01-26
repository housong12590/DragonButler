package com.aosijia.dragonbutler.ui.activity;

import android.app.Activity;
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

/**
 * 更换手机号第一步
 * Created by wanglj on 16/1/15.
 */
public class UpdateUserMobile1Activity extends BaseActivity implements View.OnClickListener{
    private TextView getCodeTextView,mobileTextView;
    private TimeCount mTimeCount;
    private LoginResp loginResp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();

        setContentView(R.layout.activity_update_user_mobile_1);
        setTitle("更换手机号码", null, NO_RES_ID);

        loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);

        getCodeTextView = (TextView) findViewById(R.id.getCodeTextView);

        mobileTextView = (TextView)findViewById(R.id.mobileTextView);
        String mobile = loginResp.getData().getUser_info().getMobile();
        //隐藏中间4位
        if (Uiutils.isMobile(mobile)) {
            mobile = mobile.replace(mobile.substring(3, 7), "****");
        }
        mobileTextView.setText(mobile);
        getCodeTextView.setOnClickListener(this);
        findViewById(R.id.nextButton).setOnClickListener(this);
        setBtnLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTimeCount = new TimeCount(getCodeTextView, 60000, 1000);
    }


    private EditText getCodeEditText(){
        return (EditText) findViewById(R.id.codeEditText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nextButton:
                String verificationCode = getCodeEditText().getText().toString();
                if(verificationCode.length() == 0){
                    Toast.makeText(this, R.string.check_smscode_not_null, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!Uiutils.isSmsCode(verificationCode)){
                    Toast.makeText(this, R.string.check_please_input_6_number, Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(this,UpdateUserMobile2Activity.class);
                intent.putExtra("oldVerificationCode",verificationCode);
                intent.putExtra("oldMobile",loginResp.getData().getUser_info().getMobile());
                startActivityForResult(intent,1);
                break;
            case R.id.getCodeTextView:

                String mobile = mobileTextView.getText().toString();
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
     * 获取验证码
     * @param mobile
     */
    private void verificationCode(String mobile){
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
                if (response.isSuccess(UpdateUserMobile1Activity.this)) {
                    Toast.makeText(UpdateUserMobile1Activity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                    mTimeCount.start();
                } else {
                    Toast.makeText(UpdateUserMobile1Activity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            finish();
        }

    }
}
