package com.app.tubemarket.models;

import java.io.Serializable;

public class CostResultModel extends StatusResponse implements Serializable {
    private String data;

    public String getData() {
        return data;
    }
}
