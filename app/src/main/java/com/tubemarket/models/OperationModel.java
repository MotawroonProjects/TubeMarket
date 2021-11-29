package com.tubemarket.models;

import java.io.Serializable;

public class OperationModel implements Serializable {
    private String id;
    private String exchange_id;
    private String campaign_id;
    private String user_id;
    private String coupon_user_id;
    private String coins;
    private String seconds;
    private String type;
    private String operation_type;
    private String created_at;
    private String updated_at;
    private UserFkModel user_fk;

    public String getId() {
        return id;
    }

    public String getExchange_id() {
        return exchange_id;
    }

    public String getCampaign_id() {
        return campaign_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getCoupon_user_id() {
        return coupon_user_id;
    }

    public String getCoins() {
        return coins;
    }

    public String getSeconds() {
        return seconds;
    }

    public String getType() {
        return type;
    }

    public String getOperation_type() {
        return operation_type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public UserFkModel getUser_fk() {
        return user_fk;
    }
}
