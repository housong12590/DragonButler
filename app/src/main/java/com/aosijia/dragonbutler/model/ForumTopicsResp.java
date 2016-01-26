package com.aosijia.dragonbutler.model;

import java.util.List;

/**
 * Created by Jacky on 2015/12/28.
 * Version 1.0
 */
public class ForumTopicsResp extends BaseResp{

    private DataEntity data;

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public static class DataEntity {
        private List<ForumTopicItem> forum_topics;

        public List<ForumTopicItem> getForum_topics() {
            return forum_topics;
        }

        public void setForum_topics(List<ForumTopicItem> forum_topics) {
            this.forum_topics = forum_topics;
        }
    }
}
