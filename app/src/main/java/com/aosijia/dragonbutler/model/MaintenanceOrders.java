package com.aosijia.dragonbutler.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 物业报修列表
 */
public class MaintenanceOrders extends BaseResp {


    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * maintenance_order_id : 1432143214 报修单ID
         * type : 1  报修类型
         * content : 自来水管入户处的管道部分有渗水。 报修内容
         * status : 1 报修状态
         * created_at : 1449908478 报修时间戳
         * pic_urls : 图片URL ["http://pics.xxx.com/public/8erquiq3234.jpg","http://pics.xxx.com/public/6wer6hdsq2.jpg"]
         */

        private List<MaintenaceOrdersEntity> maintenance_orders;

        public void setMaintenance_orders(List<MaintenaceOrdersEntity> maintenance_orders) {
            this.maintenance_orders = maintenance_orders;
        }

        public List<MaintenaceOrdersEntity> getMaintenance_orders() {
            if(maintenance_orders == null){
                maintenance_orders = new ArrayList<>();
            }
            return maintenance_orders;
        }

        public static class MaintenaceOrdersEntity implements Serializable{
            private String maintenance_order_id;
            private String type;
            private String content;
            private String status;
            private String created_at;
            private List<String> pic_urls;

            public void setMaintenance_order_id(String maintenance_order_id) {
                this.maintenance_order_id = maintenance_order_id;
            }

            public void setType(String type) {
                this.type = type;
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

            public String getMaintenance_order_id() {
                return maintenance_order_id;
            }

            public String getType() {
                return type;
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
                return pic_urls;
            }
        }
    }
}
