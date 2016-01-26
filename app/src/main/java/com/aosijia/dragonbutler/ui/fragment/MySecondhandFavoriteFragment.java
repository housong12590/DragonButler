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
import com.aosijia.dragonbutler.model.MyFavoritesResp;
import com.aosijia.dragonbutler.model.SecondHandItems;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.activity.SecondhandDetailActivity;
import com.aosijia.dragonbutler.ui.widget.CircleImageView;
import com.aosijia.dragonbutler.ui.widget.loader.LoadingAndRetryManager;
import com.aosijia.dragonbutler.ui.widget.loader.OnLoadingAndRetryListener;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshBase;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshListView;
import com.aosijia.dragonbutler.utils.Constants;
import com.aosijia.dragonbutler.utils.TimeUtils;
import com.aosijia.dragonbutler.utils.ToastUtils;
import com.aosijia.dragonbutler.utils.Uiutils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 我的收藏-二手
 * Created by Jacky on 15/12/23.
 */
public class MySecondhandFavoriteFragment extends BaseFragment {

    private PullToRefreshListView propertyBillsListView;
    private LoadingAndRetryManager mLoadingAndRetryManager;
    private MyFavoriteSecondAdapter mAdapter;
    private List<SecondHandItems.DataEntity.SecondhandItemsEntity> mSecondhandItems;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mLoadingAndRetryManager = LoadingAndRetryManager.generate(propertyBillsListView, new OnLoadingAndRetryListener() {
            @Override
            public void setRetryEvent(View retryView) {
                MySecondhandFavoriteFragment.this.setRetryEvent(retryView);
            }
        });
        loadData(null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_paid_bills, container, false);
        propertyBillsListView = (PullToRefreshListView) v.findViewById(R.id.propertyBillsListView);
        mSecondhandItems = new ArrayList<>();
        mAdapter = new MyFavoriteSecondAdapter(getActivity(), mSecondhandItems);
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
                SecondHandItems.DataEntity.SecondhandItemsEntity item = mAdapter.getItem(position - 1);
                Intent intent = new Intent(getActivity(), SecondhandDetailActivity.class);
                intent.putExtra("secondhand_item_id", item.getSecondhand_item_id());
                startActivity(intent);
            }
        });
        return v;
    }

    private void loadData(final String last_topic_id) {

        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        Map<String, String> parameter = RequestParameters.favorites(loginResp.getData().getAccess_token(), last_topic_id, Constants.FAVORITE_TYPE_SECOND_HAND);
        new OkHttpRequest.Builder().url(URLManager.FAVORITES).params(parameter).tag(this).get(new ResultCallback<MyFavoritesResp>() {
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
                if (last_topic_id == null && mSecondhandItems.size() == 0) {
                    mLoadingAndRetryManager.showLoading();
                } else {
                    mLoadingAndRetryManager.showContent();
                }
            }

            @Override
            public void onResponse(MyFavoritesResp resp) {
                if (resp.isSuccess(getActivity())) {
                    if (last_topic_id == null) {
                        mSecondhandItems.clear();
                    }
                    mSecondhandItems.addAll(resp.getData().getSecondhand_items());
                    mAdapter.notifyDataSetChanged();
                    if (mSecondhandItems != null && mSecondhandItems.size() > 0) {
                        mLoadingAndRetryManager.showContent();
                    } else {
                        mLoadingAndRetryManager.showEmpty(LoadingAndRetryManager.TYPE_EMPTY_NOFAVORITE);
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


    public class MyFavoriteSecondAdapter extends BaseAdapter {

        private List<SecondHandItems.DataEntity.SecondhandItemsEntity> list = new ArrayList<>();

        private FragmentActivity context;
        private LayoutInflater layoutInflater;

        public MyFavoriteSecondAdapter(FragmentActivity context, List<SecondHandItems.DataEntity.SecondhandItemsEntity> list) {
            this.context = context;
            this.layoutInflater = LayoutInflater.from(this.context);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public SecondHandItems.DataEntity.SecondhandItemsEntity getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.item_transfers, null);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.avatarImageView = (CircleImageView) convertView.findViewById(R.id.avatarImageView);
                viewHolder.statusImageView = (ImageView) convertView.findViewById(R.id.statusImageView);
                viewHolder.dateTextView = (TextView) convertView.findViewById(R.id.dateTextView);
                viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
                viewHolder.contentTextView = (TextView) convertView.findViewById(R.id.contentTextView);
                viewHolder.priceTextView = (TextView) convertView.findViewById(R.id.priceTextView);
                viewHolder.imageGroupView = (ImageGroupView) convertView.findViewById(R.id.igv_pic);
                convertView.setTag(viewHolder);
            }
            initializeViews(getItem(position), (ViewHolder) convertView.getTag());
            return convertView;
        }

        private void initializeViews(SecondHandItems.DataEntity.SecondhandItemsEntity secondhandItemsEntity, ViewHolder holder) {

            //头像
            ImageLoader.getInstance().getInstance().displayImage(secondhandItemsEntity.getAuthor().getAvatar_url(), holder.avatarImageView, Uiutils.displayImageOptions);

            //图片显示处理
            List<String> imageList = secondhandItemsEntity.getPic_urls();

            //状态
            if ("1".equals(secondhandItemsEntity.getStatus())) {
                imageList.clear();
                holder.statusImageView.setVisibility(View.VISIBLE);
                holder.statusImageView.setImageResource(R.drawable.ic_secondhand_complete);
            } else if ("2".equals(secondhandItemsEntity.getStatus())) {
                imageList.addAll(imageList);
                holder.statusImageView.setVisibility(View.GONE);
            }
            //日期
            holder.dateTextView.setText(TimeUtils.getDateToString(secondhandItemsEntity.getCreated_at()));
            //昵称
            holder.nameTextView.setText(secondhandItemsEntity.getAuthor().getNickname());
            //标题
            holder.contentTextView.setText(secondhandItemsEntity.getTitle());

            //价格
            float price = secondhandItemsEntity.getPrice();
            if (price >= 0) {
                holder.priceTextView.setText("￥" + price);
            } else {
                holder.priceTextView.setText("￥面议");
            }
//        //图片显示处理
//        List<String> imageList = secondhandItemsEntity.getPic_urls();
//        imageList.addAll(imageList);


            //itemimage 最多显示三个图片
            if (imageList.size() > 0) {
                holder.imageGroupView.setVisibility(View.VISIBLE);
                if (imageList.size() > 3) {
                    imageList = imageList.subList(0, 3);
                }
                holder.imageGroupView.setNetworkPhotos(imageList);
            } else {
                holder.imageGroupView.setVisibility(View.GONE);
            }


        }

        protected class ViewHolder {
            private CircleImageView avatarImageView;
            private ImageView statusImageView;
            private TextView dateTextView;
            private TextView nameTextView;
            private TextView contentTextView;
            private TextView priceTextView;
            private ImageGroupView imageGroupView;
        }

    }

}
