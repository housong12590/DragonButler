package com.aosijia.dragonbutler.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.model.Complaints;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.adapter.ComplaintsAdapter;
import com.aosijia.dragonbutler.ui.widget.loader.LoadingAndRetryManager;
import com.aosijia.dragonbutler.ui.widget.loader.OnLoadingAndRetryListener;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshBase;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshListView;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.Map;

/**
 * 我的投诉
 * Created by wanglj on 15/12/25.
 */
public class ComplaintsActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private PullToRefreshListView complaintListView;
    private LoadingAndRetryManager mLoadingAndRetryManager;
    private ComplaintsAdapter complaintsAdapter;
    private static final int REQUEST_DETAIL_CODE = 1001;
    private static final int REQUEST_CREATE_CODE = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_complaints);
        initView();
        loadData(TYPE_LOAD_FIRST);
    }

    private void initView() {
        setTitle("我的投诉", null, R.drawable.btn_back, "投诉", NO_RES_ID);
        complaintListView = (PullToRefreshListView) findViewById(R.id.complaintListView);
        setBtnLeftOnClickListener(this);
        setBtnRightTextOnClickListener(this);
        complaintListView.setOnItemClickListener(this);
        mLoadingAndRetryManager = LoadingAndRetryManager.generate(complaintListView, loadingAndRetryListener);

        complaintListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(TYPE_LOAD_REFRESH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(TYPE_LOAD_MORE);
            }
        });

        complaintsAdapter = new ComplaintsAdapter(ComplaintsActivity.this);
        complaintListView.setAdapter(complaintsAdapter);

    }


    private void loadData(final int type) {
        String last_id;
        if (type == TYPE_LOAD_MORE) {
            last_id = complaintsAdapter.getLastId();
        } else {
            last_id = null;
        }
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        Map<String, String> parameter = RequestParameters.complaints(loginResp.getData().getAccess_token(), PAGE_SIZE+"", last_id);
        new OkHttpRequest.Builder().url(URLManager.COMPLAINTS).params(parameter).tag(this).get(new ResultCallback<Complaints>() {
            @Override
            public void onError(Request request, Exception e) {
                if (type == TYPE_LOAD_FIRST) {
                    mLoadingAndRetryManager.showRetry();
                }
            }

            @Override
            public void onAfter() {
                if (type == TYPE_LOAD_FIRST) {

                } else if (type == TYPE_LOAD_REFRESH) {
                    complaintListView.onRefreshComplete();
                }
                if (complaintsAdapter != null
                        && complaintsAdapter.getCount() >= PAGE_SIZE
                        && complaintsAdapter.getCount() % PAGE_SIZE == 0) {
                    complaintListView.onLoadMoreComplete(true);
                } else {
                    complaintListView.onLoadMoreComplete(false);
                }

            }

            @Override
            public void onBefore(Request request) {
                if (type == TYPE_LOAD_FIRST) {
                    mLoadingAndRetryManager.showLoading();
                }
            }

            @Override
            public void onResponse(Complaints complaints) {
                if (complaints.isSuccess(ComplaintsActivity.this)) {
                    if (type == TYPE_LOAD_FIRST || type ==  TYPE_LOAD_REFRESH) {
                        if(complaints.getData().getComplaints().size() == 0){
                            mLoadingAndRetryManager.showEmpty(LoadingAndRetryManager.TYPE_EMPTY_NODATA);
                        }else{
                            mLoadingAndRetryManager.showContent();
                        }
                    }
                    complaintsAdapter.setComplaintsEntityList(complaints.getData().getComplaints(),type);
                } else {
                    Toast.makeText(ComplaintsActivity.this, complaints.getMsg(), Toast.LENGTH_SHORT).show();
                    if (type == 1) {
                        mLoadingAndRetryManager.showRetry();
                    }
                }
            }
        });

    }

    private OnLoadingAndRetryListener loadingAndRetryListener = new OnLoadingAndRetryListener() {
        @Override
        public void setRetryEvent(View retryView) {
            ComplaintsActivity.this.setRetryEvent(retryView);
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
            case R.id.title_righttextview:
                Intent intent = new Intent(ComplaintsActivity.this, ComplaintSubmitActivity.class);
                startActivityForResult(intent, REQUEST_CREATE_CODE);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Complaints.DataEntity.ComplaintsEntity complaintsEntity =
                (Complaints.DataEntity.ComplaintsEntity) parent.getAdapter().getItem(position);
        Intent intent = new Intent(ComplaintsActivity.this, ComplaintsDetailActivity.class);
        intent.putExtra("complaintsEntity", complaintsEntity);
        startActivityForResult(intent, REQUEST_DETAIL_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            loadData(TYPE_LOAD_REFRESH);
        }
    }
}
