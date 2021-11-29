package com.tubemarket.models;

import java.io.Serializable;

public class GoldCostModel implements Serializable {
    private String id;
    private String days;
    private String cost;
    private String is_shown;
    private String created_at;
    private String updated_at;

    public String getId() {
        return id;
    }

    public String getDays() {
        return days;
    }

    public String getCost() {
        return cost;
    }

    public String getIs_shown() {
        return is_shown;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
