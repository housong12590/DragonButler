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
import com.aosijia.dragonbutler.ui.activity.CCBLifeActivity;
import com.aosijia.dragonbutler.ui.activity.CCBActivitisActivity;
import com.aosijia.dragonbutler.ui.activity.CommonWebViewActivity;
import com.aosijia.dragonbutler.ui.activity.SecondhandActivity;
import com.aosijia.dragonbutler.ui.adapter.ConveniencesAdapter;
import com.aosijia.dragonbutler.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglj on 16/1/14.
 */
public class ConveniencesFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private List<Icon> dataList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_conveniences, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.conveniencesRecyclerView);
        initData();
        ConveniencesAdapter adapter = new ConveniencesAdapter(dataList);
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

        adapter.setOnItemClickListener(new ConveniencesAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                switch (postion) {
                    case 0://二手市场
                        startActivity(new Intent(getActivity(), SecondhandActivity.class));
                        break;
                    case 1://悦生活
                        startActivity(new Intent(getActivity(), CCBLifeActivity.class));
                        break;
                    case 2://优惠活动
                        startActivity(new Intent(getActivity(), CCBActivitisActivity.class));
                        break;
                    case 3://楼盘
                        Intent intent_build= new Intent(getActivity(), CommonWebViewActivity.class);
                        intent_build.putExtra(CommonWebViewActivity.EXTRA_KEY_TITLE,"楼盘");
                        intent_build.putExtra(CommonWebViewActivity.EXTRA_KEY_URL, Constants.URL_BUILDINGS);
                        startActivity(intent_build);
                        break;
                    case 4://家政资讯

                        break;
                    case 5://业主拼车

                        break;

                    case 6://快递插叙
                        Intent intent = new Intent(getActivity(), CommonWebViewActivity.class);
                        intent.putExtra(CommonWebViewActivity.EXTRA_KEY_URL,"http://m.kuaidi100.com/");
                        intent.putExtra(CommonWebViewActivity.EXTRA_KEY_TITLE,"快递查询");
                        startActivity(intent);
                        break;
                    case 7://天气查询
                        Intent intent1 = new Intent(getActivity(), CommonWebViewActivity.class);
                        intent1.putExtra(CommonWebViewActivity.EXTRA_KEY_URL,"http://m.weather.com.cn/mweather");
                        intent1.putExtra(CommonWebViewActivity.EXTRA_KEY_TITLE,"天气查询");
                        startActivity(intent1);
                        break;
                }
            }
        });
        return v;
    }

    private void initData() {
        Icon icon = new Icon();
        icon.setResourceId(R.drawable.convenience_secondhand_button);
        icon.setTitle("二手市场");
        dataList.add(icon);

        icon = new Icon();
        icon.setResourceId(R.drawable.convenience_life_button);
        icon.setTitle("悦生活");
        dataList.add(icon);

        icon = new Icon();
        icon.setResourceId(R.drawable.convenience_event_button);
        icon.setTitle("优惠活动");
        dataList.add(icon);

        icon = new Icon();
        icon.setResourceId(R.drawable.convenience_houses_button);
        icon.setTitle("楼盘");
        dataList.add(icon);

        icon = new Icon();
        icon.setResourceId(R.drawable.convenience_homeservice_button);
        icon.setTitle("家政资讯");
        dataList.add(icon);

        icon = new Icon();
        icon.setResourceId(R.drawable.convenience_car_button);
        icon.setTitle("业主拼车");
        dataList.add(icon);

        icon = new Icon();
        icon.setResourceId(R.drawable.convenience_express_button);
        icon.setTitle("快递查询");
        dataList.add(icon);

        icon = new Icon();
        icon.setResourceId(R.drawable.convenience_weather_button);
        icon.setTitle("天气查询");
        dataList.add(icon);


    }
}
