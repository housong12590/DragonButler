package com.aosijia.dragonbutler.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.ui.activity.MeActivity;
import com.aosijia.dragonbutler.ui.activity.OtherUserInfoActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.io.File;


public class Uiutils {

    /**
     * 创建一个圆角矩形
     */
    public static GradientDrawable createShape(Context context, int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(dip2px(context, 2));
        drawable.setColor(color);
        return drawable;
    }

    /**
     * dip转换px
     */
    public static int dip2px(Context context, int dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }


    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }


    public static SpannableString getForegroundColorSpanString(Context context, String s, int color) {
        if (TextUtils.isEmpty(s)) {
            return new SpannableString("");
        }
        SpannableString spanString = new SpannableString(s);
        ForegroundColorSpan span = new ForegroundColorSpan(color);
        spanString.setSpan(span, 0, s.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanString;
    }


    public static void showInputWindow(Context context, EditText et) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        et.requestFocus();
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void hideInputWindow(Context context, View et) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    /**
     * 是否为手机号
     * 规则为“1”开头的11位数字
     *
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {
        return !TextUtils.isEmpty(mobile) && mobile.length() == 11 && TextUtils.isDigitsOnly(mobile) && mobile.startsWith("1");
    }

    /**
     * 验证码只能输入6位数字
     *
     * @param code
     * @return
     */
    public static boolean isSmsCode(String code) {
        return !TextUtils.isEmpty(code) && TextUtils.isDigitsOnly(code) && code.length() == 6;
    }

    /**
     * 房屋验证码10位数字
     *
     * @param code
     * @return
     */
    public static boolean isHouseCode(String code) {
        return !TextUtils.isEmpty(code) && TextUtils.isDigitsOnly(code) && code.length() == 10;
    }

    /**
     * 判断是否为正数
     *
     * @return
     */
    public static boolean isPositiveNumber(String number) {
        try {
            double d = Double.parseDouble(number);
            if (d > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

    }


    public static boolean isDefaultImageUrl(String url) {
        String defaultUrl = "/img/default_avatar.png";
        if (TextUtils.isEmpty(url) || defaultUrl.equals(url)) {
            return true;
        }
        return false;
    }

    public static final DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
            .showStubImage(R.drawable.avatar_default)          // 设置图片下载期间显示的图片
            .showImageForEmptyUri(R.drawable.avatar_default)  // 设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.drawable.avatar_default)       // 设置图片加载或解码过程中发生错误显示的图片
            .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
            .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
            .build();

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    public static void jumpUserInfoPage(Context context, String cacheUserId, String avatar, String nickName, String userId) {
        Intent intent = new Intent();
        if (cacheUserId.equals(userId)) {
            intent.setClass(context, MeActivity.class);
        } else {
            intent.setClass(context, OtherUserInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("userId", userId);
            bundle.putString("avatar", avatar);
            bundle.putString("nickName", nickName);
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    public static String getHideMobile(String mobile) {
        return mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
    }

}
