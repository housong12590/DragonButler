package com.aosijia.dragonbutler.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.model.MaintenanceOrders;
import com.aosijia.dragonbutler.model.PropertyBill;
import com.aosijia.dragonbutler.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class PropertyBillsAdapter extends BaseAdapter {


    private LayoutInflater layoutInflater;
    private List<PropertyBill.DataEntity.PropertyBillsEntity> propertyBills = new ArrayList<>();

    public PropertyBillsAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    /**
     * 获取最后一条数据的ID
     * @return
     */
    public String getLastId(){
        if(getCount() > 0){
            return getItem(getCount() - 1).getBill_id();
        }else{
            return null;
        }

    }

    public void setComplaintsEntityList(List<PropertyBill.DataEntity.PropertyBillsEntity> propertyBillsEntities, int type){
        if(type == BaseActivity.TYPE_LOAD_MORE){
            this.propertyBills.addAll(propertyBillsEntities);
        }else{
            this.propertyBills = propertyBillsEntities;
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return propertyBills.size();
    }

    @Override
    public PropertyBill.DataEntity.PropertyBillsEntity getItem(int position) {
        return propertyBills.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_property_bills, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.propertyBillsTypeImageView = (ImageView) convertView.findViewById(R.id.propertyBillsTypeImageView);
            viewHolder.propertyBillsTitleTextView = (TextView) convertView.findViewById(R.id.propertyBillsTitleTextView);
            viewHolder.propertyBillsMoneyTextView = (TextView) convertView.findViewById(R.id.propertyBillsMoneyTextView);

            convertView.setTag(viewHolder);
        }
        initializeViews(getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(PropertyBill.DataEntity.PropertyBillsEntity propertyBillsEntity, ViewHolder holder) {
        //TODO implement
        int resId = R.drawable.propertyill_other_type;
        if("1".equals(propertyBillsEntity.getType())){
            resId = R.drawable.propertyill_global_type;
        }else if("2".equals(propertyBillsEntity.getType())){
            resId = R.drawable.propertyill_park_type;
        }else if("3".equals(propertyBillsEntity.getType())){
            resId = R.drawable.propertyill_water_type;
        }else if("4".equals(propertyBillsEntity.getType())){
            resId = R.drawable.propertyill_health_type;
        }
        holder.propertyBillsTypeImageView.setImageResource(resId);
        holder.propertyBillsTitleTextView.setText(propertyBillsEntity.getTitle());
        holder.propertyBillsMoneyTextView.setText("¥"+propertyBillsEntity.getAmount());
    }

    protected class ViewHolder {
        private ImageView propertyBillsTypeImageView;
        private TextView propertyBillsTitleTextView;
        private TextView propertyBillsMoneyTextView;
    }
}
