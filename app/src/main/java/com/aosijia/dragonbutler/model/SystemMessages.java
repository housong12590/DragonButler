package com.aosijia.dragonbutler.model;

import java.util.List;

/**
 * @author hs
 *         <p/>
 *         2016/1/15 0015 15:13
 */
public class SystemMessages extends BaseResp {


    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * system_message_id : 123873481
         * created_at : 1451606400
         * content : 您有一个包裹在物管，请尽快来签收！
         */

        private String unread_system_messages_count;

        public String getUnread_system_messages_count() {
            return unread_system_messages_count;
        }

        public void setUnread_system_messages_count(String unread_system_messages_count) {
            this.unread_system_messages_count = unread_system_messages_count;
        }

        private List<SystemMessagesEntity> system_messages;

        public void setSystem_messages(List<SystemMessagesEntity> system_messages) {
            this.system_messages = system_messages;
        }

        public List<SystemMessagesEntity> getSystem_messages() {
            return system_messages;
        }

        public static class SystemMessagesEntity {
            private String system_message_id;
            private String created_at;
            private String content;

            public void setSystem_message_id(String system_message_id) {
                this.system_message_id = system_message_id;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getSystem_message_id() {
                return system_message_id;
            }

            public String getCreated_at() {
                return created_at;
            }

            public String getContent() {
                return content;
            }
        }
    }
}
