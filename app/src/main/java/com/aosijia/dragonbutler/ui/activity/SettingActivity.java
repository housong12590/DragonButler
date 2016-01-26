package com.aosijia.dragonbutler.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.model.Version;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.service.ApkDownService;
import com.aosijia.dragonbutler.ui.widget.CommonDialog;
import com.aosijia.dragonbutler.ui.widget.SwitchButton;
import com.aosijia.dragonbutler.utils.FileUtils;
import com.aosijia.dragonbutler.utils.SystemUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.io.File;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_cache_size;
    private SwitchButton iv_push_switch;
    private TextView tv_current_versions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_setting);
        initView();
        initData();
    }

    private void initView() {
        setTitle("设置", null, R.drawable.btn_back, null, NO_RES_ID);
        setBtnLeftOnClickListener(this);
        findViewById(R.id.ll_push).setOnClickListener(this);
        findViewById(R.id.ll_share).setOnClickListener(this);
        findViewById(R.id.ll_cache).setOnClickListener(this);
        findViewById(R.id.ll_about).setOnClickListener(this);
        findViewById(R.id.bt_logout).setOnClickListener(this);
        findViewById(R.id.ll_opinion).setOnClickListener(this);
        findViewById(R.id.ll_versions).setOnClickListener(this);
        tv_cache_size = (TextView) findViewById(R.id.tv_cache_size);
        iv_push_switch = (SwitchButton) findViewById(R.id.iv_push_switch);
        if (JPushInterface.isPushStopped(this)) {
            iv_push_switch.setChecked(false);
        } else {
            iv_push_switch.setChecked(true);
        }
        iv_push_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            JPushInterface.resumePush(SettingActivity.this);
                        }
                    }).start();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            JPushInterface.stopPush(SettingActivity.this);
                        }
                    }).start();
                }
            }
        });

//        iv_push_switch.setOnClickListener(this);
//        if (JPushInterface.isPushStopped(this)) {
//            iv_push_switch.setImageResource(R.drawable.common_switch_off);
//            iv_push_switch.setTag(0);
//        } else {
//            iv_push_switch.setImageResource(R.drawable.common_switch_on);
//            iv_push_switch.setTag(1);
//        }
        tv_current_versions = (TextView) findViewById(R.id.tv_current_versions);
    }

    private void initData() {
        tv_current_versions.setText("当前版本 " + getAppVersion());
        tv_cache_size.setText(FileUtils.convertFileSize(getCacheSize()));
    }

    private String getAppVersion() {
        PackageManager pm = getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = pm.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "0";
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.ll_push://推送设置
            case R.id.iv_push_switch:
//                Integer status = (Integer) iv_push_switch.getTag();
//                if(status == 1){
//                    iv_push_switch.setImageResource(R.drawable.common_switch_off);
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            JPushInterface.stopPush(SettingActivity.this);
//                        }
//                    }).start();
//                    iv_push_switch.setTag(0);
//                }else{
//                    iv_push_switch.setImageResource(R.drawable.common_switch_on);
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            JPushInterface.resumePush(SettingActivity.this);
//                        }
//                    }).start();
//                    iv_push_switch.setTag(1);
//                }

                break;
            case R.id.ll_versions://版本更新
                versionsCheck();
                break;
            case R.id.ll_opinion://意见反馈
                startActivity(new Intent(this, FeedbackActivity.class));
                break;
            case R.id.ll_share://分享
                showShare();
                break;
            case R.id.ll_cache://清除缓存
                if (getCacheSize() != 0) {
                    clearCache();
                }
                break;
            case R.id.ll_about://关于我们
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.bt_logout://注销
                logout();
                break;
        }
    }

    private void versionsCheck() {
        Map<String, String> parameners = RequestParameters.version();
        new OkHttpRequest.Builder().url(URLManager.VERSION).params(parameners).tag(this).get(new ResultCallback<Version>() {
            @Override
            public void onError(Request request, Exception e) {
                showRequestError();
            }

            @Override
            public void onBefore(Request request) {
                showProgressDialow("请稍后...");
            }

            @Override
            public void onAfter() {
                dismissProgressDialog();
            }

            @Override
            public void onResponse(final Version response) {
                if (response.isSuccess(SettingActivity.this)) {
                    int version_code = response.getData().getVersion_code();
                    if (TextUtils.isEmpty(response.getData().getUpdate_url()) ||
                            SystemUtils.getAppVersionCode(SettingActivity.this) == version_code) {
                        Toast.makeText(SettingActivity.this, "已经是最新版本了", Toast.LENGTH_SHORT).show();
                    } else {
                        new CommonDialog.Builder(SettingActivity.this).setTitle(R.string.dialog_text_title)
                                .setMessage("最新版本:" + response.getData().getVersion_name() + "\n" + response.getData().getDescription()+"\n更新内容+换行+现在升级？")
                                .setNegativeButtonText(R.string.dialog_text_cancel, null)
                                .setPositiveButtonText(R.string.dialog_text_update, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(SettingActivity.this, ApkDownService.class);
                                        intent.putExtra("apkUrl", response.getData().getUpdate_url());
                                        startService(intent);
                                    }
                                }).show();
                    }
                }
            }
        });
    }


    private Long getCacheSize() {
        File file = ImageLoader.getInstance().getDiskCache().getDirectory();
        File[] files = file.listFiles();
        long size = 0;
        for (File f : files) {
            if (f.isDirectory()) {
                continue;
            }
            size += f.length();
        }
        return size;
    }

    private void clearCache() {
        new CommonDialog.Builder(this).setTitle(R.string.dialog_text_title).setMessage("确定清除图片缓存？")
                .setPositiveButtonText(R.string.dialog_text_immediately, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ImageLoader.getInstance().clearDiskCache();
                        tv_cache_size.setText(FileUtils.convertFileSize(getCacheSize()));
                    }
                }).setNegativeButtonText(R.string.dialog_text_cancel, null).show();
    }


    private void logout() {
        new CommonDialog.Builder(this).setTitle(R.string.dialog_text_title).setMessage("确定要注销当前账号吗？")
                .setNegativeButtonText(R.string.dialog_text_cancel, null)
                .setPositiveButtonText(R.string.dialog_text_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        killAll();
                        Share.remove(Share.LOGIN_RESP);
                        Intent it = new Intent(SettingActivity.this, MainTabActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("logout", true);
                        it.putExtras(bundle);
                        startActivity(it);
                    }
                }).show();
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("分享");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }
}
