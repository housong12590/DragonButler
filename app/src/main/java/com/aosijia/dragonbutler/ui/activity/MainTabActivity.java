package com.aosijia.dragonbutler.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.model.SystemMessages;
import com.aosijia.dragonbutler.model.UnreadMessage;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.fragment.CommunityFragment;
import com.aosijia.dragonbutler.ui.fragment.ConveniencesFragment;
import com.aosijia.dragonbutler.ui.fragment.HomeFragment;
import com.aosijia.dragonbutler.ui.fragment.MeFragment;
import com.aosijia.dragonbutler.ui.widget.CustomFragmentTabHost;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.Map;

/**
 * Created by wanglj on 15/12/25.
 */
public class MainTabActivity extends BaseActivity {

    public CustomFragmentTabHost mTabHost;
    public static final String TAB_HOME = "TAB_HOME";
    public static final String TAB_FORUM = "TAB_FORUM";
    public static final String TAB_CONVENIENCE = "TAB_CONVENIENCE";
    public static final String TAB_ME = "TAB_ME";


    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("logout")) {
            startActivity(new Intent(this, LoginActivity.class));
        }
        setContentView(R.layout.activity_main_tab);
        mTabHost = (CustomFragmentTabHost) findViewById(android.R.id.tabhost);
        final FragmentManager fm = getSupportFragmentManager();
        mTabHost.setup(this, fm, R.id.realtabcontent);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals(TAB_FORUM)) {
                    findViewById(R.id.rootLayout).setBackgroundResource(R.color.colorPrimary);
//                    LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
//                    if (loginResp == null) {
//                        Intent intent = new Intent(MainTabActivity.this, LoginActivity.class);
//                        intent.putExtra("action", "tab");
//                        intent.putExtra("index", 1);
//                        startActivity(intent);
//                        mTabHost.setCurrentTab(mCurrentIndex);
//                    } else if (!loginResp.getData().getUser_info().isIs_bound()) {
//                        Intent intent = new Intent(MainTabActivity.this, HouseHoldBindActivity.class);
//                        intent.putExtra("action", "tab");
//                        intent.putExtra("index", 1);
//                        startActivity(intent);
//                        mTabHost.setCurrentTab(mCurrentIndex);
//                    }
                } else if (tabId.equals(TAB_CONVENIENCE)) {
                    findViewById(R.id.rootLayout).setBackgroundResource(R.color.colorPrimary);
                } else if (tabId.equals(TAB_HOME)) {
                    findViewById(R.id.rootLayout).setBackgroundResource(R.color.colorPrimary);
                } else if (tabId.equals(TAB_ME)) {
                    findViewById(R.id.rootLayout).setBackgroundResource(R.color.colorMeTitle);
                }
                mCurrentIndex = mTabHost.getCurrentTab();
            }
        });
        mTabHost.getTabWidget().setDividerDrawable(null);
        initTabSpec();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("MainTabActivit--onDestroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //刷新个人信息
//        if (TAB_ME.equals(mTabHost.getCurrentTabTag())) {
            MeFragment meFragment = (MeFragment) getSupportFragmentManager().findFragmentByTag(TAB_ME);
            if (meFragment != null) {
                meFragment.refreshUserInfo();
                if (TAB_ME.equals(mTabHost.getCurrentTabTag())) {
                    getUnreadMessageCount(meFragment);
                    getSystemMessagesCount(meFragment);
                }
            }
//        }


    }

    private void initTabSpec() {
        // 首页
        View view = LayoutInflater.from(this).inflate(
                R.layout.main_tab_item_home, null);
        Bundle b = new Bundle();
        b.putString("key", TAB_HOME);
        mTabHost.addTab(mTabHost.newTabSpec(TAB_HOME).setIndicator(view),
                HomeFragment.class, b);

        // 小区
        view = LayoutInflater.from(this).inflate(
                R.layout.main_tab_item_neighborhood, null);
        //
        b = new Bundle();
        b.putString("key", TAB_FORUM);
        mTabHost.addTab(mTabHost.newTabSpec(TAB_FORUM).setIndicator(view),
                CommunityFragment.class, b);

        //便民
        view = LayoutInflater.from(this).inflate(
                R.layout.main_tab_item_convenience, null);

        // 便民
        b = new Bundle();
        b.putString("key", TAB_CONVENIENCE);
        mTabHost.addTab(mTabHost.newTabSpec(TAB_CONVENIENCE).setIndicator(view),
                ConveniencesFragment.class, b);
        // 更多
        view = LayoutInflater.from(this).inflate(
                R.layout.main_tab_item_me, null);

        b = new Bundle();
        b.putString("key", TAB_ME);
        mTabHost.addTab(mTabHost.newTabSpec(TAB_ME).setIndicator(view),
                MeFragment.class, b);

        mCurrentIndex = 0;
        mTabHost.setCurrentTab(mCurrentIndex);

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mTabHost.setCurrentTab(intent.getExtras().getInt("index"));
    }

    /**
     * 查询未读消息数量
     *
     * @param meFragment
     */
    public void getUnreadMessageCount(final MeFragment meFragment) {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        if (loginResp != null && loginResp.getData() != null) {
            Map<String, String> parameter = RequestParameters.unreadMessage(loginResp.getData().getAccess_token());
            new OkHttpRequest.Builder().url(URLManager.UNREAD_MESSAGES).params(parameter).tag(this).get(new ResultCallback<UnreadMessage>() {
                @Override
                public void onError(Request request, Exception e) {

                }

                @Override
                public void onResponse(UnreadMessage response) {
                    if (response.getStatus_code().equals("200")) {
                        meFragment.setUnreadMessage(response.getData().getUnread_messages_count());
                    }

                }
            });
        }

    }

    public void getSystemMessagesCount(final MeFragment meFragment) {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        if (loginResp != null && loginResp.getData() != null) {
            String access_token = loginResp.getData().getAccess_token();
            Map<String, String> parameter = RequestParameters.unreadsystemMessages(access_token);
            new OkHttpRequest.Builder().url(URLManager.UNREAD_SYSTEM_MESSAGES).params(parameter).tag(this).get(new ResultCallback<SystemMessages>() {
                @Override
                public void onError(Request request, Exception e) {

                }

                @Override
                public void onResponse(SystemMessages response) {
                    if (response.getStatus_code().equals(URLManager.STATUS_CODE_OK)) {
                        meFragment.setSystemMsgCount(Integer.parseInt(response.getData().getUnread_system_messages_count()));
                    }
                }
            });
        }

    }
}
