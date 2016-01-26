package com.aosijia.dragonbutler.model;

import java.util.List;

/**
 * 邻里投票详情
 * Created by Jacky on 2015/12/28.
 * Version 1.0
 */
public class ForumEventDetailsResp extends BaseResp {

    private ForumEvent data;

    public ForumEvent getData() {
        return data;
    }

    public void setData(ForumEvent data) {
        this.data = data;
    }

    public class ForumEvent extends BaseForum {
        private EventExtra extra;

        public class EventExtra {
            private String[] pic_urls;
            private String participants_count;
            private String start_date;
            private String end_date;
            private boolean joined;
            private List<Participant> participants;

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

            public boolean isJoined() {
                return joined;
            }

            public void setJoined(boolean joined) {
                this.joined = joined;
            }

            public List<Participant> getParticipants() {
                return participants;
            }

            public void setParticipants(List<Participant> participants) {
                this.participants = participants;
            }
        }

        public class Participant {
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
}
