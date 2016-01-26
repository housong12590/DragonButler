package com.aosijia.dragonbutler.model;

import java.util.List;

/**
 * Created by Administrator on 2016/1/7 0007.
 */
public class SecondhandComments extends BaseResp {

    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * secondhand_comment_id : 21321231
         * author : {"user_id":"787739","nickname":"四喜丸子","avatar_url":"http://avatar.xxx.com/public/129387129.jpg"}
         * content : 物超所值啊！
         * created_at : 1449908478
         * replies : [{"secondhand_comment_id":"21321232","author":{"user_id":"787739","nickname":"四喜丸子","avatar_url":"http://avatar.xxx.com/public/129387129.jpg"},"content":"那你倒是买呀！","created_at":"1449909478"},{"secondhand_comment_id":"21321232","author":{"user_id":"787739","nickname":"四喜丸子","avatar_url":"http://avatar.xxx.com/public/129387129.jpg"},"content":"不要发这些没有营养的内容！","created_at":"1449909478"}]
         */

        private List<SecondhandCommentsEntity> secondhand_comments;

        public void setSecondhand_comments(List<SecondhandCommentsEntity> secondhand_comments) {
            this.secondhand_comments = secondhand_comments;
        }

        public List<SecondhandCommentsEntity> getSecondhand_comments() {
            return secondhand_comments;
        }

        public static class SecondhandCommentsEntity{
            private String secondhand_comment_id;
            private AuthorEntity author;
            private String content;
            private String created_at;
            private List<RepliesEntity> replies;

            public void setSecondhand_comment_id(String secondhand_comment_id) {
                this.secondhand_comment_id = secondhand_comment_id;
            }

            public void setAuthor(AuthorEntity author) {
                this.author = author;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public void setReplies(List<RepliesEntity> replies) {
                this.replies = replies;
            }

            public String getSecondhand_comment_id() {
                return secondhand_comment_id;
            }

            public AuthorEntity getAuthor() {
                return author;
            }

            public String getContent() {
                return content;
            }

            public String getCreated_at() {
                return created_at;
            }

            public List<RepliesEntity> getReplies() {
                return replies;
            }

            public static class AuthorEntity {
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

            public static class RepliesEntity {
                private String secondhand_comment_id;
                /**
                 * user_id : 787739
                 * nickname : 四喜丸子
                 * avatar_url : http://avatar.xxx.com/public/129387129.jpg
                 */

                private AuthorEntity author;
                private String content;
                private String created_at;

                public void setSecondhand_comment_id(String secondhand_comment_id) {
                    this.secondhand_comment_id = secondhand_comment_id;
                }

                public void setAuthor(AuthorEntity author) {
                    this.author = author;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public void setCreated_at(String created_at) {
                    this.created_at = created_at;
                }

                public String getSecondhand_comment_id() {
                    return secondhand_comment_id;
                }

                public AuthorEntity getAuthor() {
                    return author;
                }

                public String getContent() {
                    return content;
                }

                public String getCreated_at() {
                    return created_at;
                }

                public static class AuthorEntity {
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
            }
        }
    }
}
