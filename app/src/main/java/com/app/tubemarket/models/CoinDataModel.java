package com.app.tubemarket.models;

import java.io.Serializable;
import java.util.List;

public class CoinDataModel extends StatusResponse implements Serializable {
    private List<CoinsModel> data;

    public List<CoinsModel> getData() {
        return data;
    }
}
