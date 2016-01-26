package com.aosijia.dragonbutler.ui.widget.loader;

import android.content.Context;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.aosijia.dragonbutler.R;

/**
 * Created by zhy on 15/8/26.
 */
public class LoadingAndRetryLayout extends FrameLayout
{
    private View mLoadingView;
    private View mRetryView;
    private View mContentView;
    private View mEmptyView;
    private LayoutInflater mInflater;


    private static final String TAG = LoadingAndRetryLayout.class.getSimpleName();


    public LoadingAndRetryLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
    }


    public LoadingAndRetryLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, -1);
    }

    public LoadingAndRetryLayout(Context context)
    {
        this(context, null);
    }

    private boolean isMainThread()
    {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public void showLoading()
    {
        if (isMainThread())
        {
            showView(mLoadingView,LoadingAndRetryManager.TYPE_DEFAULT);
        } else
        {
            post(new Runnable()
            {
                @Override
                public void run()
                {
                    showView(mLoadingView,LoadingAndRetryManager.TYPE_DEFAULT);
                }
            });
        }
    }

    public void showRetry()
    {
        if (isMainThread())
        {
            showView(mRetryView,LoadingAndRetryManager.TYPE_DEFAULT);
        } else
        {
            post(new Runnable()
            {
                @Override
                public void run()
                {
                    showView(mRetryView,LoadingAndRetryManager.TYPE_DEFAULT);
                }
            });
        }

    }

    public void showContent()
    {
        if (isMainThread())
        {
            showView(mContentView,LoadingAndRetryManager.TYPE_DEFAULT);
        } else
        {
            post(new Runnable()
            {
                @Override
                public void run()
                {
                    showView(mContentView,LoadingAndRetryManager.TYPE_DEFAULT);
                }
            });
        }
    }

    public void showEmpty(final int type)
    {
        if (isMainThread())
        {
            showView(mEmptyView,type);
        } else
        {
            post(new Runnable()
            {
                @Override
                public void run()
                {
                    showView(mEmptyView,type);
                }
            });
        }
    }


    private void showView(View view,int type)
    {
        if (view == null) return;

        if (view == mLoadingView)
        {
            mLoadingView.setVisibility(View.VISIBLE);
            if (mRetryView != null)
                mRetryView.setVisibility(View.GONE);
            if (mContentView != null)
                mContentView.setVisibility(View.GONE);
            if (mEmptyView != null)
                mEmptyView.setVisibility(View.GONE);
        } else if (view == mRetryView)
        {
            mRetryView.setVisibility(View.VISIBLE);
            if (mLoadingView != null)
                mLoadingView.setVisibility(View.GONE);
            if (mContentView != null)
                mContentView.setVisibility(View.GONE);
            if (mEmptyView != null)
                mEmptyView.setVisibility(View.GONE);
        } else if (view == mContentView)
        {
            mContentView.setVisibility(View.VISIBLE);
            if (mLoadingView != null)
                mLoadingView.setVisibility(View.GONE);
            if (mRetryView != null)
                mRetryView.setVisibility(View.GONE);
            if (mEmptyView != null)
                mEmptyView.setVisibility(View.GONE);
        } else if (view == mEmptyView)
        {
            mEmptyView.setVisibility(View.VISIBLE);
            ImageView imageview = (ImageView)mEmptyView.findViewById(R.id.empty_image);
            TextView textView = (TextView)mEmptyView.findViewById(R.id.errorStateContentTextView);
            switch (type){
                case LoadingAndRetryManager.TYPE_EMPTY_NODATA:
                    imageview.setImageResource(R.drawable.ic_data_empty);
                    textView.setText("暂时无数据");
                    break;
                case LoadingAndRetryManager.TYPE_EMPTY_NOFAVORITE:
                    imageview.setImageResource(R.drawable.ic_favorite_empty);
                    textView.setText("暂无收藏");
                    break;
                case LoadingAndRetryManager.TYPE_EMPTY_NOMESSAGE:
                    imageview.setImageResource(R.drawable.ic_message_empty);
                    textView.setText("暂无私信");
                    break;
                case LoadingAndRetryManager.TYPE_EMPTY_RECEIVE:
                    imageview.setImageResource(R.drawable.ic_message_empty);
                    textView.setText("暂无回复");
                    break;
                case LoadingAndRetryManager.TYPE_EMPTY_NOPUBLISH:
                    imageview.setImageResource(R.drawable.ic_data_empty);
                    textView.setText("暂无发布");
                    break;
                default:
                    imageview.setImageResource(R.drawable.ic_data_empty);
                    break;
            }
            if (mLoadingView != null)
                mLoadingView.setVisibility(View.GONE);
            if (mRetryView != null)
                mRetryView.setVisibility(View.GONE);
            if (mContentView != null)
                mContentView.setVisibility(View.GONE);
        }


    }



    public View setContentView(int layoutId)
    {
        return setContentView(mInflater.inflate(layoutId, this, false));
    }

    public View setLoadingView(int layoutId)
    {
        return setLoadingView(mInflater.inflate(layoutId, this, false));
    }

    public View setEmptyView(int layoutId)
    {
        return setEmptyView(mInflater.inflate(layoutId, this, false));
    }

    public View setRetryView(int layoutId)
    {
        return setRetryView(mInflater.inflate(layoutId, this, false));
    }
    public View setLoadingView(View view)
    {
        View loadingView = mLoadingView;
        if (loadingView != null)
        {
            Log.w(TAG, "you have already set a loading view and would be instead of this new one.");
        }
        removeView(loadingView);
        addView(view);
        mLoadingView = view;
        return mLoadingView;
    }

    public View setEmptyView(View view)
    {
        View emptyView = mEmptyView;
        if (emptyView != null)
        {
            Log.w(TAG, "you have already set a empty view and would be instead of this new one.");
        }
        removeView(emptyView);
        addView(view);
        mEmptyView = view;
        return mEmptyView;
    }

    public View setRetryView(View view)
    {
        View retryView = mRetryView;
        if (retryView != null)
        {
            Log.w(TAG, "you have already set a retry view and would be instead of this new one.");
        }
        removeView(retryView);
        addView(view);
        mRetryView = view;
        return mRetryView;

    }

    public View setContentView(View view)
    {
        View contentView = mContentView;
        if (contentView != null)
        {
            Log.w(TAG, "you have already set a retry view and would be instead of this new one.");
        }
        removeView(contentView);
        addView(view);
        mContentView = view;
        return mContentView;
    }

    public View getRetryView()
    {
        return mRetryView;
    }

    public View getLoadingView()
    {
        return mLoadingView;
    }

    public View getContentView()
    {
        return mContentView;
    }

    public View getEmptyView()
    {
        return mEmptyView;
    }
}
