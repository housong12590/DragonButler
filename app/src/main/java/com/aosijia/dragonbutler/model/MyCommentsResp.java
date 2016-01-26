package com.aosijia.dragonbutler.model;

import java.util.List;

/**
 * Created by Jacky on 2016/1/4.
 * Version 1.0
 */
public class MyCommentsResp extends BaseResp {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private List<Comment> forum_comments;

        public List<Comment> getForum_comments() {
            return forum_comments;
        }

        public void setForum_comments(List<Comment> forum_comments) {
            this.forum_comments = forum_comments;
        }
    }


    public static class Comment {

        private String comment_id;
        private String content;
        private String created_at;
        private String topic_type;
        private String topic_id;
        private String topic_title;
        private OtherSide other_side;

        public String getComment_id() {
            return comment_id;
        }

        public void setComment_id(String comment_id) {
            this.comment_id = comment_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getTopic_type() {
            return topic_type;
        }

        public void setTopic_type(String topic_type) {
            this.topic_type = topic_type;
        }

        public String getTopic_id() {
            return topic_id;
        }

        public void setTopic_id(String topic_id) {
            this.topic_id = topic_id;
        }

        public String getTopic_title() {
            return topic_title;
        }

        public void setTopic_title(String topic_title) {
            this.topic_title = topic_title;
        }

        public OtherSide getOther_side() {
            return other_side;
        }

        public void setOther_side(OtherSide other_side) {
            this.other_side = other_side;
        }
    }

    public static class OtherSide{
        private String user_id;
        private String nickname;
        private String avatar_url;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }
    }

}
