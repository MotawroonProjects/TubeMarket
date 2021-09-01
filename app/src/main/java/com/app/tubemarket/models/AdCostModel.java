package com.app.tubemarket.models;

import java.io.Serializable;

public class AdCostModel implements Serializable {
    private String id;
    private String seconds;
    private String cost;
    private String is_shown;


    public String getId() {
        return id;
    }

    public String getSeconds() {
        return seconds;
    }

    public String getCost() {
        return cost;
    }

    public String getIs_shown() {
        return is_shown;
    }
}
