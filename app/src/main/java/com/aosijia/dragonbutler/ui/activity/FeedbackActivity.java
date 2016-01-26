package com.aosijia.dragonbutler.ui.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.aosijia.dragonbutler.ui.widget.TextWatcherListener;
import com.aosijia.dragonbutler.utils.Constants;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.Map;

public class FeedbackActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_feedback);
        initView();
    }

    private void initView() {
        setTitle("意见反馈", null, R.drawable.btn_back, null, NO_RES_ID);
        setBtnLeftOnClickListener(this);
        findViewById(R.id.bt_submit).setOnClickListener(this);
        TextView textCount = (TextView) findViewById(R.id.textCount);
        et_content = (EditText) findViewById(R.id.et_content);
        et_content.addTextChangedListener(new TextWatcherListener(textCount, 200));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.bt_submit:
                submit();
                break;
        }
    }

    private void submit() {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        String access_token = loginResp.getData().getAccess_token();
        String content = et_content.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(FeedbackActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (content.length() > 200) {
            return;
        }
        Map<String, String> parameners = RequestParameters.feedback(access_token, content, Constants.ANDROID, String.valueOf(getVersionCode()));
        new OkHttpRequest.Builder().url(URLManager.FEEDBACK).params(parameners).tag(this).post(new ResultCallback<BaseResp>() {
            @Override
            public void onError(Request request, Exception e) {
                showRequestError();
            }

            @Override
            public void onBefore(Request request) {
                showProgressDialow("正在提交...");
            }

            @Override
            public void onAfter() {
                dismissProgressDialog();
            }

            @Override
            public void onResponse(BaseResp response) {
                if (response.isSuccess(FeedbackActivity.this)) {
                    finish();
                } else {
                    Toast.makeText(FeedbackActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private int getVersionCode() {
        PackageManager packageManager = getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo.versionCode;
    }

}
