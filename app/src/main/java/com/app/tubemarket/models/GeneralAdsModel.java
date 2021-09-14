package com.app.tubemarket.models;

import java.io.Serializable;

public class GeneralAdsModel implements Serializable {
    private String id;
    private String timer_limit;
    private String profit_coins;
    private String adType;

    public GeneralAdsModel(String id, String timer_limit, String profit_coins,String adType) {
        this.id = id;
        this.timer_limit = timer_limit;
        this.profit_coins = profit_coins;
        this.adType = adType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimer_limit() {
        return timer_limit;
    }

    public void setTimer_limit(String timer_limit) {
        this.timer_limit = timer_limit;
    }

    public String getProfit_coins() {
        return profit_coins;
    }

    public void setProfit_coins(String profit_coins) {
        this.profit_coins = profit_coins;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }
}
