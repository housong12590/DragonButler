package com.aosijia.dragonbutler.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *  帖子详情
 */
public class Forum extends BaseResp {


    /**
     * forum_topic_id : 8374929143
     * title : 对网络恶意诽谤的声音必须封杀
     * content : 15日下午，江苏省公安厅网警总队新浪官方微博“@江苏网警”通报，对于部分网民恶意侮辱南京大屠杀死难者事件，警方正协调有关单位对相关网友账号实施永久封号。
     * author : {"user_id":"768739","nickname":"双飞燕","avatar_url":"http://avatar.xxx.com/public/978234958.jpg"}
     * comments_count : 28
     * type : 1
     * created_at : 1449908478
     * extra : {"pic_urls":["http://pics.xxx.com/public/13612831234we.jpg","http://pics.xxx.com/public/13612831235we.jpg"]}
     * is_favorite : false
     */

    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity implements Serializable{
        private String forum_topic_id;
        private String title;
        private String content;
        /**
         * user_id : 768739
         * nickname : 双飞燕
         * avatar_url : http://avatar.xxx.com/public/978234958.jpg
         */

        private AuthorEntity author;
        private int comments_count;
        private String type;
        private String created_at;
        private ExtraEntity extra;
        private boolean is_favorite;

        public void setForum_topic_id(String forum_topic_id) {
            this.forum_topic_id = forum_topic_id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setAuthor(AuthorEntity author) {
            this.author = author;
        }

        public void setComments_count(int comments_count) {
            this.comments_count = comments_count;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public void setExtra(ExtraEntity extra) {
            this.extra = extra;
        }

        public void setIs_favorite(boolean is_favorite) {
            this.is_favorite = is_favorite;
        }

        public String getForum_topic_id() {
            return forum_topic_id;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public AuthorEntity getAuthor() {
            return author;
        }

        public int getComments_count() {
            return comments_count;
        }

        public String getType() {
            return type;
        }

        public String getCreated_at() {
            return created_at;
        }

        public ExtraEntity getExtra() {
            return extra;
        }

        public boolean isIs_favorite() {
            return is_favorite;
        }

        public static class AuthorEntity implements Serializable{
            private String user_id;
            private String nickname;
            private String avatar_url;

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public void setAvatar_url(String avatar_url) {
                this.avatar_url = avatar_url;
            }

            public String getUser_id() {
                return user_id;
            }

            public String getNickname() {
                return nickname;
            }

            public String getAvatar_url() {
                return avatar_url;
            }
        }

        public static class ExtraEntity implements Serializable{
            private List<String> pic_urls;

            public void setPic_urls(List<String> pic_urls) {
                this.pic_urls = pic_urls;
            }

            public List<String> getPic_urls() {
                if(pic_urls == null){
                    pic_urls = new ArrayList<>();
                }
                return pic_urls;
            }
        }
    }
}
