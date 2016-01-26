package com.aosijia.dragonbutler.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.ui.widget.LoadingDialogFragment;
import com.bugtags.library.Bugtags;

import java.util.LinkedList;
import cn.jpush.android.api.JPushInterface;

/**
 * 基类
 * Created by wanglj on 15/12/11.
 */
public class BaseActivity extends AppCompatActivity {

    public static final int NO_RES_ID = -1;


    public RelativeLayout titleParentlayout;
    private TextView titleLefttextview;
    private ImageView titleLeftimageview;
    private TextView titleCentertextview;
    private ImageView titleCenterimageview;
    public TextView titleRighttextview;
    private ImageView titleRightimageview;

    public LoadingDialogFragment mDialogFragment;


    public static LinkedList<Activity> activities = new LinkedList<>();


    public static final int TYPE_LOAD_MORE = 3;
    public static final int TYPE_LOAD_REFRESH = 2;
    public static final int TYPE_LOAD_FIRST = 1;
    public static final int PAGE_SIZE = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activities.add(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activities.remove(this);
    }

    protected void killAll() {
        for (int i = 0; i < activities.size(); i++) {
            activities.get(i).finish();
        }
    }

    /**
     * 不需要登录的Activity在此添加
     */
    public static final String[] NOTNEEDCHECKLOGINACTIVITY = {
            MainTabActivity.class.getName(), LoginActivity.class.getName(), HouseHoldBindActivity.class.getName(),
            SettingActivity.class.getName(),AboutActivity.class.getName()
    };


    @Override
    protected void onResume() {
        super.onResume();
        //注：回调 1
        Bugtags.onResume(this);
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //注：回调 2
        Bugtags.onPause(this);
        JPushInterface.onPause(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //注：回调 3
        Bugtags.onDispatchTouchEvent(this, event);
        return super.dispatchTouchEvent(event);
    }


    /**
     * FLAG_TRANSLUCENT_STATUS
     */
    public void setImmersionStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        if (intent.getComponent() != null) {
            String className = intent.getComponent().getClassName();
            if (!TextUtils.isEmpty(className) && isNeedCheckLogin(className)) {
                //检查是否设置过用户信息
                LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
                if (loginResp == null) {
                    intent.putExtra("action", className);
                    intent.setClass(this, LoginActivity.class);
                } else if (!loginResp.getData().getUser_info().isIs_bound()) {
                    intent.putExtra("action", className);
                    intent.setClass(this, HouseHoldBindActivity.class);
                }
            }
        }

        super.startActivity(intent);
    }


    /**
     * 检查当前启动的activity 是否需要检查登录状态
     *
     * @param name
     * @return false:不要检查登录状态 true 需要检查登录状态
     */
    public static boolean isNeedCheckLogin(String name) {
        for (String className : NOTNEEDCHECKLOGINACTIVITY) {
            if (className.equals(name)) {
                return false;
            }
        }
        return true;
    }


    public void setTitle(String title) {
        setTitle(title, null, NO_RES_ID, null, NO_RES_ID);
    }

    public void setTitle(String title, String rightTitle, int rightResId) {
        setTitle(title, null, R.drawable.btn_back, rightTitle, rightResId);
        setBtnLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void setTitleVisible(int visible) {
        if (visible == View.VISIBLE) {
            findViewById(R.id.rootLayout).setBackgroundResource(R.color.colorPrimary);
        } else {
            findViewById(R.id.rootLayout).setBackgroundResource(R.color.colorMeTitle);
        }

        titleParentlayout.setVisibility(visible);
    }


    /**
     * @param title      标题
     * @param leftTitle  左侧按钮文字 不显示为null
     * @param leftResId  左侧按钮图片 不显示为 {@link BaseActivity#NO_RES_ID}
     * @param rightTitle 右侧按钮文字 不显示为null
     * @param rightResId 右侧按钮图片 不显示为 {@link BaseActivity#NO_RES_ID}
     */
    public void setTitle(String title, String leftTitle, int leftResId, String rightTitle, int rightResId) {

        titleParentlayout = (RelativeLayout) findViewById(R.id.title_parentlayout);
        titleLefttextview = (TextView) findViewById(R.id.title_lefttextview);
        titleLeftimageview = (ImageView) findViewById(R.id.title_leftimageview);
        titleCentertextview = (TextView) findViewById(R.id.title_centertextview);
        titleCenterimageview = (ImageView) findViewById(R.id.title_centerimageview);
        titleRighttextview = (TextView) findViewById(R.id.title_righttextview);
        titleRightimageview = (ImageView) findViewById(R.id.title_rightimageview);

        if (!TextUtils.isEmpty(leftTitle)) {
            titleLefttextview.setVisibility(View.VISIBLE);
            titleLefttextview.setText(leftTitle);
        } else {
            titleLefttextview.setVisibility(View.GONE);
        }


        if (leftResId != NO_RES_ID) {
            titleLeftimageview.setVisibility(View.VISIBLE);
            titleLeftimageview.setImageResource(leftResId);
        } else {
            titleLeftimageview.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(rightTitle)) {
            titleRighttextview.setVisibility(View.VISIBLE);
            titleRighttextview.setText(rightTitle);
        } else {
            titleRighttextview.setVisibility(View.GONE);
        }

        if (rightResId != NO_RES_ID) {
            titleRightimageview.setVisibility(View.VISIBLE);
            titleRightimageview.setImageResource(rightResId);
        } else {
            titleRightimageview.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(title)) {
            titleCentertextview.setVisibility(View.VISIBLE);
            titleCentertextview.setText(title);
        } else {
            titleCentertextview.setVisibility(View.GONE);
        }

    }

    public void setBtnLeftOnClickListener(View.OnClickListener listener) {
        if (titleLefttextview.getVisibility() == View.VISIBLE) {
            titleLefttextview.setOnClickListener(listener);
        } else {
            titleLeftimageview.setOnClickListener(listener);
        }
    }


    public void setBtnRightTextOnClickListener(View.OnClickListener listener) {
        if (titleRighttextview.getVisibility() == View.VISIBLE) {
            titleRighttextview.setOnClickListener(listener);
        } else {
            titleRightimageview.setOnClickListener(listener);
        }
    }

    public String getTextNoSpace(TextView tv) {
        return tv.getText().toString().trim();
    }

    public void setRightImage(int resId) {
        if (resId == NO_RES_ID) {
            if (titleRightimageview.getVisibility() != View.GONE) {

            }
        } else {
            if (titleRightimageview.getVisibility() != View.VISIBLE) {
                titleRightimageview.setVisibility(View.VISIBLE);
            }
            titleRightimageview.setImageResource(resId);
        }
    }


    /**
     * 显示加载中对话框
     *
     * @param message
     */
    protected void showProgressDialow(String message) {
        mDialogFragment = (LoadingDialogFragment) getSupportFragmentManager().findFragmentByTag(LoadingDialogFragment.class.getSimpleName());

        if (mDialogFragment == null) {
            mDialogFragment = new LoadingDialogFragment();
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mDialogFragment.show(ft, message);
        mDialogFragment.setCancelable(false);
    }


    /**
     * 销毁对话框
     */
    protected void dismissProgressDialog() {
        if (mDialogFragment != null && mDialogFragment.isAdded()) {
            mDialogFragment.dismissAllowingStateLoss();
        }
    }


    /**
     * 网络请求错误提示
     */
    protected void showRequestError() {
        Toast.makeText(this, "网络请求异常,请检查网络配置", Toast.LENGTH_SHORT).show();
    }


}
