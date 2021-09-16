package com.app.tubemarket.models;

import java.io.Serializable;
import java.util.List;

public class MyAdsDataModel extends StatusResponse implements Serializable {
    private List<MyAdsModel> data;


    public List<MyAdsModel> getData() {
        return data;
    }



}
