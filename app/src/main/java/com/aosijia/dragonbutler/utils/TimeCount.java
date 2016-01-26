package com.aosijia.dragonbutler.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * 短信倒计时辅助类
 */
public class TimeCount extends CountDownTimer {


    private TextView mTextView;

    public TimeCount(TextView tv, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mTextView = tv;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mTextView.setClickable(false);
//		mButton.setBackgroundResource(R.color.gray);
        mTextView.setText("剩余"+millisUntilFinished / 1000+"秒");
    }

    @Override
    public void onFinish() {
        mTextView.setClickable(true);
//		mButton.setBackgroundResource(R.drawable.btn_light_red_selector);
        mTextView.setText("获取验证码");
    }



}
