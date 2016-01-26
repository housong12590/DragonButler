package com.aosijia.dragonbutler.imagegroup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.aosijia.dragonbutler.R;


public class ImageScaleActivity extends AppCompatActivity implements com.aosijia.dragonbutler.imagegroup.OnTabOneClickListener {

    private String url;
    private int placeholderDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_image);

        pareIntent();
        setFragment();
    }

    private void pareIntent() {
        Intent intent = getIntent();
        url = intent.getStringExtra(com.aosijia.dragonbutler.imagegroup.NavigatorImage.EXTRA_IMAGE_URL);
        placeholderDrawable = intent.getIntExtra(com.aosijia.dragonbutler.imagegroup.NavigatorImage.EXTRA_IMAGE_PLACE_DRAWABLE_ID, R.mipmap.ic_launcher);
    }

    private void setFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        com.aosijia.dragonbutler.imagegroup.fragment.ScaleImageFragment scaleImageFragment = com.aosijia.dragonbutler.imagegroup.fragment.ScaleImageFragment.newInstance(new com.aosijia.dragonbutler.imagegroup.model.SquareImage(null, url, null,null, com.aosijia.dragonbutler.imagegroup.model.SquareImage.PhotoType.NETWORK), placeholderDrawable);
        scaleImageFragment.setOneTabListener(this);
        fragmentTransaction.add(R.id.container, scaleImageFragment).commit();
    }

    @Override
    public void onTabOneClick() {
        this.finish();
    }
}
