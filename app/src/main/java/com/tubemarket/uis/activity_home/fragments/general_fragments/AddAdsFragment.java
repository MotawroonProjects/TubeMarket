package com.tubemarket.uis.activity_home.fragments.general_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tubemarket.R;
import com.tubemarket.databinding.FragmentAddAdsBinding;
import com.tubemarket.databinding.FragmentGetSubscribersBinding;
import com.tubemarket.models.UserModel;
import com.tubemarket.models.VideoModel;
import com.tubemarket.preferences.Preferences;
import com.tubemarket.uis.activity_home.HomeActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;

public class AddAdsFragment extends Fragment {
    private FragmentAddAdsBinding binding;
    private HomeActivity activity;
    private UserModel userModel;
    private Preferences preferences;
    private VideoModel channelModel = null;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_ads,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);

        binding.cardGetSubscribers.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.getSubscribersFragment);
        });

        binding.cardGetViews.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.getViewsFragment);
        });

        binding.cardGetSubscribersViews.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.getSubscribersViewsFragment);
        });
        binding.cardGetLikes.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.getLikesFragment);
        });

        adMob();

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

}