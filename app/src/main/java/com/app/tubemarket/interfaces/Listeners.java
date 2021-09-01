package com.app.tubemarket.interfaces;


import android.view.View;

import com.app.tubemarket.models.CampaignModel;
import com.app.tubemarket.models.CoinsModel;
import com.app.tubemarket.models.VipModel;
import com.app.tubemarket.models.WithdrawModel;

public interface Listeners {

    interface VipListener {
        void onVipPay(VipModel model);
    }

    interface CampaignListener {
        void onCampaignData(CampaignModel model, int type, View view);
    }

    interface CoinsListener {
        void onCoinsData(CoinsModel coinsModel, View view);
    }

    interface WithdrawListener {
        void onWithdrawData(WithdrawModel withdrawModel, View view);
    }


}
