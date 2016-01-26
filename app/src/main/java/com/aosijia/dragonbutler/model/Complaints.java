package com.aosijia.dragonbutler.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *  物业投诉列表
 */
public class Complaints extends BaseResp {


    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity implements Serializable{
        /**
         * complaint_id : 1432143214
         * content : 编号8743的保安态度恶劣
         * status : 1
         * created_at : 1449908478
         * pic_urls : ["http://pics.xxx.com/public/8erquiq3234.jpg","http://pics.xxx.com/public/6wer6hdsqw2.jpg"]
         */

        private List<ComplaintsEntity> complaints;

        public void setComplaints(List<ComplaintsEntity> complaints) {
            this.complaints = complaints;
        }

        public List<ComplaintsEntity> getComplaints() {
            return complaints;
        }

        public static class ComplaintsEntity implements Serializable{
            private String complaint_id;
            private String content;
            private String status;
            private String created_at;
            private List<String> pic_urls;

            public void setComplaint_id(String complaint_id) {
                this.complaint_id = complaint_id;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public void setPic_urls(List<String> pic_urls) {
                this.pic_urls = pic_urls;
            }

            public String getComplaint_id() {
                return complaint_id;
            }

            public String getContent() {
                return content;
            }

            public String getStatus() {
                return status;
            }

            public String getCreated_at() {
                return created_at;
            }

            public List<String> getPic_urls() {
                if(pic_urls == null){
                    pic_urls = new ArrayList<>();
                }
                return pic_urls;
            }
        }
    }
}
