package com.tubemarket.uis.activity_home.fragments.bottom_nav_fragment;

import android.app.AlertDialog;
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

import com.tubemarket.R;
import com.tubemarket.databinding.DialogAlertBinding;
import com.tubemarket.databinding.DialogCoinsBinding;
import com.tubemarket.databinding.FragmentViewsBinding;
import com.tubemarket.models.AdsViewDataModel;
import com.tubemarket.models.AdsViewModel;
import com.tubemarket.models.MyVideosModel;
import com.tubemarket.models.StatusResponse;
import com.tubemarket.models.UserModel;
import com.tubemarket.models.VideoDataModel;
import com.tubemarket.preferences.Preferences;
import com.tubemarket.remote.Api;
import com.tubemarket.tags.Tags;
import com.tubemarket.uis.activity_home.HomeActivity;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewsFragment extends Fragment {

    private FragmentViewsBinding binding;
    private HomeActivity activity;
    private UserModel userModel;
    private Preferences preferences;
    private String lang = "";
    private int index = 0;
    private int page =1;
    private List<MyVideosModel> list;
    private int seconds = 0,secondsAds=0;
    private Timer timer;
    private TimerTask timerTask;
    private YouTubePlayer youTubePlayer;
    private AbstractYouTubePlayerListener listener;
    private String videoId="",videoIdAds="";
    private MyVideosModel myVideosModel;
    private AdsViewModel adsViewModel;
    private int viewCount=0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_views, container, false);
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
        lang = Paper.book().read("lang", "ar");
        binding.setLang(lang);


        binding.llNext.setOnClickListener(view -> {
            int newIndex = index+1;
            if (newIndex < list.size()) {
                index +=1;
                loadVideo(list.get(newIndex));


            }else {
                int newPage = page+1;

                getVideos(newPage,newIndex);

            }

        });
        getVideosAds();

        new Handler().postDelayed(()->getVideos(1, 0), 1000);



    }

    private void getVideos(int newPage, int newIndex) {
        Log.e("ss", newIndex+"__");
        binding.llNext.setVisibility(View.INVISIBLE);
        Api.getService(Tags.base_url)
                .getViewsVideo("Bearer " + userModel.getToken(), userModel.getId(), "on", "20", "desc", newPage)
                .enqueue(new Callback<VideoDataModel>() {
                    @Override
                    public void onResponse(Call<VideoDataModel> call, Response<VideoDataModel> response) {
                        binding.llNext.setVisibility(View.VISIBLE);
                        if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                            if (response.body().getData() != null && response.body().getData().getData().size() > 0) {
                                page = newPage;
                                list.addAll(response.body().getData().getData());
                                index = newIndex;
                                loadVideo(list.get(index));

                            }
                        } else {
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string() + "_");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<VideoDataModel> call, Throwable t) {
                        binding.llNext.setVisibility(View.VISIBLE);

                        Log.e("error", t.getMessage() + "_");

                    }
                });
    }

    private void loadVideo(MyVideosModel myVideosModel) {

        this.myVideosModel = myVideosModel;
        seconds = 0;
        videoId = myVideosModel.getLink();
        binding.setCoins(myVideosModel.getProfit_coins());
        binding.setSecond(myVideosModel.getTimer_limit());
        seconds = Integer.parseInt(myVideosModel.getTimer_limit());
        binding.youtubePlayerView.enableBackgroundPlayback(true);

        getLifecycle().addObserver(binding.youtubePlayerView);
        listener = new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {

                ViewsFragment.this.youTubePlayer = youTubePlayer;
                youTubePlayer.loadVideo(videoId, 0);
                youTubePlayer.pause();


            }

            @Override
            public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {
                super.onStateChange(youTubePlayer, state);

                if (state.name().equals(PlayerConstants.PlayerState.PLAYING.name())) {

                    startTime();
                } else {
                    stopTimer();
                }
            }
        };

        if (youTubePlayer!=null){
            listener.onReady(youTubePlayer);
        }else {
            binding.youtubePlayerView.addYouTubePlayerListener(listener);
        }




    }

    private void getVideosAds() {
        Api.getService(Tags.base_url)
                .getAdsView("Bearer " + userModel.getToken(), userModel.getId(), "desc" )
                .enqueue(new Callback<AdsViewDataModel>() {
                    @Override
                    public void onResponse(Call<AdsViewDataModel> call, Response<AdsViewDataModel> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                            if (response.body().getData() != null && response.body().getData().size() > 0) {

                                activity.loadVideoAds(response.body().getData().get(0));

                            }
                        } else {
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string() + "_");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AdsViewDataModel> call, Throwable t) {

                        Log.e("error", t.getMessage() + "_");

                    }
                });
    }



    private void view(){
        Api.getService(Tags.base_url)
                .view("Bearer "+userModel.getToken(), userModel.getId(),myVideosModel.getId(),myVideosModel.getProfit_coins(), myVideosModel.getTimer_limit())
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Log.e("ddd", response.body().getStatus()+"__");
                            if (response.body().getStatus() == 200) {

                                viewCount +=1;
                                activity.getUserProfile();
                                createDialog(myVideosModel.getProfit_coins());
                            }
                        }else {
                            try {
                                Log.e("resCode", response.code()+"__"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        Log.e("ex", t.getMessage());
                    }
                });
    }

    private void stopTimer() {
        if (timer!=null&&timerTask!=null){
            timerTask.cancel();
            timer.cancel();
            timer.purge();
            timer=null;
            timerTask=null;
        }

    }

    private void startTime() {
        if (seconds>0){
            timer = new Timer();
            timerTask = new Task();
            timer.scheduleAtFixedRate(timerTask,1000,1000);
        }

    }

    public class Task extends TimerTask {

        @Override
        public void run() {
            if (seconds>0){
                seconds--;
                binding.setSecond(seconds+"");
            }else {
                if (viewCount<3){
                    view();

                }else {
                    viewCount = 0;
                    activity.adMob();
                }
                stopTimer();
            }
        }
    }


    private void createDialog (String coins){
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .create();

        DialogCoinsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_coins, null, false);

        binding.tvMsg.setText(coins+" "+getString(R.string.currency));
        binding.cardCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.inset_dialog);
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();

        new Handler().postDelayed((Runnable) () -> {
            dialog.dismiss();
        }, 3000);
    }

}