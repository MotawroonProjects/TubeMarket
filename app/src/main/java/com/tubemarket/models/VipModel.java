package com.tubemarket.models;

import java.io.Serializable;

public class VipModel implements Serializable {
    private String id;
    private String days;
    private String cost;
    private String is_shown;

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
}
