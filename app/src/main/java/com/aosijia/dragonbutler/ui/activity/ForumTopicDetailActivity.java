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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.imagegroup.NavigatorImage;
import com.aosijia.dragonbutler.imagegroup.model.SquareImage;
import com.aosijia.dragonbutler.model.BaseResp;
import com.aosijia.dragonbutler.model.Forum;
import com.aosijia.dragonbutler.model.ForumCommentCreateResp;
import com.aosijia.dragonbutler.model.ForumComments;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.adapter.CommentAdapter;
import com.aosijia.dragonbutler.ui.adapter.GalleryAdapter;
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
import com.aosijia.dragonbutler.utils.TimeUtils;
import com.aosijia.dragonbutler.utils.Uiutils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpClientManager;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ForumTopicDetailActivity extends BaseActivity implements GalleryAdapter.OnItemClickLitener, View.OnClickListener {


    private int REQUEST_CODE = 100;
    private TextView tv_repliesCount;
    private TextView tv_date;
    private RecyclerView rv_gallery;
    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_nickName;
    private CircleImageView civ_avatar;
    private LoadingAndRetryManager mLoadingAndRetryManager;
    private PullToRefreshScrollView prsv_refresh;
    private LinearLayout ll_comment;
    private Forum.DataEntity forum;
    private NoScrollListView lvComment;
    private EditText etComment;
    private TextView tvSend;
    private CommentAdapter mCommentAdapter;
    private List<ForumComments.DataEntity.ForumCommentsEntity> mComments;
    private String forum_topic_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_forum_detail);
        forum_topic_id = getIntent().getExtras().getString("forum_topic_id");

        initView();
        initCommentView();
        loadData(TYPE_LOAD_FIRST);
    }

    private void initView() {
        setTitle("帖子详情", null, R.drawable.btn_back, null, NO_RES_ID);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_nickName = (TextView) findViewById(R.id.tv_nickName);
        ll_comment = (LinearLayout) findViewById(R.id.ll_comment);
        rv_gallery = (RecyclerView) findViewById(R.id.rv_gallery);
        civ_avatar = (CircleImageView) findViewById(R.id.civ_avatar);
        tv_repliesCount = (TextView) findViewById(R.id.tv_repliesCount);
        prsv_refresh = (PullToRefreshScrollView) findViewById(R.id.prsv_refresh);
        rv_gallery.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mLoadingAndRetryManager = LoadingAndRetryManager.generate(prsv_refresh, loadingAndRetryListener);
        civ_avatar.setOnClickListener(this);
        setBtnLeftOnClickListener(this);
        setBtnRightTextOnClickListener(this);
        prsv_refresh.setHideSubText(true);
        prsv_refresh.setOnRefreshListener(refresh);
        prsv_refresh.setOnSlideListener(slideListern);
        prsv_refresh.setMode(PullToRefreshBase.Mode.BOTH);
    }

    boolean isAuthor;

    private void initData() {
        String user_id = forum.getAuthor().getUser_id();
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        int resId;
        if (user_id.equals(loginResp.getData().getUser_id())) {
            resId = R.drawable.common_navigation_more;
            isAuthor = true;
        } else {
            if (forum.isIs_favorite()) {
                resId = R.drawable.common_collection_selected;
            } else {
                resId = R.drawable.common_collection_normal;
            }
        }
        setRightImage(resId);
        tv_title.setText(forum.getTitle());
        tv_content.setText(forum.getContent());
        tv_nickName.setText(forum.getAuthor().getNickname());
        tv_date.setText(TimeUtils.getDateToString(forum.getCreated_at()));
        tv_repliesCount.setText(String.valueOf(forum.getComments_count()));
        ImageLoader.getInstance().displayImage(forum.getAuthor().getAvatar_url(), civ_avatar, DisplayOpitionFactory.sAvatarDisplayOption);

        if (forum.getExtra().getPic_urls().size() != 0) {
            rv_gallery.setVisibility(View.VISIBLE);
            GalleryAdapter adapter = new GalleryAdapter(ForumTopicDetailActivity.this, forum.getExtra().getPic_urls());
            rv_gallery.setAdapter(adapter);
            adapter.setOnItemClickLitener(ForumTopicDetailActivity.this);
        } else {
            rv_gallery.setVisibility(View.GONE);
        }
        ll_comment.setVisibility(View.VISIBLE);
        mLoadingAndRetryManager.showContent();
    }


    private void loadData(final int type) {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        String access_token = loginResp.getData().getAccess_token();
        Map<String, String> parameter = RequestParameters.forumTopic(forum_topic_id, access_token);
        new OkHttpRequest.Builder().url(URLManager.FORUM_TOPIC).params(parameter).tag(this).get(new ResultCallback<Forum>() {
            @Override
            public void onError(Request request, Exception e) {
              if(type == TYPE_LOAD_FIRST){
                  mLoadingAndRetryManager.showRetry();
              }
            }

            @Override
            public void onAfter() {
              if(type == TYPE_LOAD_REFRESH){
                  prsv_refresh.onRefreshComplete();
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
            public void onResponse(Forum response) {
                if (response.isSuccess(ForumTopicDetailActivity.this)) {
                    forum = response.getData();
                    initData();
                    getComment(null);
                } else {
                    if(type == TYPE_LOAD_FIRST){
                        mLoadingAndRetryManager.showRetry();
                    }
                }
            }
        });
    }

    private void getComment(final String last_id) {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        String access_token = loginResp.getData().getAccess_token();
        Map<String, String> parameters = RequestParameters.forumComments(access_token, forum_topic_id, null, last_id);
        new OkHttpRequest.Builder().url(URLManager.FORUM_COMMENTS).params(parameters).tag(this).get(new ResultCallback<ForumComments>() {
            @Override
            public void onError(Request request, Exception e) {
                showRequestError();
            }

            @Override
            public void onAfter() {
                prsv_refresh.onRefreshComplete();
            }

            @Override
            public void onResponse(ForumComments response) {
                if (response.isSuccess(ForumTopicDetailActivity.this)) {
                    if (last_id == null) {
                        mComments.clear();
                    }
                    mComments.addAll(response.getData().getForum_comments());
                    mCommentAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ForumTopicDetailActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void initCommentView() {
        prsv_refresh.setOnSlideListener(new PullToRefreshBase.OnSlideLisnter() {
            @Override
            public void onSlide() {
                resetEtComment(prsv_refresh);
            }
        });
        lvComment = (NoScrollListView) findViewById(R.id.sv_noScroll);
        mComments = new ArrayList<>();
        mCommentAdapter = new CommentAdapter(this, mComments);
        mCommentAdapter.setOnCommentListener(new CommentAdapter.OnCommentListener() {
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
                comment_create(forum_topic_id, comment_id, getTextNoSpace(etComment));
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
        Map<String, String> parameter = RequestParameters.forumCommentCreate(loginResp.getData().getAccess_token(),
                forum_topic_id, content, comment_id);
        new OkHttpRequest.Builder().url(URLManager.FORUM_COMMENT_CREATE).params(parameter).tag(this).post(new ResultCallback<ForumCommentCreateResp>() {
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
            public void onResponse(ForumCommentCreateResp response) {
                if (response.isSuccess(getApplicationContext())) {
                    if (!TextUtils.isEmpty(getTextNoSpace(tv_repliesCount)) && TextUtils.isDigitsOnly(getTextNoSpace(tv_repliesCount))) {
                        int count = Integer.parseInt(getTextNoSpace(tv_repliesCount));
                        tv_repliesCount.setText((count + 1) + "");
                    } else {
                        tv_repliesCount.setText(1 + "");
                    }
                    ForumComments.DataEntity.ForumCommentsEntity comment = response.getData();
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

    TitlePopup titlePopup;

    private void initPopupView(View v) {
        titlePopup = new TitlePopup(ForumTopicDetailActivity.this, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titlePopup.addAction(new ActionItem(ForumTopicDetailActivity.this, "编辑", R.drawable.ic_edit));
        titlePopup.addAction(new ActionItem(ForumTopicDetailActivity.this, "删除", R.drawable.ic_cancel));
        titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
            @Override
            public void onItemClick(ActionItem item, int position) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(ForumTopicDetailActivity.this, ForumTopicCreateActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("forum", forum);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, REQUEST_CODE);
                        break;
                    case 1:
                        new CommonDialog.Builder(ForumTopicDetailActivity.this).setTitle(R.string.dialog_text_title)
                                .setMessage("确定要删除吗?").setNegativeButtonText(R.string.dialog_text_cancel, null)
                                .setPositiveButtonText(R.string.dialog_text_confirm, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        forumTopicDelete();
                                    }
                                }).show();
                        break;
                }
            }
        });
        titlePopup.show(v);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.title_rightimageview:
                if (isAuthor) {
                    initPopupView(v);
                } else {
                    if (forum.isIs_favorite()) {
                        favoriteCancel();
                    } else {
                        favorite();
                    }
                }
                break;
            case R.id.civ_avatar:
                LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
                String cacheUserId = loginResp.getData().getUser_id();
                String avatar_url = forum.getAuthor().getAvatar_url();
                String nickname = forum.getAuthor().getNickname();
                String user_id = forum.getAuthor().getUser_id();
                Uiutils.jumpUserInfoPage(this, cacheUserId, avatar_url, nickname, user_id);
                break;
        }
    }

    private void forumTopicDelete() {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        Map<String, String> parameter = RequestParameters.forumTopicDelete(loginResp.getData().getAccess_token(), forum_topic_id);
        new OkHttpRequest.Builder().url(URLManager.FORUM_TOPIC_DELETE).params(parameter).tag(this).post(new ResultCallback<BaseResp>() {
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
                showProgressDialow("正在删除...");
            }

            @Override
            public void onResponse(BaseResp response) {
                if (response.isSuccess(ForumTopicDetailActivity.this)) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(ForumTopicDetailActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void favorite() {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        Map<String, String> parameters = RequestParameters.favoriteForumAndCommodity(loginResp.getData().getAccess_token(),
                Constants.FORUM_TYPE_TOPIC, forum_topic_id);
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
                if (response.isSuccess(ForumTopicDetailActivity.this)) {
                    setRightImage(R.drawable.common_collection_selected);
                    forum.setIs_favorite(true);
                } else {
                    Toast.makeText(ForumTopicDetailActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void favoriteCancel() {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        String access_token = loginResp.getData().getAccess_token();
        String forum_topic_id = forum.getForum_topic_id();
        String type = Constants.FAVORITE_TYPE_FORUM;
        Map<String, String> parameters = RequestParameters.favoriteCancelForumAndCommodity(access_token, type, forum_topic_id);
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
                if (response.isSuccess(ForumTopicDetailActivity.this)) {
                    setRightImage(R.drawable.common_collection_normal);
                    forum.setIs_favorite(false);
                }
            }
        });
    }


    //recycleView图片的点击事件
    @Override
    public void onItemClick(View view, int position) {
        ArrayList<SquareImage> results = new ArrayList<>();
        for (int i = 0; i < forum.getExtra().getPic_urls().size(); i++) {
            results.add(new SquareImage("", forum.getExtra().getPic_urls().get(i), "", "", SquareImage.PhotoType.NETWORK));
        }
        NavigatorImage.startImageSwitcherActivity(ForumTopicDetailActivity.this, results, position, false, R.drawable.default_pic_item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    private OnLoadingAndRetryListener loadingAndRetryListener = new OnLoadingAndRetryListener() {
        @Override
        public void setRetryEvent(View retryView) {
            ForumTopicDetailActivity.this.setRetryEvent(retryView);
        }

    };


    private PullToRefreshBase.OnRefreshListener2<ScrollView> refresh = new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
            loadData(TYPE_LOAD_REFRESH);
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
            if (mComments.size() != 0) {
                String last_id = mCommentAdapter.getItem(mCommentAdapter.getCount() - 1).getForum_comment_id();
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
    protected void onDestroy() {
        super.onDestroy();
        OkHttpClientManager.getInstance().cancelTag(this);
    }

}
