package com.aosijia.dragonbutler.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author : hs on 2016/1/18 0018 16:00
 */
public class ConvenienceStore extends BaseResp {


    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * cvs_item_id : 105
         * title : 五常大米稻花香米5KG
         * content : 正宗五常大米，保证2015年新米
         * price : 79.99
         * pic_urls : ["http://www.xxx.com/public/rice01.jpg","http://www.xxx.com/public/rice02.jpg"]
         * telephone : 13988888888
         * created_at : 1451606400
         */

        private List<CvsItemsEntity> cvs_items;

        public void setCvs_items(List<CvsItemsEntity> cvs_items) {
            this.cvs_items = cvs_items;
        }

        public List<CvsItemsEntity> getCvs_items() {
            return cvs_items;
        }

        public static class CvsItemsEntity implements Serializable {
            private String cvs_item_id;
            private String title;
            private String content;
            private double price;
            private String telephone;
            private String created_at;
            private List<String> pic_urls;

            public void setCvs_item_id(String cvs_item_id) {
                this.cvs_item_id = cvs_item_id;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public void setTelephone(String telephone) {
                this.telephone = telephone;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public void setPic_urls(List<String> pic_urls) {
                this.pic_urls = pic_urls;
            }

            public String getCvs_item_id() {
                return cvs_item_id;
            }

            public String getTitle() {
                return title;
            }

            public String getContent() {
                return content;
            }

            public double getPrice() {
                return price;
            }

            public String getTelephone() {
                return telephone;
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
