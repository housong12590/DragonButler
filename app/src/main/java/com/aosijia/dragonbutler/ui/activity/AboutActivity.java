package com.aosijia.dragonbutler.ui.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;

/**
 * 关于我们
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_appicon;
    private TextView tv_appname;
    private TextView tv_versions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_about);
        initView();
        initData();
    }

    private void initView() {
        setTitle("关于我们", null, R.drawable.btn_back, null, NO_RES_ID);
        setBtnLeftOnClickListener(this);
        iv_appicon = (ImageView) findViewById(R.id.iv_appicon);
        tv_appname = (TextView) findViewById(R.id.tv_appname);
        tv_versions = (TextView) findViewById(R.id.tv_versions);
    }

    private void initData() {
        PackageInfo appInfo = getAppInfo();
        tv_appname.setText(appInfo.applicationInfo.labelRes);
        tv_versions.setText("版本号  v " + appInfo.versionName);
        iv_appicon.setBackgroundResource(appInfo.applicationInfo.icon);
    }

    private PackageInfo getAppInfo() {
        PackageManager pm = getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo;
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
