package com.app.tubemarket.models;

import java.io.Serializable;
import java.util.List;

public class UserMessagesDataModel extends StatusResponse implements Serializable {
   private List<UserMessageModel> data;

    public List<UserMessageModel> getData() {
        return data;
    }
}
