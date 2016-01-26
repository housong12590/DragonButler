package com.aosijia.dragonbutler.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.imagegroup.ImageGroupView;
import com.aosijia.dragonbutler.model.ForumTopicItem;
import com.aosijia.dragonbutler.utils.Constants;
import com.aosijia.dragonbutler.utils.TimeUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Jacky on 2016/1/4.
 * Version 1.0
 */
public class ForumAdapter extends BaseAdapter{
    private List<ForumTopicItem> mList;
    private Context mContext;

    public ForumAdapter(List<ForumTopicItem> list, Context context) {
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
            } else if (TimeUtils.isPermanentEvent(extra.getEnd_date())) {//长期有效的活动
                holder.iv_status.setImageResource(R.drawable.ic_status_under_way);
            } else {
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

//        holder.image_group_view.setOnImageClickListener(new ImageGroupView.OnImageClickListener() {
//            @Override
//            public void onImageClick(SquareImage clickImage, ArrayList<SquareImage> squareImages, ArrayList<String> allImageInternetUrl) {
//
//            }
//        });

        for(View v: holder.image_group_view.getSquareImageViews()) {
            v.setClickable(false);
            v.setEnabled(false);
        }



       holder.tv_part_count.setText(topic.getExtra().getParticipants_count() + "人参与");
        holder.tv_nickname.setText(topic.getAuthor().getNickname());
        holder.tv_time.setText(TimeUtils.getDateToString(topic.getCreated_at()));
        holder.tv_comment_count.setText(topic.getComments_count());
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
