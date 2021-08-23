package com.app.tubemarket.uis.activity_home.fragments.bottom_nav_fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.tubemarket.R;
import com.app.tubemarket.adapters.SpinnerCountAdapter;
import com.app.tubemarket.databinding.FragmentNewAdditionSetUpBinding;
import com.app.tubemarket.models.UserModel;
import com.app.tubemarket.preferences.Preferences;
import com.app.tubemarket.tags.Tags;
import com.app.tubemarket.uis.activity_home.HomeActivity;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;

import java.util.ArrayList;
import java.util.List;


public class NewAdditionSetUpFragment extends Fragment {

    private FragmentNewAdditionSetUpBinding binding;
    private HomeActivity activity;
    private UserModel userModel;
    private Preferences preferences;
    private String videoId = "";
    private SpinnerCountAdapter viewsAdapter,secondsAdapter;
    private List<String> viewsList,secondsList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle!=null){
            videoId = bundle.getString("videoId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_new_addition_set_up,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        viewsList = new ArrayList<>();
        secondsList = new ArrayList<>();

        viewsList.add("10");
        secondsList.add("45");

        for (int index = 1;index<21;index++){
            int c = 50*index;
            viewsList.add(c+"");
        }
        viewsAdapter = new SpinnerCountAdapter(viewsList,activity);
        binding.spinnerViews.setAdapter(viewsAdapter);


        for (int index = 2;index<31;index++){
            int c;
            c = 30*index;
            secondsList.add(c+"");
        }
        secondsList.add("1200");
        secondsList.add("1500");
        secondsList.add("1800");

        secondsAdapter = new SpinnerCountAdapter(secondsList,activity);
        binding.spinnerSeconds.setAdapter(secondsAdapter);

        binding.youtubePlayerView.enableBackgroundPlayback(true);
        getLifecycle().addObserver(binding.youtubePlayerView);
        binding.youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(videoId,0);
                youTubePlayer.pause();
            }

            @Override
            public void onCurrentSecond(YouTubePlayer youTubePlayer, float second) {
                youTubePlayer.pause();
            }
        });

        binding.btnCreateCampaign.setOnClickListener(v -> {


        });


    }





}