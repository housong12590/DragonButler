package com.aosijia.dragonbutler.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.model.CCBActivitys;
import com.aosijia.dragonbutler.utils.DisplayOpitionFactory;
import com.aosijia.dragonbutler.utils.TimeUtils;
import com.aosijia.dragonbutler.utils.Uiutils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author hs
 *         优惠活动详情
 */
public class CCBActivitysDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_date;
    private TextView tv_target;
    private ImageView iv_themePic;
    private CCBActivitys.DataEntity.CcbActivitiesEntity data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = (CCBActivitys.DataEntity.CcbActivitiesEntity) getIntent().getSerializableExtra("data");
        setImmersionStatus();
        setContentView(R.layout.activity_ccbactivitys_detail);
        initView();
        setData();
    }

    private void initView() {
        setTitle("详情", null, R.drawable.btn_back, null, NO_RES_ID);
        setBtnLeftOnClickListener(this);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_target = (TextView) findViewById(R.id.tv_target);
        tv_content = (TextView) findViewById(R.id.tv_content);
        iv_themePic = (ImageView) findViewById(R.id.iv_themePic);
        int width = Uiutils.getScreenWidth(this);
        int height = (int) (Uiutils.getScreenWidth(this) / 2.5);
        iv_themePic.setLayoutParams(new LinearLayout.LayoutParams(width, height));
    }

    private void setData() {
        tv_title.setText(data.getTitle());
        tv_date.setText("活动时间: ");
        tv_date.append(Uiutils.getForegroundColorSpanString(this, TimeUtils.getDateToString2(data.getStart_date())
                + " 至 " + TimeUtils.getDateToString2(data.getEnd_date()), getResources().getColor(R.color.gray_94)));
        tv_target.setText("活动对象: ");
        tv_target.append(Uiutils.getForegroundColorSpanString(this, data.getActivity_target(),
                getResources().getColor(R.color.gray_94)));
        tv_content.setText(data.getContent());
        ImageLoader.getInstance().displayImage(data.getTheme_pic_url(), iv_themePic, DisplayOpitionFactory.sThemeDisplayOption);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
        }
    }
}
