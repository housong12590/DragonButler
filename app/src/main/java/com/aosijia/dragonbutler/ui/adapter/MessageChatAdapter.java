package com.aosijia.dragonbutler.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.model.Messages.DataEntity.MessagesEntity;
import com.aosijia.dragonbutler.ui.activity.MessagesActivity;
import com.aosijia.dragonbutler.utils.DisplayOpitionFactory;
import com.aosijia.dragonbutler.utils.TimeUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglj on 16/1/7.
 */
public class MessageChatAdapter extends BaseAdapter{
    private List<MessagesEntity> objects = new ArrayList<>();

    private Context context;
    private LayoutInflater layoutInflater;

    public MessageChatAdapter(Context context,List<MessagesEntity> objects) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
    }


    public void setData(List<MessagesEntity> objects){
        this.objects = objects;
        this.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public MessagesEntity getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        MessagesEntity messagesEntity = getItem(position);
        String messageType = messagesEntity.getType();
        return Integer.parseInt(messageType);
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type){
                case 1:
                    convertView = layoutInflater.inflate(R.layout.message_chat_send, null);
                    ViewHolder viewHolder1 = new ViewHolder();
                    viewHolder1.avatarImageView = (ImageView) convertView.findViewById(R.id.avatarImageView);
                    viewHolder1.chatFrom = (TextView) convertView.findViewById(R.id.chat_from);
                    viewHolder1.chatFromDate = (TextView) convertView.findViewById(R.id.chat_from_date);
                    viewHolder1.avatarUrl =((MessagesActivity)context).myAvatarUrl;
                    convertView.setTag(viewHolder1);
                    break;
                case 2:

                    convertView = layoutInflater.inflate(R.layout.message_chat_from, null);
                    ViewHolder viewHolder2 = new ViewHolder();
                    viewHolder2.avatarImageView = (ImageView) convertView.findViewById(R.id.avatarImageView);
                    viewHolder2.chatFrom = (TextView) convertView.findViewById(R.id.chat_from);
                    viewHolder2.chatFromDate = (TextView) convertView.findViewById(R.id.chat_from_date);
                    viewHolder2.avatarUrl = ((MessagesActivity)context).avatarUrl;
                    convertView.setTag(viewHolder2);
                    break;
            }

        }
        initializeViews(getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(MessagesEntity object, ViewHolder holder) {


        ImageLoader.getInstance().displayImage(holder.avatarUrl, holder.avatarImageView, DisplayOpitionFactory.sAvatarDisplayOption);
        holder.chatFrom.setText(object.getContent());
        holder.chatFromDate.setText(TimeUtils.getDateToString(object.getCreated_at()));

    }

    protected class ViewHolder {
        private ImageView avatarImageView;
        private TextView chatFrom;
        private TextView chatFromDate;
        private String avatarUrl;
    }
}
