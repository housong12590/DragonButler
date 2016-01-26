package com.aosijia.dragonbutler.utils;

import android.graphics.Bitmap.Config;

import com.aosijia.dragonbutler.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class DisplayOpitionFactory {

    public static DisplayImageOptions sDefaultDisplayOption = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .resetViewBeforeLoading(true).delayBeforeLoading(100)
            .bitmapConfig(Config.RGB_565)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();


    public static DisplayImageOptions sAvatarDisplayOption = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .showImageOnLoading(R.drawable.avatar_default)
            .showImageForEmptyUri(R.drawable.avatar_default)
            .showImageOnFail(R.drawable.avatar_default)
            .delayBeforeLoading(100)
            .bitmapConfig(Config.RGB_565)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

    public static DisplayImageOptions sItemDisplayOption = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .showImageOnLoading(R.drawable.default_pic_item)
            .showImageForEmptyUri(R.drawable.default_pic_item)
            .showImageOnFail(R.drawable.default_pic_item)
            .delayBeforeLoading(100)
            .bitmapConfig(Config.RGB_565)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();

    public static DisplayImageOptions sThemeDisplayOption = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .showImageOnLoading(R.drawable.default_pic_bank_adv)
            .showImageForEmptyUri(R.drawable.default_pic_bank_adv)
            .showImageOnFail(R.drawable.default_pic_bank_adv)
            .delayBeforeLoading(100)
            .bitmapConfig(Config.RGB_565)
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
}
