package com.app.tubemarket.models;

import java.io.Serializable;

public class AddMessageDataModel extends StatusResponse implements Serializable {
    private String pay_link;

    public String getPay_link() {
        return pay_link;
    }
}
