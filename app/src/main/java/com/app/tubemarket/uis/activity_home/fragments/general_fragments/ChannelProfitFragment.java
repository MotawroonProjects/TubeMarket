package com.app.tubemarket.uis.activity_home.fragments.general_fragments;

import android.app.ProgressDialog;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.tubemarket.R;
import com.app.tubemarket.adapters.SpinnerCountAdapter;
import com.app.tubemarket.databinding.FragmentChannelProfitBinding;
import com.app.tubemarket.databinding.FragmentGetSubscribersBinding;
import com.app.tubemarket.models.CostResultModel;
import com.app.tubemarket.models.StatusResponse;
import com.app.tubemarket.models.UserModel;
import com.app.tubemarket.models.VideoModel;
import com.app.tubemarket.preferences.Preferences;
import com.app.tubemarket.remote.Api;
import com.app.tubemarket.share.Common;
import com.app.tubemarket.tags.Tags;
import com.app.tubemarket.uis.activity_home.HomeActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChannelProfitFragment extends Fragment {
    private FragmentChannelProfitBinding binding;
    private HomeActivity activity;
    private UserModel userModel;
    private Preferences preferences;
    private UserModel.ChannelModel userChannel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_channel_profit, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);

        binding.btnConfirm.setOnClickListener(v -> {
            String url = binding.edtUrl.getText().toString().trim();
            String channelId = extractChannelId(url);
            if (channelId != null) {
                binding.btnConfirm.setEnabled(false);
                getChannelById(channelId);

            }
        });

        binding.btnAdd.setOnClickListener(v -> {

        });


    }


    private void getChannelById(String channelId) {
        Api.getService(Tags.tube_base_url)
                .getYouTubeChannelById("snippet", channelId, Tags.tubeKey)
                .enqueue(new Callback<VideoModel>() {
                    @Override
                    public void onResponse(Call<VideoModel> call, Response<VideoModel> response) {
                        binding.btnConfirm.setEnabled(true);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getItems() != null) {

                                if (response.body().getItems().size() > 0){
                                    VideoModel channelModel = response.body();
                                    userChannel = new UserModel.ChannelModel(channelModel.getItems().get(0).getId(), channelModel.getItems().get(0).getSnippet().getLocalized().getTitle(), channelModel.getItems().get(0).getSnippet().getLocalized().getDescription(), channelModel.getItems().get(0).getSnippet().getThumbnails().getMedium().getUrl());
                                    binding.setModel(userChannel);
                                }


                            }else {
                                Toast.makeText(activity, R.string.in_url, Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            Toast.makeText(activity, R.string.in_url, Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<VideoModel> call, Throwable t) {
                        binding.btnConfirm.setEnabled(true);

                    }
                });
    }

    private String extractChannelId(String url) {
        String channelId = null;
        Pattern pattern = Pattern.compile("^https?://.*(?:youtube\\.com/channel/)([^#&?]*)$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        if (matcher.matches()) {
            channelId = matcher.group(1);
        }

        return channelId;

    }

}