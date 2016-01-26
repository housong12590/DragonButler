package com.aosijia.dragonbutler.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.model.MessageBox;
import com.aosijia.dragonbutler.model.MessageBox.DataEntity.MessageBoxEntity;
import com.aosijia.dragonbutler.model.SecondHandItems;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.adapter.MessageAdapter;
import com.aosijia.dragonbutler.ui.adapter.TransfersOrBuyAdapter;
import com.aosijia.dragonbutler.ui.widget.loader.LoadingAndRetryManager;
import com.aosijia.dragonbutler.ui.widget.loader.OnLoadingAndRetryListener;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshBase;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshListView;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 我的私信列表
 * Created by wanglj on 16/1/7.
 */
public class MessageBoxActivity extends BaseActivity{
    private PullToRefreshListView listview;
    private LoadingAndRetryManager mLoadingAndRetryManager;
    private MessageAdapter messageAdapter;

    private List<MessageBoxEntity> messageBoxEntityList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_message_box);
        setTitle("我的私信", null, R.drawable.btn_back, null, NO_RES_ID);
        setBtnLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Intent.FLAG_ACTIVITY_NEW_TASK == getIntent().getFlags()){
                    startActivity(new Intent(MessageBoxActivity.this,MainTabActivity.class));
                }else{
                    finish();
                }

            }
        });
        listview = (PullToRefreshListView) findViewById(R.id.listView);


        mLoadingAndRetryManager = LoadingAndRetryManager.generate(listview, new OnLoadingAndRetryListener() {
            @Override
            public void setRetryEvent(View retryView) {
                MessageBoxActivity.this.setRetryEvent(retryView);
            }
        });



        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(TYPE_LOAD_REFRESH);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadData(TYPE_LOAD_MORE);

            }
        });


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MessageBoxActivity.this,MessagesActivity.class);
                Bundle bundle = new Bundle();
                MessageBoxEntity messageBoxEntity = (MessageBox.DataEntity.MessageBoxEntity)parent.getItemAtPosition(position);
                intent.putExtra("userId",messageBoxEntity.getContact().getUser_id());
                intent.putExtra("avatar",messageBoxEntity.getContact().getAvatar_url());
                intent.putExtra("nickName",messageBoxEntity.getContact().getNickname());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(TYPE_LOAD_FIRST);
    }

    /**
     * 加载数据
     * @param type
     */
    private void loadData(final int type) {

        String last_id;
        if (type == TYPE_LOAD_MORE) {
            last_id = messageAdapter.getLastId();
        } else {
            last_id = null;
        }

        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        Map<String, String> parameter = RequestParameters.messageBox(loginResp.getData().getAccess_token(), PAGE_SIZE + "", last_id);
        new OkHttpRequest.Builder().url(URLManager.MESSAGE_BOX).params(parameter).tag(this).get(new ResultCallback<MessageBox>() {
            @Override
            public void onError(Request request, Exception e) {
                if (type == TYPE_LOAD_FIRST) {
                    mLoadingAndRetryManager.showRetry();
                }
            }

            @Override
            public void onAfter() {
                if (type == TYPE_LOAD_FIRST) {
//                    mLoadingAndRetryManager.showContent();
                } else if (type == TYPE_LOAD_REFRESH) {
                    listview.onRefreshComplete();
                }
                if (messageAdapter != null
                        && messageAdapter.getCount() >= BaseActivity.PAGE_SIZE
                        && messageAdapter.getCount() % BaseActivity.PAGE_SIZE == 0) {
                    listview.onLoadMoreComplete(true);
                } else {
                    listview.onLoadMoreComplete(false);
                }

            }

            @Override
            public void onBefore(Request request) {
                if (type == TYPE_LOAD_FIRST) {
                    mLoadingAndRetryManager.showLoading();
                }
            }

            @Override
            public void onResponse(MessageBox messageBox) {
                if (messageBox.isSuccess(MessageBoxActivity.this)) {
                    if (type == TYPE_LOAD_FIRST) {
                        if (messageBox.getData().getMessage_box().size() == 0) {
                            mLoadingAndRetryManager.showEmpty(LoadingAndRetryManager.TYPE_EMPTY_NOMESSAGE);
                        } else {
                            mLoadingAndRetryManager.showContent();
                        }
                        messageAdapter = new MessageAdapter(MessageBoxActivity.this);
                        listview.setAdapter(messageAdapter);
                    }
                    messageAdapter.setData(messageBox.getData().getMessage_box(), type);
                } else {
                    Toast.makeText(MessageBoxActivity.this, messageBox.getMsg(), Toast.LENGTH_SHORT).show();
                    if (type == 1) {
                        mLoadingAndRetryManager.showRetry();
                    }
                }
            }
        });

    }

    public void setRetryEvent(View retryView) {
        View view = retryView.findViewById(R.id.id_btn_retry);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData(TYPE_LOAD_FIRST);
            }
        });
    }

    @Override
    public void onBackPressed() {

        //是否为推送消息判断
        if(Intent.FLAG_ACTIVITY_NEW_TASK == getIntent().getFlags()){
            startActivity(new Intent(this,MainTabActivity.class));
        }else{
            super.onBackPressed();
        }

//        Toast.makeText(this, "getIntent().getFlags():" + getIntent().getFlags(), Toast.LENGTH_SHORT).show();
    }
}
