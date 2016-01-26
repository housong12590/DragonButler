package com.aosijia.dragonbutler.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.model.ForumTopicItem;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.model.MyFavoritesResp;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.activity.ForumEventDetailsActivity;
import com.aosijia.dragonbutler.ui.activity.ForumTopicDetailActivity;
import com.aosijia.dragonbutler.ui.activity.ForumVoteDetailsActivity;
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
 * 我的收藏-邻里
 * Created by Jacky on 15/12/23.
 */
public class MyForumFavoriteFragment extends BaseFragment{

    private PullToRefreshListView propertyBillsListView;
    private LoadingAndRetryManager mLoadingAndRetryManager;
    private ForumAdapter mAdapter;
    private List<ForumTopicItem> mForumTopics;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mLoadingAndRetryManager = LoadingAndRetryManager.generate(propertyBillsListView, new OnLoadingAndRetryListener() {
            @Override
            public void setRetryEvent(View retryView) {
                MyForumFavoriteFragment.this.setRetryEvent(retryView);
            }
        });
        loadData(null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_paid_bills,container,false);
        propertyBillsListView = (PullToRefreshListView) v.findViewById(R.id.propertyBillsListView);
        mForumTopics = new ArrayList<>();
        mAdapter = new ForumAdapter(mForumTopics,getActivity());
        propertyBillsListView.setAdapter(mAdapter);

        propertyBillsListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(null);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mForumTopics.size() > 0) {
                    loadData(mForumTopics.get(mForumTopics.size() - 1).getForum_topic_id());
                }
            }
        });

        propertyBillsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ForumTopicItem item = (ForumTopicItem) parent.getItemAtPosition(position);
                if (item != null) {
                    if (item.getType().equals(Constants.FORUM_TYPE_TOPIC)) {
                        Intent intent = new Intent(getActivity(), ForumTopicDetailActivity.class);
                        intent.putExtra("forum_topic_id", item.getForum_topic_id());
                        startActivity(intent);
                    } else if (item.getType().equals(Constants.FORUM_TYPE_EVENT)) {
                        Intent intent = new Intent(getActivity(), ForumEventDetailsActivity.class);
                        intent.putExtra("forum_topic_id", item.getForum_topic_id());
                        startActivity(intent);
                    } else if (item.getType().equals(Constants.FORUM_TYPE_VOTE)) {
                        Intent intent = new Intent(getActivity(), ForumVoteDetailsActivity.class);
                        intent.putExtra("forum_topic_id", item.getForum_topic_id());
                        startActivity(intent);
                    }
                }
            }
        });
        return v;
    }

    private void loadData(final String last_topic_id) {

        LoginResp loginResp = (LoginResp)Share.getObject(Share.LOGIN_RESP);
        Map<String, String> parameter = RequestParameters.favorites(loginResp.getData().getAccess_token(), last_topic_id, Constants.FAVORITE_TYPE_FORUM);
        new OkHttpRequest.Builder().url(URLManager.FAVORITES).params(parameter).tag(this).get(new ResultCallback<MyFavoritesResp>() {
            @Override
            public void onError(Request request, Exception e) {
                showRequestError();
                if (mForumTopics != null && mForumTopics.size() > 0) {
                    mLoadingAndRetryManager.showContent();
                } else {
                    mLoadingAndRetryManager.showRetry();
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
                propertyBillsListView.onRefreshComplete();
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                if (last_topic_id == null && mForumTopics.size() == 0) {
                    mLoadingAndRetryManager.showLoading();
                } else {
                    mLoadingAndRetryManager.showContent();
                }
            }

            @Override
            public void onResponse(MyFavoritesResp resp) {
                if (resp.isSuccess(getActivity())) {
                    if(last_topic_id == null) {
                        mForumTopics.clear();
                    }
                    mForumTopics.addAll(resp.getData().getForum_topics());
                    mAdapter.notifyDataSetChanged();

                    if (mForumTopics != null && mForumTopics.size() > 0) {
                        mLoadingAndRetryManager.showContent();
                    }else {
                        mLoadingAndRetryManager.showEmpty(LoadingAndRetryManager.TYPE_EMPTY_NOFAVORITE);
                    }

                    if(mForumTopics.size()>0&& mForumTopics.size()%20 == 0) {
                        propertyBillsListView.onLoadMoreComplete(true);
                    }else {
                        propertyBillsListView.onLoadMoreComplete(false);
                    }

                } else {
                    ToastUtils.showToast(getActivity(), resp.getMsg());
                    if (mForumTopics != null && mForumTopics.size() > 0) {
                        mLoadingAndRetryManager.showContent();
                    } else {
                        mLoadingAndRetryManager.showRetry();
                    }
                }
            }
        });

    }

    public void setRetryEvent(View retryView) {
        View view = retryView.findViewById(R.id.id_btn_retry);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData(null);
            }
        });
    }

}
