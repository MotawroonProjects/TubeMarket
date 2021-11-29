package com.tubemarket.models;

import java.io.Serializable;

public class WithdrawModel implements Serializable {
    private String id;
    private String title;
    private String coins;
    private String cost;
    private String is_shown;
    private String type;
    private String created_at;
    private String updated_at;
    private String full_image_url;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCoins() {
        return coins;
    }

    public String getCost() {
        return cost;
    }

    public String getIs_shown() {
        return is_shown;
    }

    public String getType() {
        return type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getFull_image_url() {
        return full_image_url;
    }
}
