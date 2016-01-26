package com.aosijia.dragonbutler.model;

import java.util.List;

/**
 * @author hs
 *         <p/>
 *         2016/1/14 0014 18:28
 */
public class YellowPages extends BaseResp{


    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * title : 报刊订阅
         * telephone : 13688888888
         */

        private List<YellowPagesEntity> yellow_pages;

        public void setYellow_pages(List<YellowPagesEntity> yellow_pages) {
            this.yellow_pages = yellow_pages;
        }

        public List<YellowPagesEntity> getYellow_pages() {
            return yellow_pages;
        }

        public static class YellowPagesEntity {
            private String title;
            private String telephone;

            public void setTitle(String title) {
                this.title = title;
            }

            public void setTelephone(String telephone) {
                this.telephone = telephone;
            }

            public String getTitle() {
                return title;
            }

            public String getTelephone() {
                return telephone;
            }
        }
    }
}
