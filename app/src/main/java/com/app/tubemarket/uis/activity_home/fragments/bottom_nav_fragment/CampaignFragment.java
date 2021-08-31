package com.app.tubemarket.uis.activity_home.fragments.bottom_nav_fragment;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.app.tubemarket.R;
import com.app.tubemarket.adapters.CampaignAdapter;
import com.app.tubemarket.adapters.SpinnerInterestsAdapter;
import com.app.tubemarket.databinding.DialogAlertBinding;
import com.app.tubemarket.databinding.FragmentCampaignBinding;
import com.app.tubemarket.interfaces.Listeners;
import com.app.tubemarket.models.CampaignDataModel;
import com.app.tubemarket.models.CampaignModel;
import com.app.tubemarket.models.InterestsModel;
import com.app.tubemarket.models.StatusResponse;
import com.app.tubemarket.models.UserModel;
import com.app.tubemarket.models.VideoModel;
import com.app.tubemarket.models.VipDataModel;
import com.app.tubemarket.preferences.Preferences;
import com.app.tubemarket.remote.Api;
import com.app.tubemarket.share.Common;
import com.app.tubemarket.tags.Tags;
import com.app.tubemarket.uis.activity_home.HomeActivity;
import com.app.tubemarket.uis.activity_home.fragments.slide_menu_fragment.ProfileFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CampaignFragment extends Fragment implements Listeners.CampaignListener {

    private FragmentCampaignBinding binding;
    private HomeActivity activity;
    private UserModel userModel;
    private Preferences preferences;
    private int type;
    private List<CampaignModel> list;
    private CampaignAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_campaign, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        list = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);

        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new CampaignAdapter(activity, list, this);
        binding.recView.setAdapter(adapter);

        binding.rbVideo.setOnClickListener(v -> {
            type = 1;
        });

        binding.rbSubscribe.setOnClickListener(v -> {
            type = 2;
        });

        binding.btnSelect.setOnClickListener(v -> {
            if (type != 0) {
                binding.expandedLayout.collapse(true);
                binding.rbVideo.setChecked(false);
                binding.rbSubscribe.setChecked(false);

            }

            new Handler(Looper.myLooper())
                    .postDelayed(() -> {
                        if (type == 1) {
                            Navigation.findNavController(v).navigate(R.id.newAdditionFragment);
                        } else if (type == 2) {
                            Navigation.findNavController(v).navigate(R.id.addSubscriptionsFragment);

                        }
                    }, 300);


        });
        binding.fab.setOnClickListener(v -> {
            binding.fab.clearAnimation();
            if (binding.expandedLayout.isExpanded()) {
                binding.expandedLayout.collapse(true);
                binding.rbVideo.setChecked(false);
                binding.rbSubscribe.setChecked(false);
            } else {
                binding.expandedLayout.expand(true);
            }


        });


        getCampaign();
    }

    private void getCampaign() {
        binding.loader.setVisibility(View.VISIBLE);
        list.clear();
        adapter.notifyDataSetChanged();
        Api.getService(Tags.base_url)
                .getMyCampaign("Bearer " + userModel.getToken(), userModel.getId(), "desc")
                .enqueue(new Callback<CampaignDataModel>() {
                    @Override
                    public void onResponse(Call<CampaignDataModel> call, Response<CampaignDataModel> response) {
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
                    public void onFailure(Call<CampaignDataModel> call, Throwable t) {
                        binding.loader.setVisibility(View.GONE);
                        Log.e("fail", t.getMessage());
                    }
                });
    }

    @Override
    public void onCampaignData(CampaignModel model, int type, View root) {
        if (type == 1) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", model);
            Navigation.findNavController(root).navigate(R.id.campaignDetailsFragment, bundle);

        }else if (type==2){
            delete(model);
        }
    }

    private void delete(CampaignModel model) {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .deleteCampaign("Bearer " + userModel.getToken(), model.getId())
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                getCampaign();
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