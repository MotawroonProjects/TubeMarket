package com.app.tubemarket.models;

import java.io.Serializable;

public class CoinsDataModel extends StatusResponse implements Serializable {
    private CoinsModel data;

    public CoinsModel getData() {
        return data;
    }

    public static class CoinsModel implements Serializable{
        private String campaign_coins;
        private String profit_coins;

        public String getCampaign_coins() {
            return campaign_coins;
        }

        public String getProfit_coins() {
            return profit_coins;
        }
    }
}
