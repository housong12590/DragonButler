package com.aosijia.dragonbutler.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.imagegroup.NavigatorImage;
import com.aosijia.dragonbutler.imagegroup.activity.UserCameraActivity;
import com.aosijia.dragonbutler.model.BaseResp;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.widget.ActionSheet;
import com.aosijia.dragonbutler.upload.UploadUtils;
import com.aosijia.dragonbutler.utils.DisplayOpitionFactory;
import com.aosijia.dragonbutler.utils.Uiutils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 我的资料
 */
public class MeActivity extends BaseActivity implements View.OnClickListener {


    private TextView nickNameValueTextView;
    private TextView genderValueTextView;
    private TextView houseValueTextView;
    private TextView mobileValueTextView;
    private ImageView avatarImageView;

    public static final int REQUEST_CODE_NICKNAME = 1000;
    public static final int REQUEST_CODE_GENDER = 1001;
    public static final int REQUEST_CROP_IMAGE = 1003;
    public static final int REQUEST_CODE_MOBILE = 1004;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_me);
        initView();

    }

    private void initView() {
        findViewById(R.id.title_leftimageview).setOnClickListener(this);
        avatarImageView = (ImageView) findViewById(R.id.avatarImageView);
        avatarImageView.setOnClickListener(this);
        findViewById(R.id.nickNameLayout).setOnClickListener(this);
        nickNameValueTextView = (TextView) findViewById(R.id.nickNameValueTextView);
        findViewById(R.id.genderLayout).setOnClickListener(this);
        genderValueTextView = (TextView) findViewById(R.id.genderValueTextView);
//        findViewById(R.id.houseLayout).setOnClickListener(this);
//        houseValueTextView = (TextView) findViewById(R.id.houseValueTextView);
        findViewById(R.id.mobileLayout).setOnClickListener(this);
        mobileValueTextView = (TextView) findViewById(R.id.mobileValueTextView);

        //set value
        setNickNameText();
        setGenderText();
        setAvatarImage();
//        setHouseHold();
        setMobile();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_leftimageview://返回
                finish();
                break;
            case R.id.nickNameLayout://昵称
                Intent nickIntent = new Intent(this, MeUpdateNicknameActivity.class);
                nickIntent.putExtra("nickname", nickNameValueTextView.getText().toString());
                startActivityForResult(nickIntent, REQUEST_CODE_NICKNAME);
                break;
            case R.id.genderLayout://性别
                final LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
                String gender = loginResp.getData().getUser_info().getGender();
                Intent genderIntent = new Intent(this, MeUpdateGenderActivity.class);
                genderIntent.putExtra("gender", gender);
                startActivityForResult(genderIntent, REQUEST_CODE_GENDER);
                break;
//            case R.id.houseLayout://房屋
//                Intent intent = new Intent(this, HouseHoldBindActivity.class);
//                boolean flag = true;
//                intent.putExtra("flag", flag);
//                startActivity(intent);
//                break;
            case R.id.mobileLayout://手机
                Intent itmobile = new Intent(this, UpdateUserMobile1Activity.class);
                startActivityForResult(itmobile, REQUEST_CODE_MOBILE);
                break;
            case R.id.avatarImageView://头像
                ActionSheet.createBuilder(this, getSupportFragmentManager())
                        .setCancelButtonTitle("取消")
                        .setOtherButtonTitles("拍照", "从手机相册中选择")
                        .setCancelableOnTouchOutside(true)
                        .setListener(new ActionSheet.ActionSheetListener() {
                            @Override
                            public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                            }

                            @Override
                            public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                                if (index == 1) {
                                    NavigatorImage.startCustomAlbumActivity(MeActivity.this, 1);
                                } else if (index == 0) {
                                    startCamera(MeActivity.this);
                                }
                            }
                        }).show();
                break;
        }

    }

    private void startCamera(Activity activity) {
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            ActivityCompat.startActivityForResult(activity,
                    new Intent(activity, UserCameraActivity.class), NavigatorImage.RESULT_TAKE_PHOTO,
                    null);
        } else {
            Toast.makeText(activity, "内存卡不存在", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_MOBILE:
                setMobile();
                break;
            case REQUEST_CODE_GENDER:
                setGenderText();
                break;
            case REQUEST_CODE_NICKNAME:
                setNickNameText();
                break;
            case REQUEST_CROP_IMAGE:
                if (data != null && data.getExtras() != null) {
                    Bitmap selectedBitmap = data.getExtras().getParcelable("data");
                    avatarImageView.setImageBitmap(selectedBitmap);
                    uploadImage(UploadUtils.compressImage(selectedBitmap));
                }
                break;
            default:
                if (data != null) {
                    if (data.hasExtra(NavigatorImage.EXTRA_PHOTO_URL)) {
                        String photoTakeUrl = data.getStringExtra(NavigatorImage.EXTRA_PHOTO_URL);
                        if (requestCode == NavigatorImage.RESULT_TAKE_PHOTO && null != photoTakeUrl) {
                            Log.e("MeActivity", "====" + photoTakeUrl);
                            doCropImage(photoTakeUrl);
                        }
                    } else {
                        List<String> images = data.getStringArrayListExtra(NavigatorImage.EXTRA_PHOTOS_URL);
                        ArrayList<Integer> positions = data.getIntegerArrayListExtra(NavigatorImage.EXTRA_IMAGE_URL_POSITION);
                        if (requestCode == NavigatorImage.RESULT_SELECT_PHOTOS && null != images && images.size() > 0) {
                            Log.e("MeActivity", "====" + images.get(0));
                            doCropImage(images.get(0));
                        }
                    }

                }
                break;
        }

    }

    private void setGenderText() {
        final LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        String gender = loginResp.getData().getUser_info().getGender();
        if ("0".equals(gender)) {//女
            genderValueTextView.setText("女");
        } else if ("1".equals(gender)) {//男
            genderValueTextView.setText("男");
        } else {//2 保密
            genderValueTextView.setText("保密");
        }
    }

    private void setAvatarImage() {
        final LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        String avatarUrl = loginResp.getData().getUser_info().getAvatar_url();
        if (!TextUtils.isEmpty(avatarUrl)) {
            ImageLoader.getInstance().displayImage(avatarUrl, avatarImageView, DisplayOpitionFactory.sAvatarDisplayOption);
        }
    }

    private void setHouseHold() {
        final LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        String communityTitle = loginResp.getData().getUser_info().getHousehold().getCommunity_title();
        String unit = loginResp.getData().getUser_info().getHousehold().getUnit();
        String room = loginResp.getData().getUser_info().getHousehold().getRoom();
        String hourseInfo = communityTitle + unit + room;
        houseValueTextView.setText(hourseInfo);
    }

    private void setMobile() {
        final LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        mobileValueTextView.setText(Uiutils.getHideMobile(loginResp.getData().getUser_info().getMobile()));
    }

    private void setNickNameText() {
        final LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        String nickName = loginResp.getData().getUser_info().getNickname();
        nickNameValueTextView.setText(nickName);
    }

    private void uploadImage(byte[] b) {

        showProgressDialow("正在上传图片");
        OSSClient oss = UploadUtils.configOSS(this);

        String fileName = UploadUtils.createFileName();
        Log.e("UploadUtils", fileName);
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(UploadUtils.BUCKET, fileName, b);

        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                final String imageUrl = UploadUtils.DOWNLOAD_IMAGE + request.getObjectKey();
                final LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
                String accessToken = loginResp.getData().getAccess_token();
                String gender = loginResp.getData().getUser_info().getGender();
                String nickName = loginResp.getData().getUser_info().getNickname();
                Map<String, String> parameter = RequestParameters.userInfoUpdate(accessToken, imageUrl, nickName, gender);
                new OkHttpRequest.Builder().url(URLManager.USER_INFO_UPDATE).params(parameter).tag(this).post(new ResultCallback<BaseResp>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        dismissProgressDialog();
                        showRequestError();
                    }

                    @Override
                    public void onResponse(BaseResp response) {

                        if (response.isSuccess(MeActivity.this)) {
                            //更新本地用户信息
                            loginResp.getData().getUser_info().setAvatar_url(imageUrl);
                            Share.putObject(Share.LOGIN_RESP, loginResp);
                        }
                        Toast.makeText(MeActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                        dismissProgressDialog();
                    }
                });
                Log.d("PutObject", "UploadSuccess");

                Log.d("ETag", result.getETag());
                Log.d("RequestId", result.getRequestId());
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
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

    /**
     * 调用系统裁剪图片
     *
     * @param path
     */
    private void doCropImage(String path) {

        Intent intent = new Intent();
        File mFile = new File(path);
        Uri mUri = Uiutils.getImageContentUri(this, mFile);
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(mUri, "image/*");// mUri是已经选择的图片Uri
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);// 输出图片大小
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUEST_CROP_IMAGE);

    }


}
