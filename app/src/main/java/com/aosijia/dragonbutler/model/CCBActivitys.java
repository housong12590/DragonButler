package com.aosijia.dragonbutler.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author hs
 *         <p/>
 *         2016/1/18 0018 10:34
 */
public class CCBActivitys extends BaseResp {


    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * ccb_activity_id : 104
         * title : 龙卡信用卡赫兹租车最高75折优惠！
         * theme_pic_url : http://creditcard.ccb.com/upload/pic/Favorable/20161817854325739.jpg
         * activity_target : 龙卡信用卡持卡人
         * start_date : 1452211200
         * end_date : 1454198400
         * content : 活动期间，刷龙卡信用卡在赫兹全球参与活动的门店租车3天或以上，且最长租期不超过14天，即可享受最高达75折优惠！
         预订有效期：即日起至2016年1月31日
         取车有效期：即日起至2016年3月31日
         优惠代码：CDP#816220
         */

        private List<CcbActivitiesEntity> ccb_activities;

        public void setCcb_activities(List<CcbActivitiesEntity> ccb_activities) {
            this.ccb_activities = ccb_activities;
        }

        public List<CcbActivitiesEntity> getCcb_activities() {
            return ccb_activities;
        }

        public static class CcbActivitiesEntity implements Serializable {
            private String ccb_activity_id;
            private String title;
            private String theme_pic_url;
            private String activity_target;
            private String start_date;
            private String end_date;
            private String content;

            public void setCcb_activity_id(String ccb_activity_id) {
                this.ccb_activity_id = ccb_activity_id;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setTheme_pic_url(String theme_pic_url) {
                this.theme_pic_url = theme_pic_url;
            }

            public void setActivity_target(String activity_target) {
                this.activity_target = activity_target;
            }

            public void setStart_date(String start_date) {
                this.start_date = start_date;
            }

            public void setEnd_date(String end_date) {
                this.end_date = end_date;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getCcb_activity_id() {
                return ccb_activity_id;
            }

            public String getTitle() {
                return title;
            }

            public String getTheme_pic_url() {
                return theme_pic_url;
            }

            public String getActivity_target() {
                return activity_target;
            }

            public String getStart_date() {
                return start_date;
            }

            public String getEnd_date() {
                return end_date;
            }

            public String getContent() {
                return content;
            }
        }
    }
}
