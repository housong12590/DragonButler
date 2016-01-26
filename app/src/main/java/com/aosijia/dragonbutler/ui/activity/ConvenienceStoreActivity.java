package com.aosijia.dragonbutler.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.model.ConvenienceStore;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.adapter.ConvenienceStoreAdapter;
import com.aosijia.dragonbutler.ui.widget.loader.LoadingAndRetryManager;
import com.aosijia.dragonbutler.ui.widget.loader.OnLoadingAndRetryListener;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshBase;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshListView;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.List;
import java.util.Map;

/**
 * @author hs
 *         2016年1月19日11:06:42
 *         便利店
 */
public class ConvenienceStoreActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private PullToRefreshListView prlv_listView;
    private LoadingAndRetryManager mLoadingAndRetryManager;
    private List<ConvenienceStore.DataEntity.CvsItemsEntity> mData;
    private ConvenienceStoreAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_convenience_store);
        initView();
        loadData(TYPE_LOAD_FIRST);
    }

    private void initView() {
        setTitle("便利店", null, R.drawable.btn_back, null, NO_RES_ID);
        setBtnLeftOnClickListener(this);
        prlv_listView = (PullToRefreshListView) findViewById(R.id.prlv_listview);
        mAdapter = new ConvenienceStoreAdapter(this);
        prlv_listView.setAdapter(mAdapter);
        prlv_listView.setOnItemClickListener(this);
        prlv_listView.setOnRefreshListener(refreshListener);
        mLoadingAndRetryManager = LoadingAndRetryManager.generate(prlv_listView, loadingAndRetryListener);
    }

    private void loadData(final int type) {
        String last_id;
        if (type == TYPE_LOAD_MORE) {
            last_id = mAdapter.getLastId();
        } else {
            last_id = null;
        }
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        String access_token = loginResp.getData().getAccess_token();
        Map<String, String> paraneters = RequestParameters.convenienceStore(access_token, null, last_id);
        new OkHttpRequest.Builder().url(URLManager.CONVENIENCE_STORE).params(paraneters).tag(this).get(new ResultCallback<ConvenienceStore>() {
            @Override
            public void onError(Request request, Exception e) {
                if (type == TYPE_LOAD_FIRST) {
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
                if (type == TYPE_LOAD_REFRESH) {
                    prlv_listView.onRefreshComplete();
                }
                if (mAdapter != null
                        && mAdapter.getCount() >= PAGE_SIZE
                        && mAdapter.getCount() % PAGE_SIZE == 0) {
                    prlv_listView.onLoadMoreComplete(true);
                } else {
                    prlv_listView.onLoadMoreComplete(false);
                }
            }

            @Override
            public void onResponse(ConvenienceStore response) {
                if (response.isSuccess(ConvenienceStoreActivity.this)) {
                    if (type == TYPE_LOAD_FIRST) {
                        if (response.getData().getCvs_items().size() == 0) {
                            mLoadingAndRetryManager.showEmpty(LoadingAndRetryManager.TYPE_EMPTY_NODATA);
                        } else {
                            mLoadingAndRetryManager.showContent();
                        }
                    }
                    mData = response.getData().getCvs_items();
                    mAdapter.refresh(mData, type);
                } else {
                    Toast.makeText(ConvenienceStoreActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                    if (type == TYPE_LOAD_FIRST) {
                        mLoadingAndRetryManager.showRetry();
                    }
                }
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

    private PullToRefreshBase.OnRefreshListener2<ListView> refreshListener = new PullToRefreshBase.OnRefreshListener2<ListView>() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            loadData(TYPE_LOAD_REFRESH);
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            loadData(TYPE_LOAD_MORE);
        }
    };

    private OnLoadingAndRetryListener loadingAndRetryListener = new OnLoadingAndRetryListener() {
        @Override
        public void setRetryEvent(View retryView) {
            ConvenienceStoreActivity.this.setRetryEvent(retryView);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent it = new Intent(this, ConvenienceStoreDetailActivity.class);
        ConvenienceStore.DataEntity.CvsItemsEntity data = mAdapter.getItem(position - 1);
        it.putExtra("data", data);
        startActivity(it);
    }
}
