package com.aosijia.dragonbutler.model;

/**
 * Created by wanglj on 16/1/12.
 */
public class UnreadMessage extends BaseResp{


    /**
     * unread_messages_count : 35
     */

    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        private int unread_messages_count;

        public void setUnread_messages_count(int unread_messages_count) {
            this.unread_messages_count = unread_messages_count;
        }

        public int getUnread_messages_count() {
            return unread_messages_count;
        }
    }
}
