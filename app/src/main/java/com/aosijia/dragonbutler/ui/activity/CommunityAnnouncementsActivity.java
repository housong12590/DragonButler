package com.aosijia.dragonbutler.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.model.CommunityAnnouncement;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.adapter.CommunityAnnouncementsAdapter;
import com.aosijia.dragonbutler.ui.widget.loader.LoadingAndRetryManager;
import com.aosijia.dragonbutler.ui.widget.loader.OnLoadingAndRetryListener;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshBase;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshListView;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.Map;

/**
 * 社区公告
 */
public class CommunityAnnouncementsActivity extends BaseActivity {

    private PullToRefreshListView communityAnnouncementListView;
    private LoadingAndRetryManager mLoadingAndRetryManager;
    private CommunityAnnouncementsAdapter communityAnnouncementsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_community_announcements);
        setTitle("社区公告", null, R.drawable.btn_back, null, NO_RES_ID);

        setBtnLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        communityAnnouncementListView = (PullToRefreshListView) findViewById(R.id.listView);
        communityAnnouncementListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CommunityAnnouncement.DataEntity.CommunityAnnouncementsEntity communityAnnouncementsEntity =
                        (CommunityAnnouncement.DataEntity.CommunityAnnouncementsEntity) parent.getAdapter().getItem(position);
                Intent intent = new Intent(CommunityAnnouncementsActivity.this, CommunityAnnouncementsDetailActivity.class);
                intent.putExtra("communityAnnouncementsEntity", communityAnnouncementsEntity);
                startActivity(intent);
            }
        });


        communityAnnouncementListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(TYPE_LOAD_REFRESH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(TYPE_LOAD_MORE);
            }
        });



        mLoadingAndRetryManager = LoadingAndRetryManager.generate(communityAnnouncementListView, new OnLoadingAndRetryListener() {
            @Override
            public void setRetryEvent(View retryView) {
                CommunityAnnouncementsActivity.this.setRetryEvent(retryView);
            }
        });

        loadData(TYPE_LOAD_FIRST);

    }


    /**
     * 加载更多
     *
     * @param type 1.第一次加载 2.下拉刷新 3.加载更多
     */
    private void loadData(final int type) {

        String communityId;
        if (type == TYPE_LOAD_MORE) {
            communityId = communityAnnouncementsAdapter.getLastId();
        } else {
            communityId = null;
        }
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        Map<String, String> parameter = RequestParameters.communityAnnouncements(loginResp.getData().getAccess_token(), communityId, PAGE_SIZE + "");
        new OkHttpRequest.Builder().url(URLManager.COMMUNITY_ANNOUNCEMENTS).params(parameter).tag(this).get(new ResultCallback<CommunityAnnouncement>() {
            @Override
            public void onError(Request request, Exception e) {
                if (type == TYPE_LOAD_FIRST) {
                    mLoadingAndRetryManager.showRetry();
                }
            }

            @Override
            public void onAfter() {
                if (type == TYPE_LOAD_FIRST) {
//                    mLoadingAndRetryManager.showContent();
                } else if (type == TYPE_LOAD_REFRESH) {
                    communityAnnouncementListView.onRefreshComplete();
                }
                if (communityAnnouncementsAdapter != null
                        && communityAnnouncementsAdapter.getCount() >= PAGE_SIZE
                        && communityAnnouncementsAdapter.getCount() % PAGE_SIZE == 0) {
                    communityAnnouncementListView.onLoadMoreComplete(true);
                } else {
                    communityAnnouncementListView.onLoadMoreComplete(false);

                }

            }

            @Override
            public void onBefore(Request request) {
                if (type == TYPE_LOAD_FIRST) {
                    mLoadingAndRetryManager.showLoading();
                }
            }

            @Override
            public void onResponse(CommunityAnnouncement communityAnnouncement) {

                if (communityAnnouncement.isSuccess(CommunityAnnouncementsActivity.this)) {
                    if (type == TYPE_LOAD_FIRST) {
                        if (communityAnnouncement.getData().getCommunity_announcements().size() == 0) {
                            mLoadingAndRetryManager.showEmpty(LoadingAndRetryManager.TYPE_EMPTY_NODATA);
                        } else {
                            mLoadingAndRetryManager.showContent();
                        }
                        communityAnnouncementsAdapter = new CommunityAnnouncementsAdapter(CommunityAnnouncementsActivity.this);
                        communityAnnouncementListView.setAdapter(communityAnnouncementsAdapter);

                    }
                    communityAnnouncementsAdapter.setCommunityAnnouncementsEntityList(communityAnnouncement.getData().getCommunity_announcements(), type);
                } else {
                    Toast.makeText(CommunityAnnouncementsActivity.this, communityAnnouncement.getMsg(), Toast.LENGTH_SHORT).show();
                    if (type == 1) {
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
                loadData(TYPE_LOAD_FIRST);
//                Toast.makeText(CommunityAnnouncementsActivity.this, "retry event invoked", Toast.LENGTH_SHORT).show();

            }
        });
    }

}

