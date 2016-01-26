package com.aosijia.dragonbutler.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.aosijia.dragonbutler.model.ForumTopicItem;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.model.MyForumTopicsResp;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.activity.ForumEventDetailsActivity;
import com.aosijia.dragonbutler.ui.activity.ForumTopicDetailActivity;
import com.aosijia.dragonbutler.ui.activity.ForumVoteDetailsActivity;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 我的发布-邻里
 * Created by Jacky on 15/12/23.
 */
public class MyForumPostFragment extends BaseFragment{

    private PullToRefreshListView propertyBillsListView;
    private LoadingAndRetryManager mLoadingAndRetryManager;
    private MyForumAdapter mMyForumAdapter;
    private List<ForumTopicItem> mForumTopics;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mLoadingAndRetryManager = LoadingAndRetryManager.generate(propertyBillsListView, new OnLoadingAndRetryListener() {
            @Override
            public void setRetryEvent(View retryView) {
                MyForumPostFragment.this.setRetryEvent(retryView);
            }
        });
        loadData(null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_paid_bills,container,false);
        propertyBillsListView = (PullToRefreshListView) v.findViewById(R.id.propertyBillsListView);
        mForumTopics = new ArrayList<>();
        mMyForumAdapter = new MyForumAdapter(mForumTopics,getActivity());
        propertyBillsListView.setAdapter(mMyForumAdapter);
        propertyBillsListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(null);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mForumTopics.size() > 0) {
                    loadData(mForumTopics.get(mForumTopics.size() - 1).getForum_topic_id());
                }
            }
        });

        propertyBillsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 ForumTopicItem item = (ForumTopicItem) parent.getItemAtPosition(position);
                if(item!=null) {
                    if (item.getType().equals(Constants.FORUM_TYPE_TOPIC)) {
                        Intent intent = new Intent(getActivity(), ForumTopicDetailActivity.class);
                        intent.putExtra("forum_topic_id", item.getForum_topic_id());
                        startActivity(intent);
                    } else if (item.getType().equals(Constants.FORUM_TYPE_EVENT)) {
                        Intent intent = new Intent(getActivity(), ForumEventDetailsActivity.class);
                        intent.putExtra("forum_topic_id", item.getForum_topic_id());
                        startActivity(intent);
                    } else if (item.getType().equals(Constants.FORUM_TYPE_VOTE)) {
                        Intent intent = new Intent(getActivity(), ForumVoteDetailsActivity.class);
                        intent.putExtra("forum_topic_id", item.getForum_topic_id());
                        startActivity(intent);
                    }
                }
            }
        });
        return v;
    }

    private void loadData(final String last_topic_id) {
        LoginResp loginResp = (LoginResp)Share.getObject(Share.LOGIN_RESP);
        Map<String, String> parameter = RequestParameters.myForumTopics(loginResp.getData().getAccess_token(),last_topic_id);
        new OkHttpRequest.Builder().url(URLManager.MY_FORUM_TOPICS).params(parameter).tag(this).get(new ResultCallback<MyForumTopicsResp>() {
            @Override
            public void onError(Request request, Exception e) {
                showRequestError();
                if (mForumTopics != null && mForumTopics.size() > 0) {
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
                if (last_topic_id == null && mForumTopics.size() == 0) {
                    mLoadingAndRetryManager.showLoading();
                } else {
                    mLoadingAndRetryManager.showContent();
                }
            }

            @Override
            public void onResponse(MyForumTopicsResp resp) {
                if (resp.isSuccess(getActivity())) {
                    if(last_topic_id == null) {
                        mForumTopics.clear();
                    }
                    mForumTopics.addAll(resp.getData().getForum_topics());
                    mMyForumAdapter.notifyDataSetChanged();

                    if (mForumTopics != null && mForumTopics.size() > 0) {
                        mLoadingAndRetryManager.showContent();
                    }else {
                        mLoadingAndRetryManager.showEmpty(LoadingAndRetryManager.TYPE_EMPTY_NODATA);
                    }

                    if(mForumTopics.size()>0&& mForumTopics.size()%20 == 0) {
                        propertyBillsListView.onLoadMoreComplete(true);
                    }else {
                        propertyBillsListView.onLoadMoreComplete(false);
                    }
                } else {
                    ToastUtils.showToast(getActivity(), resp.getMsg());
                    if (mForumTopics != null && mForumTopics.size() > 0) {
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



    private class MyForumAdapter extends BaseAdapter {

        private List<ForumTopicItem> mList;
        private Context mContext;

        public MyForumAdapter(List<ForumTopicItem> list, Context context) {
            this.mContext = context;
            this.mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ForumTopicItem topic = mList.get(position);
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_forum_topic, null);
                holder = new ViewHolder();
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                holder.layout_type = (ViewGroup) convertView.findViewById(R.id.layout_type);
                holder.iv_type = (ImageView) convertView.findViewById(R.id.iv_type);
                holder.iv_status = (ImageView) convertView.findViewById(R.id.iv_status);
                holder.tv_part_count = (TextView) convertView.findViewById(R.id.tv_participants_count);
                holder.image_group_view = (ImageGroupView) convertView.findViewById(R.id.igv_pic);
                holder.tv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
                holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                holder.tv_comment_count = (TextView) convertView.findViewById(R.id.tv_comment_count);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.tv_title.setText(topic.getTitle());
            if (Constants.FORUM_TYPE_TOPIC.equals(topic.getType())) {
                holder.layout_type.setVisibility(View.GONE);
            } else if (Constants.FORUM_TYPE_EVENT.equals(topic.getType())) {
                ForumTopicItem.Extra extra = topic.getExtra();
                holder.layout_type.setVisibility(View.VISIBLE);
                holder.iv_type.setImageResource(R.drawable.ic_forum_type_event);
                if (TimeUtils.isLessThanStartTime(extra.getStart_date())) {
                    holder.iv_status.setImageResource(R.drawable.ic_status_un_start);
                } else if (TimeUtils.isPermanentEvent(extra.getEnd_date())) {
                    holder.iv_status.setImageResource(R.drawable.ic_status_under_way);
                } else {
                    long end_date = Long.parseLong(extra.getEnd_date());
                    if (TimeUtils.isGreaterThanEndTime(extra.getEnd_date())) {
                        holder.iv_status.setImageResource(R.drawable.ic_status_end);
                    } else {
                        holder.iv_status.setImageResource(R.drawable.ic_status_under_way);
                    }
                }
            } else if (Constants.FORUM_TYPE_VOTE.equals(topic.getType())) {
                holder.layout_type.setVisibility(View.VISIBLE);
                holder.iv_type.setImageResource(R.drawable.ic_forum_type_vote);
                ForumTopicItem.Extra extra = topic.getExtra();
                if (extra.isOpen()) {
                    holder.iv_status.setImageResource(R.drawable.ic_status_under_way);
                } else {
                    holder.iv_status.setImageResource(R.drawable.ic_status_close);
                }
            }

            ForumTopicItem.Extra extra = topic.getExtra();
            if (extra.getPic_urls() != null && extra.getPic_urls().length > 0) {
                holder.image_group_view.setVisibility(View.VISIBLE);
                List<String> imageList = Arrays.asList(extra.getPic_urls());
                if(imageList.size() > 3){
                    imageList = imageList.subList(0,3);
                }
                holder.image_group_view.setNetworkPhotos(imageList);
            } else {
                holder.image_group_view.setVisibility(View.GONE);
            }

            holder.tv_part_count.setText(topic.getExtra().getParticipants_count() + "人参与");
//            holder.tv_nickname.setText(topic.getAuthor().getNickname());
            holder.tv_time.setText(TimeUtils.getDateToString(topic.getCreated_at()));
            holder.tv_comment_count.setText(topic.getComments_count());
            holder.tv_nickname.setVisibility(View.GONE);
            holder.tv_part_count.setVisibility(View.GONE);

            for(View v: holder.image_group_view.getSquareImageViews()) {
                v.setClickable(false);
                v.setEnabled(false);
            }
            return convertView;
        }

        class ViewHolder {
            private TextView tv_title;
            private ViewGroup layout_type;
            private ImageView iv_type;
            private ImageView iv_status;
            private TextView tv_part_count;
            private ImageGroupView image_group_view;
            private TextView tv_nickname;
            private TextView tv_time;
            private TextView tv_comment_count;
        }
    }
}
