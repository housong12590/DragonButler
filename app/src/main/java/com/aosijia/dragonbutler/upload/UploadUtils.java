package com.aosijia.dragonbutler.upload;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.imagegroup.utils.DisplayUtils;
import com.aosijia.dragonbutler.imagegroup.utils.ImageDisplayHelper;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.rest.MD5;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.utils.ImageSizeUtils;
import com.zhy.http.okhttp.ImageUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by wanglj on 15/12/24.
 */
public final class UploadUtils {

    public static final String ENDPOINT = "http://oss-cn-shanghai.aliyuncs.com";
    public static final String ACCESSKEYID = "GSMD6g3etoSqEzdr";
    public static final String ACCESSKEYSECRET = "SDMNhVDY5LEcUwzAqFTMQWsP90Yhrm";
    public static final String BUCKET = "sharemerge-images";

    public static final String DOWNLOAD_IMAGE="http://"+BUCKET+".oss-cn-shanghai.aliyuncs.com/";

    public static final String PLATFORM_ANDROID = "Android";

    public static final int IMAGE_SIZE = 200*1024;//图片压缩指定大小

    /**
     * 上传图片文件名生成规则:md5(上传图片时时间戳_用户ID_平台类型_登录时间戳)
     * @return
     */
    public static String createFileName(){
        long uploadTime = System.currentTimeMillis();
        long loginTime = Share.getLong(Share.LOGIN_TIME, System.currentTimeMillis() / 1000);
        LoginResp loginResp = (LoginResp)Share.getObject(Share.LOGIN_RESP);
        String userId = loginResp.getData().getUser_id();
        String tempName = uploadTime+""+userId+PLATFORM_ANDROID+loginTime;
        String fileName = tempName;
        try {
            fileName = MD5.md5(fileName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return fileName+".jpg";

    }



    public static OSSClient configOSS(Context context){
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(ACCESSKEYID, ACCESSKEYSECRET);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSLog.enableLog();
        OSSClient oss = new OSSClient(context, ENDPOINT, credentialProvider, conf);
        return oss;
    }

    // 从本地文件上传，使用非阻塞的异步接口
    public static void asyncPutObjectFromLocalFile(final AppCompatActivity context,final String uploadFilePath,final UploadCallback callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {
               final  byte[] b = compressImage(getSmallBitmap(context,uploadFilePath));

                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        OSSClient oss = configOSS(context);

                        String fileName = createFileName();
                        Log.e("UploadUtils", fileName);
                        // 构造上传请求
                        PutObjectRequest put = new PutObjectRequest(BUCKET, fileName, b);

                        // 异步上传时可以设置进度回调
                        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
                            @Override
                            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                                callback.onProgress(request, currentSize, totalSize);
                                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
                            }
                        });

                        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                            @Override
                            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                                callback.onSuccess(request, result);
                                Log.d("PutObject", "UploadSuccess");

                                Log.d("ETag", result.getETag());
                                Log.d("RequestId", result.getRequestId());
                            }

                            @Override
                            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                                callback.onFailure(request,clientExcepion,serviceException);
                                // 请求异常
                                if (clientExcepion != null) {
                                    // 本地异常如网络异常等
                                    clientExcepion.printStackTrace();
                                }
                                if (serviceException != null) {
                                    // 服务异常
                                    Log.e("ErrorCode", serviceException.getErrorCode());
                                    Log.e("RequestId", serviceException.getRequestId());
                                    Log.e("HostId", serviceException.getHostId());
                                    Log.e("RawMessage", serviceException.getRawMessage());
                                }
                            }
                        });
                    }
                });
            }
        }).start();

    }


    /**
     * 压缩图片到指定大小 默认200K
     * @param
     * @return
     */
    public static byte[] compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        while (baos.toByteArray().length > IMAGE_SIZE) {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            quality -= 10;
            if(quality <= 0){
                break;
            }
        }
        return baos.toByteArray();
    }

    /**
     * Calcuate how much to compress the image
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
// Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * resize image to 480x800
     * @param filePath
     * @return
     */
    public static Bitmap getSmallBitmap(Context context,String filePath) {

        File file = new File(filePath);
        long originalSize = file.length();


        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize based on a preset ratio
        options.inSampleSize = calculateInSampleSize(options, DisplayUtils.getScreenWidth(context), DisplayUtils.getScreenHeight(context));

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap compressedImage = BitmapFactory.decodeFile(filePath, options);


        return compressedImage;
    }



}
