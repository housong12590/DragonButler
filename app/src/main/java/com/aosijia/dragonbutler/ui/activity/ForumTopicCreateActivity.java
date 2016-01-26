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
import com.aosijia.dragonbutler.model.Forum;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.widget.TextWatcherListener;
import com.aosijia.dragonbutler.utils.Constants;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.List;
import java.util.Map;

/**
 * 发表/编辑帖子
 */
public class ForumTopicCreateActivity extends BaseActivity implements View.OnClickListener {

    private Forum.DataEntity forum;
    private EditText et_title;
    private EditText et_content;
    private ImageGroupView igv_pic;
    private TextView tv_hintText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_forum_create);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("forum")) {
            forum = (Forum.DataEntity) bundle.getSerializable("forum");
        }
        initView();
    }

    private void initView() {
        igv_pic = (ImageGroupView) findViewById(R.id.igv_pic);
        TextView tv_count = (TextView) findViewById(R.id.tv_count);
        et_title = (EditText) findViewById(R.id.et_title);
        tv_hintText = (TextView) findViewById(R.id.tv_hintText);
        et_content = (EditText) findViewById(R.id.et_content);
        et_content.addTextChangedListener(new TextWatcherListener(tv_count, 2000));
        String title;
        if (forum != null) {
            title = "编辑帖子";
            et_title.setText(forum.getTitle());
            et_content.setText(forum.getContent());
            igv_pic.setNetworkPhotos(forum.getExtra().getPic_urls());
            if (forum.getExtra().getPic_urls().size() != 0) {
                tv_hintText.setVisibility(View.GONE);
            }
        } else {
            title = "发布帖子";
        }
        setTitle(title, null, R.drawable.btn_back, "发布", BaseActivity.NO_RES_ID);
        setBtnLeftOnClickListener(this);
        setBtnRightTextOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.title_righttextview:
                publishForum();
                break;
        }
    }

    private void publishForum() {
        String title = et_title.getText().toString();
        String content = et_content.getText().toString();
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        String access_token = loginResp.getData().getAccess_token();
        if (TextUtils.isEmpty(title) || title.length() > 20) {
            return;
        }
        if (content.length() < 10 || content.length() > 2000) {
            Toast.makeText(ForumTopicCreateActivity.this, "内容不能小于10字", Toast.LENGTH_SHORT).show();
            return;
        }
        Gson gson = new Gson();
        List<String> picList = igv_pic.getUploadUrl();

        String pic_url = null;

        if (picList != null && picList.size() > 0) {
            pic_url = gson.toJson(picList);
        }

        if (!igv_pic.canSubmit()) {
            Toast.makeText(this, "正在上传图片,请稍后提交", Toast.LENGTH_SHORT).show();
            return;
        }


        if (forum == null) {
            publishForum(access_token, title, content, pic_url);
        } else {
            updateForum(access_token, title, content, pic_url);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            igv_pic.onParentResult(requestCode, data);
            if (igv_pic.getLocalUrls().size() != 0) {
                tv_hintText.setVisibility(View.GONE);
            }
        }
    }

    //发布
    private void publishForum(String access_token, String title, String content, String pic_url) {
        Map<String, String> parameters = RequestParameters.forumTopicCreate(access_token, title,
                content, Constants.FORUM_TYPE_TOPIC, pic_url, null, null, null);
        new OkHttpRequest.Builder().url(URLManager.FORUM_TOPIC_CREATE).params(parameters).post(new ResultCallback<BaseResp>() {
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
                showProgressDialow("正在发布...");
            }

            @Override
            public void onResponse(BaseResp response) {
                if (response.isSuccess(ForumTopicCreateActivity.this)) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(ForumTopicCreateActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                }

            }

        });
    }

    //更新帖子
    private void updateForum(String access_token, String title, String content, String pic_url) {

        Map<String, String> parameters = RequestParameters.forumTopicUpdate(access_token, forum.getForum_topic_id(), title, content, pic_url);
        new OkHttpRequest.Builder().url(URLManager.FORUM_TOPIC_UPDATE).params(parameters).tag(this).post(new ResultCallback<BaseResp>() {
            @Override
            public void onError(Request request, Exception e) {
                showRequestError();
            }

            @Override
            public void onAfter() {
                dismissProgressDialog();
            }

            @Override
            public void onBefore(Request request) {
                showProgressDialow("正在发布...");
            }

            @Override
            public void onResponse(BaseResp response) {
                if (response.isSuccess(ForumTopicCreateActivity.this)) {
                    setResult(RESULT_OK);
                    finish();
                }
            }

        });
    }


}
