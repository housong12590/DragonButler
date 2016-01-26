package com.aosijia.dragonbutler.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.ui.activity.MainTabActivity;
import com.aosijia.dragonbutler.ui.activity.MeActivity;
import com.aosijia.dragonbutler.ui.activity.MessageBoxActivity;
import com.aosijia.dragonbutler.ui.activity.MyCommentActivity;
import com.aosijia.dragonbutler.ui.activity.MyEventActivity;
import com.aosijia.dragonbutler.ui.activity.MyFavoriteActivity;
import com.aosijia.dragonbutler.ui.activity.MyPostActivity;
import com.aosijia.dragonbutler.ui.activity.SettingActivity;
import com.aosijia.dragonbutler.ui.activity.SystemMessagesActivity;
import com.aosijia.dragonbutler.ui.widget.pulltozoomview.PullToZoomScrollViewEx;
import com.aosijia.dragonbutler.utils.DisplayOpitionFactory;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by wanglj on 15/12/25.
 */
public class MeFragment extends BaseFragment implements View.OnClickListener {
    private RelativeLayout titleLayout;
    private TextView titleCentertextview;
    private ImageView titleRightimageview;
    private RelativeLayout meLayout;
    private ImageView avatarImageView;
    private TextView nameTextView;
    private TextView communityTextView;
    private RelativeLayout messageLayout;
    private TextView tvMessage;
    private TextView tvMessageCount;
    private RelativeLayout postLayout;
    private RelativeLayout commentLayout;
    private RelativeLayout favoritesLayout;
    private RelativeLayout activityLayout;
    private RelativeLayout systemLayout;
    private TextView tvSystemMsgCount;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PullToZoomScrollViewEx scrollView = (PullToZoomScrollViewEx) view.findViewById(R.id.scroll_view);
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_me_header, null, false);
        View zoomView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_me_zoom, null, false);
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_me_content, null, false);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);
        scrollView.setHideHeader(false);

//        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
//        int mScreenHeight = localDisplayMetrics.heightPixels;
//        int mScreenWidth = localDisplayMetrics.widthPixels;
//        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
//        scrollView.setHeaderLayoutParams(localObject);

        titleLayout = (RelativeLayout) headView.findViewById(R.id.titleLayout);
        titleCentertextview = (TextView) headView.findViewById(R.id.title_centertextview);
        titleRightimageview = (ImageView) headView.findViewById(R.id.title_rightimageview);
        titleRightimageview.setOnClickListener(this);
        meLayout = (RelativeLayout) headView.findViewById(R.id.meLayout);
        meLayout.setOnClickListener(this);
        avatarImageView = (ImageView) headView.findViewById(R.id.avatarImageView);
        nameTextView = (TextView) headView.findViewById(R.id.nameTextView);
        communityTextView = (TextView) headView.findViewById(R.id.communityTextView);
        messageLayout = (RelativeLayout) contentView.findViewById(R.id.messageLayout);
        messageLayout.setOnClickListener(this);
        tvMessage = (TextView) contentView.findViewById(R.id.tv_message);
        tvMessageCount = (TextView) contentView.findViewById(R.id.tv_messageCount);
        postLayout = (RelativeLayout) contentView.findViewById(R.id.postLayout);
        postLayout.setOnClickListener(this);
        commentLayout = (RelativeLayout) contentView.findViewById(R.id.commentLayout);
        commentLayout.setOnClickListener(this);
        favoritesLayout = (RelativeLayout) contentView.findViewById(R.id.favoritesLayout);
        favoritesLayout.setOnClickListener(this);
        activityLayout = (RelativeLayout) contentView.findViewById(R.id.activityLayout);
        activityLayout.setOnClickListener(this);
        systemLayout = (RelativeLayout) contentView.findViewById(R.id.systemLayout);
        tvSystemMsgCount = (TextView) contentView.findViewById(R.id.tv_systemMsgCount);
        systemLayout.setOnClickListener(this);

        refreshUserInfo();
        ((MainTabActivity) getActivity()).getUnreadMessageCount(this);
        ((MainTabActivity)getActivity()).getSystemMessagesCount(this);

    }


    public void refreshUserInfo() {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        if (loginResp == null) {
            nameTextView.setText("未登录");
            communityTextView.setVisibility(View.GONE);
        } else {
            String avatarUrl = loginResp.getData().getUser_info().getAvatar_url();
            if (!TextUtils.isEmpty(avatarUrl)) {
                ImageLoader.getInstance().displayImage(avatarUrl, avatarImageView, DisplayOpitionFactory.sAvatarDisplayOption);
            }
            communityTextView.setVisibility(View.VISIBLE);
            nameTextView.setText(loginResp.getData().getUser_info().getNickname());
            if (loginResp.getData().getUser_info().getHousehold() != null) {
                communityTextView.setText(loginResp.getData().getUser_info().getHousehold().getCommunity_title());
            }
        }
    }

    public void setUnreadMessage(int messageCount) {
        if (messageCount > 0) {
            tvMessageCount.setVisibility(View.VISIBLE);
            if (messageCount > 99) {
                tvMessageCount.setText("99");
            } else {
                tvMessageCount.setText(String.valueOf(messageCount));
            }
        } else {
            tvMessageCount.setVisibility(View.GONE);
        }
    }

    public void setSystemMsgCount(int msgCount) {
        if (msgCount > 0) {
            tvSystemMsgCount.setVisibility(View.VISIBLE);
            if (msgCount > 99) {
                tvSystemMsgCount.setText("99");
            } else {
                tvSystemMsgCount.setText(String.valueOf(msgCount));
            }
        } else {
            tvSystemMsgCount.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.title_rightimageview://设置
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.meLayout://个人中心
                startActivity(new Intent(getActivity(), MeActivity.class));
                break;
            case R.id.messageLayout://我的消息
                startActivity(new Intent(getActivity(), MessageBoxActivity.class));
                break;
            case R.id.postLayout://我的发布
                intent.setClass(getActivity(), MyPostActivity.class);
                startActivity(intent);
                break;
            case R.id.commentLayout://我的评论
                intent.setClass(getActivity(), MyCommentActivity.class);
                startActivity(intent);
                break;
            case R.id.favoritesLayout://我的收藏
                intent.setClass(getActivity(), MyFavoriteActivity.class);
                startActivity(intent);
                break;
            case R.id.activityLayout://我的活动
                intent.setClass(getActivity(), MyEventActivity.class);
                startActivity(intent);
                break;
            case R.id.systemLayout://系统消息
                intent.setClass(getActivity(), SystemMessagesActivity.class);
                startActivity(intent);
                break;

        }
    }
}
