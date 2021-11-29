package com.tubemarket.models;

import java.io.Serializable;

public class MyVideosModel implements Serializable {
    private String id;
    private String type;
    private String user_id;
    private String link;
    private String status;
    private String timer_limit;
    private String view_limit;
    private String like_limit;
    private String subscription_limit;
    private String view_count;
    private String like_count;
    private String subscription_count;
    private String have_discount;
    private String discount_coins;
    private String profit_coins;
    private String campaign_coins;
    private LoginRegisterModel.Data user_fk;
    private OperationModel user_operations_fk;

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getLink() {
        return link;
    }

    public String getStatus() {
        return status;
    }

    public String getTimer_limit() {
        return timer_limit;
    }

    public String getView_limit() {
        return view_limit;
    }

    public String getLike_limit() {
        return like_limit;
    }

    public String getSubscription_limit() {
        return subscription_limit;
    }

    public String getView_count() {
        return view_count;
    }

    public String getLike_count() {
        return like_count;
    }

    public String getSubscription_count() {
        return subscription_count;
    }

    public String getHave_discount() {
        return have_discount;
    }

    public String getDiscount_coins() {
        return discount_coins;
    }

    public String getProfit_coins() {
        return profit_coins;
    }

    public String getCampaign_coins() {
        return campaign_coins;
    }

    public LoginRegisterModel.Data getUser_fk() {
        return user_fk;
    }

    public OperationModel getUser_operations_fk() {
        return user_operations_fk;
    }

}
