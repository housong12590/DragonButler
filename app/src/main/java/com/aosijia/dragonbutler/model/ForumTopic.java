package com.aosijia.dragonbutler.model;

/**
 * Created by Jacky on 2015/12/30.
 * Version 1.0
 */
public class ForumTopic extends BaseForum {
    private TopicExtra extra;

    public class TopicExtra {
        private String[] pic_urls;

        public String[] getPic_urls() {
            return pic_urls;
        }

        public void setPic_urls(String[] pic_urls) {
            this.pic_urls = pic_urls;
        }

    }

}
