package com.app.tubemarket.uis.activity_home.fragments.general_fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.tubemarket.R;
import com.app.tubemarket.adapters.SpinnerCountAdapter;
import com.app.tubemarket.adapters.SpinnerInterestsAdapter;
import com.app.tubemarket.databinding.FragmentGetSubscribersBinding;
import com.app.tubemarket.databinding.FragmentProfileBinding;
import com.app.tubemarket.models.InterestsModel;
import com.app.tubemarket.models.UserModel;
import com.app.tubemarket.models.VideoModel;
import com.app.tubemarket.preferences.Preferences;
import com.app.tubemarket.remote.Api;
import com.app.tubemarket.share.Common;
import com.app.tubemarket.tags.Tags;
import com.app.tubemarket.uis.activity_home.HomeActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetSubscribersFragment extends Fragment {
    private FragmentGetSubscribersBinding binding;
    private HomeActivity activity;
    private UserModel userModel;
    private Preferences preferences;
    private VideoModel channelModel = null;
    private SpinnerCountAdapter secondsAdapter;
    private List<String> secondsList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_get_subscribers,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        secondsList = new ArrayList<>();
        for (int index=1;index<9;index++)
        {
            if (index==1){
                secondsList.add("45");
            }else {
                int second = index*30;
                secondsList.add(second+"");



            }
        }
        secondsAdapter = new SpinnerCountAdapter(secondsList,activity);
        binding.spinnerSubscriptions.setAdapter(secondsAdapter);

        binding.btnConfirm.setOnClickListener(v -> {
            String url = binding.edtUrl.getText().toString().trim();
            String channelId = extractChannelId(url);
            if (channelId!=null){
                binding.btnConfirm.setEnabled(false);
                getChannelById(channelId);

            }
        });



    }


    private void getChannelById(String channelId) {
        Api.getService(Tags.tube_base_url)
                .getYouTubeChannelById("snippet", channelId, Tags.tubeKey)
                .enqueue(new Callback<VideoModel>() {
                    @Override
                    public void onResponse(Call<VideoModel> call, Response<VideoModel> response) {
                        binding.btnConfirm.setEnabled(true);
                        if (response.isSuccessful()&&response.body()!=null){
                            if (response.body().getItems()!=null&&response.body().getItems().size()>0){

                                channelModel = response.body();
                                UserModel.ChannelModel model = new UserModel.ChannelModel(channelModel.getItems().get(0).getId(),channelModel.getItems().get(0).getSnippet().getLocalized().getTitle(),channelModel.getItems().get(0).getSnippet().getLocalized().getDescription(),channelModel.getItems().get(0).getSnippet().getThumbnails().getMedium().getUrl());
                                binding.setModel(model);

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<VideoModel> call, Throwable t) {
                        binding.btnConfirm.setEnabled(true);

                    }
                });
    }

    private String extractChannelId(String url){
        String channelId = null;
        Pattern pattern = Pattern.compile("^https?://.*(?:youtube\\.com/channel/)([^#&?]*)$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        if (matcher.matches()){
            channelId = matcher.group(1);
        }

        return channelId;

    }
}