package com.tubemarket.models;

import java.io.Serializable;
import java.util.List;

public class AdCostDataModel extends StatusResponse implements Serializable {
    private List<AdCostModel> data;

    public List<AdCostModel> getData() {
        return data;
    }
}
