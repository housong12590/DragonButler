package com.aosijia.dragonbutler.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.ui.fragment.TransferOrBuyFragment;
import com.aosijia.dragonbutler.ui.widget.UnderlinePageIndicator;
import com.aosijia.dragonbutler.ui.widget.popupwindow.ActionItem;
import com.aosijia.dragonbutler.ui.widget.popupwindow.TitlePopup;

/**
 * 二手列表
 */
public class SecondhandActivity extends BaseActivity {

    private TextView transferTextView;
    public static final int REQUEST_CREATE_CODE = 1001;
    public static final int REQUEST_DETAIL_CODE = 1002;
    private TextView buyTextView;
    private UnderlinePageIndicator indicator;
    private ViewPager pager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private TitlePopup titlePopup;

    public static final String TYPE_TRANSFER = "1";
    public static final String TYPE_BUY = "2";
    private TransferOrBuyFragment transfer;
    private TransferOrBuyFragment buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_secondhand);
        setTitle("二手市场", null, R.drawable.btn_back, null, R.drawable.btn_add);
        initView();
        initPopupView();
        setBtnLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setBtnRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titlePopup.show(v);
            }
        });
    }


    private void initView() {
        transferTextView = (TextView) findViewById(R.id.transferTextView);
        buyTextView = (TextView) findViewById(R.id.buyTextView);
        indicator = (UnderlinePageIndicator) findViewById(R.id.indicator);
        pager = (ViewPager) findViewById(R.id.pager);

        transferTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
        buyTextView.setTextColor(getResources().getColor(R.color.gray_47));

        mSectionsPagerAdapter = new SectionsPagerAdapter(
                getSupportFragmentManager());
        pager.setAdapter(mSectionsPagerAdapter);
        pager.setOffscreenPageLimit(2);

        indicator.setViewPager(pager);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    transferTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                    buyTextView.setTextColor(getResources().getColor(R.color.gray_47));
                } else if (position == 1) {
                    transferTextView.setTextColor(getResources().getColor(R.color.gray_47));
                    buyTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 标签头被选中时
        transferTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentItem(0);

            }
        });
        buyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentItem(1);
            }
        });
    }

    private void initPopupView() {
        titlePopup = new TitlePopup(this, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
            @Override
            public void onItemClick(ActionItem item, int position) {
                //
                String createType = TYPE_BUY;
                if (position == 0) {
                    createType = TYPE_TRANSFER;
                } else if (position == 1) {
                    createType = TYPE_BUY;
                }
                Intent intent = new Intent(SecondhandActivity.this, SecondhandCreateActivity.class);
                intent.putExtra("createType", createType);
                startActivityForResult(intent,REQUEST_CREATE_CODE);
            }
        });
        titlePopup.addAction(new ActionItem(this, "转让", R.drawable.ic_secondhand_transfer));
        titlePopup.addAction(new ActionItem(this, "求购", R.drawable.ic_secondhand_buy));
    }


    private void setCurrentItem(int position) {
        pager.setCurrentItem(position);
        if (position == 0) {
            transferTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            buyTextView.setTextColor(getResources().getColor(R.color.gray_47));
        } else if (position == 1) {
            transferTextView.setTextColor(getResources().getColor(R.color.gray_47));
            buyTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
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
                    transfer = (TransferOrBuyFragment) Fragment.instantiate(SecondhandActivity.this,
                            TransferOrBuyFragment.class.getName(), null);
                    Bundle transferArg = new Bundle();
                    transferArg.putString("type", TYPE_TRANSFER);
                    System.out.println("transfer" + transfer.toString());
                    transfer.setArguments(transferArg);
                    return transfer;
                case 1:
                    buy = (TransferOrBuyFragment) Fragment.instantiate(SecondhandActivity.this,
                            TransferOrBuyFragment.class.getName(), null);
                    System.out.println("buy" + buy.toString());
                    Bundle buyArg = new Bundle();
                    buyArg.putString("type", TYPE_BUY);
//                    buyArg.putString("type",TYPE_TRANSFER);
                    buy.setArguments(buyArg);
                    return buy;
            }
            return new Fragment();
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (pager.getCurrentItem()) {
                case 0:
                    transfer.loadData(TYPE_LOAD_REFRESH);
                    break;
                case 1:
                    buy.loadData(TYPE_LOAD_REFRESH);
                    break;
            }
        }
    }

}
