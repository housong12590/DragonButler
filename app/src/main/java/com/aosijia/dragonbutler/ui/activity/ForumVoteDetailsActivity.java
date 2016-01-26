package com.aosijia.dragonbutler.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aosijia.dragonbutler.R;
import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.model.BaseResp;
import com.aosijia.dragonbutler.model.ForumCommentCreateResp;
import com.aosijia.dragonbutler.model.ForumComments;
import com.aosijia.dragonbutler.model.ForumVoteDetailsResp;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.rest.RequestParameters;
import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.adapter.CommentAdapter;
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
import com.aosijia.dragonbutler.utils.ToastUtils;
import com.aosijia.dragonbutler.utils.Uiutils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.callback.ResultCallback;
import com.zhy.http.okhttp.request.OkHttpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 投票详情
 * Created by Jacky on 2015/12/29.
 * Version 1.0
 */
public class ForumVoteDetailsActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_ID = "forum_topic_id";

    private PullToRefreshScrollView scrollView;

    private CircleImageView civAvatar;
    private TextView tvNickname;
    private TextView tvTime;
    private ImageView ivStatus;
    private TextView tvTitle;
    private TextView tvContent;
    private NoScrollListView lvOption;
    private Button btnVote;
    private TextView tvPartCount;

    private TextView tvCommentCount;
    private NoScrollListView lvComment;


    private EditText etComment;
    private TextView tvSend;


    private VoteOptionsAdapter mOptionAdapter;
    private List<ForumVoteDetailsResp.ForumVote.Option> mVoteOptions;

    private CommentAdapter mCommentAdapter;
    private List<ForumComments.DataEntity.ForumCommentsEntity> mComments;

    String mVoteId;
    private boolean isMe = false;
    private boolean isFavorite = false;

    private ForumVoteDetailsResp.ForumVote voteInfo;


    private LoadingAndRetryManager mloadingAndRetryManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setImmersionStatus();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_vote_details);
        initView();
        initAdapter();
        mVoteId = getIntent().getExtras().getString(EXTRA_ID);
        mloadingAndRetryManager = LoadingAndRetryManager.generate(scrollView, new OnLoadingAndRetryListener() {
            @Override
            public void setRetryEvent(View retryView) {
                View view = retryView.findViewById(R.id.id_btn_retry);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getVoteDetails(mVoteId);
                    }
                });
            }
        });
        getVoteDetails(mVoteId);
    }

    private void initView() {
        setTitle("投票详情", null, NO_RES_ID);
        setBtnRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMe) {
                    showPopupWindow(v);
                } else {
                    favorite(mVoteId, !voteInfo.is_favorite());
                }
            }
        });
        scrollView = (PullToRefreshScrollView) findViewById(R.id.scroll_view);
        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getVoteDetails(mVoteId);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                if (mComments.size() > 0) {
                    getComment(mVoteId, mComments.get(mComments.size() - 1).getForum_comment_id());
                } else {
                    getComment(mVoteId, null);
                }
            }
        });
        civAvatar = (CircleImageView) findViewById(R.id.iv_avatar);
        tvNickname = (TextView) findViewById(R.id.tv_nickname);
        tvTime = (TextView) findViewById(R.id.tv_time);
        ivStatus = (ImageView) findViewById(R.id.iv_status);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvContent = (TextView) findViewById(R.id.tv_content);
        tvPartCount = (TextView) findViewById(R.id.tv_participants_count);
        lvOption = (NoScrollListView) findViewById(R.id.lv_options);
        lvOption.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mOptionAdapter.setmSelectPosition(position);
                mOptionAdapter.notifyDataSetChanged();
            }
        });
        btnVote = (Button) findViewById(R.id.btn_vote);
        btnVote.setOnClickListener(this);
        tvCommentCount = (TextView) findViewById(R.id.tv_comment);
        lvComment = (NoScrollListView) findViewById(R.id.lv_comment);
        etComment = (EditText) findViewById(R.id.et_comment);
        tvSend = (TextView) findViewById(R.id.tv_send);
        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment_id = null;
                if (etComment.getTag() != null) {
                    comment_id = (String) etComment.getTag();
                }
                comment_create(mVoteId, comment_id, getTextNoSpace(etComment));
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


        scrollView.setHideSubText(true);
        scrollView.setMode(PullToRefreshBase.Mode.BOTH);
        scrollView.setOnSlideListener(new PullToRefreshBase.OnSlideLisnter() {
            @Override
            public void onSlide() {
                resetEtComment(scrollView);
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

    private void initAdapter() {
        mVoteOptions = new ArrayList<>();
        mOptionAdapter = new VoteOptionsAdapter(this, mVoteOptions);
        lvOption.setAdapter(mOptionAdapter);

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
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_vote) {
            if (mOptionAdapter.getmSelectPosition() >= 0) {
                ForumVoteDetailsResp.ForumVote.Option option = mVoteOptions.get(mOptionAdapter.getmSelectPosition());
                vote(mVoteId, option.getVote_option_id());
            } else {
                ToastUtils.showToast(getApplicationContext(), "请选择选项");
            }
        } else if (v.getId() == R.id.iv_avatar) {
            LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
            String cacheUserId = loginResp.getData().getUser_id();
            String avatar_url = voteInfo.getAuthor().getAvatar_url();
            String nickname = voteInfo.getAuthor().getNickname();
            String user_id = voteInfo.getAuthor().getUser_id();
            Uiutils.jumpUserInfoPage(this, cacheUserId, avatar_url, nickname, user_id);
        }
    }


    private void getVoteDetails(String vote_id) {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        Map<String, String> parameter = RequestParameters.forumTopic(vote_id, loginResp.getData().getAccess_token());
        new OkHttpRequest.Builder().url(URLManager.FORUM_TOPIC).params(parameter).tag(this).get(new ResultCallback<ForumVoteDetailsResp>() {
            @Override
            public void onError(Request request, Exception e) {
                showRequestError();
                mloadingAndRetryManager.showRetry();
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                if (voteInfo != null) {
                    mloadingAndRetryManager.showContent();
                } else {
                    mloadingAndRetryManager.showLoading();
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
                scrollView.onRefreshComplete();
            }

            @Override
            public void onResponse(ForumVoteDetailsResp response) {
                if (response.isSuccess(ForumVoteDetailsActivity.this)) {
                    setViewByInfo(response.getData());
                    mloadingAndRetryManager.showContent();
                    getComment(mVoteId, null);
                } else {
                    ToastUtils.showToast(getApplicationContext(), response.getMsg());
                    mloadingAndRetryManager.showRetry();
                }
            }
        });
    }

    private void getComment(String forum_topic_id, final String last_topic_id) {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        Map<String, String> parameter = RequestParameters.forumComments(loginResp.getData().getAccess_token(), forum_topic_id, null, last_topic_id);
        new OkHttpRequest.Builder().url(URLManager.FORUM_COMMENTS).params(parameter).tag(this).get(new ResultCallback<ForumComments>() {
            @Override
            public void onError(Request request, Exception e) {
                showRequestError();
//                dismissProgressDialog();
            }

            @Override
            public void onAfter() {
                super.onAfter();
                scrollView.onRefreshComplete();
//                dismissProgressDialog();
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
//                dismissProgressDialog();
            }

            @Override
            public void onResponse(ForumComments response) {
                if (response.isSuccess(ForumVoteDetailsActivity.this)) {
                    if (last_topic_id != null) {
                        mComments.addAll(response.getData().getForum_comments());
                    } else {
                        mComments.clear();
                        mComments.addAll(response.getData().getForum_comments());
                    }
                    if (response.getData().getForum_comments().size() == 20) {
                        scrollView.setMode(PullToRefreshBase.Mode.BOTH);
                    } else {
                        scrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }
                    mCommentAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ForumVoteDetailsActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
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
                if (response.isSuccess(ForumVoteDetailsActivity.this)) {
                    if (!TextUtils.isEmpty(getTextNoSpace(tvCommentCount)) && TextUtils.isDigitsOnly(getTextNoSpace(tvCommentCount))) {
                        int count = Integer.parseInt(getTextNoSpace(tvCommentCount));
                        tvCommentCount.setText((count + 1) + "");
                    } else {
                        tvCommentCount.setText(1 + "");
                    }
                    ForumComments.DataEntity.ForumCommentsEntity comment = response.getData();
                    mCommentAdapter.add(comment);
                    mCommentAdapter.notifyDataSetChanged();
                    resetEtComment(scrollView);
                }
                Toast.makeText(ForumVoteDetailsActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void vote(final String forum_topic_id, final String vote_option_id) {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        Map<String, String> parameter = RequestParameters.forumTopicVote(loginResp.getData().getAccess_token(), forum_topic_id, vote_option_id);
        new OkHttpRequest.Builder().url(URLManager.FORUM_TOPIC_VOTE).params(parameter).tag(this).post(new ResultCallback<BaseResp>() {
            @Override
            public void onError(Request request, Exception e) {
                dismissProgressDialog();
                showRequestError();
            }

            @Override
            public void onBefore(Request request) {
                showProgressDialow("努力加载中...");
            }

            @Override
            public void onAfter() {
                dismissProgressDialog();
            }

            @Override
            public void onResponse(BaseResp response) {
                if (response.isSuccess(ForumVoteDetailsActivity.this)) {
                    getVoteDetails(forum_topic_id);
                }
                Toast.makeText(ForumVoteDetailsActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * @param forum_topic_id
     * @param favorite       收藏或取消收藏。收藏为true
     */
    private void favorite(String forum_topic_id, boolean favorite) {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        Map<String, String> parameters = RequestParameters.favoriteForumAndCommodity(loginResp.getData().getAccess_token(),
                Constants.FAVORITE_TYPE_FORUM, forum_topic_id);
        String url = null;
        if (favorite) {
            url = URLManager.FAVORITE_CREATE;
        } else {
            url = URLManager.FAVORITE_CANCEL;
        }
        new OkHttpRequest.Builder().url(url).params(parameters).tag(this).post(new ResultCallback<BaseResp>() {
            @Override
            public void onError(Request request, Exception e) {
                dismissProgressDialog();
                showRequestError();
            }

            @Override
            public void onAfter() {
                dismissProgressDialog();
            }

            @Override
            public void onBefore(Request request) {
                showProgressDialow("");
            }

            @Override
            public void onResponse(BaseResp response) {
                if (response.getStatus_code().equals(URLManager.STATUS_CODE_OK)) {
                    voteInfo.setIs_favorite(!voteInfo.is_favorite());
                    setViewByInfo(voteInfo);
                }
                Toast.makeText(getApplicationContext(), response.getMsg(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void forumTopicDelete() {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        Map<String, String> parameter = RequestParameters.forumTopicDelete(loginResp.getData().getAccess_token(), mVoteId);
        new OkHttpRequest.Builder().url(URLManager.FORUM_TOPIC_DELETE).params(parameter).tag(this).post(new ResultCallback<BaseResp>() {


            @Override
            public void onError(Request request, Exception e) {
                dismissProgressDialog();
                showRequestError();
            }

            @Override
            public void onAfter() {
                dismissProgressDialog();
            }

            @Override
            public void onBefore(Request request) {
                showProgressDialow("");
            }

            @Override
            public void onResponse(BaseResp response) {
                if (response.isSuccess(ForumVoteDetailsActivity.this)) {
                    setResult(RESULT_OK);
                    ForumVoteDetailsActivity.this.finish();
                }
            }

        });
    }

    private void forumTopicClose() {
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        Map<String, String> parameter = RequestParameters.forumTopicDelete(loginResp.getData().getAccess_token(), mVoteId);
        new OkHttpRequest.Builder().url(URLManager.FORUM_TOPIC_CLOSE_VOTE_).params(parameter).tag(this).post(new ResultCallback<BaseResp>() {
            @Override
            public void onError(Request request, Exception e) {
                dismissProgressDialog();
                showRequestError();
            }

            @Override
            public void onAfter() {
                dismissProgressDialog();
            }

            @Override
            public void onBefore(Request request) {
                showProgressDialow("");
            }

            @Override
            public void onResponse(BaseResp response) {
                if (response.isSuccess(ForumVoteDetailsActivity.this)) {
                    getVoteDetails(mVoteId);
//                    setResult(RESULT_OK);
//                    ForumVoteDetailsActivity.this.finish();
                }
            }

        });
    }


    private void setViewByInfo(ForumVoteDetailsResp.ForumVote info) {
        voteInfo = info;
        LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
        isFavorite = info.is_favorite();
        isMe = voteInfo.getAuthor().getUser_id().equals(loginResp.getData().getUser_id());
        if (isMe) {
            if (info.getExtra().isOpen()) {
                setTitle("投票详情", null, R.drawable.common_navigation_more);
            } else {
                setTitle("投票详情", null, R.drawable.common_navigation_more);
            }
        } else {
            if (isFavorite) {
                setTitle("投票详情", null, R.drawable.common_collection_selected);
            } else {
                setTitle("投票详情", null, R.drawable.common_collection_normal);
            }

        }

        ImageLoader.getInstance().displayImage(info.getAuthor().getAvatar_url(), civAvatar, DisplayOpitionFactory.sAvatarDisplayOption);
        civAvatar.setOnClickListener(this);
        tvNickname.setText(info.getAuthor().getNickname());
        tvTime.setText(TimeUtils.getDateToString(info.getCreated_at()));

        if (info.getExtra().isOpen()) {
            ivStatus.setImageResource(R.drawable.ic_status_under_way);
        } else {
            ivStatus.setImageResource(R.drawable.ic_status_close);
        }
        tvTitle.setText(info.getTitle());
        tvContent.setText(info.getContent());

        mVoteOptions.clear();
        mVoteOptions.addAll(info.getExtra().getOptions());
        mOptionAdapter.setIsVoted(info.getExtra().isVoted());
        mOptionAdapter.setmParticipantsCount(Integer.parseInt(info.getExtra().getParticipants_count()));
        mOptionAdapter.notifyDataSetChanged();


        if (info.getExtra().isVoted()) {
            tvPartCount.setVisibility(View.VISIBLE);
            btnVote.setVisibility(View.GONE);
            tvPartCount.setText("已有" + info.getExtra().getParticipants_count() + "人参与，投票结果：");
        } else {
            tvPartCount.setVisibility(View.GONE);
            btnVote.setVisibility(View.VISIBLE);
        }

        tvCommentCount.setText(info.getComments_count());
    }


    private void showPopupWindow(View v) {
        boolean isOpen = voteInfo.getExtra().isOpen();
        final String close = "关闭";
        final String delete = "删除";
        TitlePopup titlePopup = new TitlePopup(getApplicationContext(), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
            @Override
            public void onItemClick(ActionItem item, int position) {
                if (close.equals(item.mTitle)) {
                    new CommonDialog.Builder(ForumVoteDetailsActivity.this).setTitle(R.string.dialog_text_title)
                            .setMessage("确定关闭？").setNegativeButtonText(R.string.dialog_text_cancel, null)
                            .setPositiveButtonText(R.string.dialog_text_confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    forumTopicClose();
                                }
                            }).show();
                } else {
                    new CommonDialog.Builder(ForumVoteDetailsActivity.this).setTitle(R.string.dialog_text_title)
                            .setMessage("确定删除？").setNegativeButtonText(R.string.dialog_text_cancel, null)
                            .setPositiveButtonText(R.string.dialog_text_confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    forumTopicDelete();
                                }
                            }).show();
                }
            }
        });
        if (isOpen) {
            titlePopup.addAction(new ActionItem(getApplicationContext(), close, R.drawable.ic_close));
            titlePopup.addAction(new ActionItem(getApplicationContext(), delete, R.drawable.ic_cancel));
        } else {
            titlePopup.addAction(new ActionItem(getApplicationContext(), delete, R.drawable.ic_close));
        }

        titlePopup.show(v);

    }

    private class VoteOptionsAdapter extends BaseAdapter {

        private List<ForumVoteDetailsResp.ForumVote.Option> mList;
        private Context mContext;
        private boolean isVoted = false;
        private int mSelectPosition = -1;
        private int mParticipantsCount = 1;

        public VoteOptionsAdapter(Context context, List<ForumVoteDetailsResp.ForumVote.Option> list) {
            this.mContext = context;
            this.mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ForumVoteDetailsResp.ForumVote.Option option = mList.get(position);
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_vote_option, null);
                holder = new ViewHolder();
                holder.tv_option = (TextView) convertView.findViewById(R.id.tv_option);
                holder.iv_selected = (ImageView) convertView.findViewById(R.id.iv_selected);
                holder.layoutProgress = (ViewGroup) convertView.findViewById(R.id.layout_progress);
                holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
                holder.tvProgress = (TextView) convertView.findViewById(R.id.tv_progress);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv_option.setText(option.getDescription());
            if (isVoted) {
                holder.iv_selected.setVisibility(View.GONE);
                holder.layoutProgress.setVisibility(View.VISIBLE);
                int vote_count = Integer.parseInt(option.getVotes_count());
                int progress = (int) (1.0f * vote_count / mParticipantsCount * 100);
                holder.progressBar.setProgress(progress);
                holder.tvProgress.setText(progress + "%");
            } else {
                holder.iv_selected.setVisibility(View.VISIBLE);
                holder.layoutProgress.setVisibility(View.GONE);
                if (position != mSelectPosition) {
                    holder.iv_selected.setImageResource(R.drawable.common_radio_normal);
                } else {
                    holder.iv_selected.setImageResource(R.drawable.common_radio_selected);
                }
            }
            return convertView;
        }


        public boolean isVoted() {
            return isVoted;
        }

        public void setIsVoted(boolean isVoted) {
            this.isVoted = isVoted;
        }

        public int getmSelectPosition() {
            return mSelectPosition;
        }

        public void setmSelectPosition(int mSelectPosition) {
            this.mSelectPosition = mSelectPosition;
        }

        public int getmParticipantsCount() {
            return mParticipantsCount;
        }

        public void setmParticipantsCount(int mParticipantsCount) {
            this.mParticipantsCount = mParticipantsCount;
        }

        private class ViewHolder {
            private TextView tv_option;
            private ImageView iv_selected;
            private ViewGroup layoutProgress;
            private ProgressBar progressBar;
            private TextView tvProgress;
        }
    }

}
