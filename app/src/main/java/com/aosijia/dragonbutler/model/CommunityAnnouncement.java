package com.aosijia.dragonbutler.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglj on 15/12/17.
 */
public class CommunityAnnouncement extends BaseResp {


    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * community_announcement_id : 877661223
         * type : 2
         * title : 停水通知
         * content : 冬季即将来临，为保证业主冬季正常用水，本小区将对小区供水管道进行检修，将于2016年1月5日8：00-15：00停水，请各位业主提前做好准备。给您带来不便，敬请谅解！
         * pic_urls : ["http://pics.xxx.com/public/12047312.jpg","http://pics.xxx.com/public/12044772.jpg"]
         * created_at : 2016-01-04
         */

        private List<CommunityAnnouncementsEntity> community_announcements;

        public void setCommunity_announcements(List<CommunityAnnouncementsEntity> community_announcements) {
            this.community_announcements = community_announcements;
        }

        public List<CommunityAnnouncementsEntity> getCommunity_announcements() {
            if(community_announcements == null){
                community_announcements = new ArrayList<>();
            }
            return community_announcements;
        }

        public static class CommunityAnnouncementsEntity implements Serializable{
            private String community_announcement_id;
            private String type;
            private String title;
            private String content;
            private String created_at;
            private List<String> pic_urls;

            public void setCommunity_announcement_id(String community_announcement_id) {
                this.community_announcement_id = community_announcement_id;
            }

            public void setType(String type) {
                this.type = type;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public void setPic_urls(List<String> pic_urls) {
                this.pic_urls = pic_urls;
            }

            public String getCommunity_announcement_id() {
                return community_announcement_id;
            }

            public String getType() {
                return type;
            }

            public String getTitle() {
                return title;
            }

            public String getContent() {
                return content;
            }

            public String getCreated_at() {
                return created_at;
            }

            public List<String> getPic_urls() {
                return pic_urls;
            }
        }
    }
}
