package com.aosijia.dragonbutler.imagegroup.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;


import com.aosijia.dragonbutler.imagegroup.OnTabOneClickListener;
import com.aosijia.dragonbutler.imagegroup.fragment.ScaleImageFragment;
import com.aosijia.dragonbutler.imagegroup.model.SquareImage;

import java.util.ArrayList;

public class ImagesSwitcherAdapter extends FragmentPagerAdapter {

    private ArrayList<SquareImage> images;
    private OnTabOneClickListener onTabOneClickListener;
    private int placeholderDrawable;

    public ImagesSwitcherAdapter(FragmentManager fm, int placeholderDrawable) {
        super(fm);
        this.placeholderDrawable = placeholderDrawable;
        images = new ArrayList<>();
    }

    public void setOnTabOneClickListener(OnTabOneClickListener onTabOneClickListener) {
        this.onTabOneClickListener = onTabOneClickListener;
    }

    public void setImages(ArrayList<SquareImage> strings) {
        images.clear();
        images.addAll(strings);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        ScaleImageFragment scaleImageFragment = ScaleImageFragment.newInstance(images.get(position), placeholderDrawable);
        scaleImageFragment.setOneTabListener(onTabOneClickListener);
        return scaleImageFragment;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ScaleImageFragment scaleImageFragment = (ScaleImageFragment) super.instantiateItem(container, position);
        scaleImageFragment.setSquareImage(images.get(position));
        return scaleImageFragment;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
