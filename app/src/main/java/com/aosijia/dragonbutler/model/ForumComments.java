package com.aosijia.dragonbutler.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 跟帖列表
 */
public class ForumComments extends BaseResp{

    /**
     * status_code : 200
     * msg : 获取评论列表成功
     * data : {"forum_comments":[{"forum_comment_id":"21321231","author":{"user_id":"787739","nickname":"四喜丸子","avatar_url":"http://avatar.xxx.com/public/129387129.jpg"},"content":"顶，支持，威武，有希望了！","created_at":"1449908478","replies":[{"forum_comment_id":"21321232","author":{"user_id":"787739","nickname":"四喜丸子","avatar_url":"http://avatar.xxx.com/public/129387129.jpg"},"content":"不要发这些没有营养的内容！","created_at":"1449909478"},{"forum_comment_id":"21321232","author":{"user_id":"787739","nickname":"四喜丸子","avatar_url":"http://avatar.xxx.com/public/129387129.jpg"},"content":"不要发这些没有营养的内容！","created_at":"1449909478","reply_of":"21321231"}]},{"forum_comment_id":"21321231","author":{"user_id":"787739","nickname":"四喜丸子","avatar_url":"http://avatar.xxx.com/public/193237891462139.jpg"},"content":"顶，支持，威武，有希望了！","created_at":"1449908478"}]}
     */

    private DataEntity data;


    public void setData(DataEntity data) {
        this.data = data;
    }


    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        @Override
        public String toString() {
            return "DataEntity{" +
                    "forum_comments=" + forum_comments +
                    '}';
        }

        /**
         * forum_comment_id : 21321231
         * author : {"user_id":"787739","nickname":"四喜丸子","avatar_url":"http://avatar.xxx.com/public/129387129.jpg"}
         * content : 顶，支持，威武，有希望了！
         * created_at : 1449908478
         * replies : [{"forum_comment_id":"21321232","author":{"user_id":"787739","nickname":"四喜丸子","avatar_url":"http://avatar.xxx.com/public/129387129.jpg"},"content":"不要发这些没有营养的内容！","created_at":"1449909478"},{"forum_comment_id":"21321232","author":{"user_id":"787739","nickname":"四喜丸子","avatar_url":"http://avatar.xxx.com/public/129387129.jpg"},"content":"不要发这些没有营养的内容！","created_at":"1449909478","reply_of":"21321231"}]
         */

        private List<ForumCommentsEntity> forum_comments;

        public void setForum_comments(List<ForumCommentsEntity> forum_comments) {
            this.forum_comments = forum_comments;
        }

        public List<ForumCommentsEntity> getForum_comments() {
            if(forum_comments == null){
                forum_comments = new ArrayList<>();
            }
            return forum_comments;
        }

        public static class ForumCommentsEntity {
            private String forum_comment_id;
            /**
             * user_id : 787739
             * nickname : 四喜丸子
             * avatar_url : http://avatar.xxx.com/public/129387129.jpg
             */

            private AuthorEntity author;
            private String content;
            private String created_at;
            /**
             * forum_comment_id : 21321232
             * author : {"user_id":"787739","nickname":"四喜丸子","avatar_url":"http://avatar.xxx.com/public/129387129.jpg"}
             * content : 不要发这些没有营养的内容！
             * created_at : 1449909478
             */

            private List<RepliesEntity> replies;

            public void setForum_comment_id(String forum_comment_id) {
                this.forum_comment_id = forum_comment_id;
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

            public String getForum_comment_id() {
                return forum_comment_id;
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
                if(replies == null){
                    replies = new ArrayList<>();
                }
                return replies;
            }

            @Override
            public String toString() {
                return "ForumCommentsEntity{" +
                        "forum_comment_id='" + forum_comment_id + '\'' +
                        ", author=" + author +
                        ", content='" + content + '\'' +
                        ", created_at='" + created_at + '\'' +
                        ", replies=" + replies +
                        '}';
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

                @Override
                public String toString() {
                    return "AuthorEntity{" +
                            "user_id='" + user_id + '\'' +
                            ", nickname='" + nickname + '\'' +
                            ", avatar_url='" + avatar_url + '\'' +
                            '}';
                }
            }

            public static class RepliesEntity {
                private String forum_comment_id;
                /**
                 * user_id : 787739
                 * nickname : 四喜丸子
                 * avatar_url : http://avatar.xxx.com/public/129387129.jpg
                 */

                private AuthorEntity author;
                private String content;
                private String created_at;

                public void setForum_comment_id(String forum_comment_id) {
                    this.forum_comment_id = forum_comment_id;
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

                public String getForum_comment_id() {
                    return forum_comment_id;
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

                @Override
                public String toString() {
                    return "RepliesEntity{" +
                            "forum_comment_id='" + forum_comment_id + '\'' +
                            ", author=" + author +
                            ", content='" + content + '\'' +
                            ", created_at='" + created_at + '\'' +
                            '}';
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

                    @Override
                    public String toString() {
                        return "AuthorEntity{" +
                                "user_id='" + user_id + '\'' +
                                ", nickname='" + nickname + '\'' +
                                ", avatar_url='" + avatar_url + '\'' +
                                '}';
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return "ForumComments{" +
                "status_code='" + getStatus_code() + '\'' +
                ", msg='" + getMsg() + '\'' +
                ", data=" + data +
                '}';
    }
}
