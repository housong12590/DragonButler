package com.aosijia.dragonbutler.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.ui.fragment.PropertyBillsFragment;
import com.aosijia.dragonbutler.ui.widget.UnderlinePageIndicator;

/**
 * 物业账单
 */
public class PropertyBillsActivity extends BaseActivity {




    private SectionsPagerAdapter mSectionsPagerAdapter;
    private UnderlinePageIndicator mPagerTabStrip;
    public static final int TYPE_NOT_PAID = 1;
    public static final int TYPE_PAID = 2;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    private TextView mNotPayTextView, mHasPayTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_property_bills);
        setTitle("物业账单", null, R.drawable.btn_back,null, NO_RES_ID);
        initView();

        setBtnLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void initView() {
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
                    Fragment f1 = Fragment.instantiate(PropertyBillsActivity.this,
                            PropertyBillsFragment.class.getName(), null);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("type",TYPE_NOT_PAID+"");
                    f1.setArguments(bundle1);
                    return f1;
                case 1:
                    Fragment f2 = Fragment.instantiate(PropertyBillsActivity.this,
                            PropertyBillsFragment.class.getName(), null);
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("type",TYPE_PAID+"");
                    f2.setArguments(bundle2);
                    return f2;
            }
            return new Fragment();
        }

        @Override
        public int getCount() {
            return 2;
        }
    }




}

