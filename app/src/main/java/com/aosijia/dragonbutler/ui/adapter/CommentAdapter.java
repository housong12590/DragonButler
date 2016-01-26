package com.aosijia.dragonbutler.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.model.ForumComments;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.ui.widget.CircleImageView;
import com.aosijia.dragonbutler.utils.DisplayOpitionFactory;
import com.aosijia.dragonbutler.utils.TimeUtils;
import com.aosijia.dragonbutler.utils.Uiutils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 评论ListView适配器
 * Created by Jacky on 2016/1/4.
 * Version 1.0
 */
public class CommentAdapter extends BaseAdapter{

    private List<ForumComments.DataEntity.ForumCommentsEntity> mList;
    private Context mContext;
    private OnCommentListener mCommentListener;
    private final String cacheUserId;

    public CommentAdapter(Context context, List<ForumComments.DataEntity.ForumCommentsEntity> list) {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        cacheUserId = loginResp.getData().getUser_id();
        this.mList = list;
        this.mContext = context;
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ForumComments.DataEntity.ForumCommentsEntity getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommentViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_comment, null);
            holder = new CommentViewHolder();
            holder.civ_avatar = (CircleImageView) convertView.findViewById(R.id.iv_avatar);
            holder.tv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.iv_comment = (ImageView) convertView.findViewById(R.id.iv_comment);
            holder.tv_comment = (TextView) convertView.findViewById(R.id.tv_comment);
            holder.layout_reply = (LinearLayout) convertView.findViewById(R.id.layout_reply);
            convertView.setTag(holder);
        } else {
            holder = (CommentViewHolder) convertView.getTag();
        }

        final ForumComments.DataEntity.ForumCommentsEntity comment = mList.get(position);
        ImageLoader.getInstance().displayImage(comment.getAuthor().getAvatar_url(), holder.civ_avatar, DisplayOpitionFactory.sAvatarDisplayOption);
        holder.tv_nickname.setText(comment.getAuthor().getNickname());
        holder.tv_time.setText(TimeUtils.getDateToString(comment.getCreated_at()));
        holder.tv_comment.setText(comment.getContent());
        holder.iv_comment.setTag(comment.getForum_comment_id());
        holder.iv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCommentListener != null) {
                    mCommentListener.onComment(comment.getForum_comment_id());
                }
            }
        });
        if (comment.getReplies() != null && comment.getReplies().size() > 0) {
            holder.layout_reply.setVisibility(View.VISIBLE);
            holder.layout_reply.removeAllViews();
            for (ForumComments.DataEntity.ForumCommentsEntity.RepliesEntity reply : comment.getReplies()) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_comment_reply, null);
                TextView tv = (TextView) view.findViewById(R.id.textView);
                tv.setText("");
                tv.append(Uiutils.getForegroundColorSpanString(mContext,
                        reply.getAuthor().getNickname() + "：", mContext.getResources().getColor(R.color.blue)));
                tv.append(reply.getContent());
                holder.layout_reply.addView(view);
            }
        } else {
            holder.layout_reply.setVisibility(View.GONE);
        }

        holder.civ_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String avatar_url = comment.getAuthor().getAvatar_url();
                String nickname = comment.getAuthor().getNickname();
                String user_id = comment.getAuthor().getUser_id();
                Uiutils.jumpUserInfoPage(mContext, cacheUserId, avatar_url, nickname, user_id);
            }
        });

        return convertView;
    }

    /**
     * 评论或回复成功，添加到本地数据
     *
     * @param comment
     */
    public void add(ForumComments.DataEntity.ForumCommentsEntity comment) {
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getForum_comment_id().equals(comment.getForum_comment_id())) {
                mList.remove(i);
                mList.add(i, comment);
                return;
            }
        }
        mList.add(0, comment);
    }

    public void setOnCommentListener(OnCommentListener listener) {
        this.mCommentListener = listener;
    }


    class CommentViewHolder {
        private CircleImageView civ_avatar;
        private TextView tv_nickname;
        private TextView tv_time;
        private ImageView iv_comment;
        private TextView tv_comment;
        private LinearLayout layout_reply;
    }


    public interface OnCommentListener {
        void onComment(String comment_id);
    }
}
