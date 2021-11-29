package com.tubemarket.models;

import java.io.Serializable;
import java.util.List;

public class VipDataModel extends StatusResponse implements Serializable {
    private List<VipModel> data;

    public List<VipModel> getData() {
        return data;
    }
}
