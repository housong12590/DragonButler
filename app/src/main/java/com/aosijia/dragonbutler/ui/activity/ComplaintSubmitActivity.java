package com.aosijia.dragonbutler.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.imagegroup.ImageGroupView;
import com.aosijia.dragonbutler.imagegroup.view.SquareImageView;
import com.aosijia.dragonbutler.model.BaseResp;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.widget.TextWatcherListener;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.List;
import java.util.Map;


public class ComplaintSubmitActivity extends BaseActivity {

    private ImageGroupView imageGroupView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_submit);
        setTitle("物业投诉", null, R.drawable.btn_back, getResources().getString(R.string.action_submit),NO_RES_ID);
        findViewById(R.id.rl_type).setVisibility(View.GONE);
        imageGroupView = (ImageGroupView) findViewById(R.id.igv_pic);
        ////限制内容最大输入数
        getEditTextContent().setHint("请描述您需要投诉的状况(至少10个字)");
        TextView textCount = (TextView) findViewById(R.id.tv_count);
        getEditTextContent().addTextChangedListener(new TextWatcherListener(textCount,500));

        imageGroupView.setFragmentManager(getSupportFragmentManager());
        setBtnLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        setBtnRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitComplaint();
            }
        });

    }

    /**
     * 上报投诉
     */
    private void submitComplaint(){

        List<SquareImageView> list = imageGroupView.getSquareImageViews();
        Log.e("ComplaintSubmitActivity", "list.size():" + list.size());
        Log.e("ComplaintSubmitActivity", "imageGroupView.size():" + imageGroupView.getLocalUrls().size());

        String content = getEditTextContent().getText().toString();
        if (content.length() < 10) {
            Toast.makeText(this, "不能少于10个字符", Toast.LENGTH_SHORT).show();
            return;
        }
        if (content.length() > 500) {
            Toast.makeText(this, "不能大于500个个字符", Toast.LENGTH_SHORT).show();
            return;
        }
//        List<String> pic_list = new ArrayList<>();
//        pic_list.add("url1");
//        pic_list.add("url2");
//        pic_list.add("url3");
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

        LoginResp loginResp = (LoginResp)Share.getObject(Share.LOGIN_RESP);
        Map<String, String> parameter = RequestParameters.complaintSubmit(loginResp.getData().getAccess_token(), content, pic_url);

        new OkHttpRequest.Builder().url(URLManager.COMPLAINT_SUBMIT).params(parameter).tag(this).post(new ResultCallback<BaseResp>() {
            @Override
            public void onError(Request request, Exception e) {
                showRequestError();
                dismissProgressDialog();
            }

            @Override
            public void onAfter() {
                super.onAfter();
                dismissProgressDialog();

            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                showProgressDialow("正在提交...");
            }

            @Override
            public void onResponse(BaseResp response) {
                if(response.isSuccess(ComplaintSubmitActivity.this)) {
                    setResult(RESULT_OK);
                    finish();
                }else{
                    Toast.makeText(ComplaintSubmitActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private SquareImageView getSquareImageViewByList(String path){
        List<SquareImageView> list = imageGroupView.getSquareImageViews();
        for(SquareImageView view:list){
            if(view.getLocalUrl() != null && view.getLocalUrl().equals(path)){
                return  view;
            }
        }
        return null;
    }

    private EditText getEditTextContent(){
        return (EditText) findViewById(R.id.editTextContent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            imageGroupView.onParentResult(requestCode, data);
        }
    }
}

