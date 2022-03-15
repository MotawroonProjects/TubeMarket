package com.tubemarket.uis.activity_home.fragments.general_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

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
        binding.startAppBanner.loadAd();

     /*   MobileAds.initialize(activity, initializationStatus -> {
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
        });*/
    }

}