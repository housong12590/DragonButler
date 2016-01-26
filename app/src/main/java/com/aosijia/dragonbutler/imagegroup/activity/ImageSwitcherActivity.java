package com.aosijia.dragonbutler.imagegroup.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.imagegroup.NavigatorImage;
import com.aosijia.dragonbutler.imagegroup.OnTabOneClickListener;
import com.aosijia.dragonbutler.imagegroup.adapter.ImagesSwitcherAdapter;
import com.aosijia.dragonbutler.imagegroup.model.ImageSwitcherWrapper;
import com.aosijia.dragonbutler.imagegroup.model.SquareImage;
import com.aosijia.dragonbutler.imagegroup.view.MutipleTouchViewPager;
import com.aosijia.dragonbutler.ui.activity.BaseActivity;

import java.util.ArrayList;


public class ImageSwitcherActivity extends BaseActivity implements OnTabOneClickListener {

    private MutipleTouchViewPager pager;
    private ArrayList<ImageSwitcherWrapper> imageSwitcherWrappers;
    private ImagesSwitcherAdapter mAdapter;
    private boolean canImageDelete;
    private int currentPagerPosition;
    private int placeholderDrawable;
    private ArrayList<Integer> deletePositions;
//    private TextView titleRighttextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_image_switcher);
//        setTitle("预览", View.VISIBLE, View.VISIBLE, "删除");
        setTitle("预览", null, R.drawable.btn_back, null, NO_RES_ID);
        deletePositions = new ArrayList<>();
        setBtnLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        parseIntent();
        updateView();
        updateData();
    }

    private void parseIntent() {
        Intent intent = getIntent();
        ArrayList<SquareImage> images = intent.getParcelableArrayListExtra(NavigatorImage.EXTRA_IMAGE_URL);
        setUpImageWrappers(images);
        currentPagerPosition = intent.getIntExtra(NavigatorImage.EXTRA_IMAGE_URL_POSITION, 0);
        canImageDelete = intent.getBooleanExtra(NavigatorImage.EXTRA_IMAGE_DELETE, false);
        placeholderDrawable = intent.getIntExtra(NavigatorImage.EXTRA_IMAGE_PLACE_DRAWABLE_ID, R.mipmap.ic_launcher);
    }

    private void setUpImageWrappers(ArrayList<SquareImage> images) {
        imageSwitcherWrappers = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
           imageSwitcherWrappers.add(new ImageSwitcherWrapper(images.get(i), i));
        }
    }

    private void updateView() {
        pager = (MutipleTouchViewPager) findViewById(R.id.view_pager);
//        btnDelete = titleRighttextview;
//        btnDelete.setVisibility(canImageDelete ? View.VISIBLE : View.GONE);
        titleRighttextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteImage();
            }
        });
        setUpView();
    }

    private void setUpView() {
        mAdapter = new ImagesSwitcherAdapter(getSupportFragmentManager(), placeholderDrawable);
        mAdapter.setOnTabOneClickListener(this);
        pager.setAdapter(mAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPagerPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void updateData() {
        mAdapter.setImages(createShowImages());
        setCurrentPosition();
    }

    private ArrayList<SquareImage> createShowImages() {
        ArrayList<SquareImage> squareImages = new ArrayList<>();
        for (ImageSwitcherWrapper imageSwitcherWrapper : imageSwitcherWrappers) {
            squareImages.add(imageSwitcherWrapper.squareImage);
        }
        return squareImages;
    }

    private void setCurrentPosition() {
        pager.setCurrentItem(currentPagerPosition);
    }

    private void deleteImage() {
        deletePositions.add(imageSwitcherWrappers.get(currentPagerPosition).originPosition);
        imageSwitcherWrappers.remove(currentPagerPosition);
        if (imageSwitcherWrappers.size() == 0) {
            goBackWithResult();
        } else if (currentPagerPosition == imageSwitcherWrappers.size()) {
            currentPagerPosition = currentPagerPosition - 1;
            updateData();
        } else {
            updateData();
        }
    }

    @Override
    public void onBackPressed() {
        goBackWithResult();
    }

    private void goBackWithResult() {
        Intent intent = new Intent();
        intent.putExtra(NavigatorImage.EXTRA_IMAGE_URL_POSITION, deletePositions);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onTabOneClick() {
        goBackWithResult();
    }
}
