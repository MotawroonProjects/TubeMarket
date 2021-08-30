package com.app.tubemarket.uis.activity_home.fragments.slide_menu_fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.tubemarket.R;
import com.app.tubemarket.adapters.SpinnerInterestsAdapter;
import com.app.tubemarket.adapters.VipAdapter;
import com.app.tubemarket.databinding.FragmentGoldProfileBinding;
import com.app.tubemarket.databinding.FragmentProfileBinding;
import com.app.tubemarket.interfaces.Listeners;
import com.app.tubemarket.models.InterestsModel;
import com.app.tubemarket.models.LoginRegisterModel;
import com.app.tubemarket.models.UserModel;
import com.app.tubemarket.models.VideoModel;
import com.app.tubemarket.models.VipDataModel;
import com.app.tubemarket.models.VipModel;
import com.app.tubemarket.preferences.Preferences;
import com.app.tubemarket.remote.Api;
import com.app.tubemarket.tags.Tags;
import com.app.tubemarket.uis.activity_home.HomeActivity;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gold_profile, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        list = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);

        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        vipAdapter = new VipAdapter(activity, list, this);
        binding.recView.setAdapter(vipAdapter);

        getVip();

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

    }
}