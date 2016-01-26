package com.aosijia.dragonbutler.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.model.BankAdvertisements.DataEntity.BankAdvertisementsEntity;
import com.aosijia.dragonbutler.model.CommunityAnnouncement.DataEntity.CommunityAnnouncementsEntity;
import com.aosijia.dragonbutler.ui.widget.Kanner;
import com.aosijia.dragonbutler.utils.TimeUtils;
import com.aosijia.dragonbutler.utils.Uiutils;

/**
 * 公告详情
 * Created by wanglj on 15/12/18.
 */
public class CommunityAnnouncementsDetailActivity extends BaseActivity {

    private TextView announcementTitleTextView;
    private TextView announcementDateTextView;
    private TextView announcementTypeTextView;
    private Kanner announcementImageKander;
    private TextView announcementContentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_community_announcements_detail);
        CommunityAnnouncementsEntity communityAnnouncementsEntity = null;
        BankAdvertisementsEntity bankAdvertisementsEntity = null;
        if(getIntent().hasExtra("communityAnnouncementsEntity")){
            communityAnnouncementsEntity =
                    (CommunityAnnouncementsEntity) getIntent().getExtras().get("communityAnnouncementsEntity");
        }else if(getIntent().hasExtra("bankAdvertisementsEntity")){
            bankAdvertisementsEntity =
                    (BankAdvertisementsEntity) getIntent().getExtras().get("bankAdvertisementsEntity");
        }

        setTitle("公告详情",null, R.drawable.btn_back,null,NO_RES_ID);
        setBtnLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        announcementTitleTextView = (TextView) findViewById(R.id.announcementTitleTextView);
        announcementDateTextView = (TextView) findViewById(R.id.announcementDateTextView);
        announcementTypeTextView = (TextView) findViewById(R.id.announcementTypeTextView);
        announcementImageKander = (Kanner) findViewById(R.id.announcementImageKander);
        int width =  Uiutils.getScreenWidth(this) - (int)getResources().getDimension(R.dimen.kander_padding)*2;
        int height = (int) (width * 2 / 3f);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        announcementImageKander.setLayoutParams(params);
        announcementContentTextView = (TextView) findViewById(R.id.announcementContentTextView);

        if(communityAnnouncementsEntity != null){
            announcementTitleTextView.setText(communityAnnouncementsEntity.getTitle());
            announcementDateTextView.setText("发布时间  " + TimeUtils.getDateToString(communityAnnouncementsEntity.getCreated_at()));
            announcementContentTextView.setText(communityAnnouncementsEntity.getContent());

            String type = "其他";
            if ("1".equals(communityAnnouncementsEntity.getType())) {
                type = "政府公告";
            } else if ("2".equals(communityAnnouncementsEntity.getType())) {
                type = "社区公告";
            } else if ("3".equals(communityAnnouncementsEntity.getType())) {
                type = "资讯";
            }
            announcementTypeTextView.setText(type);

            if(communityAnnouncementsEntity.getPic_urls().size() > 0){
                announcementImageKander.setVisibility(View.VISIBLE);
                announcementImageKander.setImagesUrl(communityAnnouncementsEntity.getPic_urls().toArray());
            }else{
                announcementImageKander.setVisibility(View.GONE);
            }
        }else if(bankAdvertisementsEntity != null){
            announcementTitleTextView.setText(bankAdvertisementsEntity.getTitle());
            announcementDateTextView.setText("发布时间  " + TimeUtils.getDateToString(bankAdvertisementsEntity.getCreated_at()));
            announcementContentTextView.setText(bankAdvertisementsEntity.getContent());
            announcementTypeTextView.setVisibility(View.GONE);



            if(bankAdvertisementsEntity.getPic_urls().size() > 0){
                announcementImageKander.setVisibility(View.VISIBLE);
                announcementImageKander.setImagesUrl(bankAdvertisementsEntity.getPic_urls().toArray());
            }else{
                announcementImageKander.setVisibility(View.GONE);
            }
        }




    }

}
