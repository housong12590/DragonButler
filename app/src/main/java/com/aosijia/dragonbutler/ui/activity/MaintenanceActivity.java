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
import com.aosijia.dragonbutler.model.MaintenanceOrders;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.adapter.MaintenanceAdapter;
import com.aosijia.dragonbutler.ui.widget.loader.LoadingAndRetryManager;
import com.aosijia.dragonbutler.ui.widget.loader.OnLoadingAndRetryListener;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshBase;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshListView;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.Map;

/**
 * 报修列表
 *
 * @author hs
 */
public class MaintenanceActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private int REQUEST_RELEASE_CODE = 1001;
    private int REQUEST_DETAIL_CODE = 1002;
    private PullToRefreshListView prlv_listView;
    private MaintenanceAdapter mAdapter;
    private LoadingAndRetryManager mLoadingAndRetryManager;
    public static final String INTENT_FLAG = "maintenaceOrdersEntity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_community_announcements);
        initView();
        loadData(TYPE_LOAD_FIRST);
    }

    private void initView() {
        setTitle("我的报修", null, R.drawable.btn_back, "添加", NO_RES_ID);
        prlv_listView = (PullToRefreshListView) findViewById(R.id.listView);
        setBtnLeftOnClickListener(this);
        setBtnRightTextOnClickListener(this);
        prlv_listView.setOnItemClickListener(this);
        mLoadingAndRetryManager = LoadingAndRetryManager.generate(prlv_listView, loadingAndRetryListener);

        prlv_listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(TYPE_LOAD_REFRESH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(TYPE_LOAD_MORE);
            }
        });
        mAdapter = new MaintenanceAdapter(MaintenanceActivity.this);
        prlv_listView.setAdapter(mAdapter);
    }

    private void loadData(final int type) {
        String last_id;
        if (type == TYPE_LOAD_MORE) {
            last_id = mAdapter.getLastId();
        } else {
            last_id = null;
        }
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        Map<String, String> parameter = RequestParameters.maintenanceOrders(loginResp.getData().getAccess_token(), PAGE_SIZE + "", last_id);
        new OkHttpRequest.Builder().url(URLManager.MAINTENANCE_ORDERS).params(parameter).tag(this).get(new ResultCallback<MaintenanceOrders>() {
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
            public void onBefore(Request request) {
                if (type == TYPE_LOAD_FIRST) {
                    mLoadingAndRetryManager.showLoading();
                }
            }

            @Override
            public void onResponse(MaintenanceOrders maintenanceOrders) {
                if (maintenanceOrders.isSuccess(MaintenanceActivity.this)) {
                    if (type == TYPE_LOAD_FIRST || type == TYPE_LOAD_REFRESH) {
                        if (maintenanceOrders.getData().getMaintenance_orders().size() == 0) {
                            mLoadingAndRetryManager.showEmpty(LoadingAndRetryManager.TYPE_EMPTY_NODATA);
                        } else {
                            mLoadingAndRetryManager.showContent();
                        }
                    }
                    mAdapter.setComplaintsEntityList(maintenanceOrders.getData().getMaintenance_orders(), type);
                } else {
                    Toast.makeText(MaintenanceActivity.this, maintenanceOrders.getMsg(), Toast.LENGTH_SHORT).show();
                    if (type == 1) {
                        mLoadingAndRetryManager.showRetry();
                    }
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MaintenanceOrders.DataEntity.MaintenaceOrdersEntity maintenaceOrdersEntity = mAdapter.getItem(position - 1);
        Intent intent = new Intent(MaintenanceActivity.this, MaintenanceDetailActivity.class);
        intent.putExtra(INTENT_FLAG, maintenaceOrdersEntity);
        startActivityForResult(intent, REQUEST_DETAIL_CODE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.title_righttextview:
                Intent intent = new Intent(MaintenanceActivity.this, MaintenanceSubmitActivity.class);
                startActivityForResult(intent, REQUEST_RELEASE_CODE);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadData(TYPE_LOAD_REFRESH);
        }
    }


    private OnLoadingAndRetryListener loadingAndRetryListener = new OnLoadingAndRetryListener() {
        @Override
        public void setRetryEvent(View retryView) {
            MaintenanceActivity.this.setRetryEvent(retryView);
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

}
