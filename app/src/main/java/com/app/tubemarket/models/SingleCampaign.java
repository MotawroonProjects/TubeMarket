package com.app.tubemarket.models;

import java.io.Serializable;

public class SingleCampaign extends StatusResponse implements Serializable {
    private CampaignModel data;

    public CampaignModel getData() {
        return data;
    }
}
