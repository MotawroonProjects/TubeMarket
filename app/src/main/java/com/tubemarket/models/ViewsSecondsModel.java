package com.tubemarket.models;

import java.io.Serializable;
import java.util.List;

public class ViewsSecondsModel extends StatusResponse implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public static class Data implements Serializable{
        private List<String> seconds;
        private List<String> video_view_numbers;

        public List<String> getSeconds() {
            return seconds;
        }

        public List<String> getVideo_view_numbers() {
            return video_view_numbers;
        }
    }

}
