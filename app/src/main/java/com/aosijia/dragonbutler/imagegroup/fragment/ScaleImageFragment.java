package com.aosijia.dragonbutler.imagegroup.fragment;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aosijia.dragonbutler.R;
import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;


import java.io.File;
import java.io.FileNotFoundException;

public class ScaleImageFragment extends Fragment {

    private SimpleDraweeView viewPlaceholder;
    private com.aosijia.dragonbutler.imagegroup.photodraweeview.PhotoDraweeView viewScale;
    private com.aosijia.dragonbutler.imagegroup.OnTabOneClickListener listener;
    private com.aosijia.dragonbutler.imagegroup.model.SquareImage squareImage;
    private int placeholderDrawable;
    private View mUpPlaceHolderView;

    public static ScaleImageFragment newInstance(com.aosijia.dragonbutler.imagegroup.model.SquareImage image, int placeholderDrawable) {
        ScaleImageFragment scaleImageFragment = new ScaleImageFragment();
        scaleImageFragment.squareImage = image;
        scaleImageFragment.placeholderDrawable = placeholderDrawable;
        return scaleImageFragment;
    }

    public void setOneTabListener(com.aosijia.dragonbutler.imagegroup.OnTabOneClickListener listener) {
        this.listener = listener;
    }

    public void setSquareImage(com.aosijia.dragonbutler.imagegroup.model.SquareImage squareImage) {
        this.squareImage = squareImage;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scale_image, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateView(view);
        setUpdata();
    }

    private void updateView(View view) {
        setUpPlaceHolderView(view);

        GenericDraweeHierarchyBuilder builder1 = new GenericDraweeHierarchyBuilder(getContext().getResources());
        builder1.setPlaceholderImage(ContextCompat.getDrawable(getContext(), placeholderDrawable), ScalingUtils.ScaleType.CENTER_CROP);

        viewScale = (com.aosijia.dragonbutler.imagegroup.photodraweeview.PhotoDraweeView) view.findViewById(R.id.image_scale_image);
        viewScale.getAttacher().setOnViewTapListener(new com.aosijia.dragonbutler.imagegroup.photodraweeview.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                if (viewScale.getScale() == viewScale.getMinimumScale() && listener != null) {
                    listener.onTabOneClick();
                } else {
                    viewScale.getAttacher().setScale(viewScale.getMinimumScale(), x, y, true);
                }
            }
        });
        viewScale.getAttacher().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("保存该图片？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doSaveImage(squareImage.interNetUrl);
                    }
                }).setNegativeButton("取消", null);
                builder.show();
                return false;
            }
        });
    }

    private void doSaveImage(String imageUrl) {
        ImageRequest downloadRequest = ImageRequest.fromUri(Uri.parse(imageUrl));
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(downloadRequest);
        if (ImagePipelineFactory.getInstance().getMainDiskStorageCache().hasKey(cacheKey)) {
            BinaryResource resource = ImagePipelineFactory.getInstance().getMainDiskStorageCache().getResource(cacheKey);
            File cacheFile = ((FileBinaryResource) resource).getFile();
            try {
                MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), cacheFile.getAbsolutePath(), cacheFile.getName(), "");
                Toast.makeText(getActivity(), "图片保存成功", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                showErrorMessage();
            }
        } else {
            showErrorMessage();
        }
    }

    private void showErrorMessage() {
        Toast.makeText(getActivity(), "图片正在加载中，请稍后保存", Toast.LENGTH_SHORT).show();
    }

    private void setUpdata() {
        switch (squareImage.type) {
            case LOCAL:
                com.aosijia.dragonbutler.imagegroup.utils.ImageDisplayHelper.displayImageLocal(viewPlaceholder, squareImage.localUrl, 200, 200);
                com.aosijia.dragonbutler.imagegroup.utils.ImageDisplayHelper.displayImageLocal(viewScale, squareImage.localUrl);
                break;
            case NETWORK:
                com.aosijia.dragonbutler.imagegroup.utils.ImageDisplayHelper.displayImage(viewPlaceholder, squareImage.interNetUrl, 200, 200);
                com.aosijia.dragonbutler.imagegroup.utils.ImageDisplayHelper.displayImage(viewScale, squareImage.interNetUrl);
                break;
        }
    }

    public void setUpPlaceHolderView(View view) {
        viewPlaceholder = (SimpleDraweeView) view.findViewById(R.id.image_scale_placeholder);
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(300)
                .setPlaceholderImage(ContextCompat.getDrawable(getContext(), placeholderDrawable))
                .build();
        viewPlaceholder.setHierarchy(hierarchy);
    }
}
