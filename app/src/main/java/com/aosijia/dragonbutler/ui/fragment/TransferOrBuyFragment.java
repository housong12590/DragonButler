package com.aosijia.dragonbutler.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.model.SecondHandItems;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.activity.BaseActivity;
import com.aosijia.dragonbutler.ui.activity.SecondhandActivity;
import com.aosijia.dragonbutler.ui.activity.SecondhandDetailActivity;
import com.aosijia.dragonbutler.ui.adapter.TransfersOrBuyAdapter;
import com.aosijia.dragonbutler.ui.widget.loader.LoadingAndRetryManager;
import com.aosijia.dragonbutler.ui.widget.loader.OnLoadingAndRetryListener;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshBase;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshListView;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.Map;

/**
 * 二手市场 转让
 * Created by wanglj on 15/12/29.
 */
public class TransferOrBuyFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private PullToRefreshListView transferListView;
    private TransfersOrBuyAdapter transfersAdapter;
    private LoadingAndRetryManager mLoadingAndRetryManager;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mLoadingAndRetryManager = LoadingAndRetryManager.generate(transferListView, new OnLoadingAndRetryListener() {
            @Override
            public void setRetryEvent(View retryView) {
                TransferOrBuyFragment.this.setRetryEvent(retryView);
            }
        });
        loadData(TYPE_LOAD_FIRST);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_transfer, container, false);
        transferListView = (PullToRefreshListView) v.findViewById(R.id.transferListView);
        transfersAdapter = new TransfersOrBuyAdapter(getActivity());
        transferListView.setAdapter(transfersAdapter);
        transferListView.setOnItemClickListener(this);
        transferListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
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


    public void loadData(final int type) {
        String last_id;
        if (type == TYPE_LOAD_MORE) {
            last_id = transfersAdapter.getLastId();
        } else {
            last_id = null;
        }
        String argType = getArguments().getString("type");
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        Map<String, String> parameter = RequestParameters.secondhandItems(loginResp.getData().getAccess_token(), argType, last_id);
        new OkHttpRequest.Builder().url(URLManager.SECONDHAND_ITEMS).params(parameter).tag(this).get(new ResultCallback<SecondHandItems>() {
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
                    transferListView.onRefreshComplete();
                }
                if (transfersAdapter != null
                        && transfersAdapter.getCount() >= BaseActivity.PAGE_SIZE
                        && transfersAdapter.getCount() % BaseActivity.PAGE_SIZE == 0) {
                    transferListView.onLoadMoreComplete(true);
                } else {
                    transferListView.onLoadMoreComplete(false);
                }

            }

            @Override
            public void onBefore(Request request) {
                if (type == TYPE_LOAD_FIRST) {
                    mLoadingAndRetryManager.showLoading();
                }
            }

            @Override
            public void onResponse(SecondHandItems secondHandItems) {
                if (secondHandItems.isSuccess(getActivity())) {
                    if (type == TYPE_LOAD_FIRST || type == TYPE_LOAD_REFRESH) {
                        if (secondHandItems.getData().getSecondhand_items().size() == 0) {
                            mLoadingAndRetryManager.showEmpty(LoadingAndRetryManager.TYPE_EMPTY_NODATA);
                        } else {
                            mLoadingAndRetryManager.showContent();
                        }

                    }
                    transfersAdapter.setTransfersOrBuyList(secondHandItems.getData().getSecondhand_items(), type);
                } else {
                    Toast.makeText(getActivity(), secondHandItems.getMsg(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SecondHandItems.DataEntity.SecondhandItemsEntity secondhandItemsEntity = (SecondHandItems.DataEntity.SecondhandItemsEntity) parent.getAdapter().getItem(position);
//        String secondhand_item_id = mListData.get(position - 1).getSecondhand_item_id();
        Intent intent = new Intent(getActivity(), SecondhandDetailActivity.class);
        intent.putExtra("secondhand_item_id", secondhandItemsEntity.getSecondhand_item_id());
        startActivityForResult(intent, SecondhandActivity.REQUEST_DETAIL_CODE);
    }
}
