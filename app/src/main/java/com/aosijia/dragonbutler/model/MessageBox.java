package com.aosijia.dragonbutler.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglj on 16/1/7.
 */
public class MessageBox extends BaseResp{


    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * contact : {"user_id":"92137193","nickname":"葫芦娃","avatar_url":"http://avatar.xxx.com/public/312we981.jpg"}
         * unread_messages_count : 12
         * last_message_date : 1449908478
         * last_message_content : 谢谢您的咨询，我稍后联系您！
         */

        private List<MessageBoxEntity> message_box;

        public void setMessage_box(List<MessageBoxEntity> message_box) {
            this.message_box = message_box;
        }

        public List<MessageBoxEntity> getMessage_box() {
            if(message_box == null){
                message_box = new ArrayList<>();
            }
            return message_box;
        }

        public static class MessageBoxEntity{
            /**
             * user_id : 92137193
             * nickname : 葫芦娃
             * avatar_url : http://avatar.xxx.com/public/312we981.jpg
             */

            private ContactEntity contact;
            private int unread_messages_count;
            private String last_message_date;
            private String last_message_content;

            public void setContact(ContactEntity contact) {
                this.contact = contact;
            }

            public void setUnread_messages_count(int unread_messages_count) {
                this.unread_messages_count = unread_messages_count;
            }

            public void setLast_message_date(String last_message_date) {
                this.last_message_date = last_message_date;
            }

            public void setLast_message_content(String last_message_content) {
                this.last_message_content = last_message_content;
            }

            public ContactEntity getContact() {
                return contact;
            }

            public int getUnread_messages_count() {
                return unread_messages_count;
            }

            public String getLast_message_date() {
                return last_message_date;
            }

            public String getLast_message_content() {
                return last_message_content;
            }

            public static class ContactEntity{
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
