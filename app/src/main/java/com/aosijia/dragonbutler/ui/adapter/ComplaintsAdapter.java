package com.aosijia.dragonbutler.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.model.CommunityAnnouncement;
import com.aosijia.dragonbutler.model.Complaints;
import com.aosijia.dragonbutler.ui.activity.BaseActivity;
import com.aosijia.dragonbutler.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class ComplaintsAdapter extends BaseAdapter {


    private Context context;
    private LayoutInflater layoutInflater;
    private List<Complaints.DataEntity.ComplaintsEntity> mData = new ArrayList<>();

    public ComplaintsAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Complaints.DataEntity.ComplaintsEntity getItem(int position) {
        return mData.get(position);
    }

    /**
     * 获取最后一条数据的ID
     * @return
     */
    public String getLastId(){
        if(getCount() > 0){
            return getItem(getCount() - 1).getComplaint_id();
        }else{
            return null;
        }

    }

    public void setComplaintsEntityList(List<Complaints.DataEntity.ComplaintsEntity> complaintsEntities,int type){
        if(type == BaseActivity.TYPE_LOAD_MORE){
            this.mData.addAll(complaintsEntities);
        }else{
            this.mData = complaintsEntities;
        }
        this.notifyDataSetChanged();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_pull_listview, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.complaintsTypeImageView = (ImageView) convertView.findViewById(R.id.itemTypeImageView);
            viewHolder.complaintsTitleTextView = (TextView) convertView.findViewById(R.id.itemTitleTextView);
            viewHolder.complaintsDateTextView = (TextView) convertView.findViewById(R.id.itemDateTextView);
            convertView.setTag(viewHolder);
        }
        initializeViews(getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(Complaints.DataEntity.ComplaintsEntity complaintsEntity, ViewHolder holder) {
        //TODO implement
        int resId = R.drawable.common_circle_pending;
        if("1".equals(complaintsEntity.getStatus())){
            resId = R.drawable.common_circle_pending;
        }else if("2".equals(complaintsEntity.getStatus())){
            resId = R.drawable.common_circle_processing;
        }else if("3".equals(complaintsEntity.getStatus())){
            resId = R.drawable.common_circle_processed;
        }else if("4".equals(complaintsEntity.getStatus())){
            resId = R.drawable.common_circle_completed;
        }
        holder.complaintsTypeImageView.setImageResource(resId);
        holder.complaintsTitleTextView.setText(complaintsEntity.getContent());
        holder.complaintsDateTextView.setText(TimeUtils.getDateToString(complaintsEntity.getCreated_at()));
    }

    protected class ViewHolder {
        private ImageView complaintsTypeImageView;
        private TextView complaintsTitleTextView;
        private TextView complaintsDateTextView;
    }
}
