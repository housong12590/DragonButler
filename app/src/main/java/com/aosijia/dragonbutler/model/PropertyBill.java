package com.aosijia.dragonbutler.model;

import java.util.List;

/**
 * Created by Jacky on 2015/12/21.
 * Version 1.0
 */
public class PropertyBill extends  BaseResp{


    private DataEntity data;

    public void setData(DataEntity data) {
        this.data = data;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * bill_id : 41319375
         * type : 1
         * title : 2015年第二季度物业费
         * amount : 45.20
         * status : 1
         * created_at : 2015-07-15
         */

        private List<PropertyBillsEntity> property_bills;

        public void setProperty_bills(List<PropertyBillsEntity> property_bills) {
            this.property_bills = property_bills;
        }

        public List<PropertyBillsEntity> getProperty_bills() {
            return property_bills;
        }

        public static class PropertyBillsEntity {
            private String bill_id;
            private String type;
            private String title;
            private String amount;
            private String status;
            private String created_at;

            public void setBill_id(String bill_id) {
                this.bill_id = bill_id;
            }

            public void setType(String type) {
                this.type = type;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getBill_id() {
                return bill_id;
            }

            public String getType() {
                return type;
            }

            public String getTitle() {
                return title;
            }

            public String getAmount() {
                return amount;
            }

            public String getStatus() {
                return status;
            }

            public String getCreated_at() {
                return created_at;
            }
        }
    }
}
