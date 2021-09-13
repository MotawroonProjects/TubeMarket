package com.app.tubemarket.uis.activity_home.fragments.general_fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.tubemarket.R;
import com.app.tubemarket.databinding.FragmentBuyMessageBinding;
import com.app.tubemarket.models.AddMessageDataModel;
import com.app.tubemarket.models.AddMessageModel;
import com.app.tubemarket.models.BuyMessageModel;
import com.app.tubemarket.models.StatusResponse;
import com.app.tubemarket.models.UserModel;
import com.app.tubemarket.preferences.Preferences;
import com.app.tubemarket.remote.Api;
import com.app.tubemarket.share.Common;
import com.app.tubemarket.tags.Tags;
import com.app.tubemarket.uis.activity_home.HomeActivity;
import com.app.tubemarket.uis.activity_view.ViewActivity;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BuyMessageFragment extends Fragment {
    private FragmentBuyMessageBinding binding;
    private HomeActivity activity;
    private BuyMessageModel model;
    private Preferences preferences;
    private UserModel userModel;
    private AddMessageModel addMessageModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            model = (BuyMessageModel) getArguments().getSerializable("data");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_buy_message, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        addMessageModel = new AddMessageModel();
        binding.setModel(addMessageModel);
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
                .addMessage("Bearer "+userModel.getToken(),userModel.getId(), model.getId(), addMessageModel.getLink(),addMessageModel.getContent())
                .enqueue(new Callback<AddMessageDataModel>() {
                    @Override
                    public void onResponse(Call<AddMessageDataModel> call, Response<AddMessageDataModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()){
                            Navigation.findNavController(binding.getRoot()).popBackStack();
                            Intent intent = new Intent(activity, ViewActivity.class);
                            intent.putExtra("url", response.body().getPay_link());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);

                        }else {
                            try {
                                Log.e("error", response.code()+"__"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AddMessageDataModel> call, Throwable t) {
                        dialog.dismiss();

                        Log.e("failed", t.getMessage()+"__");
                    }
                });
    }
}