package com.app.tubemarket.uis.activity_home.fragments.bottom_nav_fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.app.tubemarket.R;
import com.app.tubemarket.databinding.FragmentViewsBinding;
import com.app.tubemarket.models.MyVideosModel;
import com.app.tubemarket.models.UserModel;
import com.app.tubemarket.models.VideoDataModel;
import com.app.tubemarket.preferences.Preferences;
import com.app.tubemarket.remote.Api;
import com.app.tubemarket.tags.Tags;
import com.app.tubemarket.uis.activity_home.HomeActivity;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewsFragment extends Fragment {

    private FragmentViewsBinding binding;
    private HomeActivity activity;
    private UserModel userModel;
    private Preferences preferences;
    private String lang="";
    private int page = 1;
    private List<MyVideosModel> list;
    private int seconds = 0;
    private boolean isPlaying = false;
    private Handler handler;
    private Runnable runnable;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_views,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        list = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        binding.youtubePlayerView.enableBackgroundPlayback(true);
        getLifecycle().addObserver(binding.youtubePlayerView);
        binding.llNext.setOnClickListener(view -> {
            int newPage = page++;
            if (newPage<list.size()-2){
                getVideos(newPage);
            }else {
                loadVideo(list.get(newPage));
            }

        });

        binding.youtubePlayerView.addYouTubePlayerListener(new YouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {

            }

            @Override
            public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState playerState) {

            }

            @Override
            public void onPlaybackQualityChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackQuality playbackQuality) {

            }

            @Override
            public void onPlaybackRateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackRate playbackRate) {

            }

            @Override
            public void onError(YouTubePlayer youTubePlayer, PlayerConstants.PlayerError playerError) {

            }

            @Override
            public void onCurrentSecond(YouTubePlayer youTubePlayer, float v) {

            }

            @Override
            public void onVideoDuration(YouTubePlayer youTubePlayer, float v) {

            }

            @Override
            public void onVideoLoadedFraction(YouTubePlayer youTubePlayer, float v) {

            }

            @Override
            public void onVideoId(YouTubePlayer youTubePlayer, String s) {

            }

            @Override
            public void onApiChange(YouTubePlayer youTubePlayer) {

            }
        });
        getVideos(1);



    }

    private void getVideos(int newPage)
    {
        binding.llNext.setVisibility(View.INVISIBLE);
        Api.getService(Tags.base_url)
                .getViewsVideo("Bearer "+userModel.getToken(), userModel.getId(), "on","20","desc",page)
                .enqueue(new Callback<VideoDataModel>() {
                    @Override
                    public void onResponse(Call<VideoDataModel> call, Response<VideoDataModel> response) {
                        binding.llNext.setVisibility(View.VISIBLE);
                        if (response.isSuccessful() && response.body() != null&&response.body().getStatus()==200) {
                            if (response.body().getData() != null &&response.body().getData().getData().size()>0) {
                                page = newPage;
                                list.addAll(response.body().getData().getData());
                                if (page==1){
                                    loadVideo(list.get(0));
                                }
                            }
                        }else {
                            try {
                                Log.e("error",response.code()+"__"+response.errorBody().string()+"_");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<VideoDataModel> call, Throwable t) {
                        binding.llNext.setVisibility(View.VISIBLE);

                        Log.e("error",t.getMessage()+"_");

                    }
                });
    }

    private void loadVideo(MyVideosModel myVideosModel){
        binding.setCoins(myVideosModel.getCampaign_coins());
        binding.setSecond(myVideosModel.getTimer_limit());
        seconds = Integer.parseInt(myVideosModel.getTimer_limit());


        binding.youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(myVideosModel.getLink(),0);
                youTubePlayer.pause();
                handler = new Handler();
                runnable = () -> {
                    if (seconds>0){
                        seconds--;
                        binding.setSecond(seconds+"");
                    }else {
                        isPlaying = false;
                    }
                };

            }

            @Override
            public void onCurrentSecond(YouTubePlayer youTubePlayer, float second) {

            }


        });
    }

    private void updateSeconds() {
        while (isPlaying){
            handler.postDelayed(runnable,1000);
        }
    }

}