package com.aosijia.dragonbutler.model;

import android.content.Context;
import android.content.Intent;

import com.aosijia.dragonbutler.rest.URLManager;
import com.aosijia.dragonbutler.ui.activity.LoginActivity;

import java.io.Serializable;

/**
 * Created by wanglj on 15/12/15.
 */
public class BaseResp implements Serializable {


    /**
     * status_code : 200
     * msg : 获取验证码成功
     */

    private String status_code;
    private String msg;

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus_code() {
        return status_code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "BaseResp{" +
                "status_code='" + status_code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }


    public boolean isSuccess(Context context) {
        if(URLManager.STATUS_INVALID_ACCESSTOKEN.equals(getStatus_code())){
            Intent intent = new Intent();
            intent.setClass(context, LoginActivity.class);
            context.startActivity(intent);
            return false;
        }
        return URLManager.STATUS_CODE_OK.equals(getStatus_code()) || URLManager.STATUS_CODE_OK_TOAST.equals(getStatus_code());
    }
}
