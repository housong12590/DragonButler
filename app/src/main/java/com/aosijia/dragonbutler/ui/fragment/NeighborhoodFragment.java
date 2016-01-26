package com.aosijia.dragonbutler.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.model.ForumTopicItem;
import com.aosijia.dragonbutler.model.ForumTopicsResp;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.activity.ForumEventCreateActivity;
import com.aosijia.dragonbutler.ui.activity.ForumEventDetailsActivity;
import com.aosijia.dragonbutler.ui.activity.ForumTopicCreateActivity;
import com.aosijia.dragonbutler.ui.activity.ForumTopicDetailActivity;
import com.aosijia.dragonbutler.ui.activity.ForumVoteCreateActivity;
import com.aosijia.dragonbutler.ui.activity.ForumVoteDetailsActivity;
import com.aosijia.dragonbutler.ui.adapter.ForumAdapter;
import com.aosijia.dragonbutler.ui.widget.loader.LoadingAndRetryManager;
import com.aosijia.dragonbutler.ui.widget.loader.OnLoadingAndRetryListener;
import com.aosijia.dragonbutler.ui.widget.popupwindow.ActionItem;
import com.aosijia.dragonbutler.ui.widget.popupwindow.TitlePopup;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshBase;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshListView;
import com.aosijia.dragonbutler.utils.Constants;
import com.aosijia.dragonbutler.utils.ToastUtils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpClientManager;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 邻里
 * Created by wanglj on 15/12/25.
 */
public class NeighborhoodFragment extends BaseFragment {
    public static final int REQUEST_CODE_ADD = 1001;


    private LoadingAndRetryManager mLoadingAndRetryManager;

    private PullToRefreshListView forumListView;
    private ForumAdapter mForumAdapter;
    private List<ForumTopicItem> mForumTopicItems;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLoadingAndRetryManager = LoadingAndRetryManager.generate(forumListView, new OnLoadingAndRetryListener() {
            @Override
            public void setRetryEvent(View retryView) {
                NeighborhoodFragment.this.setRetryEvent(retryView);
            }
        });
        initPopupView();
        initPopupData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mForumTopicItems != null && mForumTopicItems.size() == 0) {
            loadData(null);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_neighborhood, container, false);
        forumListView = (PullToRefreshListView) v.findViewById(R.id.lv_forum_topic);
        forumListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ForumTopicItem item = (ForumTopicItem) parent.getAdapter().getItem(position);
                if (item.getType().equals(Constants.FORUM_TYPE_TOPIC)) {
                    Intent intent = new Intent(getActivity(), ForumTopicDetailActivity.class);
                    intent.putExtra("forum_topic_id", item.getForum_topic_id());
                    startActivityForResult(intent, REQUEST_CODE_ADD);
                } else if (item.getType().equals(Constants.FORUM_TYPE_EVENT)) {
                    Intent intent = new Intent(getActivity(), ForumEventDetailsActivity.class);
                    intent.putExtra("forum_topic_id", item.getForum_topic_id());
                    startActivityForResult(intent, REQUEST_CODE_ADD);
                } else if (item.getType().equals(Constants.FORUM_TYPE_VOTE)) {
                    Intent intent = new Intent(getActivity(), ForumVoteDetailsActivity.class);
                    intent.putExtra("forum_topic_id", item.getForum_topic_id());
                    startActivityForResult(intent, REQUEST_CODE_ADD);
                }
            }
        });
        v.findViewById(R.id.title_leftimageview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        v.findViewById(R.id.title_rightimageview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titlePopup.show(v);
            }
        });


        forumListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(null);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mForumTopicItems.size() > 0) {
                    loadData((mForumTopicItems.get(mForumTopicItems.size() - 1)).getForum_topic_id());
                }
            }
        });

        mForumTopicItems = new ArrayList<>();
        mForumAdapter = new ForumAdapter(mForumTopicItems, getActivity());
        forumListView.setAdapter(mForumAdapter);

        return v;
    }


    TitlePopup titlePopup;

    private void initPopupView() {
        titlePopup = new TitlePopup(getActivity(), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
            @Override
            public void onItemClick(ActionItem item, int position) {
                switch (position) {
                    case 0://发布帖子
                        startActivityForResult(new Intent(getActivity(), ForumTopicCreateActivity.class), NeighborhoodFragment.REQUEST_CODE_ADD);
                        break;
                    case 1://发布活动发起投票
                        startActivityForResult(new Intent(getActivity(), ForumEventCreateActivity.class), NeighborhoodFragment.REQUEST_CODE_ADD);
                        break;
                    case 2://发起投票
                        startActivityForResult(new Intent(getActivity(), ForumVoteCreateActivity.class), NeighborhoodFragment.REQUEST_CODE_ADD);
                        break;
                }
            }
        });
    }

    private void initPopupData() {
        //给标题栏弹窗添加子类
        titlePopup.addAction(new ActionItem(getActivity(), "发布帖子", R.drawable.ic_forum_topic));
        titlePopup.addAction(new ActionItem(getActivity(), "发起活动", R.drawable.ic_forum_event));
        titlePopup.addAction(new ActionItem(getActivity(), "发起投票", R.drawable.ic_forum_vote));
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

    private void loadData(final String last_topic_id) {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        if (loginResp == null) {
            mLoadingAndRetryManager.showRetry();
            return;
        }
        OkHttpClientManager.getInstance().cancelTag(URLManager.FORUM_TOPICS);
        Map<String, String> parameter = RequestParameters.forumTopics(loginResp.getData().getAccess_token(), null, last_topic_id);

        new OkHttpRequest.Builder().url(URLManager.FORUM_TOPICS).params(parameter).tag(URLManager.FORUM_TOPICS).get(new ResultCallback<ForumTopicsResp>() {
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
                if (last_topic_id == null && mForumTopicItems.size() == 0) {
                    mLoadingAndRetryManager.showLoading();
                } else {
                    mLoadingAndRetryManager.showContent();
                }
            }

            @Override
            public void onResponse(ForumTopicsResp forumTopicsResp) {
                if (forumTopicsResp.isSuccess(getActivity())) {
                    if (last_topic_id == null) {
                        mForumTopicItems.clear();
                    }
                    mForumTopicItems.addAll(forumTopicsResp.getData().getForum_topics());
                    mForumAdapter.notifyDataSetChanged();
                    if (mForumTopicItems != null && mForumTopicItems.size() > 0) {
                        mLoadingAndRetryManager.showContent();
                    }else {
                        mLoadingAndRetryManager.showEmpty(LoadingAndRetryManager.TYPE_EMPTY_NODATA);
                    }
                    if(forumTopicsResp.getData().getForum_topics().size()==20) {
//                        forumListView.setMode(PullToRefreshBase.Mode.BOTH);
                        forumListView.onLoadMoreComplete(true);
                    }else {
                        forumListView.onLoadMoreComplete(false);
                    }
                } else {
                    ToastUtils.showToast(getActivity(), forumTopicsResp.getMsg());
                    if (mForumTopicItems != null && mForumTopicItems.size() > 0) {
                        mLoadingAndRetryManager.showContent();
                    } else {
                        mLoadingAndRetryManager.showRetry();
                    }
                }
            }
        });
    }


    private void showSelectForumTypePopupWindow(View view) {
        View contentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.popup_select_forum_type, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.btg_global_translucent_white)));

        popupWindow.showAsDropDown(view, 0, 0);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD) {
            if (resultCode == Activity.RESULT_OK) {
                loadData(null);
            }
        }

    }

}
