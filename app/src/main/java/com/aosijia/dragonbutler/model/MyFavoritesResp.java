package com.aosijia.dragonbutler.model;

import java.util.List;

/**
 * Created by Jacky on 2016/1/4.
 * Version 1.0
 */
public class MyFavoritesResp extends BaseResp{

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data{

        private List<ForumTopicItem> forum_topics;
        private List<SecondHandItems.DataEntity.SecondhandItemsEntity> secondhand_items;

        public List<ForumTopicItem> getForum_topics() {
            return forum_topics;
        }

        public void setForum_topics(List<ForumTopicItem> forum_topics) {
            this.forum_topics = forum_topics;
        }

        public List<SecondHandItems.DataEntity.SecondhandItemsEntity> getSecondhand_items() {
            return secondhand_items;
        }

        public void setSecondhand_items(List<SecondHandItems.DataEntity.SecondhandItemsEntity> secondhand_items) {
            this.secondhand_items = secondhand_items;
        }
    }
}
