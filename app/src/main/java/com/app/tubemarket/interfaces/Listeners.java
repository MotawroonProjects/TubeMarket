package com.app.tubemarket.interfaces;


import android.view.View;

import com.app.tubemarket.models.CampaignModel;
import com.app.tubemarket.models.VipModel;

public interface Listeners {

    interface VipListener {
        void onVipPay(VipModel model);
    }

    interface CampaignListener {
        void onCampaignData(CampaignModel model, int type, View view);
    }


}
