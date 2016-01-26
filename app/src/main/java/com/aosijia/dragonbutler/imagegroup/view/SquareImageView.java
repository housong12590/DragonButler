package com.aosijia.dragonbutler.imagegroup.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.imagegroup.model.SquareImage;
import com.aosijia.dragonbutler.imagegroup.utils.ImageGroupDisplayHelper;
import com.aosijia.dragonbutler.utils.DisplayOpitionFactory;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.ImageLoader;


public class SquareImageView extends SimpleDraweeView implements View.OnClickListener{

    private String mLocalUrl;
    private String mUploadKey;
    private String mUploadUrl;
    private String mInternetUrl;
    private int placeholderDrawable;
    private int width;
    private boolean mClickUpload = true;
    private ProgressBar progressBar;
    private ImageView imageView;

    public SquareImageView(Context context) {
        this(context, null);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        placeholderDrawable = R.mipmap.ic_launcher;
        setScaleType(ScaleType.CENTER_CROP);
        setClickable(mClickUpload);
        setOnClickListener(this);
        GenericDraweeHierarchyBuilder builder1 = new GenericDraweeHierarchyBuilder(getContext().getResources());
        builder1.setPlaceholderImage(ContextCompat.getDrawable(getContext(), placeholderDrawable), ScalingUtils.ScaleType.CENTER_CROP);
        getControllerBuilder().build().setHierarchy(builder1.build());
    }

    public void setWidthByParent(int widthByParent) {
        width = widthByParent;
    }

    public void setPlaceholderDrawable(int src) {
        placeholderDrawable = src;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width, width);
    }

    public void setLocalUrl(String localUrl) {
        if (!TextUtils.isEmpty(mInternetUrl)) mInternetUrl = null;
        mLocalUrl = localUrl;
//        ImageLoader.getInstance().displayImage(mLocalUrl,this, DisplayOpitionFactory.sItemDisplayOption);
        ImageGroupDisplayHelper.displayImageLocal(this, mLocalUrl, 500, 500);
    }

    @SuppressWarnings("unused")
    public void setmInternetUrl(String internetUrl) {
        mInternetUrl = internetUrl;
    }

    @SuppressWarnings("unused")
    public void setUploadKey(String key) {
        mUploadKey = key;
    }

    public String getUploadImageKey() {
        return mUploadKey;
    }

    public String getLocalUrl() {
        return mLocalUrl;
    }
    public String getImageLocalUrl() {
        return mLocalUrl;
    }

    public void setProgressBar(ProgressBar progressBar){
        this.progressBar = progressBar;
    }

    public ProgressBar getProgressBar(){
        return this.progressBar;
    }

    public void setDeleteView(ImageView imageView){
        this.imageView = imageView;
    }

    public ImageView getDeleteView(){
        return imageView;
    }

    public String getInternetUrl() {
        return mInternetUrl;
    }

    public void setInternetData(String netUrl) {
        mInternetUrl = netUrl;
        mLocalUrl = null;
        if (netUrl == null) {
            setImageResource(placeholderDrawable);
            return;
        }
//        setImageResource(R.drawable.default_pic_item);
        ImageLoader.getInstance().displayImage(mInternetUrl,this, DisplayOpitionFactory.sItemDisplayOption);
//        ImageGroupDisplayHelper.displayImage(this, mInternetUrl, R.drawable.default_pic_item, 300, 300);
    }

    public void setUploadUrl(String url){
        this.mUploadUrl = url;
    }

    public String getUploadUrl(){
        return mUploadUrl;
    }

    public void setImageData(SquareImage squareImage) {
        if (squareImage.localUrl != null) setLocalUrl(squareImage.localUrl);
        if (squareImage.interNetUrl != null) setInternetData(squareImage.interNetUrl);
        if (squareImage.urlKey != null) setUploadKey(squareImage.urlKey);
        if (squareImage.uploadUrl != null) setUploadUrl(squareImage.uploadUrl);
    }

    @SuppressWarnings("unused")
    public void setClickAble (boolean able) {
        mClickUpload = able;
    }

    @Override
    public void onClick(View v) {
        if (mClickUpload) {

        } else {

        }
    }

    public SquareImage getSquareImage() {
        return mLocalUrl == null && mInternetUrl == null
                ? null
                : new SquareImage(getLocalUrl(), getInternetUrl(), getUploadImageKey(),getUploadUrl()
                , mLocalUrl == null ? SquareImage.PhotoType.NETWORK :SquareImage.PhotoType.LOCAL);
    }


    public void setRoundAsCircle(boolean flag) {
        if (flag == false) return;
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setRoundAsCircle(flag);
        getHierarchy().setRoundingParams(roundingParams);

    }
}
