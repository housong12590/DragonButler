package com.aosijia.dragonbutler.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.ui.fragment.MyCommentsFragment;
import com.aosijia.dragonbutler.ui.fragment.MyCommentsReceivedFragment;
import com.aosijia.dragonbutler.ui.widget.UnderlinePageIndicator;

/**
 * 我的评论
 * Created by Jacky on 2016/1/4.
 * Version 1.0
 */
public class MyCommentActivity extends BaseActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private UnderlinePageIndicator mPagerTabStrip;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    private TextView mNotPayTextView, mHasPayTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_my_comments);
        initView();
    }

    private void initView() {
        setTitle(getString(R.string.my_comments), null, NO_RES_ID);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mPagerTabStrip = (UnderlinePageIndicator)
                findViewById(R.id.indicator);
        mSectionsPagerAdapter = new SectionsPagerAdapter(
                getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mSectionsPagerAdapter.notifyDataSetChanged();
        mPagerTabStrip.setViewPager(mViewPager);
        mPagerTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mNotPayTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                    mHasPayTextView.setTextColor(getResources().getColor(R.color.gray_47));
                } else if (position == 1) {
                    mNotPayTextView.setTextColor(getResources().getColor(R.color.gray_47));
                    mHasPayTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mNotPayTextView = (TextView)
                findViewById(R.id.notPayTextView);
        mHasPayTextView = (TextView) findViewById(R.id.hasPayTextView);
        // 标签头被选中时
        mNotPayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentItem(0);

            }
        });
        mHasPayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentItem(1);
            }
        });
        mNotPayTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
        mHasPayTextView.setTextColor(getResources().getColor(R.color.gray_47));


    }

    private void setCurrentItem(int position){
        mViewPager.setCurrentItem(position);
        if(position == 0){
            mNotPayTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            mHasPayTextView.setTextColor(getResources().getColor(R.color.gray_47));
        }else if(position == 1){
            mNotPayTextView.setTextColor(getResources().getColor(R.color.gray_47));
            mHasPayTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return Fragment.instantiate(MyCommentActivity.this,
                            MyCommentsReceivedFragment.class.getName(), null);
                case 1:
                    return Fragment.instantiate(MyCommentActivity.this,
                            MyCommentsFragment.class.getName(), null);
            }
            return new Fragment();
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
