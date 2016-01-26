package com.aosijia.dragonbutler.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.model.CCBActivitys;
import com.aosijia.dragonbutler.ui.activity.BaseActivity;
import com.aosijia.dragonbutler.utils.TimeUtils;
import com.aosijia.dragonbutler.utils.Uiutils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hs
 *         <p/>
 *         2016/1/18 0018 10:24
 */
public class CCBActivitysAdapter extends BaseAdapter {

    private Context context;
    private List<CCBActivitys.DataEntity.CcbActivitiesEntity> mData;


    public CCBActivitysAdapter(Context context) {
        this.context = context;
        mData = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public CCBActivitys.DataEntity.CcbActivitiesEntity getItem(int position) {
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
            convertView = View.inflate(context, R.layout.item_ccb_activitys_listview, null);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_target = (TextView) convertView.findViewById(R.id.tv_target);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CCBActivitys.DataEntity.CcbActivitiesEntity entity = mData.get(position);
        holder.tv_title.setText(entity.getTitle());
        holder.tv_date.setText("");
        holder.tv_date.append("活动时间 :");
        holder.tv_date.append(Uiutils.getForegroundColorSpanString(context, TimeUtils.getDateToString2(entity.getStart_date())
                + "~" + TimeUtils.getDateToString2(entity.getEnd_date()), context.getResources().getColor(R.color.gray_94)));
        holder.tv_target.setText("活动对象 :");
        holder.tv_target.append(Uiutils.getForegroundColorSpanString(context, entity.getActivity_target(),
                context.getResources().getColor(R.color.gray_94)));
        return convertView;
    }


    public void refresh(List<CCBActivitys.DataEntity.CcbActivitiesEntity> data, int type) {
        if (type == BaseActivity.TYPE_LOAD_MORE) {
            this.mData.addAll(data);
        } else {
            this.mData = data;
        }
        notifyDataSetChanged();
    }

    public String getLastId() {
        if (getCount() > 0) {
            return getItem(getCount() - 1).getCcb_activity_id();
        } else {
            return null;
        }
    }


    private class ViewHolder {
        TextView tv_date;
        TextView tv_title;
        TextView tv_target;
    }
}
