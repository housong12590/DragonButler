package com.aosijia.dragonbutler.model;

/**
 * Created by Administrator on 2016/1/7 0007.
 */
public class SecondHandCommentBase extends BaseResp {

  private SecondhandComments.DataEntity.SecondhandCommentsEntity data;


    public SecondhandComments.DataEntity.SecondhandCommentsEntity getData() {
        return data;
    }

    public void setData(SecondhandComments.DataEntity.SecondhandCommentsEntity data) {
        this.data = data;
    }
}
