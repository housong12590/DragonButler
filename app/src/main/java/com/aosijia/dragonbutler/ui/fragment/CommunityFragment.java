package com.aosijia.dragonbutler.ui.fragment;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.model.Icon;
import com.aosijia.dragonbutler.ui.activity.ComplaintsActivity;
import com.aosijia.dragonbutler.ui.activity.ConvenienceStoreActivity;
import com.aosijia.dragonbutler.ui.activity.YellowPagesActivity;
import com.aosijia.dragonbutler.ui.adapter.CommunityAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglj on 16/1/14.
 */
public class CommunityFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private List<Icon> dataList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_community, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.communityRecyclerView);
        initData();
        CommunityAdapter adapter = new CommunityAdapter(dataList);
        recyclerView.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                Paint paint = new Paint();
                paint.setColor(parent.getContext().getResources().getColor(R.color.gray_94));

                //获得RecyclerView中总条目数量
                int childCount = parent.getChildCount();

                //遍历一下
                for (int i = 0; i < childCount; i++) {
                    //获得子View，也就是一个条目的View，准备给他画上边框
                    View childView = parent.getChildAt(i);

                    //先获得子View的长宽，以及在屏幕上的位置，方便我们得到边框的具体坐标
                    float x = childView.getX();
                    float y = childView.getY();
                    int width = childView.getWidth();
                    int height = childView.getHeight();

                    //根据这些点画条目的四周的线
                    c.drawLine(x, y, x + width, y, paint);
                    c.drawLine(x, y, x, y + height, paint);
                    c.drawLine(x + width, y, x + width, y + height, paint);
                    c.drawLine(x, y + height, x + width, y + height, paint);

                }
                super.onDraw(c, parent, state);
            }
        });

        adapter.setOnItemClickListener(new CommunityAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                switch (postion) {
                    case 0://投诉
                        startActivity(new Intent(getActivity(), ComplaintsActivity.class));
                        break;
                    case 1://便利店
                        startActivity(new Intent(getActivity(), ConvenienceStoreActivity.class));
                        break;
                    case 2://网点
                        break;
                    case 3://黄页
                        startActivity(new Intent(getActivity(), YellowPagesActivity.class));
                        break;
                }
            }
        });
        return v;
    }

    private void initData() {
        Icon icon = new Icon();
        icon.setResourceId(R.drawable.community_complaint_button);
        icon.setTitle("投诉");
        dataList.add(icon);

        icon = new Icon();
        icon.setResourceId(R.drawable.community_store_button);
        icon.setTitle("便利店");
        dataList.add(icon);

        icon = new Icon();
        icon.setResourceId(R.drawable.community_webpoint_button);
        icon.setTitle("周边网点");
        dataList.add(icon);

        icon = new Icon();
        icon.setResourceId(R.drawable.community_pages_button);
        icon.setTitle("黄页");
        dataList.add(icon);


    }
}
