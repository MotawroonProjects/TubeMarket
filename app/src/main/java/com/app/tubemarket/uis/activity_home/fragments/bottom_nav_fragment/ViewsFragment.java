package com.app.tubemarket.uis.activity_home.fragments.bottom_nav_fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.tubemarket.R;
import com.app.tubemarket.adapters.SpinnerCountAdapter;
import com.app.tubemarket.databinding.FragmentNewAdditionSetUpBinding;
import com.app.tubemarket.databinding.FragmentViewsBinding;
import com.app.tubemarket.models.UserModel;
import com.app.tubemarket.preferences.Preferences;
import com.app.tubemarket.uis.activity_home.HomeActivity;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

import java.util.ArrayList;
import java.util.List;


public class ViewsFragment extends Fragment {

    private FragmentViewsBinding binding;
    private HomeActivity activity;
    private UserModel userModel;
    private Preferences preferences;
    private String videoId = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_views,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        videoId="QPaI8xVWBjY";
//        //binding.youtubePlayerView.enableBackgroundPlayback(true);
//        //getLifecycle().addObserver(binding.youtubePlayerView);
//     //   binding.youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
//            @Override
//            public void onReady(YouTubePlayer youTubePlayer) {
//                youTubePlayer.loadVideo(videoId,0);
//                youTubePlayer.pause();
//            }
//
//            @Override
//            public void onCurrentSecond(YouTubePlayer youTubePlayer, float second) {
//            }
//        });




    }


}