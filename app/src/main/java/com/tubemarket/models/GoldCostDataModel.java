package com.tubemarket.models;

import java.io.Serializable;
import java.util.List;

public class GoldCostDataModel extends StatusResponse implements Serializable {
    private List<GoldCostModel> data;

    public List<GoldCostModel> getData() {
        return data;
    }
}
