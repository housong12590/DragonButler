package com.aosijia.dragonbutler.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 二手列表
 * Created by wanglj on 15/12/29.
 */
public class SecondHandItems extends BaseResp{

    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * author : {"user_id":"12398432","nickname":"达达尼安","avatar_url":"http://avatar.xxx.com/public/2342909483.jpg"}
         * secondhand_item_id : 238743829
         * type : 1
         * created_at : 1449238935
         * title : 湖笔一枝
         * price : 350
         * pic_urls : ["http://pic.xxx.com/public/238ewr98wer.jpg","http://pic.xxx.com/public/238ewer2132.jpg"]
         * status : 1
         */

        private List<SecondhandItemsEntity> secondhand_items;

        public void setSecondhand_items(List<SecondhandItemsEntity> secondhand_items) {
            this.secondhand_items = secondhand_items;
        }

        public List<SecondhandItemsEntity> getSecondhand_items() {
            return secondhand_items;
        }

        public static class SecondhandItemsEntity {
            /**
             * user_id : 12398432
             * nickname : 达达尼安
             * avatar_url : http://avatar.xxx.com/public/2342909483.jpg
             */

            private AuthorEntity author;
            private String secondhand_item_id;
            private String type;
            private String created_at;
            private String title;
            private float price;
            private String status;
            private List<String> pic_urls;

            public void setAuthor(AuthorEntity author) {
                this.author = author;
            }

            public void setSecondhand_item_id(String secondhand_item_id) {
                this.secondhand_item_id = secondhand_item_id;
            }

            public void setType(String type) {
                this.type = type;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setPrice(float price) {
                this.price = price;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public void setPic_urls(List<String> pic_urls) {
                this.pic_urls = pic_urls;
            }

            public AuthorEntity getAuthor() {
                return author;
            }

            public String getSecondhand_item_id() {
                return secondhand_item_id;
            }

            public String getType() {
                return type;
            }

            public String getCreated_at() {
                return created_at;
            }

            public String getTitle() {
                return title;
            }

            public float getPrice() {
                return price;
            }

            public String getStatus() {
                return status;
            }

            public List<String> getPic_urls() {
                if(pic_urls == null){
                    pic_urls = new ArrayList<>();
                }
                return pic_urls;
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
        }
    }
}
