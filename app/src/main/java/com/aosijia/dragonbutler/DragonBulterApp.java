package com.aosijia.dragonbutler;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.ui.widget.loader.LoadingAndRetryManager;
import com.aosijia.dragonbutler.utils.DisplayOpitionFactory;
import com.bugtags.library.Bugtags;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhy.http.okhttp.OkHttpClientManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.net.ssl.HostnameVerifier;

import cn.jpush.android.api.JPushInterface;
import okio.Buffer;

/**
 * Created by wanglj on 15/12/11.
 */
public class DragonBulterApp extends Application {

    public static final String SHARE_CACHE = "SHARE_CACHE";




    @Override
    public void onCreate() {
        super.onCreate();
        //极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        //信任HTTPS 所有证书
        OkHttpClientManager.getInstance().setCertificates();


        Bugtags.start("2b8c82f078d3638230cf336cf435669e", this, Bugtags.BTGInvocationEventBubble);
        //image group
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(getApplicationContext())
                .setDownsampleEnabled(true)
                .build();

        Fresco.initialize(this, config);

        LoadingAndRetryManager.BASE_RETRY_LAYOUT_ID = R.layout.loader_base_retry;
        LoadingAndRetryManager.BASE_LOADING_LAYOUT_ID = R.layout.loader_base_loading;
        LoadingAndRetryManager.BASE_EMPTY_LAYOUT_ID = R.layout.loader_base_empty;

        //初始化本地存储

        File file = getCacheDir(this, SHARE_CACHE);
        if (!file.exists()) {
            file.mkdirs();
        }
        Share.init("CACHE", 10 * 1024, file.toString());
        initImageLoader();
    }

    // Creates a unique subdirectory of the designated app cache directory. Tries to use external but if not mounted, falls back on internal storage.
    public static File getCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir otherwise use internal cache dir
        final String cachePath = (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED ||
                !Environment.isExternalStorageRemovable()) ?
                context.getExternalCacheDir().getPath() :
                context.getCacheDir().getPath();

        return new File(cachePath + File.separator + uniqueName);
    }


    private void initImageLoader() {

        DisplayImageOptions displayOptions = DisplayOpitionFactory.sDefaultDisplayOption;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .defaultDisplayImageOptions(displayOptions)
                .memoryCache(new WeakMemoryCache()).writeDebugLogs()
                .denyCacheImageMultipleSizesInMemory().build();
        ImageLoader.getInstance().init(config);
    }
}
