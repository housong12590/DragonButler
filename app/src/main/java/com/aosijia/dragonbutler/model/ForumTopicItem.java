package com.aosijia.dragonbutler.model;

import android.text.TextUtils;

/**
 * Created by Jacky on 2015/12/28.
 * Version 1.0
 */
public class ForumTopicItem {

    private String forum_topic_id;
    private String title;
    private String comments_count;
    private String type;
    private String created_at;
    private Extra extra;
    private Author author;

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getForum_topic_id() {
        return forum_topic_id;
    }

    public void setForum_topic_id(String forum_topic_id) {
        this.forum_topic_id = forum_topic_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComments_count() {
        if (TextUtils.isEmpty(comments_count)) {
            return "0";
        } else {
            int temp_count = Integer.parseInt(comments_count);
            if (temp_count > 999) {
                return "999+";
            } else {
                return comments_count;
            }
        }
    }

    public void setComments_count(String comments_count) {
        this.comments_count = comments_count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Extra getExtra() {
        return extra;
    }

    public void setExtra(Extra extra) {
        this.extra = extra;
    }

    public class Extra {
        private String[] pic_urls;
        private String participants_count;
        private String start_date;
        private String end_date;
        private boolean open;

        public String[] getPic_urls() {
            return pic_urls;
        }

        public void setPic_urls(String[] pic_urls) {
            this.pic_urls = pic_urls;
        }

        public String getParticipants_count() {
            return participants_count;
        }

        public void setParticipants_count(String participants_count) {
            this.participants_count = participants_count;
        }

        public String getStart_date() {
            return start_date;
        }

        public void setStart_date(String start_date) {
            this.start_date = start_date;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public boolean isOpen() {
            return open;
        }

        public void setOpen(boolean open) {
            this.open = open;
        }
    }

}
