package com.aosijia.dragonbutler.ui.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.imagegroup.ImageGroupView;
import com.aosijia.dragonbutler.model.BaseResp;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.model.SecondhandItem;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.widget.TextWatcherListener;
import com.aosijia.dragonbutler.utils.Uiutils;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.List;
import java.util.Map;

/**
 * 发布二手转让和求购
 * Created by wanglj on 15/12/29.
 */
public class SecondhandCreateActivity extends BaseActivity {

    private TextView textCount;
    private ImageGroupView imageGroupView;
    private TextView hintText;

    public String createType;
    private SecondhandItem secondhandItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("secondhandItem")) {
            secondhandItem = (SecondhandItem) bundle.getSerializable("secondhandItem");
            createType = secondhandItem.getData().getType();
        } else if (getIntent().getExtras().containsKey("createType")) {
            createType = getIntent().getStringExtra("createType");
        }
        setImmersionStatus();
        setContentView(R.layout.activity_secondhand_create);


        if (SecondhandActivity.TYPE_BUY.equals(createType)) {
            setTitle("求购", null, R.drawable.btn_back, "发布", NO_RES_ID);
        } else if (SecondhandActivity.TYPE_TRANSFER.equals(createType)) {
            setTitle("转让", null, R.drawable.btn_back, "发布", NO_RES_ID);
        }

        setBtnRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondHandCreate();
            }
        });


        setBtnLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initView();
        TextView textCount = (TextView) findViewById(R.id.tv_count);
        getContentEditText().addTextChangedListener(new TextWatcherListener(textCount, 200));
    }

    private void initView() {
        textCount = (TextView) findViewById(R.id.tv_count);
        imageGroupView = (ImageGroupView) findViewById(R.id.igv_pic);
        hintText = (TextView) findViewById(R.id.hintText);
        if (secondhandItem != null) {
            getTitleEditText().setText(secondhandItem.getData().getTitle());
            getContentEditText().setText(secondhandItem.getData().getContent());
            if (secondhandItem.getData().getPrice().equals("-1")) {
               getPriceEditText().setText("");
            }else{
                getPriceEditText().setText(secondhandItem.getData().getPrice());
            }
            getPhoneEditText().setText(secondhandItem.getData().getMobile());
            imageGroupView.setNetworkPhotos(secondhandItem.getData().getPic_urls());
        }
    }

    private EditText getTitleEditText() {
        return (EditText) findViewById(R.id.titleEditText);
    }

    private EditText getContentEditText() {
        return (EditText) findViewById(R.id.contentEditText);
    }

    private EditText getPriceEditText() {
        return (EditText) findViewById(R.id.priceEditText);
    }

    private EditText getPhoneEditText() {
        return (EditText) findViewById(R.id.phoneEditText);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            imageGroupView.onParentResult(requestCode, data);
        }
    }

    private void secondHandCreate() {
        String title = getTitleEditText().getText().toString();
        String content = getContentEditText().getText().toString();
        String price = getPriceEditText().getText().toString();
        String mobile = getPhoneEditText().getText().toString();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "请输入宝贝标题", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(content)) {
            content = null;
        }

        if (TextUtils.isEmpty(price)) {
            price = "-1";
        } else {
            if (!Uiutils.isPositiveNumber(price)) {
                Toast.makeText(this, "价格必须大于0", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (TextUtils.isEmpty(mobile)) {
            mobile = null;
        } else {
            if (!Uiutils.isMobile(mobile)) {
                Toast.makeText(this, "请输入11位号码", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        String access_token = loginResp.getData().getAccess_token();
//        List<String> picList = imageGroupView.getInternetUrls();
//        String pic_url = null;
//        if (picList != null && picList.size() > 0) {
//            pic_url = new Gson().toJson(picList);
//        }

        Gson gson = new Gson();
        List<String> picList = imageGroupView.getUploadUrl();

        String pic_url = null;

        if(picList != null && picList.size() > 0){
            pic_url = gson.toJson(picList);
        }

        if(!imageGroupView.canSubmit()){
            Toast.makeText(this, "正在上传图片,请稍后提交", Toast.LENGTH_SHORT).show();
            return;
        }

        if (secondhandItem == null) {
            createPublish(access_token, title, content, pic_url, price, mobile);
        } else {
            editPublish(access_token, title, content, pic_url, price, mobile);
        }
    }

    //编辑发布
    private void editPublish(String access_token, String title, String content, String pic_url, String price, String mobile) {
        Map<String, String> parameter = RequestParameters.secondhandItemUpdate(access_token, secondhandItem.getData().getSecondhand_item_id()
                , title, content, pic_url, price, createType, mobile);
        new OkHttpRequest.Builder().url(URLManager.SECONDHAND_ITEM_UPDATE).params(parameter).tag(this).post(new ResultCallback<BaseResp>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onAfter() {
                dismissProgressDialog();
            }

            @Override
            public void onBefore(Request request) {
                showProgressDialow("正在发布");
            }

            @Override
            public void onResponse(BaseResp response) {
                if (response.isSuccess(SecondhandCreateActivity.this)) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(SecondhandCreateActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //创建发布
    private void createPublish(String access_token, String title, String content, String pic_url, String price, String mobile) {
        Map<String, String> parameter = RequestParameters.secondhandItemCreate(access_token, title, content, pic_url, price, createType, mobile);
        new OkHttpRequest.Builder().url(URLManager.SECONDHAND_ITEMS_CREATE).params(parameter).tag(this).post(new ResultCallback<BaseResp>() {
            @Override
            public void onError(Request request, Exception e) {
                dismissProgressDialog();
                showRequestError();
            }

            @Override
            public void onAfter() {
                super.onAfter();
                dismissProgressDialog();
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                showProgressDialow("正在发布...");
            }

            @Override
            public void onResponse(BaseResp response) {
                if (response.isSuccess(SecondhandCreateActivity.this)) {
                    Toast.makeText(SecondhandCreateActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(SecondhandCreateActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


}
