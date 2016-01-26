package com.aosijia.dragonbutler.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.model.ConvenienceStore;
import com.aosijia.dragonbutler.ui.activity.BaseActivity;
import com.aosijia.dragonbutler.utils.DisplayOpitionFactory;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : hs on 2016/1/18 0018 16:54
 */
public class ConvenienceStoreAdapter extends BaseAdapter {

    private Context context;
    private List<ConvenienceStore.DataEntity.CvsItemsEntity> mData;

    public ConvenienceStoreAdapter(Context context) {
        this.context = context;
        mData = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public ConvenienceStore.DataEntity.CvsItemsEntity getItem(int position) {
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
            convertView = View.inflate(context, R.layout.item_convenience_store, null);
            holder.iv_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ConvenienceStore.DataEntity.CvsItemsEntity entity = mData.get(position);
        holder.tv_title.setText(entity.getTitle());
        holder.tv_price.setText("￥ " + entity.getPrice());
        if (entity.getPic_urls().size() != 0) {
            ImageLoader.getInstance().displayImage(entity.getPic_urls().get(0), holder.iv_pic,
                    DisplayOpitionFactory.sItemDisplayOption);
        } else {
            holder.iv_pic.setVisibility(View.GONE);
        }
        return convertView;
    }

    public void refresh(List<ConvenienceStore.DataEntity.CvsItemsEntity> data, int type) {
        if (type == BaseActivity.TYPE_LOAD_MORE) {
            mData.addAll(data);
        } else {
            mData = data;
        }
        notifyDataSetChanged();
    }

    public String getLastId() {
        if (getCount() > 0) {
            return getItem(getCount() - 1).getCvs_item_id();
        } else {
            return null;
        }
    }

    private class ViewHolder {
        ImageView iv_pic;
        TextView tv_title;
        TextView tv_price;
    }
}
