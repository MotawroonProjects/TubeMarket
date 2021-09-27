package com.app.tubemarket.uis.activity_home.fragments.general_fragments;

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
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.app.tubemarket.R;
import com.app.tubemarket.adapters.CoinsAdapter;
import com.app.tubemarket.adapters.WithdrawAdapter;
import com.app.tubemarket.databinding.FragmentBuyCoinBinding;
import com.app.tubemarket.databinding.FragmentWithdrawBinding;
import com.app.tubemarket.interfaces.Listeners;
import com.app.tubemarket.models.CoinDataModel;
import com.app.tubemarket.models.CoinsModel;
import com.app.tubemarket.models.MessageResponseModel;
import com.app.tubemarket.models.PayModel;
import com.app.tubemarket.models.StatusResponse;
import com.app.tubemarket.models.UserModel;
import com.app.tubemarket.models.WithdrawDataModel;
import com.app.tubemarket.models.WithdrawModel;
import com.app.tubemarket.preferences.Preferences;
import com.app.tubemarket.remote.Api;
import com.app.tubemarket.share.Common;
import com.app.tubemarket.tags.Tags;
import com.app.tubemarket.uis.activity_home.HomeActivity;
import com.app.tubemarket.uis.activity_view.ViewActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WithdrawFragment extends Fragment implements Listeners.WithdrawListener {

    private FragmentWithdrawBinding binding;
    private HomeActivity activity;
    private UserModel userModel;
    private Preferences preferences;
    private List<WithdrawModel> list;
    private WithdrawAdapter adapter;
    private Animation animation;
    private WithdrawModel withdraw;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_withdraw, container, false);
        initView();
        return binding.getRoot();
    }


    private void initView() {
        list = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        animation = AnimationUtils.loadAnimation(activity, R.anim.fade_in);

        binding.recView.setLayoutManager(new GridLayoutManager(activity, 2));
        adapter = new WithdrawAdapter(activity, list, this);
        binding.recView.setAdapter(adapter);

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


        getWithdraw();
        binding.cardClose.setOnClickListener(v -> {
            closeDialog();

        });
        binding.btnConfirm.setOnClickListener(v -> {
            if (!binding.edtCode.getText().toString().isEmpty()) {
                exchangeCoins(withdraw);

                closeDialog();
            }
        });
    }

    private void getWithdraw() {
        binding.loader.setVisibility(View.VISIBLE);
        list.clear();
        adapter.notifyDataSetChanged();
        Api.getService(Tags.base_url)
                .getWithdrawCoins("Bearer " + userModel.getToken(), "desc")
                .enqueue(new Callback<WithdrawDataModel>() {
                    @Override
                    public void onResponse(Call<WithdrawDataModel> call, Response<WithdrawDataModel> response) {
                        binding.loader.setVisibility(View.GONE);

                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                list.clear();
                                list.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();

                            }
                        } else {
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<WithdrawDataModel> call, Throwable t) {
                        binding.loader.setVisibility(View.GONE);
                        Log.e("fail", t.getMessage());
                    }
                });
    }

    @Override
    public void onWithdrawData(WithdrawModel withdrawModel, View view) {
        binding.flDialog.clearAnimation();
        if (withdrawModel.getType().equals("paypal")) {
            binding.edtCode.setHint(R.string.paypal_email);
            binding.tvCode.setText(R.string.paypal_email);

        } else {
            binding.edtCode.setHint(R.string.phone);
            binding.tvCode.setText(R.string.phone);
        }
        binding.flDialog.startAnimation(animation);
        withdraw = withdrawModel;

    }

    public void closeDialog() {
        binding.edtCode.setText(null);
        binding.edtCode.setError(null);
        if (binding.flDialog.getVisibility() == View.VISIBLE) {
            binding.flDialog.setVisibility(View.GONE);
        }
    }

    private void exchangeCoins(WithdrawModel withdrawModel) {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .exchangeCoins("Bearer " + userModel.getToken(), userModel.getId(), withdrawModel.getId(), withdrawModel.getCost(), binding.edtCode.getText().toString())
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                Toast.makeText(activity, activity.getString(R.string.suc), Toast.LENGTH_SHORT).show();
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