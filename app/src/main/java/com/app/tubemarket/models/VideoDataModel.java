package com.app.tubemarket.models;

import java.io.Serializable;
import java.util.List;

public class VideoDataModel extends StatusResponse implements Serializable {
    private DataModel data;

    public DataModel getData() {
        return data;
    }

    public static class DataModel implements Serializable{
        private int current_page;
        private List<MyVideosModel> data;
        public List<MyVideosModel> getData() {
            return data;
        }

        public int getCurrent_page() {
            return current_page;
        }
    }


}
