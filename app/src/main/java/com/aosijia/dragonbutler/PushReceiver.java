package com.aosijia.dragonbutler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aosijia.dragonbutler.ui.activity.MessageBoxActivity;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by wanglj on 16/1/12.
 */
public class PushReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        if(JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())){
            Intent i = new Intent(context, MessageBoxActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }

    }
}
