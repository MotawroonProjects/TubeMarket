package com.app.tubemarket.interfaces;


import android.view.View;

import com.app.tubemarket.models.BuyMessageModel;
import com.app.tubemarket.models.CampaignModel;
import com.app.tubemarket.models.CoinsModel;
import com.app.tubemarket.models.MyAdsModel;
import com.app.tubemarket.models.UserMessageModel;
import com.app.tubemarket.models.VipModel;
import com.app.tubemarket.models.WithdrawModel;

public interface Listeners {

    interface UserMessageListener {
        void onUserMessage(UserMessageModel model, View itemView);
    }

    interface BuyMessageListener {
        void onBuyMessage(BuyMessageModel model, View itemView);
    }
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

    interface MyAdsListener {
        void onMyAdsData(MyAdsModel model,int type, View view);
    }

}
