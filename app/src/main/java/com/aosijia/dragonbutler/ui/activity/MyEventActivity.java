package com.aosijia.dragonbutler.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.model.ForumTopicItem;
import com.aosijia.dragonbutler.model.ForumTopicsResp;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.adapter.ForumAdapter;
import com.aosijia.dragonbutler.ui.widget.loader.LoadingAndRetryManager;
import com.aosijia.dragonbutler.ui.widget.loader.OnLoadingAndRetryListener;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshBase;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshListView;
import com.aosijia.dragonbutler.utils.Constants;
import com.aosijia.dragonbutler.utils.ToastUtils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 我的活动
 * Created by Jacky on 2016/1/4.
 * Version 1.0
 */
public class MyEventActivity extends BaseActivity{

    private static final int  REQUEST_CODE_ADD = 1101;
    private LoadingAndRetryManager mLoadingAndRetryManager;

    private PullToRefreshListView forumListView;
    private ForumAdapter mForumAdapter;
    private List<ForumTopicItem> mForumTopicItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_my_event);
        initView();

        mLoadingAndRetryManager = LoadingAndRetryManager.generate(forumListView, new OnLoadingAndRetryListener() {
            @Override
            public void setRetryEvent(View retryView) {
              MyEventActivity.this.setRetryEvent(retryView);
            }
        });

        loadData();
    }

    private void initView() {
        setTitle("我的活动", null, NO_RES_ID);

        forumListView = (PullToRefreshListView) findViewById(R.id.lv_forum_topic);
        forumListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ForumTopicItem item = (ForumTopicItem) parent.getAdapter().getItem(position);
                if (item.getType().equals(Constants.FORUM_TYPE_EVENT)) {
                    Intent intent = new Intent(MyEventActivity.this, ForumEventDetailsActivity.class);
                    intent.putExtra("forum_topic_id", item.getForum_topic_id());
                    startActivityForResult(intent, REQUEST_CODE_ADD);
                }
            }
        });

        forumListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData();
            }
        });
        mForumTopicItems = new ArrayList<>();
        mForumAdapter = new ForumAdapter(mForumTopicItems, this);
        forumListView.setAdapter(mForumAdapter);

    }

    public void setRetryEvent(View retryView) {
        View view = retryView.findViewById(R.id.id_btn_retry);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
    }


    private void loadData() {

        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        Map<String, String> parameter = RequestParameters.my_participation(loginResp.getData().getAccess_token());
        new OkHttpRequest.Builder().url(URLManager.MY_PARTICIPATION).params(parameter).tag(this).get(new ResultCallback<ForumTopicsResp>() {
            @Override
            public void onError(Request request, Exception e) {
                showRequestError();
                if (mForumTopicItems != null && mForumTopicItems.size() > 0) {
                    mLoadingAndRetryManager.showContent();
                } else {
                    mLoadingAndRetryManager.showRetry();
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
                forumListView.onRefreshComplete();
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                if (mForumTopicItems.size() == 0) {
                    mLoadingAndRetryManager.showLoading();
                } else {
                    mLoadingAndRetryManager.showContent();
                }
            }

            @Override
            public void onResponse(ForumTopicsResp forumTopicsResp) {
                if (forumTopicsResp.isSuccess(MyEventActivity.this)) {
                    mForumTopicItems.clear();
                    mForumTopicItems.addAll(forumTopicsResp.getData().getForum_topics());
                    mForumAdapter.notifyDataSetChanged();
                    if(mForumTopicItems.size()== 0) {
                        mLoadingAndRetryManager.showEmpty(LoadingAndRetryManager.TYPE_EMPTY_NODATA);
                    }else {
                        mLoadingAndRetryManager.showContent();
                    }

                    if(mForumTopicItems.size()>0&& mForumTopicItems.size()%20 == 0) {
                        forumListView.onLoadMoreComplete(true);
                    }else {
                        forumListView.onLoadMoreComplete(false);
                    }
                } else {
                    ToastUtils.showToast(getApplicationContext(), forumTopicsResp.getMsg());
                    if (mForumTopicItems != null && mForumTopicItems.size() > 0) {
                        mLoadingAndRetryManager.showContent();
                    } else {
                        mLoadingAndRetryManager.showRetry();
                    }
                }
            }
        });
    }
}
