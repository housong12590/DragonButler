package com.aosijia.dragonbutler.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.model.MaintenanceOrders;
import com.aosijia.dragonbutler.ui.activity.BaseActivity;
import com.aosijia.dragonbutler.utils.TimeUtils;
import com.aosijia.dragonbutler.utils.Uiutils;

import java.util.ArrayList;
import java.util.List;

/**
 * 报修列表adapter
 */
public class MaintenanceAdapter extends BaseAdapter {

    private List<MaintenanceOrders.DataEntity.MaintenaceOrdersEntity> mData = new ArrayList<>();
    private Context mContext;

    public MaintenanceAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * 获取最后一条数据的ID
     * @return
     */
    public String getLastId(){
        if(getCount() > 0){
            return getItem(getCount() - 1).getMaintenance_order_id();
        }else{
            return null;
        }

    }

    public void setComplaintsEntityList(List<MaintenanceOrders.DataEntity.MaintenaceOrdersEntity> maintenaceOrdersEntities, int type){
        if(type == BaseActivity.TYPE_LOAD_MORE){
            this.mData.addAll(maintenaceOrdersEntities);
        }else{
            this.mData = maintenaceOrdersEntities;
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public MaintenanceOrders.DataEntity.MaintenaceOrdersEntity getItem(int position) {
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
            convertView = View.inflate(mContext, R.layout.item_pull_listview, null);
            holder.itemDateTextView = (TextView) convertView.findViewById(R.id.itemDateTextView);
            holder.itemTitleTextView = (TextView) convertView.findViewById(R.id.itemTitleTextView);
            holder.itemTypeImageView = (ImageView) convertView.findViewById(R.id.itemTypeImageView);
            holder.itemType = (TextView) convertView.findViewById(R.id.itemType);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //图片
        int resId = R.drawable.common_circle_pending; // 默认未处理
        if ("1".equals(getItem(position).getStatus())) {
            resId = R.drawable.common_circle_pending;
        } else if ("2".equals(getItem(position).getStatus())) {
            resId = R.drawable.common_circle_processing;
        } else if ("3".equals(getItem(position).getStatus())) {
            resId = R.drawable.common_circle_processed;
        } else if ("4".equals(getItem(position).getStatus())) {
            resId = R.drawable.common_circle_completed;
        }
        holder.itemTypeImageView.setImageResource(resId);
        //类型
        String[] type = mContext.getResources().getStringArray(R.array.maintenance_type);
        switch (Integer.parseInt(getItem(position).getType())) {
            case 1://供水报修
                holder.itemType.setText(type[0]);
                setMaintenanceOrdersDateTypeColor(holder, R.color.blue);
                break;
            case 2://供电报修
                holder.itemType.setText(type[1]);
                setMaintenanceOrdersDateTypeColor(holder, R.color.pink);
                break;
            case 3://燃气报修
                holder.itemType.setText(type[2]);
                setMaintenanceOrdersDateTypeColor(holder, R.color.brown);
                break;
            case 100://其他报修
                holder.itemType.setText(type[3]);
                setMaintenanceOrdersDateTypeColor(holder, R.color.green);
                break;
        }
        holder.itemDateTextView.setText(TimeUtils.getDateToString(getItem(position).getCreated_at()));
        holder.itemTitleTextView.setText(getItem(position).getContent());
        return convertView;
    }

    private void setMaintenanceOrdersDateTypeColor(ViewHolder holder, int color) {
        holder.itemType.setBackgroundDrawable(Uiutils.createShape(mContext, getResourcesColor(color)));
    }

    private int getResourcesColor(int color) {
        return mContext.getResources().getColor(color);
    }


    class ViewHolder {
        ImageView itemTypeImageView;
        TextView itemTitleTextView;
        TextView itemDateTextView;
        TextView itemType;
    }

}
