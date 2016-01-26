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
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.model.MyCommentsResp;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.activity.ForumEventDetailsActivity;
import com.aosijia.dragonbutler.ui.activity.ForumTopicDetailActivity;
import com.aosijia.dragonbutler.ui.activity.ForumVoteDetailsActivity;
import com.aosijia.dragonbutler.ui.activity.SecondhandDetailActivity;
import com.aosijia.dragonbutler.ui.adapter.MyCommentAdapter;
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
 * 我的评论-我评论的
 * Created by Jacky on 15/12/23.
 */
public class MyCommentsFragment extends BaseFragment {
    private PullToRefreshListView propertyBillsListView;
    private LoadingAndRetryManager mLoadingAndRetryManager;
    private MyCommentAdapter mAdapter;
    private List<MyCommentsResp.Comment> mComments;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mLoadingAndRetryManager = LoadingAndRetryManager.generate(propertyBillsListView, new OnLoadingAndRetryListener() {
            @Override
            public void setRetryEvent(View retryView) {
                MyCommentsFragment.this.setRetryEvent(retryView);
            }
        });
        loadData(null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_paid_bills, container, false);
        propertyBillsListView = (PullToRefreshListView) v.findViewById(R.id.propertyBillsListView);
        mComments = new ArrayList<>();
        mAdapter = new MyCommentAdapter(mComments, getActivity());
        propertyBillsListView.setAdapter(mAdapter);
        propertyBillsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyCommentsResp.Comment comment = (MyCommentsResp.Comment) parent.getItemAtPosition(position);
                Intent intent = new Intent();
                if (Constants.FORUM_TYPE_TOPIC.equals(comment.getTopic_type())) {
                    intent.setClass(getActivity(), ForumTopicDetailActivity.class);
                    intent.putExtra("forum_topic_id", comment.getTopic_id());
                } else if (Constants.FORUM_TYPE_EVENT.equals(comment.getTopic_type())) {
                    intent.setClass(getActivity(), ForumEventDetailsActivity.class);
                    intent.putExtra("forum_topic_id", comment.getTopic_id());
                } else if (Constants.FORUM_TYPE_VOTE.equals(comment.getTopic_type())) {
                    intent.setClass(getActivity(), ForumVoteDetailsActivity.class);
                    intent.putExtra(ForumVoteDetailsActivity.EXTRA_ID, comment.getTopic_id());
                } else if (Constants.TOPIC_TYPE_SECONDHAND.equals(comment.getTopic_type())) {
                    intent.setClass(getActivity(), SecondhandDetailActivity.class);
                    intent.putExtra("secondhand_item_id", comment.getTopic_id());
                }
                startActivity(intent);
            }
        });

        propertyBillsListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(null);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(mComments.get(mComments.size()-1).getComment_id());
            }
        });
        return v;
    }

    private void loadData(final String last_comment_id) {

        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        Map<String, String> parameter = RequestParameters.myComments(loginResp.getData().getAccess_token(), last_comment_id, Constants.COMMENT_TYPE_COMMENT_SEND);
        new OkHttpRequest.Builder().url(URLManager.MY_COMMENTS).params(parameter).tag(this).get(new ResultCallback<MyCommentsResp>() {
            @Override
            public void onError(Request request, Exception e) {
                showRequestError();
                if (mComments != null && mComments.size() > 0) {
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
                if (last_comment_id == null && mComments.size() == 0) {
                    mLoadingAndRetryManager.showLoading();
                } else {
                    mLoadingAndRetryManager.showContent();
                }
            }

            @Override
            public void onResponse(MyCommentsResp resp) {
                if (resp.isSuccess(getActivity())) {
                    if(last_comment_id== null) {
                        mComments.clear();
                    }
                    mComments.addAll(resp.getData().getForum_comments());
                    mAdapter.notifyDataSetChanged();
                    if (mComments != null && mComments.size() > 0) {
                        mLoadingAndRetryManager.showContent();
                    }else {
                        mLoadingAndRetryManager.showEmpty(LoadingAndRetryManager.TYPE_EMPTY_RECEIVE);
                    }

                    if(mComments.size()>0&& mComments.size()%20 == 0) {
                        propertyBillsListView.onLoadMoreComplete(true);
                    }else {
                        propertyBillsListView.onLoadMoreComplete(false);
                    }

                } else {
                    ToastUtils.showToast(getActivity(), resp.getMsg());
                    mLoadingAndRetryManager.showRetry();
                    if (mComments != null && mComments.size() > 0) {
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
