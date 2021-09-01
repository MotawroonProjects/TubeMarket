package com.app.tubemarket.uis.activity_home.fragments.bottom_nav_fragment;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.tubemarket.R;
import com.app.tubemarket.databinding.FragmentNewAdditionBinding;
import com.app.tubemarket.databinding.FragmentSubscriptionBinding;
import com.app.tubemarket.models.MyVideosModel;
import com.app.tubemarket.models.UserModel;
import com.app.tubemarket.models.VideoDataModel;
import com.app.tubemarket.preferences.Preferences;
import com.app.tubemarket.remote.Api;
import com.app.tubemarket.share.Common;
import com.app.tubemarket.tags.Tags;
import com.app.tubemarket.uis.activity_home.HomeActivity;
import com.app.tubemarket.uis.activity_web_view.WebViewActivity;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SubscriptionFragment extends Fragment {

    private FragmentSubscriptionBinding binding;
    private HomeActivity activity;
    private UserModel userModel;
    private Preferences preferences;
    private String videoId="";
    private String lang;
    private List<MyVideosModel> list;
    private int index = 0;
    private int page =1;
    private MyVideosModel myVideosModel;
    private ActivityResultLauncher<Intent> launcher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_subscription,container,false);
        initView();
        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode()== Activity.RESULT_OK){
                    index = 0;
                    page = 1;
                    getVideos(page, index);
                }
            }
        });
    }

    private void initView()
    {
        list  = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);


        binding.flSubscribe.setOnClickListener(v -> {
            String vidUrl ="https://youtu.be/"+videoId;
            String url = "https://accounts.google.com/ServiceLogin?service=youtube";
            Bundle bundle = new Bundle();
            bundle.putString("url",url);
            bundle.putString("vidUrl",vidUrl);
            bundle.putSerializable("data", myVideosModel);


            Intent intent = new Intent(activity, WebViewActivity.class);
            intent.putExtras(bundle);
            launcher.launch(intent);

        });

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

        getVideos(1, 0);
    }
    private void getVideos(int newPage, int newIndex)
    {
        binding.setModel(null);
        binding.llNext.setVisibility(View.INVISIBLE);
        binding.flSubscribe.setVisibility(View.INVISIBLE);
        Api.getService(Tags.base_url)
                .getChannel("Bearer " + userModel.getToken(), userModel.getId(), "on", "20", "desc", newPage)
                .enqueue(new Callback<VideoDataModel>() {
                    @Override
                    public void onResponse(Call<VideoDataModel> call, Response<VideoDataModel> response) {
                        binding.llNext.setVisibility(View.VISIBLE);
                        binding.flSubscribe.setVisibility(View.VISIBLE);

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
                        binding.flSubscribe.setVisibility(View.INVISIBLE);

                        Log.e("error", t.getMessage() + "_");

                    }
                });
    }
    private void loadVideo(MyVideosModel myVideosModel)
    {
        this.myVideosModel = myVideosModel;
        videoId = myVideosModel.getLink();
        binding.setModel(myVideosModel);
        /*binding.tvCoins.setText(myVideosModel.getProfit_coins());
        binding.tvCoins.measure(0,0);

        int w =binding.tvCoins.getMeasuredWidth();

        int width = (pxToDp(w)*2);
        startAnimation(0,width);*/

    }

    private void startAnimation(int width,int reqWidth){
        ValueAnimator animator = ValueAnimator.ofInt(width,reqWidth);
        animator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams params = binding.tvCoins.getLayoutParams();
            params.width = value;
            binding.tvCoins.requestLayout();
        });
        animator.setDuration(200);
        animator.start();
    }

    private int pxToDp(int px){
        return (int) (px/getResources().getDisplayMetrics().density);
    }


}