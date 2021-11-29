package com.tubemarket.uis.activity_home.fragments.general_fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tubemarket.R;
import com.tubemarket.adapters.ViewUsersAdapter;
import com.tubemarket.databinding.FragmentCampaignDetailsBinding;
import com.tubemarket.databinding.FragmentGetViewsBinding;
import com.tubemarket.models.CampaignModel;
import com.tubemarket.models.SingleCampaign;
import com.tubemarket.models.UserModel;
import com.tubemarket.preferences.Preferences;
import com.tubemarket.remote.Api;
import com.tubemarket.tags.Tags;
import com.tubemarket.uis.activity_home.HomeActivity;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CampaignDetailsFragment extends Fragment {
    private FragmentCampaignDetailsBinding binding;
    private HomeActivity activity;
    private UserModel userModel;
    private Preferences preferences;
    private CampaignModel campaignModel = null;
    private ViewUsersAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle!=null){
            campaignModel = (CampaignModel) bundle.getSerializable("data");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_campaign_details,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        binding.setModel(campaignModel);
        try {
            int progress =Math.round ((Float.parseFloat(campaignModel.getView_count())/Float.parseFloat(campaignModel.getView_limit()))*100);
            binding.progBar.setProgress(progress);
        }catch (Exception e){}

        if (campaignModel!=null){
            getCampaign();

        }

    }

    private void getCampaign() {
        Api.getService(Tags.base_url)
                .getSingleCampaign("Bearer " + userModel.getToken(), userModel.getId(), campaignModel.getId())
                .enqueue(new Callback<SingleCampaign>() {
                    @Override
                    public void onResponse(Call<SingleCampaign> call, Response<SingleCampaign> response) {

                        if (response.isSuccessful() ) {
                            if (response.body() != null&&response.body().getStatus() == 200) {
                             campaignModel = response.body().getData();
                             binding.setModel(campaignModel);
                             updateUi();
                            }
                        }else {
                            try {
                                Log.e("error", response.code()+"__"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SingleCampaign> call, Throwable t) {
                        Log.e("fail", t.getMessage());
                    }
                });
    }

    private void updateUi() {
        if (campaignModel.getOperations_fk()!=null&&campaignModel.getOperations_fk().size()>0){
            binding.recView.setLayoutManager(new LinearLayoutManager(activity));
            binding.recView.addItemDecoration(new DividerItemDecoration(activity,LinearLayoutManager.VERTICAL));
            adapter = new ViewUsersAdapter(activity,campaignModel.getOperations_fk());
            binding.recView.setAdapter(adapter);
        }

    }

}