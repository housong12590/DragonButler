package com.aosijia.dragonbutler.model;

import android.text.TextUtils;

/**
 * Created by Jacky on 2015/12/28.
 * Version 1.0
 */
public class BaseForum {

    private String forum_topic_id;
    private String title;
    private String content;
    private Author author;
    private String comments_count;
    private String type;
    private String created_at;
    private boolean is_favorite;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean is_favorite() {
        return is_favorite;
    }

    public void setIs_favorite(boolean is_favorite) {
        this.is_favorite = is_favorite;
    }
}
