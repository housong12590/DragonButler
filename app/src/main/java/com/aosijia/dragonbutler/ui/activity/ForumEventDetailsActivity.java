package com.aosijia.dragonbutler.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.aosijia.dragonbutler.model.Event;
import com.aosijia.dragonbutler.model.ForumCommentCreateResp;
import com.aosijia.dragonbutler.model.ForumComments;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.adapter.AvatarAdapter;
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
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 活动详情
 *
 * @author hs
 */
public class ForumEventDetailsActivity extends BaseActivity implements GalleryAdapter.OnItemClickLitener, View.OnClickListener {
    private Event event;
    private View divider;
    private Button bt_join;
    private TextView tv_date;
    private TextView tv_title;
    private ImageView tv_status;
    private TextView tv_content;
    private TextView tv_endDate;
    private RecyclerView rv_pic;
    private TextView tv_nickName;
    private String forum_topic_id;
    private TextView tv_startDate;
    private LinearLayout ll_comment;
    private CircleImageView civ_avatar;
    private TextView tv_participantsCount;
    private RecyclerView rv_participantsAvatar;
    private LoadingAndRetryManager mLoadingAndRetryManager;

    private TextView tvSend;
    private EditText et_comment;
    private TextView tv_repliesCount;
    private NoScrollListView lv_comment;
    private CommentAdapter mCommentAdapter;
    private PullToRefreshScrollView prsv_refresh;
    private List<ForumComments.DataEntity.ForumCommentsEntity> mComments = new ArrayList<>();
    private AvatarAdapter avatarAdapter;
    private List<String> avatar_url = new ArrayList<>();
    private String cacheUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setImmersionStatus();
        setContentView(R.layout.activity_event_detail);
        forum_topic_id = getIntent().getExtras().getString("forum_topic_id");
        initView();
        initCommentView();
        loadData(TYPE_LOAD_FIRST);
    }

    private void initView() {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        cacheUserId = loginResp.getData().getUser_id();
        setTitle("活动详情", null, R.drawable.btn_back, null, NO_RES_ID);
        divider = findViewById(R.id.divider);
        tvSend = (TextView) findViewById(R.id.tv_send);
        bt_join = (Button) findViewById(R.id.bt_join);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_status = (ImageView) findViewById(R.id.tv_status);
        et_comment = (EditText) findViewById(R.id.et_comment);
        tv_endDate = (TextView) findViewById(R.id.tv_endDate);
        rv_pic = (RecyclerView) findViewById(R.id.rv_gallery);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_nickName = (TextView) findViewById(R.id.tv_nickName);
        ll_comment = (LinearLayout) findViewById(R.id.ll_comment);
        tv_startDate = (TextView) findViewById(R.id.tv_startDate);
        civ_avatar = (CircleImageView) findViewById(R.id.civ_avatar);
        lv_comment = (NoScrollListView) findViewById(R.id.sv_noScroll);
        tv_repliesCount = (TextView) findViewById(R.id.tv_repliesCount);
        prsv_refresh = (PullToRefreshScrollView) findViewById(R.id.prsv_refresh);
        prsv_refresh = (PullToRefreshScrollView) findViewById(R.id.prsv_refresh);
        tv_participantsCount = (TextView) findViewById(R.id.tv_participantsCount);
        rv_participantsAvatar = (RecyclerView) findViewById(R.id.ry_participantsAvatar);
        mLoadingAndRetryManager = LoadingAndRetryManager.generate(prsv_refresh, loadingAndRetryListener);
        rv_pic.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv_participantsAvatar.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        prsv_refresh.setOnRefreshListener(refresh);
        prsv_refresh.setMode(PullToRefreshBase.Mode.BOTH);
        prsv_refresh.setOnSlideListener(slideListern);
        prsv_refresh.setHideSubText(true);
        bt_join.setOnClickListener(this);
        setBtnLeftOnClickListener(this);
        setBtnRightTextOnClickListener(this);
    }


    private void loadData(final int type) {
        final LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        String access_token = loginResp.getData().getAccess_token();
        Map<String, String> parameters = RequestParameters.forumTopic(forum_topic_id, access_token);
        System.out.println(parameters.toString());
        new OkHttpRequest.Builder().url(URLManager.FORUM_TOPIC).params(parameters).tag(this).get(new ResultCallback<Event>() {
            @Override
            public void onError(Request request, Exception e) {
                if (type == TYPE_LOAD_FIRST) {
                    mLoadingAndRetryManager.showRetry();
                }
            }

            @Override
            public void onBefore(Request request) {
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
            public void onResponse(Event response) {
                if (response.isSuccess(ForumEventDetailsActivity.this)) {
                    event = response;
                    initData();
                    getComments(null);
                } else {
                    Toast.makeText(ForumEventDetailsActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                    if (type == TYPE_LOAD_FIRST) {
                        mLoadingAndRetryManager.showRetry();
                    }
                }
            }

        });
    }

    private void getComments(final String last_id) {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        String access_token = loginResp.getData().getAccess_token();
        Map<String, String> parameter = RequestParameters.forumComments(access_token, forum_topic_id, null, last_id);
        new OkHttpRequest.Builder().url(URLManager.FORUM_COMMENTS).params(parameter).tag(this).get(new ResultCallback<ForumComments>() {
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
                if (response.isSuccess(ForumEventDetailsActivity.this)) {
                    if (last_id == null) {
                        mComments.clear();
                    }
                    mComments.addAll(response.getData().getForum_comments());
                    mCommentAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ForumEventDetailsActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    boolean isAuthor = false;

    private void initData() {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        String user_id = loginResp.getData().getUser_id();
        int resId;
        if (user_id.equals(event.getData().getAuthor().getUser_id())) {
            resId = R.drawable.common_navigation_more;
            isAuthor = true;
        } else {
            isAuthor = false;
            if (event.getData().isIs_favorite()) {
                resId = R.drawable.common_collection_selected;
            } else {
                resId = R.drawable.common_collection_normal;
            }
        }

        setTitle("活动详情", null, R.drawable.btn_back, null, resId);
        tv_title.setText(event.getData().getTitle());
        tv_content.setText(event.getData().getContent());
        tv_nickName.setText(event.getData().getAuthor().getNickname());
        tv_date.setText(TimeUtils.getDateToString(event.getData().getCreated_at()));
        tv_startDate.setText(TimeUtils.getDateToString(event.getData().getExtra().getStart_date()));
        tv_participantsCount.setText("已有" + event.getData().getExtra().getParticipants().size() + "人参与");
        if (event.getData().getExtra().getEnd_date() != null) {
            tv_endDate.setText(TimeUtils.getDateToString(event.getData().getExtra().getEnd_date()));
        } else {
            tv_endDate.setText("长期有效");
        }
        if (event.getData().getExtra().isJoined()) {
            divider.setVisibility(View.GONE);
            bt_join.setVisibility(View.GONE);
        } else {
            bt_join.setText("参加活动");
        }
        if (event.getData().getExtra().getPic_urls().size() != 0) {
            rv_pic.setVisibility(View.VISIBLE);
            GalleryAdapter adapter = new GalleryAdapter(this, event.getData().getExtra().getPic_urls());
            rv_pic.setAdapter(adapter);
            adapter.setOnItemClickLitener(this);
        }

        long startTime = Long.parseLong(event.getData().getExtra().getStart_date());
        long currentTime = System.currentTimeMillis() / 1000;
        long endTime;
        if (event.getData().getExtra().getEnd_date() != null) {
            endTime = Long.parseLong(event.getData().getExtra().getEnd_date());
        } else {
            endTime = Long.MAX_VALUE;
        }
        if (startTime > currentTime) {
            tv_status.setImageResource(R.drawable.ic_status_un_start);
        }
        if (endTime < currentTime) {
            tv_status.setImageResource(R.drawable.ic_status_end);
        }
        if (startTime < currentTime && endTime > currentTime) {
            tv_status.setImageResource(R.drawable.ic_status_under_way);
        }

        avatarAdapter = new AvatarAdapter(this, avatar_url);
        avatarAdapter.setOnItemClickListener(new AvatarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Event.DataEntity.ExtraEntity.ParticipantsEntity entity = event.getData().getExtra().getParticipants().get(position);
                String avatar = entity.getAvatar_url();
                String nickName = entity.getNickname();
                String userId = entity.getUser_id();
                Uiutils.jumpUserInfoPage(ForumEventDetailsActivity.this, cacheUserId, avatar, nickName, userId);
            }
        });
        rv_participantsAvatar.setAdapter(avatarAdapter);
        avatar_url.clear();
        for (int i = 0; i < event.getData().getExtra().getParticipants().size(); i++) {
            avatar_url.add(event.getData().getExtra().getParticipants().get(i).getAvatar_url());
        }

        if (avatar_url.size() != 0) {
            rv_participantsAvatar.setVisibility(View.VISIBLE);
        }

        ImageLoader.getInstance().displayImage(event.getData().getAuthor().getAvatar_url(), civ_avatar, DisplayOpitionFactory.sAvatarDisplayOption);
        civ_avatar.setOnClickListener(this);

        tv_repliesCount.setText(String.valueOf(event.getData().getComments_count()));
        ll_comment.setVisibility(View.VISIBLE);

        mLoadingAndRetryManager.showContent();
    }

    private void initCommentView() {

        prsv_refresh.setOnSlideListener(new PullToRefreshBase.OnSlideLisnter() {
            @Override
            public void onSlide() {
                resetEtComment(prsv_refresh);
            }
        });

        mCommentAdapter = new CommentAdapter(this, mComments);
        mCommentAdapter.setOnCommentListener(new CommentAdapter.OnCommentListener() {
            @Override
            public void onComment(String comment_id) {
                et_comment.setHint("回复评论");
                et_comment.setTag(comment_id);
                Uiutils.showInputWindow(getApplicationContext(), et_comment);
            }
        });
        lv_comment.setAdapter(mCommentAdapter);
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment_id = null;
                if (et_comment.getTag() != null) {
                    comment_id = (String) et_comment.getTag();
                }
                comment_create(forum_topic_id, comment_id, getTextNoSpace(et_comment));
            }
        });
        et_comment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        et_comment.setHint("评论");
        et_comment.setText("");
        et_comment.setTag(null);
        et_comment.clearFocus();
        focus.requestFocus();
        Uiutils.hideInputWindow(getApplicationContext(), et_comment);
    }

    private PullToRefreshBase.OnRefreshListener2<ScrollView> refresh = new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
            loadData(TYPE_LOAD_REFRESH);
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
            if (mComments.size() > 0) {
                String last_id = mCommentAdapter.getItem(mCommentAdapter.getCount() - 1).getForum_comment_id();
                getComments(last_id);
            } else {
                getComments(null);
            }
        }
    };


    @Override
    public void onItemClick(View view, int position) {
        ArrayList<SquareImage> results = new ArrayList<>();
        for (int i = 0; i < event.getData().getExtra().getPic_urls().size(); i++) {
            results.add(new SquareImage("", event.getData().getExtra().getPic_urls().get(i), "", "", SquareImage.PhotoType.NETWORK));
        }
        NavigatorImage.startImageSwitcherActivity(ForumEventDetailsActivity.this, results, position, false, R.drawable.default_pic_item);
    }

    TitlePopup titlePopup;

    private void initPopupView(View v) {
        titlePopup = new TitlePopup(ForumEventDetailsActivity.this, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titlePopup.addAction(new ActionItem(ForumEventDetailsActivity.this, "删除", R.drawable.ic_cancel));
        titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
            @Override
            public void onItemClick(ActionItem item, int position) {
                switch (position) {
                    case 0:
                        new CommonDialog.Builder(ForumEventDetailsActivity.this).setTitle(R.string.dialog_text_title)
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
                if (response.isSuccess(ForumEventDetailsActivity.this)) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(ForumEventDetailsActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_join:
                joinEvent();
                break;
            case R.id.title_leftimageview:
                finish();
                break;
            case R.id.title_rightimageview:
                if (isAuthor) {
                    initPopupView(v);
                } else {
                    if (event.getData().isIs_favorite()) {
                        favoriteCancel();
                    } else {
                        favorite();
                    }
                }

                break;
            case R.id.civ_avatar:
                String user_id = event.getData().getAuthor().getUser_id();
                String avatar_url = event.getData().getAuthor().getAvatar_url();
                String nickname = event.getData().getAuthor().getNickname();
                Uiutils.jumpUserInfoPage(ForumEventDetailsActivity.this, cacheUserId, avatar_url, nickname, user_id);
                break;

        }
    }

    private void joinEvent() {
        final LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        String access_token = loginResp.getData().getAccess_token();
        Map<String, String> parameters = RequestParameters.forumTopicJoin(access_token, forum_topic_id);
        new OkHttpRequest.Builder().url(URLManager.FORUM_TOPIC_JOIN).params(parameters).tag(this).post(new ResultCallback<BaseResp>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                showProgressDialow("请稍候...");
            }

            @Override
            public void onAfter() {
                super.onAfter();
                dismissProgressDialog();
            }

            @Override
            public void onResponse(BaseResp response) {
                if (response.isSuccess(ForumEventDetailsActivity.this)) {
                    bt_join.setVisibility(View.GONE);
                    divider.setVisibility(View.GONE);
                    tv_participantsCount.setText("已有" + (event.getData().getExtra().getParticipants().size() + 1) + "人参与");
                    if (rv_participantsAvatar.getVisibility() == View.GONE) {
                        rv_participantsAvatar.setVisibility(View.VISIBLE);
                    }

                    Event.DataEntity.ExtraEntity.ParticipantsEntity participantsEntity = new Event.DataEntity.ExtraEntity.ParticipantsEntity();
                    participantsEntity.setAvatar_url(loginResp.getData().getUser_info().getAvatar_url());
                    participantsEntity.setUser_id(loginResp.getData().getUser_id());
                    participantsEntity.setNickname(loginResp.getData().getUser_info().getNickname());
                    event.getData().getExtra().getParticipants().add(participantsEntity);

                    avatar_url.add(loginResp.getData().getUser_info().getAvatar_url());
                    avatarAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ForumEventDetailsActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
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
                if (response.isSuccess(ForumEventDetailsActivity.this)) {
                    setRightImage(R.drawable.common_collection_selected);
                    event.getData().setIs_favorite(true);
                } else {
                    Toast.makeText(ForumEventDetailsActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void favoriteCancel() {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        String access_token = loginResp.getData().getAccess_token();
        Map<String, String> parameters = RequestParameters.favoriteCancelForumAndCommodity(access_token, Constants.FAVORITE_TYPE_FORUM, forum_topic_id);
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
                if (response.isSuccess(ForumEventDetailsActivity.this)) {
                    setRightImage(R.drawable.common_collection_normal);
                    event.getData().setIs_favorite(false);
                }
            }
        });
    }

    private PullToRefreshBase.OnSlideLisnter slideListern = new PullToRefreshBase.OnSlideLisnter() {
        @Override
        public void onSlide() {
            resetEtComment(prsv_refresh);
        }
    };

    private OnLoadingAndRetryListener loadingAndRetryListener = new OnLoadingAndRetryListener() {
        @Override
        public void setRetryEvent(View retryView) {
            ForumEventDetailsActivity.this.setRetryEvent(retryView);
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


}
