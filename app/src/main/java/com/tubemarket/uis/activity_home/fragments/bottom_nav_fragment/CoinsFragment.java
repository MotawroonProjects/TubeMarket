package com.tubemarket.uis.activity_home.fragments.bottom_nav_fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.tubemarket.R;
import com.tubemarket.databinding.FragmentCampaignBinding;
import com.tubemarket.databinding.FragmentCoinsBinding;
import com.tubemarket.models.StatusResponse;
import com.tubemarket.models.UserModel;
import com.tubemarket.preferences.Preferences;
import com.tubemarket.remote.Api;
import com.tubemarket.share.Common;
import com.tubemarket.tags.Tags;
import com.tubemarket.uis.activity_chat_admin.ChatAdminActivity;
import com.tubemarket.uis.activity_home.HomeActivity;

import java.io.IOException;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

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

        binding.cardActivateChannel.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.channelProfitFragment);
        });


        binding.cardCoupon.setOnClickListener(v -> {
            activity.adMob();
            binding.flDialog.clearAnimation();
            binding.flDialog.startAnimation(animation);

        });

        binding.btnConfirm.setOnClickListener(v -> {
            if (!binding.edtCode.getText().toString().isEmpty()){
                activateCode();
            }
        });
        binding.cardClose.setOnClickListener(v -> {
           closeDialog();

        });

        binding.cardAdsChannel.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.addAdsFragment);
        });

        binding.cardBuyMessage.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.messagesFragment);
        });

        binding.cardSupport.setOnClickListener(v -> {
            Intent intent =new Intent(activity, ChatAdminActivity.class);
            startActivity(intent);
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


    private void activateCode() {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .activateCoupon("Bearer " + userModel.getToken(), userModel.getId(), binding.edtCode.getText().toString())
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                Toast.makeText(activity, activity.getString(R.string.suc), Toast.LENGTH_SHORT).show();
                                activity.getUserProfile();
                                closeDialog();
                            }

                        } else {
                            dialog.dismiss();
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                            } else {
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                } else {
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });
    }
}