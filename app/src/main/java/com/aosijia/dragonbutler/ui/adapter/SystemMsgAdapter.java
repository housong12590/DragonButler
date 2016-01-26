package com.aosijia.dragonbutler.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.model.SystemMessages;
import com.aosijia.dragonbutler.ui.activity.BaseActivity;
import com.aosijia.dragonbutler.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hs
 *         <p/>
 *         2016/1/15 0015 15:29
 */
public class SystemMsgAdapter extends BaseAdapter {

    private Context mContext;
    private List<SystemMessages.DataEntity.SystemMessagesEntity> mData;

    public SystemMsgAdapter(Context context) {
        this.mContext = context;
        mData = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public SystemMessages.DataEntity.SystemMessagesEntity getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.system_msg_item, null);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SystemMessages.DataEntity.SystemMessagesEntity entity = mData.get(position);
        holder.tv_content.setText(entity.getContent());
        holder.tv_date.setText(TimeUtils.getDateToString(entity.getCreated_at()));
        return convertView;
    }

    public void refresh(List<SystemMessages.DataEntity.SystemMessagesEntity> data,int type) {
        if(type == BaseActivity.TYPE_LOAD_MORE){
            this.mData.addAll(data);
        }else{
            this.mData = data;
        }
        this.notifyDataSetChanged();
    }

    /**
     * 获取最后一条数据的ID
     * @return
     */
    public String getLastId(){
        if(getCount() > 0){
            return getItem(getCount() - 1).getSystem_message_id();
        }else{
            return null;
        }

    }

    private class ViewHolder {
        TextView tv_content;
        TextView tv_date;
    }
}
