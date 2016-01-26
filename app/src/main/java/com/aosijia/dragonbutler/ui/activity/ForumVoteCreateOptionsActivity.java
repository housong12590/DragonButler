package com.aosijia.dragonbutler.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.model.BaseResp;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.widget.CommonDialog;
import com.aosijia.dragonbutler.utils.Constants;
import com.aosijia.dragonbutler.utils.ToastUtils;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 发起投票-设置选项
 * Created by Jacky on 2015/12/29.
 * Version 1.0
 */
public class ForumVoteCreateOptionsActivity extends BaseActivity {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_CONTENT = "content";

    private LinearLayout layoutOptions;
    private Button btnAdd;
    List<View> mLayouts;

    private String mTitle;
    private String mContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_forum_vote_create_options);
        initView();
        initData();

        mTitle = getIntent().getExtras().getString(EXTRA_TITLE);
        mContent = getIntent().getExtras().getString(EXTRA_CONTENT);
    }

    private void initView() {
        setTitle("编辑选项", "发布", NO_RES_ID);
        setBtnRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发布
                postVote();
            }
        });

        layoutOptions = (LinearLayout) findViewById(R.id.layout_options);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //新增选项
                addOptions();
            }
        });


    }

    private void initData() {
        mLayouts = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            addOptions();
        }

    }


    private void addOptions() {

        if (mLayouts.size() >= 12) {
            ToastUtils.showToast(getApplicationContext(), "最多设置12个选项！");
            return;
        }

        final View view = LayoutInflater.from(this).inflate(R.layout.layout_vote_options, null);
        ImageButton btn_delete = (ImageButton) view.findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLayouts.size() <= 2) {
                    ToastUtils.showToast(getApplicationContext(), "选项至少要有两个哦！");
                    return;
                }
                EditText et_content = (EditText) view.findViewById(R.id.et_option);
                if (!TextUtils.isEmpty(et_content.getText().toString().trim())) {
                    new CommonDialog.Builder(ForumVoteCreateOptionsActivity.this).setTitle(R.string.dialog_text_title)
                            .setMessage("确定删除？").setNegativeButtonText(R.string.dialog_text_cancel, null)
                            .setPositiveButtonText(R.string.dialog_text_confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    layoutOptions.removeView(view);
                                    mLayouts.remove(view);
                                }
                            }).show();
                } else {
                    layoutOptions.removeView(view);
                    mLayouts.remove(view);
                }
            }
        });
        layoutOptions.addView(view);
        mLayouts.add(view);
    }


    private void postVote() {
        String[] options = new String[mLayouts.size()];
        for (int i = 0; i < mLayouts.size(); i++) {
            EditText et = (EditText) mLayouts.get(i).findViewById(R.id.et_option);
            options[i] = getTextNoSpace(et);
            if (TextUtils.isEmpty(options[i])) {
                ToastUtils.showToast(getApplicationContext(), getString(R.string.error_vote_options_empty));
                return;
            }
        }

        String options_str = new Gson().toJson(options);

        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        Map<String, String> parameter = RequestParameters.forumTopicCreate(loginResp.getData().getAccess_token(), mTitle, mContent,
                Constants.FORUM_TYPE_VOTE, null, null, null, options_str);
        new OkHttpRequest.Builder().url(URLManager.FORUM_TOPIC_CREATE).params(parameter).tag(this).post(new ResultCallback<BaseResp>() {
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
                showProgressDialow("加载中...");
            }

            @Override
            public void onResponse(BaseResp resp) {
                if (resp.isSuccess(ForumVoteCreateOptionsActivity.this)) {
                    ToastUtils.showToast(getApplicationContext(), resp.getMsg());
                    setResult(RESULT_OK);
                    ForumVoteCreateOptionsActivity.this.finish();
                } else {
                    ToastUtils.showToast(getApplicationContext(), resp.getMsg());
                }
            }
        });
    }


}
