package com.app.tubemarket.models;

import java.io.Serializable;
import java.util.List;

public class SubscribeSecondsModel extends StatusResponse implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data implements Serializable{
        private List<String> list_of_subscriptions;
        private List<String> list_of_seconds;

        public List<String> getList_of_subscriptions() {
            return list_of_subscriptions;
        }

        public List<String> getList_of_seconds() {
            return list_of_seconds;
        }
    }

}
