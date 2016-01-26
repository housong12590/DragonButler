package com.aosijia.dragonbutler.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.imagegroup.ImageGroupView;
import com.aosijia.dragonbutler.model.BaseResp;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.widget.ActionSheet;
import com.aosijia.dragonbutler.ui.widget.TextWatcherListener;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.List;
import java.util.Map;

public class MaintenanceSubmitActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_type;
    private EditText et_content;
    private ImageGroupView igv_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_submit);
        initView();
    }

    private void initView() {
        setTitle("物业报修", null, R.drawable.btn_back, "提交", NO_RES_ID);
        findViewById(R.id.rl_type).setOnClickListener(this);
        TextView textCount = (TextView) findViewById(R.id.tv_count);
        et_content = (EditText) findViewById(R.id.editTextContent);
        et_content.setHint("请描述您的报修问题(至少10个字)");
        et_content.addTextChangedListener(new TextWatcherListener(textCount, 200));

        tv_type = (TextView) findViewById(R.id.textViewType);
        igv_pic = (ImageGroupView) findViewById(R.id.igv_pic);

        igv_pic.setFragmentManager(getSupportFragmentManager());
        setBtnLeftOnClickListener(this);
        setBtnRightTextOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_type:
                selectorType();
                break;
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.title_righttextview:
                submit();
                break;
        }
    }

    //选择报修的类型
    private int type = -1;
    private String[] items;

    private void selectorType() {
        items = getResources().getStringArray(R.array.maintenance_type);
        ActionSheet.createBuilder(this, getSupportFragmentManager())
                .setCancelButtonTitle(getResources().getString(R.string.cancel))
                .setOtherButtonTitles(items[0], items[1], items[2], items[3])
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        tv_type.setText(items[index]);
                        switch (index) {
                            case 0:type = 1;break;
                            case 1:type = 2;break;
                            case 2:type = 3;break;
                            case 3:type = 100;break;
                        }
                    }
                }).show();
    }

    private void submit() {
        if (type == -1) {
            Toast.makeText(MaintenanceSubmitActivity.this, "请选择报修的类型", Toast.LENGTH_SHORT).show();
            return;
        }
        String content = et_content.getText().toString();
        if (content.length() == 0 ) {
            Toast.makeText(this, "报修内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (content.length() >0 && content.length() < 10 ) {
            Toast.makeText(this, "内文字容至少10个字", Toast.LENGTH_SHORT).show();
            return;
        }

        if (content.length() > 200) {
            Toast.makeText(this, "文字内容不能超过200字", Toast.LENGTH_SHORT).show();
            return;
        }

//        List<String> picList = igv_pic.getInternetUrls();
//        String pic_url = null;
//        if (picList != null && picList.size() > 0) {
//            pic_url = new Gson().toJson(picList);
//        }

        Gson gson = new Gson();
        List<String> picList = igv_pic.getUploadUrl();

        String pic_url = null;

        if(picList != null && picList.size() > 0){
            pic_url = gson.toJson(picList);
        }

        if(!igv_pic.canSubmit()){
            Toast.makeText(this, "正在上传图片,请稍后提交", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        String access_token = loginResp.getData().getAccess_token();
        Map<String, String> parameter = RequestParameters.maintenanceOrderSubmit(access_token, content, String.valueOf(type), pic_url);
        new OkHttpRequest.Builder().url(URLManager.MAINTENANCE_ORDER_SUBMIT).params(parameter).tag(MaintenanceSubmitActivity.this)
                .post(new ResultCallback<BaseResp>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        dismissProgressDialog();
                        showRequestError();
                    }

                    @Override
                    public void onBefore(Request request) {
                        super.onBefore(request);
                        showProgressDialow("正在发布...");
                    }

                    @Override
                    public void onAfter() {
                        super.onAfter();
                        dismissProgressDialog();
                    }

                    @Override
                    public void onResponse(BaseResp response) {
                        if (response.isSuccess(MaintenanceSubmitActivity.this)) {
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            igv_pic.onParentResult(requestCode, data);
        }
    }
}
