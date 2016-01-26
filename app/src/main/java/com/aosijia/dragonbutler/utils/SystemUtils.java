package com.aosijia.dragonbutler.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 * Create by : hs
 * Date : 2016/1/22 0022 10:52
 */
public class SystemUtils {

    /**
     * 跳转至拨号界面
     *
     * @param mContext    上下文Context
     * @param phoneNumber 需要呼叫的手机号码
     */
    public static void toCallPhoneActivity(Context mContext, String phoneNumber) {
        Uri uri = Uri.parse("tel:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        if (intent.resolveActivity(mContext.getPackageManager()) != null){
            mContext.startActivity(intent);
        }
    }

    /**
     * 获取APP版本号
     */
    public static int getAppVersionCode(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
