package com.aosijia.dragonbutler.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.model.HouseHold;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.utils.Uiutils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.Map;

/**
 * 房屋绑定
 * Created by wanglj on 15/12/15.
 */
public class HouseHoldBindActivity extends BaseActivity implements View.OnClickListener {

    private String className;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_house_bind);
        setTitle("房屋绑定", null, NO_RES_ID, "取消", NO_RES_ID);
        findViewById(R.id.houseHoldBindButton).setOnClickListener(this);

        if (getIntent() != null && getIntent().hasExtra("action")) {
            className = getIntent().getStringExtra("action");
        }

        if (getIntent() != null && getIntent().getExtras().containsKey("flag")) {
            TextView tv_desc = (TextView) findViewById(R.id.tv_desc);
            tv_desc.setVisibility(View.GONE);
        }

        setBtnRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private EditText getHouseHoldCodeEditText() {
        return (EditText) findViewById(R.id.houseHoldCodeEditText);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.houseHoldBindButton:
                String code = getHouseHoldCodeEditText().getText().toString();
                bindHouseHold(code);
                break;
        }
    }

    /**
     * 绑定房屋编号
     */
    private void bindHouseHold(String code) {

        if (code.length() == 0) {
            Toast.makeText(this, R.string.check_housecode_not_null, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Uiutils.isHouseCode(code)) {
            Toast.makeText(this, R.string.check_please_input_10_number, Toast.LENGTH_SHORT).show();
            return;
        }


        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        Map<String, String> parameter = RequestParameters.bindHousehold(code, loginResp.getData().getAccess_token());
        new OkHttpRequest.Builder().url(URLManager.BIND_HOUSEHOLD).params(parameter).tag(this).post(new ResultCallback<HouseHold>() {
            @Override
            public void onError(Request request, Exception e) {
                showRequestError();
                dismissProgressDialog();
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                showProgressDialow("正在绑定...");
            }

            @Override
            public void onAfter() {
                super.onAfter();
                dismissProgressDialog();
            }

            @Override
            public void onResponse(HouseHold houseHold) {
                if (houseHold.isSuccess(HouseHoldBindActivity.this)) {
                    LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
                    loginResp.getData().getUser_info().setIs_bound(true);
                    LoginResp.DataEntity.UserInfoEntity.HouseholdEntity householdEntity = new LoginResp.DataEntity.UserInfoEntity.HouseholdEntity();
                    householdEntity.setCommunity_id(houseHold.getData().getHousehold().getCommunity_id());
                    householdEntity.setCommunity_title(houseHold.getData().getHousehold().getCommunity_title());
                    householdEntity.setHousehold_id(houseHold.getData().getHousehold().getHousehold_id());
                    householdEntity.setRoom(houseHold.getData().getHousehold().getRoom());
                    householdEntity.setUnit(houseHold.getData().getHousehold().getUnit());
                    loginResp.getData().getUser_info().setHousehold(householdEntity);
                    Share.putObject(Share.LOGIN_RESP, loginResp);
                    if (className != null) {
                        Intent it = new Intent();

                        if (className.equals("tab")) {
                            it.setClassName(getApplicationContext(), MainTabActivity.class.getName());
                            it.putExtra("index", getIntent().getExtras().getInt("index"));
                            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(it);
                        } else {
                            it.setClassName(getApplicationContext(), className);
                            startActivity(it);
                        }
                        finish();
                    } else {
                        finish();
                    }
                } else {
                    Toast.makeText(HouseHoldBindActivity.this, houseHold.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
