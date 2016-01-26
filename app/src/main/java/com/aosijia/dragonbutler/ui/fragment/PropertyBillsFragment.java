package com.aosijia.dragonbutler.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.model.PropertyBill;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.activity.BaseActivity;
import com.aosijia.dragonbutler.ui.adapter.PropertyBillsAdapter;
import com.aosijia.dragonbutler.ui.widget.loader.LoadingAndRetryManager;
import com.aosijia.dragonbutler.ui.widget.loader.OnLoadingAndRetryListener;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshBase;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshListView;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.Map;

/**
 * 未缴费账单
 * Created by wanglj on 15/12/23.
 */
public class PropertyBillsFragment extends BaseFragment{

    private PullToRefreshListView propertyBillsListView;
    private LoadingAndRetryManager mLoadingAndRetryManager;
    private PropertyBillsAdapter propertyBillsAdapter;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mLoadingAndRetryManager = LoadingAndRetryManager.generate(propertyBillsListView, new OnLoadingAndRetryListener() {
            @Override
            public void setRetryEvent(View retryView) {
                PropertyBillsFragment.this.setRetryEvent(retryView);
            }
        });

        loadData(TYPE_LOAD_FIRST);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_paid_bills,container,false);
        propertyBillsListView = (PullToRefreshListView) v.findViewById(R.id.propertyBillsListView);
        propertyBillsListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(TYPE_LOAD_REFRESH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(TYPE_LOAD_MORE);
            }
        });

        return v;
    }

    private void loadData(final int type) {
        String last_id;
        if (type == TYPE_LOAD_MORE) {
            last_id = propertyBillsAdapter.getLastId();
        } else {
            last_id = null;
        }

        LoginResp loginResp = (LoginResp)Share.getObject(Share.LOGIN_RESP);
        Map<String, String> parameter = RequestParameters.propertyBills(loginResp.getData().getAccess_token(), getArguments().getString("type"), last_id, BaseActivity.PAGE_SIZE+"");
        new OkHttpRequest.Builder().url(URLManager.PROPERTY_BILLS).params(parameter).tag(this).get(new ResultCallback<PropertyBill>() {
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
                    propertyBillsListView.onRefreshComplete();
                }
                    if (propertyBillsAdapter != null
                            && propertyBillsAdapter.getCount() >= BaseActivity.PAGE_SIZE
                            && propertyBillsAdapter.getCount() % BaseActivity.PAGE_SIZE == 0) {
                        propertyBillsListView.onLoadMoreComplete(true);
                    } else {
                        propertyBillsListView.onLoadMoreComplete(false);
                    }



            }

            @Override
            public void onBefore(Request request) {
                if (type == TYPE_LOAD_FIRST) {
                    mLoadingAndRetryManager.showLoading();
                }
            }

            @Override
            public void onResponse(PropertyBill propertyBill) {
                if (propertyBill.isSuccess(getActivity())) {
                    if (type == TYPE_LOAD_FIRST) {
                        if (propertyBill.getData().getProperty_bills().size() == 0) {
                            mLoadingAndRetryManager.showEmpty(LoadingAndRetryManager.TYPE_EMPTY_NODATA);
                        } else {
                            mLoadingAndRetryManager.showContent();
                        }
                        propertyBillsAdapter = new PropertyBillsAdapter(getActivity());
                        propertyBillsListView.setAdapter(propertyBillsAdapter);
                    }
                    propertyBillsAdapter.setComplaintsEntityList(propertyBill.getData().getProperty_bills(),type);
                } else {
                    Toast.makeText(getActivity(), propertyBill.getMsg(), Toast.LENGTH_SHORT).show();
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
            }
        });
    }
}
