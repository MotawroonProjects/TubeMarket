package com.app.tubemarket.models;

import java.io.Serializable;
import java.util.List;

public class WithdrawDataModel extends StatusResponse implements Serializable {
    private List<WithdrawModel> data;

    public List<WithdrawModel> getData() {
        return data;
    }
}
