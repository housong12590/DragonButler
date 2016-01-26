package com.aosijia.dragonbutler.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.utils.TimeUtils;

public class SystemMessagesDetailActivity extends BaseActivity implements View.OnClickListener {

    private String content;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("content") && bundle.containsKey("date")) {
            content = bundle.getString("content");
            date = bundle.getString("date");

        }

        setContentView(R.layout.activity_system_messages_detail);
        setTitle("消息详情", null, R.drawable.btn_back, null, NO_RES_ID);
        setBtnLeftOnClickListener(this);
        TextView tv_content = (TextView) findViewById(R.id.tv_content);
        tv_content.setText(content);
        TextView tv_date = (TextView) findViewById(R.id.tv_date);
        tv_date.setText(TimeUtils.getDateToString(date));
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
