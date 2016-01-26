package com.aosijia.dragonbutler.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.utils.Constants;
import com.aosijia.dragonbutler.utils.Uiutils;

/**
 * 信用卡
 * Created by Jacky on 2016/1/7.
 * Version 1.0
 */
public class CardActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_card);

        setTitle("信用卡", null, NO_RES_ID);

        int margin = Uiutils.dip2px(this, 13);
        int width = Uiutils.getScreenWidth(this) - margin*2;
        int height = (int) (width/5.394);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
        params.setMargins(margin, margin, margin, 0);

        findViewById(R.id.btn_apply).setLayoutParams(params);
        findViewById(R.id.btn_apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CommonWebViewActivity.class);
                intent.putExtra("url", Constants.URL_CARD_APPLY);
                intent.putExtra("title", "信用卡申请");
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_query).setLayoutParams(params);
        findViewById(R.id.btn_query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CommonWebViewActivity.class);
                intent.putExtra("url", Constants.URL_CARD_QUERY);
                intent.putExtra("title", "办卡进度查询");
                startActivity(intent);
            }
        });

    }

}
