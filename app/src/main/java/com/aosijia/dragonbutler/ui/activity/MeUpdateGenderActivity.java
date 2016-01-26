package com.aosijia.dragonbutler.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.model.BaseResp;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.upload.UploadUtils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.Map;

/**
 * Created by wanglj on 16/1/6.
 */
public class MeUpdateGenderActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout manLayout;
    private RadioButton manRadio;
    private RelativeLayout womenLayout;
    private RadioButton womenRadio;
    private RelativeLayout secretLayout;
    private RadioButton secretRadio;
    private  String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_me_update_gender);
        gender = getIntent().getStringExtra("gender");
        setTitle("修改性别", null, R.drawable.btn_back, "确认", NO_RES_ID);
        setBtnLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setBtnRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (manRadio.isChecked()) {
                    gender = "1";
                } else if (womenRadio.isChecked()) {
                    gender = "0";
                } else if (secretRadio.isChecked()) {
                    gender = "2";
                }

                final LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
                String avatarUrl = loginResp.getData().getUser_info().getAvatar_url();
                String accessToken = loginResp.getData().getAccess_token();
                String nickName = loginResp.getData().getUser_info().getNickname();
                Map<String, String> parameter = RequestParameters.userInfoUpdate(accessToken, avatarUrl, nickName, gender);
                new OkHttpRequest.Builder().url(URLManager.USER_INFO_UPDATE).params(parameter).tag(this).post(new ResultCallback<BaseResp>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        dismissProgressDialog();
                        showRequestError();
                    }

                    @Override
                    public void onBefore(Request request) {
                        showProgressDialow("正在修改...");
                    }

                    @Override
                    public void onAfter() {
                        dismissProgressDialog();
                    }

                    @Override
                    public void onResponse(BaseResp response) {

                        if (response.isSuccess(MeUpdateGenderActivity.this)) {
                            //更新本地用户信息
                            loginResp.getData().getUser_info().setGender(gender);
                            Share.putObject(Share.LOGIN_RESP, loginResp);
                            Toast.makeText(MeUpdateGenderActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                            dismissProgressDialog();
                            finish();
                        } else {
                            dismissProgressDialog();
                            Toast.makeText(MeUpdateGenderActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }
        });

        manLayout = (RelativeLayout) findViewById(R.id.manLayout);
        manLayout.setOnClickListener(this);
        manRadio = (RadioButton) findViewById(R.id.manImage);
        womenLayout = (RelativeLayout) findViewById(R.id.womenLayout);
        womenLayout.setOnClickListener(this);
        womenRadio = (RadioButton) findViewById(R.id.womenImage);
        secretLayout = (RelativeLayout) findViewById(R.id.secretLayout);
        secretRadio = (RadioButton) findViewById(R.id.secretImage);
        secretLayout.setOnClickListener(this);


        if("0".equals(gender)){
            womenRadio.setChecked(true);
        }else if("1".equals(gender)){
            manRadio.setChecked(true);
        }else{
            secretRadio.setChecked(true);
        }


        secretRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    manRadio.setChecked(false);
                    womenRadio.setChecked(false);
                }
            }
        });
        manRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    secretRadio.setChecked(false);
                    womenRadio.setChecked(false);
                }
            }
        });
        womenRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    manRadio.setChecked(false);
                    secretRadio.setChecked(false);
                }
            }
        });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.manLayout:
                manRadio.setChecked(true);
                break;
            case R.id.womenLayout:
                womenRadio.setChecked(true);
                break;
            case R.id.secretLayout:
                secretRadio.setChecked(true);
                break;
        }
    }
}
