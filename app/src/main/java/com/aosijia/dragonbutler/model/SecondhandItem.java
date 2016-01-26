package com.aosijia.dragonbutler.model;

import java.io.Serializable;
import java.util.List;

/**
 * 二手商品详情
 */
public class SecondhandItem extends BaseResp implements Serializable{

    /**
     * author : {"user_id":"27631831","nickname":"玉树临风","avatar_url":"http://avatar.xxx.com/public/werw83294.jpg"}
     * secondhand_item_id : 213971837
     * created_at : 1482398847
     * title : 湖笔一枝
     * content : 现有祖传狼毫一枝
     * type : 1
     * price : 450.00
     * mobile : 18028378331
     * is_favorite : true
     * status : 1
     * pic_urls : ["http://secondhand.xxx.com/public/item1.jpg","http://secondhand.xxx.com/public/item2.jpg"]
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
         * user_id : 27631831
         * nickname : 玉树临风
         * avatar_url : http://avatar.xxx.com/public/werw83294.jpg
         */

        private AuthorEntity author;
        private String secondhand_item_id;
        private String created_at;
        private String title;
        private String content;
        private String type;
        private String price;
        private String mobile;
        private boolean is_favorite;
        private String status;
        private String comments_count;
        private List<String> pic_urls;


        public void setAuthor(AuthorEntity author) {
            this.author = author;
        }

        public void setSecondhand_item_id(String secondhand_item_id) {
            this.secondhand_item_id = secondhand_item_id;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public void setComments_count (String comments_count){
            this.comments_count = comments_count;
        }

        public String getComments_count (){
            return comments_count;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public void setIs_favorite(boolean is_favorite) {
            this.is_favorite = is_favorite;
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

        public String getCreated_at() {
            return created_at;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public String getType() {
            return type;
        }

        public String getPrice() {
            return price;
        }

        public String getMobile() {
            return mobile;
        }

        public boolean isIs_favorite() {
            return is_favorite;
        }

        public String getStatus() {
            return status;
        }

        public List<String> getPic_urls() {
            return pic_urls;
        }

        public static class AuthorEntity implements Serializable {
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
