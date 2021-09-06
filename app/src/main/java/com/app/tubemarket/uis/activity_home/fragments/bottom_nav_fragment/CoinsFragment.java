package com.app.tubemarket.uis.activity_home.fragments.bottom_nav_fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.app.tubemarket.R;
import com.app.tubemarket.databinding.FragmentCampaignBinding;
import com.app.tubemarket.databinding.FragmentCoinsBinding;
import com.app.tubemarket.models.UserModel;
import com.app.tubemarket.preferences.Preferences;
import com.app.tubemarket.uis.activity_home.HomeActivity;

import io.paperdb.Paper;


public class CoinsFragment extends Fragment {
    private FragmentCoinsBinding binding;
    private HomeActivity activity;
    private UserModel userModel;
    private Preferences preferences;
    private String lang;
    private int dialog_type;
    private Animation animation;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_coins,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        animation = AnimationUtils.loadAnimation(activity,R.anim.fade_in);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        binding.setLang(lang);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.flDialog.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        binding.cardGoldenAccount.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.goldProfileFragment);
        });
        binding.cardWithdraw.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.withdrawFragment);
        });

        binding.cardBuyCoin.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.buyCoinFragment);
        });

        binding.cardCoupon.setOnClickListener(v -> {
            binding.flDialog.clearAnimation();
            binding.flDialog.startAnimation(animation);

        });

        binding.cardClose.setOnClickListener(v -> {
           closeDialog();

        });

        binding.cardAdsChannel.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.addAdsFragment);
        });
    }

    public void closeDialog(){
        binding.edtCode.setText(null);
        binding.edtCode.setError(null);
        if (binding.flDialog.getVisibility()==View.VISIBLE){
            binding.flDialog.setVisibility(View.GONE);
        }
    }

    public boolean getDialogVisibility(){
        return binding.flDialog.getVisibility() == View.VISIBLE;
    }


}