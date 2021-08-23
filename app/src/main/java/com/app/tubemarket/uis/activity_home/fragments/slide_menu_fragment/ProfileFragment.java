package com.app.tubemarket.uis.activity_home.fragments.slide_menu_fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.tubemarket.R;
import com.app.tubemarket.adapters.SpinnerInterestsAdapter;
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

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private SpinnerInterestsAdapter adapter;
    private List<InterestsModel> list;
    private HomeActivity activity;
    private UserModel userModel;
    private Preferences preferences;
    private VideoModel channelModel = null;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        binding.setModel(userModel.getChannelModel());
        list = new ArrayList<>();
        list.add(new InterestsModel(0,getString(R.string.choose)));
        list.add(new InterestsModel(1,getString(R.string.sports)));
        list.add(new InterestsModel(2,getString(R.string.games)));
        list.add(new InterestsModel(3,getString(R.string.cooks)));
        list.add(new InterestsModel(4,getString(R.string.writes)));
        adapter = new SpinnerInterestsAdapter(list,activity);
        binding.spinner.setAdapter(adapter);
        binding.btnAdd.setOnClickListener(v -> {
            String url = binding.edtUrl.getText().toString().trim();
            if (!url.isEmpty()){
                String vidId = extractYTId(url);
                if (vidId!=null){
                    Common.CloseKeyBoard(activity,binding.edtUrl);
                    getVideoById(vidId);
                }
            }
        });
        binding.btnUpdate.setOnClickListener(v -> {
            if (channelModel!=null){
                UserModel.ChannelModel model = new UserModel.ChannelModel(channelModel.getId(),channelModel.getItems().get(0).getSnippet().getLocalized().getTitle(),channelModel.getItems().get(0).getSnippet().getLocalized().getDescription(),channelModel.getItems().get(0).getSnippet().getThumbnails().getMedium().getUrl());
                userModel.setChannelModel(model);
                preferences.create_update_userdata(activity,userModel);
            }

        });
    }

    private void getVideoById(String vidId) {
        binding.flLoading.setVisibility(View.VISIBLE);
        binding.cardView.setCardElevation(0);
        Api.getService(Tags.tube_base_url)
                .getYouTubeVideoById("snippet,contentDetails", vidId, Tags.tubeKey)
                .enqueue(new Callback<VideoModel>() {
                    @Override
                    public void onResponse(Call<VideoModel> call, Response<VideoModel> response) {
                        if (response.isSuccessful()&&response.body()!=null){
                            if (response.body().getItems()!=null&&response.body().getItems().size()>0){
                                getChannelById(response.body().getItems().get(0).getSnippet().getChannelId());
                            }else {
                                binding.cardView.setCardElevation(3);

                            }
                        }else {
                            binding.cardView.setCardElevation(3);

                        }
                    }

                    @Override
                    public void onFailure(Call<VideoModel> call, Throwable t) {
                        binding.flLoading.setVisibility(View.GONE);
                        binding.cardView.setCardElevation(3);

                    }
                });
    }

    private void getChannelById(String channelId) {
        Api.getService(Tags.tube_base_url)
                .getYouTubeChannelById("snippet", channelId, Tags.tubeKey)
                .enqueue(new Callback<VideoModel>() {
                    @Override
                    public void onResponse(Call<VideoModel> call, Response<VideoModel> response) {
                        binding.flLoading.setVisibility(View.GONE);
                        binding.cardView.setCardElevation(3);

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
                        binding.flLoading.setVisibility(View.GONE);
                        binding.cardView.setCardElevation(3);

                    }
                });
    }


    private String extractYTId(String ytUrl) {
        String vId = null;
        Pattern pattern = Pattern.compile("^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(ytUrl);
        if (matcher.matches()){
            vId = matcher.group(1);
        }
        return vId;
    }
}