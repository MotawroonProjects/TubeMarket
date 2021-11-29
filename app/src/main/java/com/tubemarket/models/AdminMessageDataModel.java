package com.tubemarket.models;

import java.io.Serializable;
import java.util.List;

public class AdminMessageDataModel extends StatusResponse implements Serializable {
    public AdminRoomModel room;
    public List<AdminMessageModel> data;
    public int current_page;

    public AdminRoomModel getRoom() {
        return room;
    }

    public List<AdminMessageModel> getData() {
        return data;
    }

    public int getCurrent_page() {
        return current_page;
    }
}
