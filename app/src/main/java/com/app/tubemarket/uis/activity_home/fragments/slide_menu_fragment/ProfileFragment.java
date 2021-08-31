package com.app.tubemarket.uis.activity_home.fragments.slide_menu_fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.app.tubemarket.R;
import com.app.tubemarket.adapters.SpinnerInterestsAdapter;
import com.app.tubemarket.databinding.FragmentProfileBinding;
import com.app.tubemarket.models.InterestsModel;
import com.app.tubemarket.models.LoginRegisterModel;
import com.app.tubemarket.models.UserModel;
import com.app.tubemarket.models.VideoModel;
import com.app.tubemarket.preferences.Preferences;
import com.app.tubemarket.remote.Api;
import com.app.tubemarket.share.Common;
import com.app.tubemarket.tags.Tags;
import com.app.tubemarket.uis.activity_home.HomeActivity;
import com.app.tubemarket.uis.activity_login.LoginActivity;

import java.io.IOException;
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
    private VideoModel videoModel, channelModel = null;
    private int interests_id = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);

        updateUI();


        if (userModel.getInterestsModel() != null) {
            interests_id = userModel.getInterestsModel().getId();
            String interests = "";
            switch (userModel.getInterestsModel().getId()) {
                case 1:
                    interests = getString(R.string.sports);
                    break;
                case 2:
                    interests = getString(R.string.games);

                    break;
                case 3:
                    interests = getString(R.string.cooks);

                    break;
                case 4:
                    interests = getString(R.string.writes);

                    break;

            }

            InterestsModel interestsModel = userModel.getInterestsModel();
            interestsModel.setName(interests);
            userModel.setInterestsModel(interestsModel);

        }else {

            list = new ArrayList<>();
            list.add(new InterestsModel(0, getString(R.string.choose)));
            list.add(new InterestsModel(1, getString(R.string.sports)));
            list.add(new InterestsModel(2, getString(R.string.games)));
            list.add(new InterestsModel(3, getString(R.string.cooks)));
            list.add(new InterestsModel(4, getString(R.string.writes)));
            adapter = new SpinnerInterestsAdapter(list, activity);
            binding.spinner.setAdapter(adapter);
            binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != 0) {
                        interests_id = list.get(position).getId();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            binding.btnAdd.setOnClickListener(v -> {
                String url = binding.edtUrl.getText().toString().trim();
                if (!url.isEmpty()) {
                    String vidId = extractYTId(url);
                    if (vidId != null) {
                        Common.CloseKeyBoard(activity, binding.edtUrl);
                        getVideoById(vidId);
                    }
                }
            });
            binding.btnUpdate.setOnClickListener(v -> {
                if (channelModel != null) {
                    UserModel.ChannelModel model = new UserModel.ChannelModel(channelModel.getId(), channelModel.getItems().get(0).getSnippet().getLocalized().getTitle(), channelModel.getItems().get(0).getSnippet().getLocalized().getDescription(), channelModel.getItems().get(0).getSnippet().getThumbnails().getMedium().getUrl());
                    userModel.setChannelModel(model);
                    preferences.create_update_userdata(activity, userModel);
                }

            });
        }
        binding.setModel(userModel.getChannelModel());
        binding.setUserModel(userModel);

        Log.e("ddd", userModel.getId()+"__"+userModel.getGoogle_id()+"__"+userModel.getToken());

        binding.btnUpdate.setOnClickListener(v -> {
            if ((videoModel!=null&&channelModel!=null)|interests_id!=0){
                updateProfile();
            }

        });



    }

    private void updateUI() {
        if (userModel.getChannelModel()==null){
            binding.edtUrl.setEnabled(true);
            binding.btnAdd.setEnabled(true);
            binding.btnUpdate.setEnabled(true);
        }else {
            if (userModel.getIs_vip().equals("no")){
                binding.edtUrl.setEnabled(false);
                binding.btnAdd.setEnabled(false);
                binding.btnUpdate.setEnabled(false);
            }else{
                binding.edtUrl.setEnabled(true);
                binding.btnAdd.setEnabled(true);
                binding.btnUpdate.setEnabled(true);

            }
        }
    }

    private void getVideoById(String vidId) {
        binding.flLoading.setVisibility(View.VISIBLE);
        binding.cardView.setCardElevation(0);
        Api.getService(Tags.tube_base_url)
                .getYouTubeVideoById("snippet,contentDetails", vidId, Tags.tubeKey)
                .enqueue(new Callback<VideoModel>() {
                    @Override
                    public void onResponse(Call<VideoModel> call, Response<VideoModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getItems() != null && response.body().getItems().size() > 0) {
                                videoModel = response.body();
                                getChannelById(response.body().getItems().get(0).getSnippet().getChannelId());
                            } else {
                                binding.cardView.setCardElevation(3);

                            }
                        } else {
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

                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getItems() != null && response.body().getItems().size() > 0) {

                                channelModel = response.body();
                                UserModel.ChannelModel model = new UserModel.ChannelModel(channelModel.getItems().get(0).getId(), channelModel.getItems().get(0).getSnippet().getLocalized().getTitle(), channelModel.getItems().get(0).getSnippet().getLocalized().getDescription(), channelModel.getItems().get(0).getSnippet().getThumbnails().getMedium().getUrl());
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
        if (matcher.matches()) {
            vId = matcher.group(1);
        }
        return vId;
    }

    private void updateProfile() {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        String channel_id ="";
        String channel_name="";
        String channel_desc="";
        String channel_image="";
        String video_id ="";
        String video_name="";
        String video_desc="";
        String video_image="";

        if (userModel.getChannelModel()!=null){
            channel_id = userModel.getChannelModel().getId();
            channel_name = userModel.getChannelModel().getTitle();
            channel_desc = userModel.getChannelModel().getDescriptions();
            channel_image = userModel.getChannelModel().getUrl();

        }else if (channelModel!=null){

            channel_id = channelModel.getItems().get(0).getId();
            channel_name =  channelModel.getItems().get(0).getSnippet().getTitle();
            channel_desc = channelModel.getItems().get(0).getSnippet().getDescription();
            channel_image = channelModel.getItems().get(0).getSnippet().getThumbnails().getMedium().getUrl();
        }

        if (userModel.getVideoModel()!=null){
            video_id    = userModel.getVideoModel().getId();
            video_name  = userModel.getVideoModel().getTitle();
            video_desc  = userModel.getVideoModel().getDescriptions();
            video_image = userModel.getVideoModel().getUrl();
        }else if (videoModel!=null){

            video_id    = videoModel.getItems().get(0).getId();
            video_name  =  videoModel.getItems().get(0).getSnippet().getTitle();
            video_desc  = videoModel.getItems().get(0).getSnippet().getDescription();
            video_image = videoModel.getItems().get(0).getSnippet().getThumbnails().getMedium().getUrl();



        }

        Log.e("video_id", video_id+"_");
        Log.e("video_name", video_name+"_");
        Log.e("video_desc", video_desc+"_");
        Log.e("video_image", video_image+"_");
        Log.e("channel_id", channel_id+"_");
        Log.e("channel_name", channel_name+"_");
        Log.e("channel_desc", channel_desc+"_");
        Log.e("channel_image", channel_image+"_");

        if (video_desc.length()>450){
            video_desc = video_desc.substring(0,450);
        }

        if (channel_desc.length()>450){
            channel_desc = channel_desc.substring(0,450);
        }


        Api.getService(Tags.base_url)
                .updateProfile("Bearer " + userModel.getToken(), userModel.getId(), userModel.getGoogle_id(),video_name ,video_image ,video_desc ,video_id ,channel_id ,channel_id ,channel_name,channel_image , channel_desc,String.valueOf(interests_id))
                .enqueue(new Callback<LoginRegisterModel>() {
                    @Override
                    public void onResponse(Call<LoginRegisterModel> call, Response<LoginRegisterModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                LoginRegisterModel model = response.body();

                                UserModel.ChannelModel channelModel = null;
                                UserModel.VideoModel videoModel = null;
                                InterestsModel interestsModel = null;
                                if (model.getData().getChannel_id() != null) {
                                    channelModel = new UserModel.ChannelModel(model.getData().getChannel_id(), model.getData().getChannel_name(), model.getData().getChannel_description(), model.getData().getChannel_image());

                                }

                                if (model.getData().getChannel_video_link() != null) {
                                    videoModel = new UserModel.VideoModel(model.getData().getChannel_video_link(), model.getData().getChannel_video_name(), model.getData().getChannel_video_description(), model.getData().getChannel_video_image());

                                }

                                if (interests_id != 0) {
                                    String interests = "";
                                    switch (userModel.getInterestsModel().getId()) {
                                        case 1:
                                            interests = getString(R.string.sports);
                                            break;
                                        case 2:
                                            interests = getString(R.string.games);

                                            break;
                                        case 3:
                                            interests = getString(R.string.cooks);

                                            break;
                                        case 4:
                                            interests = getString(R.string.writes);

                                            break;

                                    }
                                    interestsModel = new InterestsModel(model.getData().getInterested(), interests);

                                }


                                userModel = new UserModel(model.getData().getId(), model.getData().getGoogle_id(), model.getData().getEmail(), model.getData().getName(), model.getData().getImage(), model.getData().getCoins(), model.getData().getCode(), model.getData().getUser_type(), model.getData().getIs_vip(), model.getData().getToken(), channelModel, videoModel, interestsModel);
                                binding.setUserModel(userModel);
                                preferences.create_update_userdata(activity, userModel);

                                updateUI();
                            }
                        }else {
                            try {
                                Log.e("code",response.code()+"__"+response.errorBody().string()+"__");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginRegisterModel> call, Throwable t) {
                        dialog.dismiss();
                        Log.e("error",t.getMessage()+"_");
                    }
                });
    }


}