package com.tubemarket.models;

import java.io.Serializable;
import java.util.List;

public class CampaignDataModel extends StatusResponse implements Serializable {
    private List<CampaignModel> data;

    public List<CampaignModel> getData() {
        return data;
    }
}
