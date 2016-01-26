package com.aosijia.dragonbutler.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.aosijia.dragonbutler.R;


/**
 * 启动页
 * Created by wanglj on 15/12/11.
 */
public class LoadingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_loading);


        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                startActivity(new Intent(LoadingActivity.this, MainTabActivity.class));
                LoadingActivity.this.finish();
            }
        };
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(100);
            }
        },2000);


//        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////
////                ActionSheet.createBuilder(LoadingActivity.this, getSupportFragmentManager())
////                        .setCancelButtonTitle("取消")
////                        .setOtherButtonTitles("相册", "拍照")
////                        .setCancelableOnTouchOutside(true)
////                        .setListener(new ActionSheet.ActionSheetListener() {
////                            @Override
////                            public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
////
////                            }
////
////                            @Override
////                            public void onOtherButtonClick(ActionSheet actionSheet, int index) {
////                                if (index == 0) {
////                                } else if (index == 1) {
////                                }
////                            }
////                        }).show();
//                startActivity(new Intent(LoadingActivity.this, MainTabActivity.class));
//                overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_bottom);
//            }
//        });

//        findViewById(R.id.imagegroup).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LoadingActivity.this, ImageGroupDemo.class));
//
//            }
//        });


//        findViewById(R.id.baoxiuliebiao).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LoadingActivity.this, MaintenanceActivity.class));
//            }
//        });
//
//        findViewById(R.id.complaint).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LoadingActivity.this,ComplaintsActivity.class));
//            }
//        });



    }

}
