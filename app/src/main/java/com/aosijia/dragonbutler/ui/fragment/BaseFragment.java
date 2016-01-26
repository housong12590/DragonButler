package com.aosijia.dragonbutler.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import com.aosijia.dragonbutler.cache.Share;
import com.aosijia.dragonbutler.model.LoginResp;
import com.aosijia.dragonbutler.ui.activity.BaseActivity;
import com.aosijia.dragonbutler.ui.activity.HouseHoldBindActivity;
import com.aosijia.dragonbutler.ui.activity.LoginActivity;
import com.aosijia.dragonbutler.ui.widget.LoadingDialogFragment;

/**
 * Created by Jacky on 2015/12/28.
 * Version 1.0
 */
public class BaseFragment extends Fragment {
    private BaseActivity activity;
    private LoadingDialogFragment mDialogFragment;
    public static final int TYPE_LOAD_MORE = 3;
    public static final int TYPE_LOAD_REFRESH = 2;
    public static final int TYPE_LOAD_FIRST = 1;


    @Override
    public void onAttach(Context context) {
        if (getActivity() instanceof BaseActivity) {
            activity = (BaseActivity) getActivity();
        }
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void startActivity(Intent intent) {

        String className = intent.getComponent().getClassName();
        if (BaseActivity.isNeedCheckLogin(className)) {
            //检查是否设置过用户信息
            LoginResp loginResp = (LoginResp) Share.getObject(Share.LOGIN_RESP);
            if (loginResp == null) {
                intent.putExtra("action", className);
                intent.setClass(getActivity(), LoginActivity.class);
            } else if (!loginResp.getData().getUser_info().isIs_bound()) {
                intent.putExtra("action", className);
                intent.setClass(getActivity(), HouseHoldBindActivity.class);
            }
        }
        super.startActivity(intent);
    }

    protected void setTitle(String title) {
        if (getActivity() instanceof BaseActivity) {
            activity = (BaseActivity) getActivity();
            activity.setTitle(title);
        }
    }

    protected void setTitle(String title, String leftTitle, int leftResId, String rightTitle, int rightResId) {
        if (getActivity() instanceof BaseActivity) {
            activity = (BaseActivity) getActivity();
            activity.setTitle(title, leftTitle, leftResId, rightTitle, rightResId);
        }
    }


    public void setBtnLeftOnClickListener(View.OnClickListener listener) {

        if (getActivity() instanceof BaseActivity) {
            activity = (BaseActivity) getActivity();
            activity.setBtnLeftOnClickListener(listener);
        }
    }


    public void setBtnRightTextOnClickListener(View.OnClickListener listener) {
        if (getActivity() instanceof BaseActivity) {
            activity = (BaseActivity) getActivity();
            activity.setBtnRightTextOnClickListener(listener);
        }
    }

    /**
     * 显示加载中对话框
     *
     * @param message
     */
    protected void showProgressDialow(String message) {
        mDialogFragment = (LoadingDialogFragment) getFragmentManager().findFragmentByTag(LoadingDialogFragment.class.getSimpleName());

        if (mDialogFragment == null) {
            mDialogFragment = new LoadingDialogFragment();
        }
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        mDialogFragment.show(ft, message);
        mDialogFragment.setCancelable(false);
    }


    /**
     * 销毁对话框
     */
    protected void dismissProgressDialog() {
        if (mDialogFragment != null && mDialogFragment.isAdded()) {
            mDialogFragment.dismissAllowingStateLoss();
        }
    }


    /**
     * 网络请求错误提示
     */
    protected void showRequestError() {
        if(getActivity()!=null) {
            Toast.makeText(getActivity(), "网络请求异常,请检查网络配置", Toast.LENGTH_SHORT).show();
        }
    }





}
