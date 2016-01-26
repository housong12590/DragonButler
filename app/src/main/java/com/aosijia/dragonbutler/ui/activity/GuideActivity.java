package com.aosijia.dragonbutler.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;

import java.util.ArrayList;
import java.util.prefs.Preferences;

/**
 * 引导页
 * 
 * @author wanglj
 * 
 */
@SuppressLint("InflateParams")
public class GuideActivity extends BaseActivity {

	private ViewPager viewPager;
	private ArrayList<View> pageViews;

	private LinearLayout mDotLinearLayout;
	private ImageView[] mDotsImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);

		
//		final boolean isFromAbout = getIntent().getBooleanExtra("isFromAbout",
//				false);
		mDotLinearLayout = (LinearLayout) findViewById(R.id.linearlayout);
		viewPager = (ViewPager) findViewById(R.id.pager);
		if (Share.getBoolean(Share.GUIDE_V1,false)/*&& !isFromAbout*/) {
			viewPager.setVisibility(View.GONE);
			mDotLinearLayout.setVisibility(View.GONE);
			startActivity(new Intent(GuideActivity.this, LoadingActivity.class));
			finish();
		} else {
			viewPager.setVisibility(View.VISIBLE);
			mDotLinearLayout.setVisibility(View.VISIBLE);
		}

		pageViews = new ArrayList<View>();

        ImageView imageView1 = new ImageView(this);
        imageView1.setBackgroundResource(R.drawable.guide1);

        ImageView imageView2 = new ImageView(this);
        imageView2.setBackgroundResource(R.drawable.guide2);

        ImageView imageView3 = new ImageView(this);
        imageView3.setBackgroundResource(R.drawable.guide3);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Share.putBoolean(Share.GUIDE_V1,true);
                startActivity(new Intent(GuideActivity.this,
									LoadingActivity.class));
                finish();
            }
        });


        pageViews.add(imageView1);
        pageViews.add(imageView2);
        pageViews.add(imageView3);



		viewPager.setAdapter(new GuidePageAdapter());
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		initDots();
	}

	private void initDots() {
		int size = pageViews.size();
		mDotsImage = new ImageView[size];
		for (int i = 0; i < size; i++) {
			ImageView imageView = new ImageView(this);
			LayoutParams param = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			param.leftMargin = 20;
			imageView.setLayoutParams(param);
			// imageView.setPadding(40, 0, 40, 0);
			mDotsImage[i] = imageView;
			if (i == 0) {
				// 默认选中第一张图片
				mDotsImage[i]
						.setBackgroundResource(R.drawable.dot_focus);
			} else {
				mDotsImage[i].setBackgroundResource(R.drawable.dot_blur);
			}
			mDotLinearLayout.addView(mDotsImage[i]);
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int position) {
			for (int i = 0; i < mDotsImage.length; i++) {
				mDotsImage[position]
						.setBackgroundResource(R.drawable.dot_focus);
				if (position != i) {
					mDotsImage[i]
							.setBackgroundResource(R.drawable.dot_blur);
				}
			}
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	// 指引页面数据适配器
	class GuidePageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).removeView(pageViews.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).addView(pageViews.get(arg1));
			return pageViews.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub

		}
	}

}
