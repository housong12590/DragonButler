package com.aosijia.dragonbutler.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.model.MyCommentsResp;
import com.aosijia.dragonbutler.ui.widget.CircleImageView;
import com.aosijia.dragonbutler.utils.Constants;
import com.aosijia.dragonbutler.utils.DisplayOpitionFactory;
import com.aosijia.dragonbutler.utils.TimeUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Jacky on 2016/1/5.
 * Version 1.0
 */
public class MyCommentAdapter extends BaseAdapter {
    private List<MyCommentsResp.Comment> mList;
    private Context mContext;

    public MyCommentAdapter(List<MyCommentsResp.Comment> list, Context context) {
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
        MyCommentsResp.Comment comment = mList.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_my_comment, null);
            holder = new ViewHolder();
            holder.civ_avatar = (CircleImageView) convertView.findViewById(R.id.iv_avatar);
            holder.tv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_comment = (TextView) convertView.findViewById(R.id.tv_comment);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ImageLoader.getInstance().displayImage(comment.getOther_side().getAvatar_url(), holder.civ_avatar, DisplayOpitionFactory.sAvatarDisplayOption);
        holder.tv_nickname.setText(comment.getOther_side().getNickname());
        holder.tv_time.setText(TimeUtils.getDateToString(comment.getCreated_at()));
        holder.tv_comment.setText(comment.getContent());

        String type = "";
        if (Constants.FORUM_TYPE_TOPIC.equals(comment.getTopic_type())) {
            type = "帖子";
        } else if (Constants.FORUM_TYPE_EVENT.equals(comment.getTopic_type())) {
            type = "活动";
        } else if (Constants.FORUM_TYPE_VOTE.equals(comment.getTopic_type())) {
            type = "投票";
        } else if (Constants.TOPIC_TYPE_SECONDHAND.equals(comment.getTopic_type())) {
            type = "二手";
        }
        holder.tv_title.setText(type + ":" + comment.getTopic_title());
        return convertView;
    }

    class ViewHolder {
        private CircleImageView civ_avatar;
        private TextView tv_title;
        private TextView tv_nickname;
        private TextView tv_time;
        private TextView tv_comment;
    }
}
