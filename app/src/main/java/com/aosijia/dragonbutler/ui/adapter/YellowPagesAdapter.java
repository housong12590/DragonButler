package com.aosijia.dragonbutler.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.model.YellowPages;

import java.util.List;

/**
 * @author hs
 *         <p/>
 *         2016/1/14 0014 18:31
 */
public class YellowPagesAdapter extends BaseAdapter {

    private Context context;
    private List<YellowPages.DataEntity.YellowPagesEntity> mDatas;

    public YellowPagesAdapter(Context context,List<YellowPages.DataEntity.YellowPagesEntity> datas){
        this.context = context;
        this.mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public YellowPages.DataEntity.YellowPagesEntity getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.yellow_pages_item,null);
            holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.iv_phone = (ImageView) convertView.findViewById(R.id.iv_phone);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        final YellowPages.DataEntity.YellowPagesEntity entity = mDatas.get(position);
        holder.tv_phone.setText(String.valueOf(entity.getTelephone()));
        holder.tv_title.setText(String.valueOf(entity.getTitle()));
        holder.iv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("tel:" + mDatas.get(position).getTelephone());
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
            }
        });
        return convertView;
    }

    private class ViewHolder{
        TextView tv_title;
        TextView tv_phone;
        ImageView iv_phone;
    }
}
