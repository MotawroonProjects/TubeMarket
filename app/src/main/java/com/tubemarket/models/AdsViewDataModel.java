package com.tubemarket.models;

import java.io.Serializable;
import java.util.List;

public class AdsViewDataModel extends StatusResponse implements Serializable {
    private List<AdsViewModel> data;


    public List<AdsViewModel> getData() {
        return data;
    }



}
