package com.aosijia.dragonbutler.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.model.YellowPages;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.adapter.YellowPagesAdapter;
import com.aosijia.dragonbutler.ui.widget.loader.LoadingAndRetryManager;
import com.aosijia.dragonbutler.ui.widget.loader.OnLoadingAndRetryListener;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshBase;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshListView;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class YellowPagesActivity extends BaseActivity implements View.OnClickListener{

    private PullToRefreshListView prlv_listview;
    private YellowPagesAdapter mAdapter;
    private LoadingAndRetryManager mLoadingAndRetryManager;
    private List<YellowPages.DataEntity.YellowPagesEntity> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_yellow_pages);
        initView();
        loadData(TYPE_LOAD_FIRST);
    }

    private void initView() {
        setTitle("黄页", null, R.drawable.btn_back, null, NO_RES_ID);
        setBtnLeftOnClickListener(this);
        prlv_listview = (PullToRefreshListView) findViewById(R.id.listView);
        mData = new ArrayList<>();
        mAdapter = new YellowPagesAdapter(this, mData);
        prlv_listview.setAdapter(mAdapter);
        prlv_listview.setOnRefreshListener(refresh);
        mLoadingAndRetryManager = new LoadingAndRetryManager(prlv_listview, loadingAndRetryListener);
    }

    private void loadData(final int type) {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        String access_token = loginResp.getData().getAccess_token();
        Map<String, String> parameners = RequestParameters.yellowPages(access_token);
        new OkHttpRequest.Builder().url(URLManager.YELLOW_PAGES).params(parameners).tag(this).get(new ResultCallback<YellowPages>() {
            @Override
            public void onError(Request request, Exception e) {
              if(type == TYPE_LOAD_FIRST){
                  mLoadingAndRetryManager.showRetry();
              }
            }

            @Override
            public void onBefore(Request request) {
                if (type == TYPE_LOAD_FIRST) {
                    mLoadingAndRetryManager.showLoading();
                }
            }

            @Override
            public void onAfter() {
                prlv_listview.onRefreshComplete();
            }

            @Override
            public void onResponse(YellowPages response) {
                if (response.isSuccess(YellowPagesActivity.this)) {
                    mData.clear();
                    mData.addAll(response.getData().getYellow_pages());
                    if (mData.size() == 0) {
                        mLoadingAndRetryManager.showEmpty(LoadingAndRetryManager.TYPE_EMPTY_NODATA);
                    } else {
                        mLoadingAndRetryManager.showContent();
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    mLoadingAndRetryManager.showRetry();
                    Toast.makeText(YellowPagesActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private PullToRefreshBase.OnRefreshListener<ListView> refresh =  new PullToRefreshBase.OnRefreshListener<ListView>() {
        @Override
        public void onRefresh(PullToRefreshBase<ListView> refreshView) {
            loadData(TYPE_LOAD_REFRESH);
        }
    };

    private OnLoadingAndRetryListener loadingAndRetryListener = new OnLoadingAndRetryListener() {
        @Override
        public void setRetryEvent(View retryView) {
            YellowPagesActivity.this.setRetryEvent(retryView);
        }
    };

    public void setRetryEvent(View retryView) {
        retryView.findViewById(R.id.id_btn_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData(TYPE_LOAD_FIRST);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
        }
    }
}
