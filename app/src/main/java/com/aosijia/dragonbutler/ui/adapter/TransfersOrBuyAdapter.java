package com.aosijia.dragonbutler.ui.adapter;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.imagegroup.ImageGroupView;
import com.aosijia.dragonbutler.model.SecondHandItems.DataEntity.SecondhandItemsEntity;
import com.aosijia.dragonbutler.ui.activity.BaseActivity;
import com.aosijia.dragonbutler.ui.widget.CircleImageView;
import com.aosijia.dragonbutler.utils.TimeUtils;
import com.aosijia.dragonbutler.utils.Uiutils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglj on 15/12/29.
 */
public class TransfersOrBuyAdapter extends BaseAdapter {

    private List<SecondhandItemsEntity> list = new ArrayList<>();

    private LayoutInflater layoutInflater;

    public TransfersOrBuyAdapter(FragmentActivity context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    /**
     * 获取最后一条数据的ID
     * @return
     */
    public String getLastId(){
        if(getCount() > 0){
            return getItem(getCount() - 1).getSecondhand_item_id();
        }else{
            return null;
        }

    }

    public void setTransfersOrBuyList(List<SecondhandItemsEntity> list, int type){
        if(type == BaseActivity.TYPE_LOAD_MORE){
            this.list.addAll(list);
        }else{
            this.list = list;
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public SecondhandItemsEntity getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_transfers, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.avatarImageView = (CircleImageView) convertView.findViewById(R.id.avatarImageView);
            viewHolder.statusImageView = (ImageView) convertView.findViewById(R.id.statusImageView);
            viewHolder.dateTextView = (TextView) convertView.findViewById(R.id.dateTextView);
            viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.nameTextView);
            viewHolder.contentTextView = (TextView) convertView.findViewById(R.id.contentTextView);
            viewHolder.priceTextView = (TextView) convertView.findViewById(R.id.priceTextView);
            viewHolder.imageGroupView = (ImageGroupView) convertView.findViewById(R.id.igv_pic);
            convertView.setTag(viewHolder);
        }
        initializeViews(getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(SecondhandItemsEntity secondhandItemsEntity, ViewHolder holder) {

        //头像
        ImageLoader.getInstance().getInstance().displayImage(secondhandItemsEntity.getAuthor().getAvatar_url(), holder.avatarImageView, Uiutils.displayImageOptions);

        //图片显示处理
        List<String> imageList = secondhandItemsEntity.getPic_urls();

        //状态
        if ("1".equals(secondhandItemsEntity.getStatus())) {
//            imageList.clear();
            holder.statusImageView.setVisibility(View.GONE);
        } else if ("2".equals(secondhandItemsEntity.getStatus())) {
//            imageList.addAll(imageList);
            holder.statusImageView.setVisibility(View.VISIBLE);
            holder.statusImageView.setImageResource(R.drawable.ic_secondhand_complete);
        }
        //日期
        holder.dateTextView.setText(TimeUtils.getDateToString(secondhandItemsEntity.getCreated_at()));
        //昵称
        holder.nameTextView.setText(secondhandItemsEntity.getAuthor().getNickname());
        //标题
        holder.contentTextView.setText(secondhandItemsEntity.getTitle());

        //价格
        float price = secondhandItemsEntity.getPrice();
        if (price >= 0) {
            holder.priceTextView.setText("￥" + price);
        } else {
            holder.priceTextView.setText("￥面议");
        }
//        //图片显示处理
//        List<String> imageList = secondhandItemsEntity.getPic_urls();
//        imageList.addAll(imageList);


        //itemimage 最多显示三个图片
        if (imageList.size() > 0) {
            holder.imageGroupView.setVisibility(View.VISIBLE);
            if (imageList.size() > 3) {
                imageList = imageList.subList(0, 3);
            }
            holder.imageGroupView.setNetworkPhotos(imageList);
        } else {
            holder.imageGroupView.setVisibility(View.GONE);
        }


    }

    protected class ViewHolder {
        private CircleImageView avatarImageView;
        private ImageView statusImageView;
        private TextView dateTextView;
        private TextView nameTextView;
        private TextView contentTextView;
        private TextView priceTextView;
        private ImageGroupView imageGroupView;
    }

}
