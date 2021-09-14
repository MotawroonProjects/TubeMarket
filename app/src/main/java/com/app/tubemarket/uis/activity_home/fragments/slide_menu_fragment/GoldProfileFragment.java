package com.app.tubemarket.uis.activity_home.fragments.slide_menu_fragment;

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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.tubemarket.R;
import com.app.tubemarket.adapters.SpinnerInterestsAdapter;
import com.app.tubemarket.adapters.VipAdapter;
import com.app.tubemarket.databinding.FragmentGoldProfileBinding;
import com.app.tubemarket.databinding.FragmentProfileBinding;
import com.app.tubemarket.interfaces.Listeners;
import com.app.tubemarket.models.AddMessageDataModel;
import com.app.tubemarket.models.InterestsModel;
import com.app.tubemarket.models.LoginRegisterModel;
import com.app.tubemarket.models.MessageResponseModel;
import com.app.tubemarket.models.PayModel;
import com.app.tubemarket.models.UserModel;
import com.app.tubemarket.models.VideoModel;
import com.app.tubemarket.models.VipDataModel;
import com.app.tubemarket.models.VipModel;
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
        initView();
        return binding.getRoot();
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