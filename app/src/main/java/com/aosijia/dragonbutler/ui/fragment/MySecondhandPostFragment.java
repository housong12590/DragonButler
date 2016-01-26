package com.aosijia.dragonbutler.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.imagegroup.ImageGroupView;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.model.MySecondhandItems;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.activity.SecondhandDetailActivity;
import com.aosijia.dragonbutler.ui.widget.loader.LoadingAndRetryManager;
import com.aosijia.dragonbutler.ui.widget.loader.OnLoadingAndRetryListener;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshBase;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshListView;
import com.aosijia.dragonbutler.utils.Constants;
import com.aosijia.dragonbutler.utils.TimeUtils;
import com.aosijia.dragonbutler.utils.ToastUtils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 我的发布-二手
 * Created by Jacky on 15/12/23.
 */
public class MySecondhandPostFragment extends BaseFragment{

    private PullToRefreshListView propertyBillsListView;
    private LoadingAndRetryManager mLoadingAndRetryManager;
    private MySecondhandAdapter mAdapter;
    private List<MySecondhandItems.DataEntity.SecondhandItemsEntity> mSecondhandItems;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mLoadingAndRetryManager = LoadingAndRetryManager.generate(propertyBillsListView, new OnLoadingAndRetryListener() {
            @Override
            public void setRetryEvent(View retryView) {
                MySecondhandPostFragment.this.setRetryEvent(retryView);
            }
        });

        loadData(null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_paid_bills,container,false);
        propertyBillsListView = (PullToRefreshListView) v.findViewById(R.id.propertyBillsListView);
        mSecondhandItems = new ArrayList<>();
        mAdapter = new MySecondhandAdapter(getActivity(),mSecondhandItems);
        propertyBillsListView.setAdapter(mAdapter);
        propertyBillsListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(null);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mSecondhandItems.size() > 0) {
                    loadData(mSecondhandItems.get(mSecondhandItems.size() - 1).getSecondhand_item_id());
                }
            }
        });

        propertyBillsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>=0 && position<mAdapter.getCount()) {
                    MySecondhandItems.DataEntity.SecondhandItemsEntity item =  mAdapter.getItem(position);
                    Intent intent = new Intent(getActivity(), SecondhandDetailActivity.class);
                    intent.putExtra("secondhand_item_id",item.getSecondhand_item_id());
                    startActivity(intent);
                }
            }
        });
        return v;
    }

    private void loadData(final String last_item_id) {

        LoginResp loginResp = (LoginResp)Share.getObject(Share.LOGIN_RESP);
        Map<String, String> parameter = RequestParameters.mySecondhandItems(loginResp.getData().getAccess_token(),last_item_id);
        new OkHttpRequest.Builder().url(URLManager.MY_SECONDHAND_ITEMS).params(parameter).tag(this).get(new ResultCallback<MySecondhandItems>() {
            @Override
            public void onError(Request request, Exception e) {
                showRequestError();
                if (mSecondhandItems != null && mSecondhandItems.size() > 0) {
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
                if (last_item_id == null && mSecondhandItems.size() == 0) {
                    mLoadingAndRetryManager.showLoading();
                } else {
                    mLoadingAndRetryManager.showContent();
                }

            }

            @Override
            public void onResponse(MySecondhandItems resp) {
                if (resp.isSuccess(getActivity())) {
                    if(last_item_id== null) {
                        mSecondhandItems.clear();
                    }
                    mSecondhandItems.addAll(resp.getData().getSecondhand_items());
                    mAdapter.notifyDataSetChanged();
                    if (mSecondhandItems != null && mSecondhandItems.size() > 0) {
                        mLoadingAndRetryManager.showContent();
                    }else {
                        mLoadingAndRetryManager.showEmpty(LoadingAndRetryManager.TYPE_EMPTY_NODATA);
                    }


                    if(mSecondhandItems.size()>0&& mSecondhandItems.size()%20 == 0) {
                        propertyBillsListView.onLoadMoreComplete(true);
                    }else {
                        propertyBillsListView.onLoadMoreComplete(false);
                    }
                } else {
                    ToastUtils.showToast(getActivity(), resp.getMsg());
                    if (mSecondhandItems != null && mSecondhandItems.size() > 0) {
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


    public class MySecondhandAdapter extends BaseAdapter {

        private List<MySecondhandItems.DataEntity.SecondhandItemsEntity> list =  new ArrayList<>();

        private LayoutInflater layoutInflater;

        public MySecondhandAdapter(FragmentActivity context, List<MySecondhandItems.DataEntity.SecondhandItemsEntity> list) {
            this.layoutInflater = LayoutInflater.from(context);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public MySecondhandItems.DataEntity.SecondhandItemsEntity getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.list_item_my_secondhand, null);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.statusImageView = (ImageView) convertView.findViewById(R.id.statusImageView);
                viewHolder.dateTextView = (TextView) convertView.findViewById(R.id.dateTextView);
                viewHolder.contentTextView = (TextView) convertView.findViewById(R.id.contentTextView);
                viewHolder.priceTextView = (TextView) convertView.findViewById(R.id.priceTextView);
                viewHolder.imageGroupView = (ImageGroupView) convertView.findViewById(R.id.igv_pic);
                convertView.setTag(viewHolder);
            }
            initializeViews(getItem(position), (ViewHolder) convertView.getTag());
            return convertView;
        }

        private void initializeViews(MySecondhandItems.DataEntity.SecondhandItemsEntity secondhandItemsEntity, ViewHolder holder) {

            //图片显示处理
            List<String> imageList = secondhandItemsEntity.getPic_urls();

            //状态
            if("1".equals(secondhandItemsEntity.getStatus())){
                holder.statusImageView.setVisibility(View.GONE);
            }else if("2".equals(secondhandItemsEntity.getStatus())){
                holder.statusImageView.setImageResource(R.drawable.ic_secondhand_complete);
                holder.statusImageView.setVisibility(View.VISIBLE);
            }
            //日期
            holder.dateTextView.setText(TimeUtils.getDateToString(secondhandItemsEntity.getCreated_at()));
            //标题
            holder.contentTextView.setText(secondhandItemsEntity.getTitle());
            if(secondhandItemsEntity.getType().equals(Constants.SECONDHAND_TYPE_BUY)) {
                holder.contentTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_secondhand_type_buy, 0, 0, 0);
            }else {
                holder.contentTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_secondhand_type_transfer,0,0,0);
            }

            //价格
            float price = secondhandItemsEntity.getPrice();
            if(price >= 0){
                holder.priceTextView.setText("￥"+price);
            }else{
                holder.priceTextView.setText("￥面议");
            }

            //itemimage 最多显示三个图片
            if(imageList.size() > 0){
                holder.imageGroupView.setVisibility(View.VISIBLE);
                if(imageList.size() > 3){
                    imageList = imageList.subList(0,3);
                }
                holder.imageGroupView.setNetworkPhotos(imageList);
            }else{
                holder.imageGroupView.setVisibility(View.GONE);
            }
        }

        protected class ViewHolder {
            private ImageView statusImageView;
            private TextView dateTextView;
            private TextView contentTextView;
            private TextView priceTextView;
            private ImageGroupView imageGroupView;
        }

    }
}
