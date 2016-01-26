package com.aosijia.dragonbutler.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.model.BankAdvertisements;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.activity.CardActivity;
import com.aosijia.dragonbutler.ui.activity.CommonWebViewActivity;
import com.aosijia.dragonbutler.ui.activity.CommunityAnnouncementsActivity;
import com.aosijia.dragonbutler.ui.activity.CommunityAnnouncementsDetailActivity;
import com.aosijia.dragonbutler.ui.activity.MaintenanceActivity;
import com.aosijia.dragonbutler.ui.activity.NeighborHoodActivity;
import com.aosijia.dragonbutler.ui.activity.PropertyBillsActivity;
import com.aosijia.dragonbutler.ui.widget.Kanner;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshBase;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshScrollView;
import com.aosijia.dragonbutler.utils.Constants;
import com.aosijia.dragonbutler.utils.Uiutils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by wanglj on 15/12/25.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private PullToRefreshScrollView scrollView;
    private Kanner homeImageKander;
    private ImageView homeCommunityAnnouncement;
    private ImageView homeMaintenanceImage;
    private ImageView homePropertyBillImage;
    private ImageView homeForumImage;
    private TextView homeCardText;
    private TextView homeLoanText;
    private TextView homeEBusinessText;
    List<BankAdvertisements.DataEntity.BankAdvertisementsEntity> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scrollView = (PullToRefreshScrollView) view.findViewById(R.id.scroll_view);
        homeImageKander = (Kanner) view.findViewById(R.id.homeImageKander);
        int width = Uiutils.getScreenWidth(getActivity());
        int height = (int) (width * 11 / 38f);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        homeImageKander.setLayoutParams(params);
        homeCommunityAnnouncement = (ImageView) view.findViewById(R.id.homeCommunityAnnouncement);
        homeMaintenanceImage = (ImageView) view.findViewById(R.id.homeMaintenanceImage);
        homePropertyBillImage = (ImageView) view.findViewById(R.id.homePropertyBillImage);
        homeForumImage = (ImageView) view.findViewById(R.id.homeForumImage);
        homeCardText = (TextView) view.findViewById(R.id.homeCardTextView);
        homeLoanText = (TextView) view.findViewById(R.id.homeLoanText);
        homeEBusinessText = (TextView) view.findViewById(R.id.homeEBusinessText);


        homeCommunityAnnouncement.setOnClickListener(this);
        homeMaintenanceImage.setOnClickListener(this);
        homePropertyBillImage.setOnClickListener(this);
        homeForumImage.setOnClickListener(this);
        homeCardText.setOnClickListener(this);
        homeLoanText.setOnClickListener(this);
        homeEBusinessText.setOnClickListener(this);
        homeImageKander.setOnItemClickListener(new Kanner.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (list.size() > 0) {
                    Intent intent = new Intent(getActivity(), CommunityAnnouncementsDetailActivity.class);
                    intent.putExtra("bankAdvertisementsEntity", list.get(position - 1));
                    startActivity(intent);
                }

            }
        });


        scrollView.setHideSubText(true);
        scrollView.setScrollingWhileRefreshingEnabled(true);
        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {

                getBankAdv(true);

            }
        });


        getBankAdv(false);
    }

    private void getBankAdv(final boolean isRefresh) {
        Map<String, String> parameter = RequestParameters.bankAdvertisements();
        new OkHttpRequest.Builder().url(URLManager.BANK_ADVERTISEMENTS).params(parameter).tag(this).get(new ResultCallback<BankAdvertisements>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(BankAdvertisements response) {
                if (isRefresh) {
                    scrollView.onRefreshComplete();
                }
                list = response.getData().getBank_advertisements();
                String[] urls = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    urls[i] = list.get(i).getTheme_pic_url();
                }
                if (urls.length > 0) {
                    homeImageKander.setImagesUrl(urls, R.drawable.default_pic_bank_adv);
                }


            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //社区公告
            case R.id.homeCommunityAnnouncement:
                startActivity(new Intent(getActivity(), CommunityAnnouncementsActivity.class));
                break;
            //物业保修
            case R.id.homeMaintenanceImage:
                startActivity(new Intent(getActivity(), MaintenanceActivity.class));
                break;
            //物业账单
            case R.id.homePropertyBillImage:
                startActivity(new Intent(getActivity(), PropertyBillsActivity.class));
                break;
            //论坛
            case R.id.homeForumImage:
                startActivity(new Intent(getActivity(), NeighborHoodActivity.class));
                break;
            //信用卡
            case R.id.homeCardTextView:
                Intent intent_card= new Intent(getActivity(), CardActivity.class);
                startActivity(intent_card);
                break;
            //贷款
            case R.id.homeLoanText:
                Intent intent_loans= new Intent(getActivity(), CommonWebViewActivity.class);
                intent_loans.putExtra(CommonWebViewActivity.EXTRA_KEY_TITLE,"贷款");
                intent_loans.putExtra(CommonWebViewActivity.EXTRA_KEY_URL, Constants.URL_LOANS);
                startActivity(intent_loans);
                break;
            //善融
            case R.id.homeEBusinessText:
                Intent intent_eb= new Intent(getActivity(), CommonWebViewActivity.class);
                intent_eb.putExtra(CommonWebViewActivity.EXTRA_KEY_TITLE,"善融");
                intent_eb.putExtra(CommonWebViewActivity.EXTRA_KEY_URL, Constants.URL_SHANRONG);
                startActivity(intent_eb);
                break;
        }

    }


}
