package com.aosijia.dragonbutler.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.imagegroup.NavigatorImage;
import com.aosijia.dragonbutler.imagegroup.model.SquareImage;
import com.aosijia.dragonbutler.model.BaseResp;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.model.SecondHandCommentBase;
import com.aosijia.dragonbutler.model.SecondhandComments;
import com.aosijia.dragonbutler.model.SecondhandItem;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.adapter.GalleryAdapter;
import com.aosijia.dragonbutler.ui.adapter.SecondCommentAdapter;
import com.aosijia.dragonbutler.ui.widget.CircleImageView;
import com.aosijia.dragonbutler.ui.widget.CommonDialog;
import com.aosijia.dragonbutler.ui.widget.list.NoScrollListView;
import com.aosijia.dragonbutler.ui.widget.loader.LoadingAndRetryManager;
import com.aosijia.dragonbutler.ui.widget.loader.OnLoadingAndRetryListener;
import com.aosijia.dragonbutler.ui.widget.popupwindow.ActionItem;
import com.aosijia.dragonbutler.ui.widget.popupwindow.TitlePopup;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshBase;
import com.aosijia.dragonbutler.ui.widget.pulllistview.PullToRefreshScrollView;
import com.aosijia.dragonbutler.utils.Constants;
import com.aosijia.dragonbutler.utils.DisplayOpitionFactory;
import com.aosijia.dragonbutler.utils.SystemUtils;
import com.aosijia.dragonbutler.utils.TimeUtils;
import com.aosijia.dragonbutler.utils.Uiutils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SecondhandDetailActivity extends BaseActivity implements GalleryAdapter.OnItemClickLitener, View.OnClickListener {

    private RecyclerView rv_gallery;
    private TextView tv_date;
    private TextView tv_title;
    private TextView tv_price;
    private ImageView iv_phone;
    private TextView tv_content;
    private TextView tv_nickName;
    private LinearLayout ll_comment;
    private CircleImageView civ_avatar;
    private PullToRefreshScrollView prsv_refresh;
    private LoadingAndRetryManager mLoadingAndRetryManager;
    private String secondhand_item_id;//商品ID
    private SecondhandItem secondhandItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        secondhand_item_id = getIntent().getExtras().getString("secondhand_item_id");
        setContentView(R.layout.activity_secondhand_detail);
        initView();
        initCommentView();
        loadData(TYPE_LOAD_FIRST);
    }

    private void initView() {
        setTitle(getResources().getString(R.string.detail), null, R.drawable.btn_back, null, BaseActivity.NO_RES_ID);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_price = (TextView) findViewById(R.id.tv_price);
        iv_phone = (ImageView) findViewById(R.id.iv_phone);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_nickName = (TextView) findViewById(R.id.tv_nickName);
        ll_comment = (LinearLayout) findViewById(R.id.ll_comment);
        rv_gallery = (RecyclerView) findViewById(R.id.rv_gallery);
        civ_avatar = (CircleImageView) findViewById(R.id.civ_avatar);
        prsv_refresh = (PullToRefreshScrollView) findViewById(R.id.pullToRefreshScrollView);
        mLoadingAndRetryManager = LoadingAndRetryManager.generate(prsv_refresh, loadingAndRetryListener);
        setBtnLeftOnClickListener(this);
        setBtnRightTextOnClickListener(this);
        prsv_refresh.setHideSubText(true);
        prsv_refresh.setOnRefreshListener(refresh);
        prsv_refresh.setOnSlideListener(slideListern);
        prsv_refresh.setMode(PullToRefreshBase.Mode.BOTH);
//        findViewById(R.id.infoLayout).setOnClickListener(this);
        civ_avatar.setOnClickListener(this);

    }

    private void loadData(final int type) {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        String access_token = loginResp.getData().getAccess_token();
        Map<String, String> parameters = RequestParameters.secondhandItem(access_token, secondhand_item_id);
        new OkHttpRequest.Builder().url(URLManager.SECONDHAND_ITEM).params(parameters).tag(this).get(new ResultCallback<SecondhandItem>() {
            @Override
            public void onError(Request request, Exception e) {
                if (type == TYPE_LOAD_FIRST) {
                    mLoadingAndRetryManager.showRetry();
                }
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                if (type == TYPE_LOAD_FIRST) {
                    mLoadingAndRetryManager.showLoading();
                }
            }

            @Override
            public void onAfter() {
                if (type == TYPE_LOAD_REFRESH) {
                    prsv_refresh.onRefreshComplete();
                }
            }

            @Override
            public void onResponse(SecondhandItem response) {
                if (response.isSuccess(SecondhandDetailActivity.this)) {
                    secondhandItem = response;
                    initData();
                    getComment(null);
                } else {
                    if (type == TYPE_LOAD_FIRST) {
                        mLoadingAndRetryManager.showRetry();
                    }
                }
            }
        });
    }

    private void getComment(final String last_id) {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        String access_token = loginResp.getData().getAccess_token();
        Map<String, String> parameter = RequestParameters.secondhandComments(access_token, secondhand_item_id, null, last_id);
        new OkHttpRequest.Builder().url(URLManager.SECONDHAND_COMMENTS).params(parameter).tag(this).get(new ResultCallback<SecondhandComments>() {
            @Override
            public void onError(Request request, Exception e) {
                showRequestError();
            }

            @Override
            public void onAfter() {
                prsv_refresh.onRefreshComplete();
            }

            @Override
            public void onResponse(SecondhandComments response) {
                if (response.isSuccess(SecondhandDetailActivity.this)) {
                    if (last_id == null) {
                        mComments.clear();
                    }
                    mComments.addAll(response.getData().getSecondhand_comments());
                    mCommentAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(SecondhandDetailActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    boolean isAuther;

    private void initData() {
        if (secondhandItem.getData().getType().equals(Constants.SECONDHAND_TYPE_BUY)) {
            setTitle("转让详情", null, R.drawable.btn_back, null, NO_RES_ID);
        } else if (secondhandItem.getData().getType().equals(Constants.SECONDHAND_TYPE_TRANSFER)) {
            setTitle("求购详情", null, R.drawable.btn_back, null, NO_RES_ID);
        }
        String user_id = secondhandItem.getData().getAuthor().getUser_id();
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        int resId;
        if (user_id.equals(loginResp.getData().getUser_id())) {
            isAuther = true;
            if (secondhandItem.getData().getStatus().equals(Constants.SECONDHAND_STATUS_NORMAL)) {
                resId = R.drawable.common_navigation_more;
            } else {
                resId = NO_RES_ID;
            }
        } else {
            if (secondhandItem.getData().isIs_favorite()) {
                resId = R.drawable.common_collection_selected;
            } else {
                resId = R.drawable.common_collection_normal;
            }
        }
        setRightImage(resId);

        tv_title.setText(secondhandItem.getData().getTitle());
        tv_content.setText(secondhandItem.getData().getContent());
        tv_nickName.setText(secondhandItem.getData().getAuthor().getNickname());
        tv_date.setText(TimeUtils.getDateToString(secondhandItem.getData().getCreated_at()));
        tv_repliesCount.setText(secondhandItem.getData().getComments_count());
        if (secondhandItem.getData().getPrice().equals("-1")) {
            tv_price.setText("￥ 面议");
        } else {
            tv_price.setText("￥" + secondhandItem.getData().getPrice());
        }
        ImageLoader.getInstance().displayImage(secondhandItem.getData().getAuthor().getAvatar_url(), civ_avatar, DisplayOpitionFactory.sAvatarDisplayOption);
        if (secondhandItem.getData().getMobile() != null && !TextUtils.isEmpty(secondhandItem.getData().getMobile())) {
            iv_phone.setVisibility(View.VISIBLE);
            iv_phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SystemUtils.toCallPhoneActivity(SecondhandDetailActivity.this, secondhandItem.getData().getMobile());
                }
            });
        }
        if (secondhandItem.getData().getPic_urls().size() != 0) {
            rv_gallery.setVisibility(View.VISIBLE);
            rv_gallery.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            //recyclerView适配器
            GalleryAdapter adapter = new GalleryAdapter(SecondhandDetailActivity.this, secondhandItem.getData().getPic_urls());
            rv_gallery.setAdapter(adapter);
            adapter.setOnItemClickLitener(SecondhandDetailActivity.this);
        }
        ll_comment.setVisibility(View.VISIBLE);
        mLoadingAndRetryManager.showContent();
    }


    private TextView tv_repliesCount;
    private NoScrollListView lvComment;
    private EditText etComment;
    private TextView tvSend;
    private SecondCommentAdapter mCommentAdapter;
    private List<SecondhandComments.DataEntity.SecondhandCommentsEntity> mComments;

    private void initCommentView() {
        tv_repliesCount = (TextView) findViewById(R.id.tv_repliesCount);
        prsv_refresh.setOnSlideListener(new PullToRefreshBase.OnSlideLisnter() {
            @Override
            public void onSlide() {
                resetEtComment(prsv_refresh);
            }
        });
        mComments = new ArrayList<>();
        lvComment = (NoScrollListView) findViewById(R.id.noScrollListView);
        mCommentAdapter = new SecondCommentAdapter(this, mComments);
        mCommentAdapter.setOnCommentListener(new SecondCommentAdapter.OnCommentListener() {
            @Override
            public void onComment(String comment_id) {
                etComment.setHint("回复评论");
                etComment.setTag(comment_id);
                Uiutils.showInputWindow(getApplicationContext(), etComment);
            }
        });
        lvComment.setAdapter(mCommentAdapter);
        etComment = (EditText) findViewById(R.id.et_comment);
        tvSend = (TextView) findViewById(R.id.tv_send);
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment_id = null;
                if (etComment.getTag() != null) {
                    comment_id = (String) etComment.getTag();
                }
                comment_create(secondhand_item_id, comment_id, getTextNoSpace(etComment));
            }
        });
        etComment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tvSend.setVisibility(View.VISIBLE);
                } else {
                    tvSend.setVisibility(View.GONE);
                }
            }
        });
    }


    private void comment_create(String forum_topic_id, final String comment_id, String content) {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        Map<String, String> parameter = RequestParameters.secondhandCommentCreate(loginResp.getData().getAccess_token(),
                forum_topic_id, content, comment_id);
        new OkHttpRequest.Builder().url(URLManager.SECONDHAND_COMMENT_CREATE).params(parameter).tag(this)
                .post(new ResultCallback<SecondHandCommentBase>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        showRequestError();
                        dismissProgressDialog();
                    }

                    @Override
                    public void onBefore(Request request) {
                        super.onBefore(request);
                        showProgressDialow("加载中...");
                    }

                    @Override
                    public void onAfter() {
                        super.onAfter();
                        dismissProgressDialog();
                    }

                    @Override
                    public void onResponse(SecondHandCommentBase response) {
                        if (response.isSuccess(getApplicationContext())) {
                            if (!TextUtils.isEmpty(getTextNoSpace(tv_repliesCount)) && TextUtils.isDigitsOnly(getTextNoSpace(tv_repliesCount))) {
                                int count = Integer.parseInt(getTextNoSpace(tv_repliesCount));
                                tv_repliesCount.setText((count + 1) + "");
                            } else {
                                tv_repliesCount.setText(1 + "");
                            }

                            SecondhandComments.DataEntity.SecondhandCommentsEntity comment = response.getData();
                            mCommentAdapter.add(comment);
                            mCommentAdapter.notifyDataSetChanged();
                            resetEtComment(prsv_refresh);
                        } else {
                            Toast.makeText(getApplicationContext(), response.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void resetEtComment(View focus) {
        etComment.setHint("评论");
        etComment.setText("");
        etComment.setTag(null);
        etComment.clearFocus();
        focus.requestFocus();
        Uiutils.hideInputWindow(getApplicationContext(), etComment);
    }


    //失败重试
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
    public void onItemClick(View view, int position) {
        ArrayList<SquareImage> results = new ArrayList<>();
        for (int i = 0; i < secondhandItem.getData().getPic_urls().size(); i++) {
            results.add(new SquareImage("", secondhandItem.getData().getPic_urls().get(i), "", "", SquareImage.PhotoType.NETWORK));
        }
        NavigatorImage.startImageSwitcherActivity(SecondhandDetailActivity.this, results, position, false, R.drawable.default_pic_item);
    }


    TitlePopup titlePopup;

    //初始化泡泡窗口
    private void initPopupView(View v) {
        titlePopup = new TitlePopup(SecondhandDetailActivity.this, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titlePopup.addAction(new ActionItem(SecondhandDetailActivity.this, "编辑交易", R.drawable.ic_edit));
        titlePopup.addAction(new ActionItem(SecondhandDetailActivity.this, "完成交易", R.drawable.ic_complete));
        titlePopup.addAction(new ActionItem(SecondhandDetailActivity.this, "取消交易", R.drawable.ic_cancel));
        titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
            @Override
            public void onItemClick(ActionItem item, int position) {
                switch (position) {
                    case 0: //编辑交易
                        Intent intent = new Intent(SecondhandDetailActivity.this, SecondhandCreateActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("secondhandItem", secondhandItem);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 1);
                        break;
                    case 1://完成交易
                        new CommonDialog.Builder(SecondhandDetailActivity.this).setTitle(R.string.dialog_text_title)
                                .setMessage("确定交易已完成？").setNegativeButtonText(R.string.dialog_text_cancel, null)
                                .setPositiveButtonText(R.string.dialog_text_confirm, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        closeSecondhand();
                                    }
                                }).show();
                        break;
                    case 2://取消交易
                        new CommonDialog.Builder(SecondhandDetailActivity.this).setTitle(R.string.dialog_text_title)
                                .setMessage("确定删除？").setNegativeButtonText(R.string.dialog_text_cancel, null)
                                .setPositiveButtonText(R.string.dialog_text_confirm, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        cancleSecondhand();
                                    }
                                }).show();
                        break;
                }
            }
        });
        titlePopup.show(v);
    }


    private PullToRefreshBase.OnRefreshListener2<ScrollView> refresh = new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
            loadData(TYPE_LOAD_REFRESH);
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
            if (mComments.size() != 0) {
                String last_id = mCommentAdapter.getItem(mCommentAdapter.getCount() - 1).getSecondhand_comment_id();
                getComment(last_id);
            } else {
                getComment(null);
            }
        }
    };

    private PullToRefreshBase.OnSlideLisnter slideListern = new PullToRefreshBase.OnSlideLisnter() {
        @Override
        public void onSlide() {
            resetEtComment(prsv_refresh);
        }
    };


    //关闭二手交易
    private void closeSecondhand() {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        String access_token = loginResp.getData().getAccess_token();
        Map<String, String> parameters = RequestParameters.secondhandItemCancelOrClose(access_token, secondhand_item_id);
        new OkHttpRequest.Builder().url(URLManager.SECONDHAND_ITEM_CLOSE).params(parameters).tag(this).post(new ResultCallback<BaseResp>() {
            @Override
            public void onError(Request request, Exception e) {
                showRequestError();
            }

            @Override
            public void onAfter() {
                super.onAfter();
                dismissProgressDialog();
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                showProgressDialow("正在关闭...");
            }

            @Override
            public void onResponse(BaseResp response) {
                if (response.isSuccess(SecondhandDetailActivity.this)) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(SecondhandDetailActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    //取消二手交易
    private void cancleSecondhand() {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        String access_token = loginResp.getData().getAccess_token();
        Map<String, String> parameters = RequestParameters.secondhandItemCancelOrClose(access_token, secondhand_item_id);
        new OkHttpRequest.Builder().url(URLManager.SECONDHAND_ITEM_CANCEL).params(parameters).tag(this).post(new ResultCallback<BaseResp>() {
            @Override
            public void onError(Request request, Exception e) {
                showRequestError();
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                showProgressDialow("正在取消...");
            }

            @Override
            public void onAfter() {
                super.onAfter();
                dismissProgressDialog();
            }

            @Override
            public void onResponse(BaseResp response) {
                if (response.isSuccess(SecondhandDetailActivity.this)) {
                    setResult(RESULT_OK);
                    finish();
                }
                Toast.makeText(SecondhandDetailActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    //收藏
    private void favoriteSecondhand() {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        String access_token = loginResp.getData().getAccess_token();
        Map<String, String> parameters = RequestParameters.favoriteForumAndCommodity(access_token,
                Constants.FORUM_TYPE_EVENT, secondhand_item_id);
        new OkHttpRequest.Builder().url(URLManager.FAVORITE_CREATE).params(parameters).tag(this).post(new ResultCallback<BaseResp>() {
            @Override
            public void onError(Request request, Exception e) {
                showRequestError();
                dismissProgressDialog();
            }

            @Override
            public void onAfter() {
                super.onAfter();
                dismissProgressDialog();
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                showProgressDialow("正在加载...");
            }

            @Override
            public void onResponse(BaseResp response) {
                if (response.isSuccess(SecondhandDetailActivity.this)) {
                    setRightImage(R.drawable.common_collection_selected);
                    secondhandItem.getData().setIs_favorite(true);
                } else {
                    Toast.makeText(SecondhandDetailActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void favoriteCancel() {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        String access_token = loginResp.getData().getAccess_token();
        String secondhand_item_id = secondhandItem.getData().getSecondhand_item_id();
        String type = Constants.FAVORITE_TYPE_SECOND_HAND;
        Map<String, String> parameters = RequestParameters.favoriteCancelForumAndCommodity(access_token, type, secondhand_item_id);
        new OkHttpRequest.Builder().url(URLManager.FAVORITE_CANCEL).params(parameters).tag(this).post(new ResultCallback<BaseResp>() {
            @Override
            public void onError(Request request, Exception e) {
                showRequestError();
            }

            @Override
            public void onBefore(Request request) {
                showProgressDialow("正在取消...");
            }

            @Override
            public void onAfter() {
                dismissProgressDialog();
            }

            @Override
            public void onResponse(BaseResp response) {
                if (response.isSuccess(SecondhandDetailActivity.this)) {
                    setRightImage(R.drawable.common_collection_normal);
                    secondhandItem.getData().setIs_favorite(false);
                }
            }
        });
    }

    private OnLoadingAndRetryListener loadingAndRetryListener = new OnLoadingAndRetryListener() {
        @Override
        public void setRetryEvent(View retryView) {
            SecondhandDetailActivity.this.setRetryEvent(retryView);
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.title_rightimageview:
                if (isAuther) {
                    initPopupView(view);
                } else {
                    if (secondhandItem.getData().isIs_favorite()) {
                        favoriteCancel();
                    } else {
                        favoriteSecondhand();
                    }
                }
                break;
            case R.id.civ_avatar:
                LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
                String cacheUserId = loginResp.getData().getUser_id();
                String avatar = secondhandItem.getData().getAuthor().getAvatar_url();
                String nickName = secondhandItem.getData().getAuthor().getNickname();
                String userId = secondhandItem.getData().getAuthor().getUser_id();
                Uiutils.jumpUserInfoPage(this, cacheUserId, avatar, nickName, userId);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
