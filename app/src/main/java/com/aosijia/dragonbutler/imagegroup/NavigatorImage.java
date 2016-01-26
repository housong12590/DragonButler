package com.aosijia.dragonbutler.imagegroup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;


import com.aosijia.dragonbutler.imagegroup.activity.AlbumActivity;
import com.aosijia.dragonbutler.imagegroup.activity.ImageSwitcherActivity;
import com.aosijia.dragonbutler.imagegroup.model.SquareImage;

import java.util.ArrayList;

public class NavigatorImage {

    public static final String EXTRA_PHOTO_URL = "extra_photo_url";
    public static final String EXTRA_PHOTOS_URL = "extra_photos_url";
    public static final String EXTRA_IMAGE_URL = "image_url";
    public static final String EXTRA_IMAGE_SELECT_MAX_NUM = "extra_image_select_max_num";
    public static final String EXTRA_IMAGE_URL_POSITION = "image_position";
    public static final String EXTRA_IMAGE_DELETE = "extra_image_delete";
    public static final String EXTRA_IMAGE_PLACE_DRAWABLE_ID = "extra_image_delete";
    public static final String EXTRA_IMAGE_FOLDER = "extra_image_folder";

    public static final int RESULT_SELECT_PHOTO = 2001;
    public static final int RESULT_TAKE_PHOTO = 2003;
    public static final int RESULT_IMAGE_SWITCHER = 2004;
    public static final int RESULT_SELECT_PHOTOS = 2005;

    public static final String[] IMAGE_PROJECTION = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media._ID };

    public static void startImageSwitcherActivity(Context context, ArrayList<SquareImage> images, int position, boolean showAddButton, int placeholderDrawable) {
        Intent intent = new Intent(context, ImageSwitcherActivity.class);
        intent.putParcelableArrayListExtra(NavigatorImage.EXTRA_IMAGE_URL, images);
        intent.putExtra(EXTRA_IMAGE_DELETE, showAddButton);
        intent.putExtra(EXTRA_IMAGE_URL_POSITION, position);
        intent.putExtra(EXTRA_IMAGE_PLACE_DRAWABLE_ID, placeholderDrawable);
        ((Activity)context).startActivityForResult(intent, RESULT_IMAGE_SWITCHER);
    }

    public static void startCustomAlbumActivity(Context context, int canSelectMaxNum) {
        Intent intent = new Intent(context, AlbumActivity.class);
        intent.putExtra(EXTRA_IMAGE_SELECT_MAX_NUM, canSelectMaxNum);
        ((Activity)context).startActivityForResult(intent, RESULT_SELECT_PHOTOS);
    }

}