package com.aosijia.dragonbutler.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.model.SystemMessages;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.adapter.SystemMsgAdapter;
import com.aosijia.dragonbutler.ui.widget.loader.LoadingAndRetryManager;
import com.aosijia.dragonbutler.ui.widget.loader.OnLoadingAndRetryListener;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshBase;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshListView;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.Map;

public class SystemMessagesActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private PullToRefreshListView prlv_listview;
    private LoadingAndRetryManager mLoadingAndRetryManager;
    private SystemMsgAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_system_messages);
        initView();
        loadData(TYPE_LOAD_FIRST);
    }

    private void initView() {
        String title = (String) getText(R.string.system_message);
        setTitle(title, null, R.drawable.btn_back, null, NO_RES_ID);
        prlv_listview = (PullToRefreshListView) findViewById(R.id.prlv_listview);
        mLoadingAndRetryManager = new LoadingAndRetryManager(prlv_listview, loadingAndRetryListener);
        prlv_listview.setOnRefreshListener(refresh);
        setBtnLeftOnClickListener(this);
        mAdapter = new SystemMsgAdapter(SystemMessagesActivity.this);
        prlv_listview.setAdapter(mAdapter);
        prlv_listview.setOnItemClickListener(this);

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
        Map<String, String> parameters = RequestParameters.systemMessages(access_token, null, last_id);
        new OkHttpRequest.Builder().url(URLManager.SYSTEM_MESSAGES).params(parameters).tag(this).get(new ResultCallback<SystemMessages>() {
            @Override
            public void onError(Request request, Exception e) {
                if (type == TYPE_LOAD_FIRST) {
                    mLoadingAndRetryManager.showRetry();
                }
            }

            @Override
            public void onBefore(Request request) {
                if(type == TYPE_LOAD_FIRST){
                    mLoadingAndRetryManager.showLoading();
                }
            }

            @Override
            public void onAfter() {
                if (type == TYPE_LOAD_FIRST) {

                } else if (type == TYPE_LOAD_REFRESH) {
                    prlv_listview.onRefreshComplete();
                }
                if (mAdapter != null
                        && mAdapter.getCount() >= PAGE_SIZE
                        && mAdapter.getCount() % PAGE_SIZE == 0) {
                    prlv_listview.onLoadMoreComplete(true);
                } else {
                    prlv_listview.onLoadMoreComplete(false);
                }
            }

            @Override
            public void onResponse(SystemMessages response) {
                if (response.isSuccess(SystemMessagesActivity.this)) {
                    if (type == TYPE_LOAD_FIRST) {
                        if(response.getData().getSystem_messages().size() == 0){
                            mLoadingAndRetryManager.showEmpty(LoadingAndRetryManager.TYPE_EMPTY_NODATA);
                        }else{
                            mLoadingAndRetryManager.showContent();
                        }
                    }
                    mAdapter.refresh(response.getData().getSystem_messages(), type);
                } else {
                    Toast.makeText(SystemMessagesActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                    mLoadingAndRetryManager.showRetry();
                }
            }
        });
    }


    private PullToRefreshBase.OnRefreshListener2<ListView> refresh = new PullToRefreshBase.OnRefreshListener2<ListView>() {
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
            SystemMessagesActivity.this.setRetryEvent(retryView);
        }
    };

    public void setRetryEvent(View retryView) {
        View view = retryView.findViewById(R.id.id_btn_retry);
        view.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,SystemMessagesDetailActivity.class);
        SystemMessages.DataEntity.SystemMessagesEntity item = mAdapter.getItem(position-1);
        Bundle bundle = new Bundle();
        bundle.putString("content",item.getContent());
        bundle.putString("date",item.getCreated_at());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
