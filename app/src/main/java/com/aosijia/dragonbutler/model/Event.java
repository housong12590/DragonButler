package com.aosijia.dragonbutler.model;

import java.util.List;

/**
 * Created by Administrator on 2016/1/5 0005.
 */
public class Event extends BaseResp {


    /**
     * forum_topic_id : 8374928143
     * title : 为偏远山区的孩子捐献冬衣
     * content : 冬日临近，南京市政公用事业管理局将联合泉州市公园管理中心等，在市区雨花台公园东大门举办“以孩子的名义·衣暖童心”志愿服务活动，面向社会各界爱心人士，为云南、贵州等偏远贫困山区儿童募集冬衣。

     　　活动现场接受捐赠洗涤干净无破损，适合0~12岁孩子穿着的棉衣、外套(捐赠者可在捐物中放置爱心卡，快乐结缘)。

     　　每捐赠一件符合需求的冬衣，活动方将匹配捐赠10元人民币，用于支付二手冬衣的储运和必要的清洗费用。市民捐赠冬衣后，在登记表上登记，将获得志愿服务手册一本、爱心捐赠证书一份及面值60元DIY的蛋挞券一张。

     　　同时，为主办方还将开展“公益童子军”招募及评选，现场募集若干名儿童参与衣物分拣及打包整理等工作。
     * author : {"user_id":"9875323","nickname":"三板斧","avatar_url":"http://avatar.xxx.com/public/129832193821.jpg"}
     * comments_count : 67
     * type : 2
     * created_at : 1449908478
     * is_favorite : true
     * extra : {"pic_urls":["http://pics.xxx.com/public/13612831234e8.jpg","http://pics.xxx.com/public/1361283123ew4.jpg"],"participants_count":2,"start_date":"1449908478","end_date":"1449928478","joined":true,"participants":[{"user_id":"1238717821","nickname":"如果爱","avatar_url":"http://avatar.xxx.com/public/219021791.jpg"},{"user_id":"1238717812","nickname":"离恨天","avatar_url":"http://avatar.xxx.com/public/219021719.jpg"}]}
     */

    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        private String forum_topic_id;
        private String title;
        private String content;
        /**
         * user_id : 9875323
         * nickname : 三板斧
         * avatar_url : http://avatar.xxx.com/public/129832193821.jpg
         */

        private AuthorEntity author;
        private int comments_count;
        private String type;
        private String created_at;
        private boolean is_favorite;
        /**
         * pic_urls : ["http://pics.xxx.com/public/13612831234e8.jpg","http://pics.xxx.com/public/1361283123ew4.jpg"]
         * participants_count : 2
         * start_date : 1449908478
         * end_date : 1449928478
         * joined : true
         * participants : [{"user_id":"1238717821","nickname":"如果爱","avatar_url":"http://avatar.xxx.com/public/219021791.jpg"},{"user_id":"1238717812","nickname":"离恨天","avatar_url":"http://avatar.xxx.com/public/219021719.jpg"}]
         */

        private ExtraEntity extra;

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

        public void setIs_favorite(boolean is_favorite) {
            this.is_favorite = is_favorite;
        }

        public void setExtra(ExtraEntity extra) {
            this.extra = extra;
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

        public boolean isIs_favorite() {
            return is_favorite;
        }

        public ExtraEntity getExtra() {
            return extra;
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

        public static class ExtraEntity {
            private int participants_count;
            private String start_date;
            private String end_date;
            private boolean joined;
            private List<String> pic_urls;
            /**
             * user_id : 1238717821
             * nickname : 如果爱
             * avatar_url : http://avatar.xxx.com/public/219021791.jpg
             */

            private List<ParticipantsEntity> participants;

            public void setParticipants_count(int participants_count) {
                this.participants_count = participants_count;
            }

            public void setStart_date(String start_date) {
                this.start_date = start_date;
            }

            public void setEnd_date(String end_date) {
                this.end_date = end_date;
            }

            public void setJoined(boolean joined) {
                this.joined = joined;
            }

            public void setPic_urls(List<String> pic_urls) {
                this.pic_urls = pic_urls;
            }

            public void setParticipants(List<ParticipantsEntity> participants) {
                this.participants = participants;
            }

            public int getParticipants_count() {
                return participants_count;
            }

            public String getStart_date() {
                return start_date;
            }

            public String getEnd_date() {
                return end_date;
            }

            public boolean isJoined() {
                return joined;
            }

            public List<String> getPic_urls() {
                return pic_urls;
            }

            public List<ParticipantsEntity> getParticipants() {
                return participants;
            }

            public static class ParticipantsEntity {
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
