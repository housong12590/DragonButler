package com.aosijia.dragonbutler.model;

import java.io.Serializable;

/**
 * Created by wanglj on 15/12/30.
 */
public class LoginResp extends BaseResp implements Serializable{


    /**
     * user_id : 12
     * access_token : 923ew8uiweur923eu773ucnv
     * user_info : {"mobile":"13988881234","nickname":"hellokitty","gender":"0","avatar_url":"http://www.xxx.com/563ad8b6be488a07a694.jpg","is_bound":true,"household":{"community_id":"86682341","community_title":"育才高教小区","household_id":"94389347234","unit":"阳光广场东8栋","room":"504"}}
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
         * mobile : 13988881234
         * nickname : hellokitty
         * gender : 0
         * avatar_url : http://www.xxx.com/563ad8b6be488a07a694.jpg
         * is_bound : true
         * household : {"community_id":"86682341","community_title":"育才高教小区","household_id":"94389347234","unit":"阳光广场东8栋","room":"504"}
         */

        private UserInfoEntity user_info;


        public void setUser_info(UserInfoEntity user_info) {
            this.user_info = user_info;
        }

        public String getUser_id() {
            return getUser_info().getUser_id();
        }

        public String getAccess_token() {
            return getUser_info().getAccess_token();
        }



        public UserInfoEntity getUser_info() {
            return user_info;
        }

        public static class UserInfoEntity implements Serializable{
            private String user_id;
            private String access_token;

            private String mobile;
            private String nickname;
            private String gender;
            private String avatar_url;
            private boolean is_bound;
            /**
             * community_id : 86682341
             * community_title : 育才高教小区
             * household_id : 94389347234
             * unit : 阳光广场东8栋
             * room : 504
             */

            private HouseholdEntity household;


            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public void setAccess_token(String access_token) {
                this.access_token = access_token;
            }
            public String getUser_id() {
                return user_id;
            }

            public String getAccess_token() {
                return access_token;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public void setAvatar_url(String avatar_url) {
                this.avatar_url = avatar_url;
            }

            public void setIs_bound(boolean is_bound) {
                this.is_bound = is_bound;
            }

            public void setHousehold(HouseholdEntity household) {
                this.household = household;
            }

            public String getMobile() {
                return mobile;
            }

            public String getNickname() {
                return nickname;
            }

            public String getGender() {
                return gender;
            }

            public String getAvatar_url() {
                return avatar_url;
            }

            public boolean isIs_bound() {
                return is_bound;
            }

            public HouseholdEntity getHousehold() {
                return household;
            }

            public static class HouseholdEntity implements Serializable{
                private String community_id;
                private String community_title;
                private String household_id;
                private String unit;
                private String room;
                private String city_code;

                public String getCity_code() {
                    return city_code;
                }

                public void setCity_code(String city_code) {
                    this.city_code = city_code;
                }

                public void setCommunity_id(String community_id) {
                    this.community_id = community_id;
                }

                public void setCommunity_title(String community_title) {
                    this.community_title = community_title;
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

                public String getCommunity_title() {
                    return community_title;
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
}
