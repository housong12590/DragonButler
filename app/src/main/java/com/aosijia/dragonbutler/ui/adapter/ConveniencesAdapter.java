package com.aosijia.dragonbutler.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.model.Icon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglj on 16/1/14.
 */
public class ConveniencesAdapter extends RecyclerView.Adapter {

    //数据源
    private List<Icon> dataList = new ArrayList<>();

    public ConveniencesAdapter(List<Icon> dataList) {
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new BodyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_community_recyclerview, parent, false));
    }


    /**
     * 给GridView中的条目用的ViewHolder，里面只有一个TextView
     */
    public class BodyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textView;
        private ImageView imageView;

        public BodyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_tv);
            imageView = (ImageView) itemView.findViewById(R.id.item_image);
            itemView.setOnClickListener(this);
        }

        public TextView getTextView() {
            return textView;
        }

        public ImageView getImageView() {
            return imageView;
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public interface MyItemClickListener {
        void onItemClick(View view, int postion);
    }

    private MyItemClickListener mItemClickListener;

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BodyViewHolder) holder).getTextView().setText(dataList.get(position).getTitle());
        ((BodyViewHolder) holder).getImageView().setImageResource(dataList.get(position).getResourceId());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
