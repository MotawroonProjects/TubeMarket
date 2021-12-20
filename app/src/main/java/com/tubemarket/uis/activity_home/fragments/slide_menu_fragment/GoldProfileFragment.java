package com.tubemarket.uis.activity_home.fragments.slide_menu_fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tubemarket.R;
import com.tubemarket.adapters.VipAdapter;
import com.tubemarket.databinding.FragmentGoldProfileBinding;
import com.tubemarket.databinding.FragmentProfileBinding;
import com.tubemarket.interfaces.Listeners;
import com.tubemarket.models.MessageResponseModel;
import com.tubemarket.models.PayModel;
import com.tubemarket.models.UserModel;
import com.tubemarket.models.VipDataModel;
import com.tubemarket.models.VipModel;
import com.tubemarket.preferences.Preferences;
import com.tubemarket.remote.Api;
import com.tubemarket.share.Common;
import com.tubemarket.tags.Tags;
import com.tubemarket.uis.activity_home.HomeActivity;
import com.tubemarket.uis.activity_view.ViewActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoldProfileFragment extends Fragment implements Listeners.VipListener {

    private FragmentGoldProfileBinding binding;
    private List<VipModel> list;
    private HomeActivity activity;
    private UserModel userModel;
    private Preferences preferences;
    private VipAdapter vipAdapter;
    private ActivityResultLauncher<Intent>launcher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gold_profile, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode()== Activity.RESULT_OK){
                    activity.getUserProfile();
                }
            }
        });
    }

    private void initView() {
        list = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);

        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        vipAdapter = new VipAdapter(activity, list, this);
        binding.recView.setAdapter(vipAdapter);

        activity.adMob();
        adMob();
        getVip();

    }

    private void adMob() {
        MobileAds.initialize(activity, initializationStatus -> {
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
        binding.adView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });
    }


    private void getVip() {
        Api.getService(Tags.base_url)
                .getVipPay("Bearer " + userModel.getToken(),"desc")
                .enqueue(new Callback<VipDataModel>() {
                    @Override
                    public void onResponse(Call<VipDataModel> call, Response<VipDataModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                list.clear();
                                list.addAll(response.body().getData());
                                vipAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<VipDataModel> call, Throwable t) {

                    }
                });
    }

    @Override
    public void onVipPay(VipModel model) {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .payGoldAccount("Bearer "+userModel.getToken(),userModel.getId(), model.getId())
                .enqueue(new Callback<PayModel>() {
                    @Override
                    public void onResponse(Call<PayModel> call, Response<PayModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()){
                            Intent intent = new Intent(activity, ViewActivity.class);
                            intent.putExtra("url", response.body().getPay_link());
                            intent.putExtra("data",new MessageResponseModel.Data());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            launcher.launch(intent);

                        }else {
                            try {
                                Log.e("error", response.code()+"__"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PayModel> call, Throwable t) {
                        dialog.dismiss();

                        Log.e("failed", t.getMessage()+"__");
                    }
                });

    }
}