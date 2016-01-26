package com.aosijia.dragonbutler.upload;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

/**
 * Created by wanglj on 15/12/24.
 */
public interface UploadCallback {
    void onProgress(PutObjectRequest request, long currentSize, long totalSize);
    void onSuccess(PutObjectRequest request, PutObjectResult result);
    void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException);
}
