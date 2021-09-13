package com.app.tubemarket.models;

import java.io.Serializable;
import java.util.List;

public class BuyMessageDataModel extends StatusResponse implements Serializable {
    private List<BuyMessageModel> data;

    public List<BuyMessageModel> getData() {
        return data;
    }
}
