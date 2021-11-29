package com.tubemarket.models;

import java.io.Serializable;

public class MessageResponseModel extends StatusResponse implements Serializable {

    private Data data;
    private String pay_link;
    public Data getData() {
        return data;
    }

    public String getPay_link() {
        return pay_link;
    }

    public static class Data implements Serializable{
        private String id;
        private String user_id;
        private String buy_message_id;
        private String status;
        private String link;
        private String contents;
        private String coupon_code;
        private String expiration_coupon;
        private String coins;
        private String type;
        private String count_of_users;

        public String getId() {
            return id;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getBuy_message_id() {
            return buy_message_id;
        }

        public String getStatus() {
            return status;
        }

        public String getLink() {
            return link;
        }

        public String getContents() {
            return contents;
        }

        public String getCoupon_code() {
            return coupon_code;
        }

        public String getExpiration_coupon() {
            return expiration_coupon;
        }

        public String getCoins() {
            return coins;
        }

        public String getType() {
            return type;
        }

        public String getCount_of_users() {
            return count_of_users;
        }
    }
}
