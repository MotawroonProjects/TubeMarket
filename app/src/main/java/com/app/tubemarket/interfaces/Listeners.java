package com.app.tubemarket.interfaces;


import com.app.tubemarket.models.VipModel;

public interface Listeners {

    interface VipListener {
        void onVipPay(VipModel model);
    }


}
