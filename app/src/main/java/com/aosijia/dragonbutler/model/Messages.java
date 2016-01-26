package com.aosijia.dragonbutler.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglj on 16/1/7.
 */
public class Messages extends BaseResp{


    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * messege_id : 132973
         * type : 1
         * created_at : 1440123459
         * content : 您好，我想问一下，您发起的那个活动已经关闭了，我还能参加吗？
         */

        private List<MessagesEntity> messages;

        public void setMessages(List<MessagesEntity> messages) {
            this.messages = messages;
        }

        public List<MessagesEntity> getMessages() {
            if(messages == null){
                messages = new ArrayList<>();
            }
            return messages;
        }

        public static class MessagesEntity {
            private String messege_id;
            private String type;
            private String created_at;
            private String content;

            public void setMessege_id(String messege_id) {
                this.messege_id = messege_id;
            }

            public void setType(String type) {
                this.type = type;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getMessege_id() {
                return messege_id;
            }

            public String getType() {
                return type;
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
