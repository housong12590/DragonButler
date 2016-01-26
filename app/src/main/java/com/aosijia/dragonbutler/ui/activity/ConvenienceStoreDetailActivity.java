package com.aosijia.dragonbutler.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.model.ConvenienceStore;
import com.aosijia.dragonbutler.ui.widget.Kanner;
import com.aosijia.dragonbutler.utils.SystemUtils;
import com.aosijia.dragonbutler.utils.Uiutils;

/**
 * @author hs
 *         2016年1月19日11:06:13
 *         便利店详情
 */
public class ConvenienceStoreDetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title;
    private Kanner kanner;
    private TextView tv_price;
    private TextView tv_content;
    private LinearLayout ll_conn;
    private ConvenienceStore.DataEntity.CvsItemsEntity data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        data = (ConvenienceStore.DataEntity.CvsItemsEntity) getIntent().getSerializableExtra("data");
        setContentView(R.layout.activity_convenience_store_detail);
        initView();
        setData();
    }


    private void initView() {
        setTitle("商品详情", null, R.drawable.btn_back, null, NO_RES_ID);
        setBtnLeftOnClickListener(this);
        kanner = (Kanner) findViewById(R.id.kanner);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_price = (TextView) findViewById(R.id.tv_price);
        ll_conn = (LinearLayout) findViewById(R.id.ll_conn);
        tv_content = (TextView) findViewById(R.id.tv_content);
        ll_conn.setOnClickListener(this);

        int width = Uiutils.getScreenWidth(this);
        int height = (width * 2 / 3);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        kanner.setLayoutParams(params);
    }

    private void setData() {
        tv_title.setText(data.getTitle());
        tv_price.setText("￥ " + data.getPrice());
        tv_content.setText(data.getContent());
        if (data.getPic_urls().size() > 0) {
            kanner.setVisibility(View.VISIBLE);
            kanner.setImagesUrl(data.getPic_urls().toArray());
        } else {
            kanner.setVisibility(View.GONE);
        }
        if (data.getTelephone().length() == 0) {
            ll_conn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.ll_conn:
                SystemUtils.toCallPhoneActivity(this,data.getTelephone());
            break;
        }
    }
}
