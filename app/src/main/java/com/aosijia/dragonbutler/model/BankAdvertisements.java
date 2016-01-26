package com.aosijia.dragonbutler.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglj on 15/12/25.
 */
public class BankAdvertisements extends BaseResp{

    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * created_at : 1449908478
         * theme_pic_url : http://pic.xxx.com/public/1449908478.jpg
         * content : 龙管家金融社区App上线，注册有好礼赠送。
         * title : 龙管家金融社区App上线啦！
         * pic_urls : ["http://pics.xxx.com/public/13612831234e8.jpg","http://pics.xxx.com/public/1361283123ew4.jpg"]
         */

        private List<BankAdvertisementsEntity> bank_advertisements;

        public void setBank_advertisements(List<BankAdvertisementsEntity> bank_advertisements) {
            this.bank_advertisements = bank_advertisements;
        }

        public List<BankAdvertisementsEntity> getBank_advertisements() {
            if(bank_advertisements == null){
                bank_advertisements = new ArrayList<>();
            }
            return bank_advertisements;
        }

        public static class BankAdvertisementsEntity implements Serializable{
            private String created_at;
            private String theme_pic_url;
            private String content;
            private String title;
            private List<String> pic_urls;

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public void setTheme_pic_url(String theme_pic_url) {
                this.theme_pic_url = theme_pic_url;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setPic_urls(List<String> pic_urls) {
                this.pic_urls = pic_urls;
            }

            public String getCreated_at() {
                return created_at;
            }

            public String getTheme_pic_url() {
                return theme_pic_url;
            }

            public String getContent() {
                return content;
            }

            public String getTitle() {
                return title;
            }

            public List<String> getPic_urls() {
                return pic_urls;
            }
        }
    }
}
