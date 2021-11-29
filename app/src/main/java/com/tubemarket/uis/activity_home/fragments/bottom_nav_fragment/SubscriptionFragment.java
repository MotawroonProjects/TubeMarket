package com.tubemarket.uis.activity_home.fragments.bottom_nav_fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tubemarket.R;
import com.tubemarket.databinding.FragmentNewAdditionBinding;
import com.tubemarket.databinding.FragmentSubscriptionBinding;
import com.tubemarket.models.AdsViewDataModel;
import com.tubemarket.models.GeneralAdsModel;
import com.tubemarket.models.MyVideosModel;
import com.tubemarket.models.UserModel;
import com.tubemarket.models.VideoDataModel;
import com.tubemarket.preferences.Preferences;
import com.tubemarket.remote.Api;
import com.tubemarket.tags.Tags;
import com.tubemarket.uis.activity_home.HomeActivity;
import com.tubemarket.uis.activity_web_view.WebViewActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private int subsCount=0;


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
                    subsCount +=1;
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

            if (subsCount<3){
                String vidUrl ="https://youtu.be/"+videoId;
                String url = "https://accounts.google.com/ServiceLogin?service=youtube";
                Bundle bundle = new Bundle();
                bundle.putString("url",url);
                bundle.putString("vidUrl",vidUrl);

                GeneralAdsModel generalAdsModel = new GeneralAdsModel(myVideosModel.getId(), myVideosModel.getTimer_limit(), myVideosModel.getProfit_coins(),Tags.NORMAL_AD);
                bundle.putSerializable("data", generalAdsModel);

                Intent intent = new Intent(activity, WebViewActivity.class);
                intent.putExtras(bundle);
                launcher.launch(intent);
            }else {
                subsCount = 0;
                activity.adMob();
            }

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
        getSubscriptionAds();
    }
    private void getVideos(int newPage, int newIndex)
    {

        binding.setModel(null);
        binding.llNext.setVisibility(View.INVISIBLE);
        binding.flSubscribe.setVisibility(View.INVISIBLE);
        binding.progBar.setVisibility(View.VISIBLE);
        Api.getService(Tags.base_url)
                .getChannel("Bearer " + userModel.getToken(), userModel.getId(), "on", "20", "desc", newPage)
                .enqueue(new Callback<VideoDataModel>() {
                    @Override
                    public void onResponse(Call<VideoDataModel> call, Response<VideoDataModel> response) {
                        binding.llNext.setVisibility(View.VISIBLE);
                        binding.flSubscribe.setVisibility(View.VISIBLE);
                        binding.progBar.setVisibility(View.GONE);


                        if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                            if (response.body().getData() != null && response.body().getData().getData().size() > 0) {
                                page = newPage;
                                list.addAll(response.body().getData().getData());
                                index = newIndex;
                                loadVideo(list.get(index));

                                Log.e("size", list.size()+"_");

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
                        binding.progBar.setVisibility(View.GONE);

                        Log.e("error", t.getMessage() + "_");

                    }
                });
    }
    private void loadVideo(MyVideosModel myVideosModel)
    {
        this.myVideosModel = myVideosModel;
        videoId = myVideosModel.getLink();
        binding.setModel(myVideosModel);

    }

    private void getSubscriptionAds() {
        Api.getService(Tags.base_url)
                .getAllSubscriptions("Bearer " + userModel.getToken(), userModel.getId(), "desc" )
                .enqueue(new Callback<AdsViewDataModel>() {
                    @Override
                    public void onResponse(Call<AdsViewDataModel> call, Response<AdsViewDataModel> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                            if (response.body().getData() != null && response.body().getData().size() > 0) {

                                activity.loadSubscriptionAds(response.body().getData().get(0));

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



}