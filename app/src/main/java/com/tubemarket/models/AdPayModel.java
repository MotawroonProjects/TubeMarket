package com.tubemarket.models;

import java.io.Serializable;

public class AdPayModel extends StatusResponse implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data implements Serializable{
        private String pay_link;
        private String pay_link_success;
        private String pay_link_fail;

        public String getPay_link() {
            return pay_link;
        }

        public String getPay_link_success() {
            return pay_link_success;
        }

        public String getPay_link_fail() {
            return pay_link_fail;
        }
    }
}
