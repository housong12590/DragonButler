package com.aosijia.dragonbutler.imagegroup;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.imagegroup.activity.UserCameraActivity;
import com.aosijia.dragonbutler.imagegroup.model.SquareImage;
import com.aosijia.dragonbutler.imagegroup.utils.DisplayUtils;
import com.aosijia.dragonbutler.imagegroup.utils.ImageGroupDisplayHelper;
import com.aosijia.dragonbutler.imagegroup.utils.ImageGroupSavedState;
import com.aosijia.dragonbutler.imagegroup.utils.ImageGroupUtils;
import com.aosijia.dragonbutler.imagegroup.view.SquareImageView;
import com.aosijia.dragonbutler.ui.widget.ActionSheet;
import com.aosijia.dragonbutler.upload.UploadCallback;
import com.aosijia.dragonbutler.upload.UploadUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ImageGroupView extends LinearLayout {

    private final static int COLUMN = 3;
    private final static int CHILD_MARGIN = 24;
    private final static int MAX_VALUE = -1;
    private LinearLayout mLayoutItem;
    private ArrayList<Integer> mPhotoViewIDs;
    private FragmentManager mManager;
    private String unionKey;
    private ImageGroupSavedState imageGroupSavedState;
    private Uri preTakePhotoUri;
    private ArrayList<SquareImage> preImage;
    private OnImageClickListener clickListener;
    private int addButtonDrawable;
    private int deleteDrawable;
    private int placeholderDrawable;

    private boolean showAddButton, roundAsCircle;
    private int childMargin;
    private int maxImageNum;
    private int column;

    public ImageGroupView(Context context) {
        this(context, null);
    }

    public ImageGroupView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageGroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initData();
        getAttrs(context, attrs, defStyleAttr);
        setUpTreeObserver();
    }

    private void initData() {
        setOrientation(VERTICAL);
        mPhotoViewIDs = new ArrayList<>();
        preImage = new ArrayList<>();
    }

    private void getAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs == null) return;
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageGroupView, defStyleAttr, 0);
        if (a == null) return;

        showAddButton = a.getBoolean(R.styleable.ImageGroupView_showAddButton, false);
        roundAsCircle = false;
        maxImageNum = a.getInteger(R.styleable.ImageGroupView_maxImageNum, MAX_VALUE);
        childMargin = a.getDimensionPixelSize(R.styleable.ImageGroupView_childMargin, CHILD_MARGIN);
        column = a.getInteger(R.styleable.ImageGroupView_column, COLUMN);
        addButtonDrawable = a.getResourceId(R.styleable.ImageGroupView_addButtonDrawable, R.drawable.common_add_image_selector);
        deleteDrawable = a.getResourceId(R.styleable.ImageGroupView_deleteDrawable, R.drawable.common_add_image_delete);
        placeholderDrawable = a.getResourceId(R.styleable.ImageGroupView_imagePlaceholderDrawable, R.drawable.default_pic_item);

        a.recycle();
    }

    private void setUpTreeObserver() {
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    setPhotoBySquareImageByObserver();
                    restoreView();
                }
            });
        }
    }

    private void restoreView() {
        if (imageGroupSavedState != null && showAddButton) {
            refreshLayout(imageGroupSavedState.getSquarePhotos());
            imageGroupSavedState = null;
        }
        if (preTakePhotoUri != null) {
            refreshPhotoView(preTakePhotoUri);
            preTakePhotoUri = null;
        }
    }

    /**
     * 判断图片是否上传完成
     *
     * @return
     */
    public boolean canSubmit() {
        List<String> urlList = this.getUploadUrl();
        List<SquareImageView> imagelist = this.getSquareImageViews();
        boolean hasAddButton = false;//是否包含加号
        for (SquareImageView squareImageView : imagelist) {
            if (isAddButton(squareImageView)) {
                hasAddButton = true;
            }
        }

        if (imagelist.size() == 5) {
            if (hasAddButton) {
                return urlList.size() == imagelist.size() - 1;
            } else {
                return urlList.size() == imagelist.size();
            }
        } else {
            return urlList.size() == (imagelist.size() - 1);
        }
    }

    private void initLayoutItem() {
        addNewLayoutItemWithoutTopMargin();
        addPhotoView();
    }

    private void addNewLayoutItemWithTopMargin() {
        addNewLayoutItem(true);
    }

    private void addNewLayoutItemWithoutTopMargin() {
        addNewLayoutItem(false);
    }

    private void addNewLayoutItem(boolean withTopMargin) {
        mLayoutItem = new LinearLayout(getContext());
        LayoutParams lParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        if (withTopMargin) {
            lParams.topMargin = childMargin;
        }
        mLayoutItem.setLayoutParams(lParams);
        addView(mLayoutItem);
    }

    private void addPhotoView() {
        SquareImageView squareImageView = new SquareImageView(getContext());
        squareImageView.setImageResource(addButtonDrawable);
        int imageViewWidth = getImageWidth();
        squareImageView.setWidthByParent(imageViewWidth);
        squareImageView.setPlaceholderDrawable(placeholderDrawable);
        squareImageView.setRoundAsCircle(roundAsCircle);

        FrameLayout frame = new FrameLayout(getContext());
        LayoutParams frameParams = new LayoutParams(imageViewWidth, imageViewWidth);
        frame.addView(squareImageView, frameParams);
        LayoutParams layoutParams = new LayoutParams(imageViewWidth, imageViewWidth);
        if (mPhotoViewIDs.size() % column != 0) layoutParams.leftMargin = childMargin;
        if (mPhotoViewIDs.size() > 0 && mPhotoViewIDs.size() % column == 0) {
            addNewLayoutItemWithTopMargin();
        }
        mLayoutItem.addView(frame, layoutParams);

        final int squarePhotoViewId = createIndex();
        squareImageView.setId(squarePhotoViewId);
        mPhotoViewIDs.add(squarePhotoViewId);

        squareImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SquareImageView view = (SquareImageView) v;
                if (isAddButton(view)) {
                    final AppCompatActivity activity = ((AppCompatActivity) getContext());
                    ActionSheet.createBuilder(getContext(), activity.getSupportFragmentManager())
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
                                        doPhotoClickSelectable();
                                    } else if (index == 0) {
                                        startCamera(activity);
                                    }
                                }
                            }).show();

                } else if (clickListener != null) {
                    clickListener.onImageClick(view.getSquareImage(), getSquarePhotos(), getInternetUrls());
                } else {
                    NavigatorImage.startImageSwitcherActivity(getContext(), getSquarePhotos(), mPhotoViewIDs.indexOf(squarePhotoViewId), showAddButton, placeholderDrawable);
                }
            }
        });

////        if (!isAddButton(squareImageView)) {
//            doDeletePhoto(squareImageView);
////        }
//
//
////        if (isAddButton(squareImageView)) {
//            FrameLayout frameLayout = (FrameLayout) squareImageView.getParent();
//            FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
//                    Gravity.CENTER);
//            TextView textView = new TextView(getContext());
//
//            frameLayout.addView(textView, frameLayoutParams);
//            squareImageView.setTextView(textView);
////        }


//        squareImageView.setOnLongClickListener(new OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                if (canClickToDelete(v)) {
//                    return true;
//                }
//                doDeletePhoto(v);
//                return true;
//            }
//        });
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

    private boolean isAddButton(SquareImageView view) {
        return view.getImageLocalUrl() == null
                && view.getInternetUrl() == null;
    }

    private boolean canClickToDelete(View v) {
        return !showAddButton || (((SquareImageView) v).getImageLocalUrl() == null
                && ((SquareImageView) v).getInternetUrl() == null);
    }

    private int getImageWidth() {
        int width = getMeasuredWidth();


        if (width == 0) {
            ViewGroup.LayoutParams lp = getLayoutParams();
            if (lp instanceof RelativeLayout.LayoutParams) {
                width = DisplayUtils.getScreenWidth(getContext()) - ((RelativeLayout.LayoutParams) lp).leftMargin - ((RelativeLayout.LayoutParams) lp).rightMargin - getResources().getDimensionPixelSize(R.dimen.list_horizontal_margin) * 2;
            } else if (lp instanceof LayoutParams) {
                width = DisplayUtils.getScreenWidth(getContext()) - ((LinearLayout.LayoutParams) lp).leftMargin - ((LinearLayout.LayoutParams) lp).rightMargin - getResources().getDimensionPixelSize(R.dimen.list_horizontal_margin) * 2;
            } else {
                width = DisplayUtils.getScreenWidth(getContext()) - getResources().getDimensionPixelSize(R.dimen.list_horizontal_margin) * 2;
            }
//            width = DisplayUtils.getScreenWidth(getContext()) -  getResources().getDimensionPixelSize(R.dimen.list_horizontal_margin) * 2;
        }
        return (width - childMargin * (column - 1) - getPaddingRight() - getPaddingLeft()) / column;
    }

    private void doDeletePhoto(View v) {

        final SquareImageView squarView = (SquareImageView) v;
        FrameLayout frame = (FrameLayout) squarView.getParent();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                Gravity.RIGHT | Gravity.TOP);


        final ImageView image = new ImageView(getContext());
        image.setId(v.getId() + 10);

        image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                if ("retry".equals(v.getTag())) {

                    final AppCompatActivity activity = ((AppCompatActivity) getContext());
                    ActionSheet.createBuilder(getContext(), activity.getSupportFragmentManager())
                            .setCancelButtonTitle("取消")
                            .setOtherButtonTitles("重试", "删除")
                            .setCancelableOnTouchOutside(true)
                            .setListener(new ActionSheet.ActionSheetListener() {
                                @Override
                                public void onDismiss(ActionSheet actionSheet, boolean isCancel) {

                                }

                                @Override
                                public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                                    if (index == 1) {
                                        doPhotoDelete(squarView.getId());
                                    } else if (index == 0) {
                                        image.setVisibility(View.GONE);
                                        image.setImageResource(R.drawable.common_add_image_retry);
                                        v.setTag("delete");
                                        uploadImage(squarView);
                                    }
                                }
                            }).show();


                } else {
                    doPhotoDelete(squarView.getId());
                }

            }
        });
        image.setImageResource(deleteDrawable);
        frame.addView(image, layoutParams);
        squarView.setDeleteView(image);
        if (squarView.getUploadUrl() != null && showAddButton) {
            image.setVisibility(View.VISIBLE);
        } else {
            image.setVisibility(View.GONE);
        }


        FrameLayout.LayoutParams progressLayoutParam = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);

        ProgressBar progressBar = (ProgressBar) LayoutInflater.from(getContext()).inflate(R.layout.progressbar, null);
        progressBar.setId(v.getId() + 11);
        if (squarView.getUploadUrl() != null) {
            progressBar.setProgress(100);
        } else {
            progressBar.setProgress(5);
        }

        frame.addView(progressBar, progressLayoutParam);
        squarView.setProgressBar(progressBar);
    }

    private void doPhotoClickSelectable() {
        doUpLoadPhotoClick();
    }


    private void doPhotoDelete(final int photoId) {
        removeView(findViewById(photoId));
        mPhotoViewIDs.remove(new Integer(photoId));
        refreshImages();
    }

    private void refreshImages() {
        requestLayout();
        refresh();
    }

    public void showAddButton(Boolean b) {
        showAddButton = b;
        refresh();
    }

    public void showWithCircle(Boolean b) {
        roundAsCircle = b;
        refresh();
    }

    public void setRoundAsCircle(Boolean flag) {
        roundAsCircle = flag;
    }


    public void doPhotosDelete(final ArrayList<Integer> positions) {
        if (positions == null) return;
        ArrayList<Integer> deleteIds = new ArrayList<>();
        for (int position : positions) {
            deleteIds.add(mPhotoViewIDs.get(position));
        }
        for (int id : deleteIds) {
            removeView(findViewById(id));
            mPhotoViewIDs.remove(new Integer(id));
        }
        refreshImages();
    }

    private void refresh() {
        refreshLayout(getSquarePhotos());
    }

    private ArrayList<SquareImage> getSquarePhotos() {
        ArrayList<SquareImage> results = new ArrayList<>();
        for (Integer i : mPhotoViewIDs) {
            SquareImageView squareImageView = (SquareImageView) findViewById(i);
            if (squareImageView.getSquareImage() != null)
                results.add(squareImageView.getSquareImage());
        }
        return results;
    }


    private void refreshLayout(ArrayList<SquareImage> photos) {
        setHintText();
        mPhotoViewIDs.clear();
        removeAllViews();
        initLayoutItem();
        for (int i = 0; i < photos.size(); i++) {
            SquareImageView squareImageView = (SquareImageView) findViewById(mPhotoViewIDs.get(mPhotoViewIDs.size() - 1));
            squareImageView.setImageData(photos.get(i));
            Log.e("ImageGroupView", (squareImageView.getInternetUrl() + "==" + squareImageView.getLocalUrl()) + "==" + squareImageView.getId());
            addButton(squareImageView);
            if (showAddButton) {
                addPhoto(mPhotoViewIDs.get(mPhotoViewIDs.size() - 1));
            } else {
                addPhotoWithoutButton(i, photos.size());
            }
        }
    }


    private void setHintText() {
        //显示最多选择五张照片提示
        final AppCompatActivity activity = ((AppCompatActivity) getContext());
        View v = activity.findViewById(R.id.hintText);
        if (mPhotoViewIDs.size() > 1) {
            if (v != null) {
                v.setVisibility(View.GONE);
            }
        } else {
            if (v != null) {
                v.setVisibility(View.VISIBLE);
            }
        }
    }

    private void doUpLoadPhotoClick() {
        NavigatorImage.startCustomAlbumActivity(getContext(), getCanSelectMaxNum());
    }

    private int getCanSelectMaxNum() {
        if (maxImageNum == MAX_VALUE) return 0;
        return maxImageNum - mPhotoViewIDs.size() + 1;
    }

    private void selectImageFromSystemAlbum() {
        Intent i = new Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ((Activity) getContext()).startActivityForResult(i, NavigatorImage.RESULT_SELECT_PHOTO);
    }

    private void addPhoto(int viewId) {
        boolean isEndPhotoView = true;
        for (int i = 0; i < mPhotoViewIDs.size() - 1; i++) {
            if (mPhotoViewIDs.get(i) == viewId) {
                isEndPhotoView = false;
            }
        }

        if (isEndPhotoView && (maxImageNum == MAX_VALUE || mPhotoViewIDs.size() < maxImageNum)) {
            addPhotoView();
        }
    }

    public void setNetworkPhotos(List<String> photos) {
        if (photos == null) return;
        preImage.clear();
        for (String url : photos) {
            preImage.add(new SquareImage(null, url, null, url, SquareImage.PhotoType.NETWORK));
        }
        setPhotoBySquareImage();
    }

    public void setLocalPhotos(List<String> photos) {
        if (photos == null) return;
        preImage.clear();
        for (String url : photos) {
            preImage.add(new SquareImage(url, null, null, null, SquareImage.PhotoType.LOCAL));
        }
        setPhotoBySquareImage();
    }

    public void setPhotosBySquareImage(List<SquareImage> photos) {
        if (photos == null) return;
        preImage.clear();
        preImage.addAll(photos);
        setPhotoBySquareImage();
    }

    public void setNetworkPhotosWithKey(ArrayList<String> urls) {
        if (urls == null) return;
        preImage.clear();
        for (String url : urls) {
            String[] headWithKey = url.split("/");
            preImage.add(new SquareImage(null, url, headWithKey[headWithKey.length - 1], null, SquareImage.PhotoType.NETWORK));
        }
        setPhotoBySquareImage();
    }

    private void setPhotoBySquareImageByObserver() {
        setPhotoBySquareImage();
        if (mPhotoViewIDs.size() > 0) {
            SquareImageView squareImageView = (SquareImageView) this.findViewById(mPhotoViewIDs.get(mPhotoViewIDs.size() - 1));
            squareImageView.requestLayout();
            squareImageView.invalidate();
        }
    }

    private void setPhotoBySquareImage() {
        if (showAddButton) {
            setSquareImagesWithButton(preImage);
        } else {
            setSquareImagesWithoutButton(preImage);
        }
    }

    private void setSquareImagesWithButton(ArrayList<SquareImage> photos) {
        setHintText();
        removeAllViews();
        mPhotoViewIDs.clear();
        initLayoutItem();

        int size = photos != null ? photos.size() : 0;
        for (int i = 0; i < size; i++) {
            SquareImageView squareImageView = (SquareImageView) this.findViewById(mPhotoViewIDs.get(mPhotoViewIDs.size() - 1));
            squareImageView.setImageData(photos.get(i));
            addButton(squareImageView);
            addPhoto(mPhotoViewIDs.get(mPhotoViewIDs.size() - 1));
        }
    }

    private void setSquareImagesWithoutButton(ArrayList<SquareImage> photos) {

        removeAllViews();
        mPhotoViewIDs.clear();
        initLayoutItem();
        int size = photos != null ? photos.size() : 0;
        for (int i = 0; i < size; i++) {
            SquareImageView squareImageView = (SquareImageView) findViewById(mPhotoViewIDs.get(i));
            squareImageView.setImageData(photos.get(i));
            addPhotoWithoutButton(i, size);
        }
    }

    private void addPhotoWithoutButton(int position, int size) {
        if (position != size - 1) addPhotoView();
    }

    @SuppressWarnings("unused")
    public ArrayList<String> getLocalUrls() {
        ArrayList<String> map = new ArrayList<>();
        for (Integer i : mPhotoViewIDs) {
            SquareImageView squareImageView = (SquareImageView) this.findViewById(i);
            if (TextUtils.isEmpty(squareImageView.getInternetUrl()) && !TextUtils.isEmpty(squareImageView.getLocalUrl())) {
                map.add(squareImageView.getLocalUrl());
            }
        }
        return map;
    }

    public ArrayList<SquareImageView> getSquareImageViews() {
        ArrayList<SquareImageView> views = new ArrayList<>();
        for (Integer i : mPhotoViewIDs) {
            SquareImageView squareImageView = (SquareImageView) this.findViewById(i);
            views.add(squareImageView);
        }
        return views;
    }

    @SuppressWarnings("unused")
    public ArrayList<String> getInternetUrls() {
        ArrayList<String> photos = new ArrayList<>();
        for (Integer i : mPhotoViewIDs) {
            SquareImageView squareImageView = (SquareImageView) this.findViewById(i);
            if (!TextUtils.isEmpty(squareImageView.getInternetUrl())) {
                photos.add(squareImageView.getInternetUrl());
            }
        }
        return photos;
    }

    @SuppressWarnings("unused")
    public boolean isEmpty() {
        boolean empty = true;
        for (Integer i : mPhotoViewIDs) {
            SquareImageView squareImageView = (SquareImageView) this.findViewById(i);
            if (squareImageView.getUploadImageKey() != null) {
                empty = false;
            }
        }
        return empty;
    }

    @SuppressWarnings("unused")
    public SquareImageView findFirstSquareView() {
        return (SquareImageView) this.findViewById(mPhotoViewIDs.get(0));
    }

    public void setFragmentManager(FragmentManager manager) {
        mManager = manager;
    }

    public void setUnionKey(String key) {
        unionKey = key;
    }

    private int createIndex() {
        long time = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("mmss");
        String date = sdf.format(new Date(time));
        int num1 = Integer.valueOf(date + Long.toString(time % 1000));
        int i = num1 * 10 + mPhotoViewIDs.size();
        return i;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable parcelable = super.onSaveInstanceState();
        final ImageGroupSavedState imageSaveState = new ImageGroupSavedState(parcelable);
        imageSaveState.setSquarePhotos(getSquarePhotos());
        return imageSaveState;
    }

    @Override
    protected void onRestoreInstanceState(final Parcelable state) {
        if (!(state instanceof ImageGroupSavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        final ImageGroupSavedState ss = (ImageGroupSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        imageGroupSavedState = ss;
    }

    public void onParentResult(int requestCode, Intent data) {
        if (data != null) {
            if (data.hasExtra(NavigatorImage.EXTRA_PHOTO_URL)) {
                String photoTakeUrl = data.getStringExtra(NavigatorImage.EXTRA_PHOTO_URL);
                if (requestCode == NavigatorImage.RESULT_TAKE_PHOTO && null != photoTakeUrl) {
                    List<String> image = new ArrayList<>();
                    image.add(photoTakeUrl);
                    doSelectPhotos(image);
                }
            } else {
                List<String> images = data.getStringArrayListExtra(NavigatorImage.EXTRA_PHOTOS_URL);
                ArrayList<Integer> positions = data.getIntegerArrayListExtra(NavigatorImage.EXTRA_IMAGE_URL_POSITION);
                if (requestCode == NavigatorImage.RESULT_IMAGE_SWITCHER && null != positions) {
                    doPhotosDelete(positions);
                } else if (requestCode == NavigatorImage.RESULT_SELECT_PHOTOS && null != images) {
                    doSelectPhotos(images);
                }
            }

        }


    }

    private void doSelectPhotos(List<String> images) {
        for (String url : images) {
            doSelectPhotosByUrl(url);
        }
    }

    private void doSelectPhotosByUrl(String url) {
        refreshPhotoView(url);
    }

    private void refreshPhotoView(String url) {
        final SquareImageView squarePhotoView = (SquareImageView) findViewById(mPhotoViewIDs.get(mPhotoViewIDs.size() - 1));
        ImageGroupDisplayHelper.displayImageLocal(squarePhotoView, url, 200, 200);
        squarePhotoView.setFocusable(true);
        squarePhotoView.setFocusableInTouchMode(true);
        squarePhotoView.requestFocus();
        squarePhotoView.setLocalUrl(url);
        squarePhotoView.setUploadKey("image_" + unionKey + "_" + System.currentTimeMillis());

        addButton(squarePhotoView);
        addPhoto(squarePhotoView.getId());
        uploadImage(squarePhotoView);
        setHintText();
    }

    private void uploadImage(final SquareImageView squarePhotoView) {
        final AppCompatActivity activity = ((AppCompatActivity) getContext());
        UploadUtils.asyncPutObjectFromLocalFile(activity, squarePhotoView.getLocalUrl(), new UploadCallback() {
            @Override
            public void onProgress(PutObjectRequest request, final long currentSize, final long totalSize) {

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        double percent = (currentSize * 1.0 / totalSize * 1.0) * 90;
                        FrameLayout frameLayout = (FrameLayout) squarePhotoView.getParent();

                        ProgressBar progressBar = (ProgressBar) frameLayout.findViewById(squarePhotoView.getId() + 11);
                        progressBar.setProgress((int) percent);
                    }
                });


            }

            @Override
            public void onSuccess(final PutObjectRequest request, PutObjectResult result) {

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        FrameLayout frameLayout = (FrameLayout) squarePhotoView.getParent();
                        ProgressBar progressBar = (ProgressBar) frameLayout.findViewById(squarePhotoView.getId() + 11);
                        progressBar.setProgress(100);
                        ImageView imageView = (ImageView) frameLayout.findViewById(squarePhotoView.getId() + 10);
                        imageView.setTag("delete");
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setImageResource(R.drawable.common_add_image_delete);
//                        squarePhotoView.setUploadKey(UploadUtils.DOWNLOAD_IMAGE + request.getObjectKey());
//                        squarePhotoView.setInternetData(UploadUtils.DOWNLOAD_IMAGE + request.getObjectKey());
                        squarePhotoView.setUploadUrl(UploadUtils.DOWNLOAD_IMAGE + request.getObjectKey());
                        Log.e("ImageGroupView", (UploadUtils.DOWNLOAD_IMAGE + request.getObjectKey()));

                    }
                });


            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        FrameLayout frameLayout = (FrameLayout) squarePhotoView.getParent();
                        ProgressBar progressBar = (ProgressBar) frameLayout.findViewById(squarePhotoView.getId() + 11);
                        ImageView imageView = (ImageView) frameLayout.findViewById(squarePhotoView.getId() + 10);
                        progressBar.setProgress(0);
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setTag("retry");
                        imageView.setImageResource(R.drawable.common_add_image_retry);
                    }
                });

            }
        });
    }

    private void addButton(SquareImageView squarePhotoView) {
        if (!isAddButton(squarePhotoView)) {
            doDeletePhoto(squarePhotoView);
        }
    }

    private void refreshPhotoView(Uri uri) {
        if (imageGroupSavedState != null) {
            preTakePhotoUri = uri;
            return;
        } else {
            preTakePhotoUri = null;
        }

        SquareImageView squarePhotoView = (SquareImageView) findViewById(mPhotoViewIDs.get(mPhotoViewIDs.size() - 1));
        ImageGroupDisplayHelper.displayImage(squarePhotoView, uri, 200, 200);
        squarePhotoView.setFocusable(true);
        squarePhotoView.setFocusableInTouchMode(true);
        squarePhotoView.requestFocus();
        squarePhotoView.setLocalUrl(ImageGroupUtils.getPathOfPhotoByUri(getContext(), uri));
        squarePhotoView.setUploadKey("image_" + unionKey + "_" + System.currentTimeMillis());
        addButton(squarePhotoView);
        addPhoto(squarePhotoView.getId());
    }

    public HashMap<String, String> getUploadImageUrlKeys() {
        HashMap<String, String> map = new HashMap<>();
        for (Integer i : mPhotoViewIDs) {
            SquareImageView squareImageView = (SquareImageView) this.findViewById(i);
            if (TextUtils.isEmpty(squareImageView.getInternetUrl()) && !TextUtils.isEmpty(squareImageView.getLocalUrl())) {
                map.put(squareImageView.getUploadImageKey(), squareImageView.getLocalUrl());
            }
        }
        return map;
    }

    public ArrayList<String> getImageKeys() {
        ArrayList<String> map = new ArrayList<>();
        for (Integer i : mPhotoViewIDs) {
            SquareImageView squareImageView = (SquareImageView) this.findViewById(i);
            if (!TextUtils.isEmpty(squareImageView.getInternetUrl()) || !TextUtils.isEmpty(squareImageView.getLocalUrl())) {
                map.add(squareImageView.getUploadImageKey());
            }
        }
        return map;
    }

    public ArrayList<String> getUploadUrl() {
        ArrayList<String> map = new ArrayList<>();
        for (Integer i : mPhotoViewIDs) {
            SquareImageView squareImageView = (SquareImageView) this.findViewById(i);
            if (!TextUtils.isEmpty(squareImageView.getUploadUrl())) {
                map.add(squareImageView.getUploadUrl());
            } else if (!TextUtils.isEmpty(squareImageView.getInternetUrl())) {
                map.add(squareImageView.getInternetUrl());
            }
        }
        return map;
    }

    public String getImageKeyString() {
        ArrayList<String> keys = getImageKeys();
        if (keys.isEmpty()) return null;
        StringBuffer sb = new StringBuffer();
        for (String key : keys) {
            sb.append(key);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    @SuppressWarnings("unused")
    public void setOnImageClickListener(OnImageClickListener listener) {
        clickListener = listener;
    }

    public interface OnImageClickListener {
        void onImageClick(SquareImage clickImage, ArrayList<SquareImage> squareImages, ArrayList<String> allImageInternetUrl);
    }
}
