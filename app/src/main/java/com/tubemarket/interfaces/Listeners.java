package com.tubemarket.interfaces;


import android.view.View;

import com.tubemarket.models.BuyMessageModel;
import com.tubemarket.models.CampaignModel;
import com.tubemarket.models.CoinsModel;
import com.tubemarket.models.MyAdsModel;
import com.tubemarket.models.UserMessageModel;
import com.tubemarket.models.VipModel;
import com.tubemarket.models.WithdrawModel;

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
