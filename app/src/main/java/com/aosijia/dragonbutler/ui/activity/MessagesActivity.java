package com.aosijia.dragonbutler.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.model.BaseResp;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.model.Messages;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.adapter.MessageChatAdapter;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshBase;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshListView;
import com.aosijia.dragonbutler.utils.Constants;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by wanglj on 16/1/7.
 */
public class MessagesActivity extends BaseActivity implements View.OnClickListener {

    private PullToRefreshListView listview;
    private MessageChatAdapter messageChatAdapter;
    private List<Messages.DataEntity.MessagesEntity> messagesEntityList = new ArrayList<>();
    public String nickName;
    public String avatarUrl;
    public String userId;
    public String myAvatarUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_messages);
        nickName = getIntent().getStringExtra("nickName");
        avatarUrl = getIntent().getStringExtra("avatar");
        userId = getIntent().getStringExtra("userId");
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        myAvatarUrl = loginResp.getData().getUser_info().getAvatar_url();
        setTitle(nickName, null, R.drawable.btn_back, null, NO_RES_ID);


        setBtnLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listview = (PullToRefreshListView) findViewById(R.id.listView);
        listview.getLoadingLayoutProxy().setPullLabel("加载更多");
        listview.getLoadingLayoutProxy().setReleaseLabel("正在载入");
        findViewById(R.id.sendButton).setOnClickListener(this);
        getMessages(false);

        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                getMessages(true);
            }
        });

    }


    private void getMessages(final boolean isLoadMore) {
        String messageId = null;
        if(isLoadMore && messagesEntityList.size() >= 20){
            messageId = messagesEntityList.get(messagesEntityList.size() - 1).getMessege_id();
        }

        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        Map<String, String> parameter = RequestParameters.messages(loginResp.getData().getAccess_token(), userId, Constants.PAGE_SIZE, messageId);
        new OkHttpRequest.Builder().url(URLManager.MESSAGES).params(parameter).tag(this).get(new ResultCallback<Messages>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(Messages messages) {
                if (messages.isSuccess(MessagesActivity.this)) {
                    if(isLoadMore){
                        messagesEntityList.addAll(messages.getData().getMessages());
                        Collections.sort(messagesEntityList, new Comparator<Messages.DataEntity.MessagesEntity>() {
                            @Override
                            public int compare(Messages.DataEntity.MessagesEntity lhs, Messages.DataEntity.MessagesEntity rhs) {
                                return lhs.getCreated_at().compareTo(rhs.getCreated_at());
                            }
                        });
                        messageChatAdapter.setData(messagesEntityList);
                        listview.onRefreshComplete();
                    }else{
                        messagesEntityList = messages.getData().getMessages();
                        Collections.sort(messagesEntityList, new Comparator<Messages.DataEntity.MessagesEntity>() {
                            @Override
                            public int compare(Messages.DataEntity.MessagesEntity lhs, Messages.DataEntity.MessagesEntity rhs) {
                                return lhs.getCreated_at().compareTo(rhs.getCreated_at());
                            }
                        });
                        messageChatAdapter = new MessageChatAdapter(MessagesActivity.this, messagesEntityList);
                        listview.setAdapter(messageChatAdapter);
                        if( messages.getData().getMessages().size() == 20){
                            listview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        }else{
                            listview.setMode(PullToRefreshBase.Mode.DISABLED);
                        }
                    }

                } else {
                    Toast.makeText(MessagesActivity.this, messages.getMsg(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void messageCreate(final String content) {
        getSendEidtText().setText("");


        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);

        Map<String, String> parameter = RequestParameters.messageCreate(loginResp.getData().getAccess_token(), userId, content);
        new OkHttpRequest.Builder().url(URLManager.MESSAGE_CREATE).params(parameter).tag(this).post(new ResultCallback<BaseResp>() {
            @Override
            public void onError(Request request, Exception e) {
                dismissProgressDialog();
                showRequestError();
            }

            @Override
            public void onAfter() {
                super.onAfter();
                dismissProgressDialog();
            }

            @Override
            public void onBefore(Request request) {
                showProgressDialow("正在发送...");
            }

            @Override
            public void onResponse(BaseResp response) {
                if (response.isSuccess(MessagesActivity.this)) {
                    Messages.DataEntity.MessagesEntity messagesEntity = new Messages.DataEntity.MessagesEntity();
                    messagesEntity.setContent(content);
                    messagesEntity.setCreated_at(System.currentTimeMillis() / 1000 + "");
                    messagesEntity.setType("1");
                    messagesEntity.setMessege_id("");
                    messagesEntityList.add(messagesEntityList.size(), messagesEntity);
                    if (messageChatAdapter == null) {
                        messageChatAdapter = new MessageChatAdapter(MessagesActivity.this, messagesEntityList);
                        listview.setAdapter(messageChatAdapter);
                    } else {
                        messageChatAdapter.setData(messagesEntityList);
                    }

                }
                Toast.makeText(MessagesActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private EditText getSendEidtText() {
        return (EditText) findViewById(R.id.sendEidtText);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendButton:
                //TODO implement
                String content = getSendEidtText().getText().toString();
                if (!TextUtils.isEmpty(content)) {
                    messageCreate(content);
                } else {
                    Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
