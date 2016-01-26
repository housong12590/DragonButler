package com.aosijia.dragonbutler.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.model.MessageBox.DataEntity.MessageBoxEntity;
import com.aosijia.dragonbutler.model.SecondHandItems;
import com.aosijia.dragonbutler.ui.activity.BaseActivity;
import com.aosijia.dragonbutler.utils.DisplayOpitionFactory;
import com.aosijia.dragonbutler.utils.TimeUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglj on 16/1/7.
 */
public class MessageAdapter extends BaseAdapter {
    private List<MessageBoxEntity> objects = new ArrayList<>();

    private LayoutInflater layoutInflater;

    public MessageAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    /**
     * 获取最后一条数据的ID
     *
     * @return
     */
    public String getLastId() {
        if (getCount() > 0) {
            return getItem(getCount() - 1).getLast_message_date();
        } else {
            return null;
        }

    }


    public void setData(List<MessageBoxEntity> list, int type) {
        if (type == BaseActivity.TYPE_LOAD_MORE) {
            this.objects.addAll(list);
        } else {
            this.objects = list;
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public MessageBoxEntity getItem(int position) {
        return objects.get(position);
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_message_listview, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.avatarImageView = (ImageView) convertView.findViewById(R.id.avatarImageView);
            viewHolder.unreadTextView = (TextView) convertView.findViewById(R.id.unreadTextView);
            viewHolder.nickNameTextView = (TextView) convertView.findViewById(R.id.nickNameTextView);
            viewHolder.dateTextView = (TextView) convertView.findViewById(R.id.dateTextView);
            viewHolder.messageTextView = (TextView) convertView.findViewById(R.id.messageTextView);

            convertView.setTag(viewHolder);
        }
        initializeViews(getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(MessageBoxEntity object, ViewHolder holder) {
        //TODO implement
        ImageLoader.getInstance().displayImage(object.getContact().getAvatar_url(), holder.avatarImageView, DisplayOpitionFactory.sAvatarDisplayOption);
        holder.dateTextView.setText(TimeUtils.getDateToString(object.getLast_message_date()));
        holder.messageTextView.setText(object.getLast_message_content());
        holder.nickNameTextView.setText(object.getContact().getNickname());
        if(object.getUnread_messages_count() > 0){
            holder.unreadTextView.setVisibility(View.VISIBLE);
            holder.unreadTextView.setText(object.getUnread_messages_count()+"");
        }else{
            holder.unreadTextView.setVisibility(View.GONE);
        }

    }

    protected class ViewHolder {
        private ImageView avatarImageView;
        private TextView unreadTextView;
        private TextView nickNameTextView;
        private TextView dateTextView;
        private TextView messageTextView;
    }
}
