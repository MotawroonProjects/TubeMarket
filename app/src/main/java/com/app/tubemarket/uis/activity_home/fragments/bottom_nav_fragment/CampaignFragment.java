package com.app.tubemarket.uis.activity_home.fragments.bottom_nav_fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.app.tubemarket.R;
import com.app.tubemarket.adapters.SpinnerInterestsAdapter;
import com.app.tubemarket.databinding.DialogAlertBinding;
import com.app.tubemarket.databinding.FragmentCampaignBinding;
import com.app.tubemarket.models.InterestsModel;
import com.app.tubemarket.models.UserModel;
import com.app.tubemarket.models.VideoModel;
import com.app.tubemarket.preferences.Preferences;
import com.app.tubemarket.share.Common;
import com.app.tubemarket.uis.activity_home.HomeActivity;
import com.app.tubemarket.uis.activity_home.fragments.slide_menu_fragment.ProfileFragment;

import java.util.ArrayList;
import java.util.List;


public class CampaignFragment extends Fragment {

    private FragmentCampaignBinding binding;
    private HomeActivity activity;
    private UserModel userModel;
    private Preferences preferences;
    private int type;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_campaign,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);

        binding.rbVideo.setOnClickListener(v -> {
            type = 1;
        });

        binding.rbSubscribe.setOnClickListener(v -> {
            type = 2;
        });

        binding.btnSelect.setOnClickListener(v -> {
            if(type!=0){
                binding.expandedLayout.collapse(true);
                binding.rbVideo.setChecked(false);
                binding.rbSubscribe.setChecked(false);

            }

            new Handler(Looper.myLooper())
                    .postDelayed(() -> {
                        if (type==1){
                            Navigation.findNavController(v).navigate(R.id.newAdditionFragment);
                        }else if (type==2){
                            Navigation.findNavController(v).navigate(R.id.addSubscriptionsFragment);

                        }
                    }, 300);




        });
        binding.fab.setOnClickListener(v -> {
            binding.fab.clearAnimation();
            if (binding.expandedLayout.isExpanded()){
                binding.expandedLayout.collapse(true);
                binding.rbVideo.setChecked(false);
                binding.rbSubscribe.setChecked(false);
            }else {
                binding.expandedLayout.expand(true);
            }


        });
    }

}