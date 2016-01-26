package com.aosijia.dragonbutler.model;

/**
 * Created by Administrator on 2016/1/12 0012.
 */
public class Version extends BaseResp {

    /**
     * version_code : 6352
     * version_name : V1.1-b1204
     * update_url : http://download.aosijia.com/public/DragonButler219218.apk
     * is_required : false
     * description : 1.修复了大图上传闪退的bug
     */

    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        private int version_code;
        private String version_name;
        private String update_url;
        private boolean is_required;
        private String description;

        public void setVersion_code(int version_code) {
            this.version_code = version_code;
        }

        public void setVersion_name(String version_name) {
            this.version_name = version_name;
        }

        public void setUpdate_url(String update_url) {
            this.update_url = update_url;
        }

        public void setIs_required(boolean is_required) {
            this.is_required = is_required;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getVersion_code() {
            return version_code;
        }

        public String getVersion_name() {
            return version_name;
        }

        public String getUpdate_url() {
            return update_url;
        }

        public boolean isIs_required() {
            return is_required;
        }

        public String getDescription() {
            return description;
        }
    }
}
