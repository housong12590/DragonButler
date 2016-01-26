package com.aosijia.dragonbutler.model;

import java.io.Serializable;

/**
 * Created by wanglj on 15/12/15.
 */
public class HouseHold extends BaseResp{


    /**
     * household : {"community_id":"86682341","household_id":"94389347234","unit":"阳光广场东8栋","room":"504"}
     */

    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity implements Serializable{
        /**
         * community_id : 86682341
         * household_id : 94389347234
         * unit : 阳光广场东8栋
         * room : 504
         */

        private HouseholdEntity household;

        public void setHousehold(HouseholdEntity household) {
            this.household = household;
        }

        public HouseholdEntity getHousehold() {
            return household;
        }

        public static class HouseholdEntity implements Serializable{
            private String community_id;
            private String household_id;
            private String community_title;
            private String unit;
            private String room;

            public String getCommunity_title() {
                return community_title;
            }

            public void setCommunity_title(String community_title) {
                this.community_title = community_title;
            }

            public void setCommunity_id(String community_id) {
                this.community_id = community_id;
            }

            public void setHousehold_id(String household_id) {
                this.household_id = household_id;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }

            public void setRoom(String room) {
                this.room = room;
            }

            public String getCommunity_id() {
                return community_id;
            }

            public String getHousehold_id() {
                return household_id;
            }

            public String getUnit() {
                return unit;
            }

            public String getRoom() {
                return room;
            }
        }
    }
}
