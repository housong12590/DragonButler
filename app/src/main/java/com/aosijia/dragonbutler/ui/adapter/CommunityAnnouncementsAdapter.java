package com.aosijia.dragonbutler.ui.adapter;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.model.CommunityAnnouncement;
import com.aosijia.dragonbutler.model.CommunityAnnouncement.DataEntity.CommunityAnnouncementsEntity;
import com.aosijia.dragonbutler.ui.activity.BaseActivity;
import com.aosijia.dragonbutler.utils.TimeUtils;

public class CommunityAnnouncementsAdapter extends BaseAdapter {


    private Context context;
    private LayoutInflater layoutInflater;
    private List<CommunityAnnouncementsEntity> communityAnnouncementsEntityList = new ArrayList<>();

    public CommunityAnnouncementsAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setCommunityAnnouncementsEntityList(List<CommunityAnnouncementsEntity> communityAnnouncementsEntityList,int type){
        if(type == BaseActivity.TYPE_LOAD_MORE){
            this.communityAnnouncementsEntityList.addAll(communityAnnouncementsEntityList);
        }else{
            this.communityAnnouncementsEntityList = communityAnnouncementsEntityList;
        }
        this.notifyDataSetChanged();
    }

    /**
     * 获取最后一条数据的ID
     * @return
     */
    public String getLastId(){
        if(getCount() > 0){
            return getItem(getCount() - 1).getCommunity_announcement_id();
        }else{
            return null;
        }

    }

    @Override
    public int getCount() {
        return communityAnnouncementsEntityList.size();
    }

    @Override
    public CommunityAnnouncementsEntity getItem(int position) {
        return communityAnnouncementsEntityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_community_announcements, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.communityAnnouncementTypeImageView = (ImageView) convertView.findViewById(R.id.communityAnnouncementTypeImageView);
            viewHolder.communityAnnouncementTitleTextView = (TextView) convertView.findViewById(R.id.communityAnnouncementTitleTextView);
            viewHolder.communityAnnouncementDateTextView = (TextView) convertView.findViewById(R.id.communityAnnouncementDateTextView);
            convertView.setTag(viewHolder);
        }
        initializeViews(getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(CommunityAnnouncement.DataEntity.CommunityAnnouncementsEntity communityAnnouncementsEntity, ViewHolder holder) {
        //TODO implement
        int resId = R.drawable.communityannouncement_other_type;
        if("1".equals(communityAnnouncementsEntity.getType())){
            resId = R.drawable.communityannouncement_community_type;
        }else if("2".equals(communityAnnouncementsEntity.getType())){
            resId = R.drawable.communityannouncement_government_type;
        }else if("3".equals(communityAnnouncementsEntity.getType())){
            resId = R.drawable.communityannouncement_information_type;
        }else if("100".equals(communityAnnouncementsEntity.getType())){
            resId = R.drawable.communityannouncement_other_type;
        }
        holder.communityAnnouncementTypeImageView.setImageResource(resId);
        holder.communityAnnouncementTitleTextView.setText(communityAnnouncementsEntity.getTitle());
        holder.communityAnnouncementDateTextView.setText(TimeUtils.getDateToString(communityAnnouncementsEntity.getCreated_at()));
    }

    protected class ViewHolder {
        private ImageView communityAnnouncementTypeImageView;
        private TextView communityAnnouncementTitleTextView;
        private TextView communityAnnouncementDateTextView;
    }
}
