package com.tubemarket.models;

import java.io.Serializable;

public class SingleAdminMessageDataModel extends StatusResponse implements Serializable {
    private AdminMessageModel data;

    public AdminMessageModel getData() {
        return data;
    }
}
