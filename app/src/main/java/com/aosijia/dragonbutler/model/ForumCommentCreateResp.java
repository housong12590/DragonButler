package com.aosijia.dragonbutler.model;

/**
 * Created by Jacky on 2015/12/31.
 * Version 1.0
 */
public class ForumCommentCreateResp extends BaseResp{

    public ForumComments.DataEntity.ForumCommentsEntity data;

    public ForumComments.DataEntity.ForumCommentsEntity getData() {
        return data;
    }

    public void setData(ForumComments.DataEntity.ForumCommentsEntity data) {
        this.data = data;
    }

}
