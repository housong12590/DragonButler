package com.aosijia.dragonbutler.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.model.BaseResp;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.widget.ClearEditText;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.Map;

/**
 * Created by wanglj on 16/1/6.
 */
public class MeUpdateNicknameActivity extends BaseActivity{
    private ClearEditText nickNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_me_update_nickname);
        setTitle("修改昵称", null, R.drawable.btn_back, "确认", NO_RES_ID);
        String nickName = getIntent().getStringExtra("nickname");
        nickNameEditText = (ClearEditText) findViewById(R.id.nickNameEditText);
        nickNameEditText.setText(nickName);
        nickNameEditText.setSelection(nickName.length());
        setBtnRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickName = nickNameEditText.getText().toString();
                if (nickName.length() == 0) {
                    Toast.makeText(MeUpdateNicknameActivity.this, "昵称不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (nickName.length() >15) {
                    Toast.makeText(MeUpdateNicknameActivity.this, "昵称不能超过15位", Toast.LENGTH_SHORT).show();
                    return;
                }
                final LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
                String avatarUrl = loginResp.getData().getUser_info().getAvatar_url();
                String accessToken = loginResp.getData().getAccess_token();
                String gender = loginResp.getData().getUser_info().getGender();

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

                        if (response.isSuccess(MeUpdateNicknameActivity.this)) {
                            //更新本地用户信息
                            loginResp.getData().getUser_info().setNickname(nickNameEditText.getText().toString());
                            Share.putObject(Share.LOGIN_RESP, loginResp);
                            Toast.makeText(MeUpdateNicknameActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                            dismissProgressDialog();
                            finish();
                        } else {
                            dismissProgressDialog();
                            Toast.makeText(MeUpdateNicknameActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        setBtnLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
