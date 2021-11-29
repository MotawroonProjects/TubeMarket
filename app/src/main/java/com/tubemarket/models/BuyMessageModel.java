package com.tubemarket.models;

import java.io.Serializable;

public class BuyMessageModel implements Serializable {
    private String id;
    private String messages_count;
    private String cost;
    private String type;
    private String contents;
    private String is_shown;

    public String getId() {
        return id;
    }

    public String getMessages_count() {
        return messages_count;
    }

    public String getCost() {
        return cost;
    }

    public String getType() {
        return type;
    }

    public String getContents() {
        return contents;
    }

    public String getIs_shown() {
        return is_shown;
    }
}
