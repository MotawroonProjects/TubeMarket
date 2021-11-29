package com.tubemarket.uis.activity_home.fragments.general_fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tubemarket.R;
import com.tubemarket.databinding.FragmentBuyMessageBinding;
import com.tubemarket.databinding.FragmentBuyMessageWithCouponBinding;
import com.tubemarket.models.AddMessageModel;
import com.tubemarket.models.MessageResponseModel;
import com.tubemarket.models.UserModel;
import com.tubemarket.preferences.Preferences;
import com.tubemarket.remote.Api;
import com.tubemarket.share.Common;
import com.tubemarket.tags.Tags;
import com.tubemarket.uis.activity_home.HomeActivity;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BuyMessageWithCouponFragment extends Fragment {

    private FragmentBuyMessageWithCouponBinding binding;
    private HomeActivity activity;
    private MessageResponseModel model;
    private Preferences preferences;
    private UserModel userModel;
    private AddMessageModel addMessageModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            model = (MessageResponseModel) getArguments().getSerializable("data");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_buy_message_with_coupon, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        addMessageModel = new AddMessageModel();
        binding.setModel(addMessageModel);
        binding.setCoupon(model.getData().getCoupon_code());
        binding.btnAdd.setOnClickListener(v -> {
            if (addMessageModel.isDataValid(activity)){
                addMessage();
            }
        });

    }

    private void addMessage() {

        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .updateMessageWithCoupon("Bearer "+userModel.getToken(),userModel.getId(),model.getData().getId(),model.getData().getBuy_message_id(), addMessageModel.getLink(), addMessageModel.getContent(),model.getData().getCoupon_code())
                .enqueue(new Callback<MessageResponseModel>() {
                    @Override
                    public void onResponse(Call<MessageResponseModel> call, Response<MessageResponseModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()&&response.body()!=null&&response.body().getStatus()==200){
                            Navigation.findNavController(binding.getRoot()).popBackStack();



                        }else {
                            try {
                                Log.e("error", response.code()+"__"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageResponseModel> call, Throwable t) {
                        dialog.dismiss();

                        Log.e("failed", t.getMessage()+"__");
                    }
                });
    }
}