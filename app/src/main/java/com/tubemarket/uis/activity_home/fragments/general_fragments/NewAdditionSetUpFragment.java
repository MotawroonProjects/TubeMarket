package com.tubemarket.uis.activity_home.fragments.general_fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.tubemarket.R;
import com.tubemarket.adapters.SpinnerCountAdapter;
import com.tubemarket.databinding.FragmentNewAdditionSetUpBinding;
import com.tubemarket.models.CoinsDataModel;
import com.tubemarket.models.StatusResponse;
import com.tubemarket.models.UserModel;
import com.tubemarket.models.ViewsSecondsModel;
import com.tubemarket.preferences.Preferences;
import com.tubemarket.remote.Api;
import com.tubemarket.share.Common;
import com.tubemarket.tags.Tags;
import com.tubemarket.uis.activity_home.HomeActivity;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewAdditionSetUpFragment extends Fragment {

    private FragmentNewAdditionSetUpBinding binding;
    private HomeActivity activity;
    private UserModel userModel;
    private Preferences preferences;
    private String videoId = "";
    private SpinnerCountAdapter viewsAdapter,secondsAdapter;
    private List<String> viewsList,secondsList;
    private String views="",seconds="";
    private CoinsDataModel.CoinsModel coinsModel;
    private String have_discount ="no";
    private int discount_coins =0;
    private int total_coins =0;

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
        binding.setUserModel(userModel);
        viewsList = new ArrayList<>();
        secondsList = new ArrayList<>();
        viewsAdapter = new SpinnerCountAdapter(viewsList,activity);
        binding.spinnerViews.setAdapter(viewsAdapter);


        secondsAdapter = new SpinnerCountAdapter(secondsList,activity);
        binding.spinnerSeconds.setAdapter(secondsAdapter);
        binding.spinnerViews.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                views = viewsList.get(i);
                calculateCoins();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spinnerSeconds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                seconds = secondsList.get(i);
                calculateCoins();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.tvUpdate.setOnClickListener(v -> {
            if (userModel.getIs_vip().equals("yes")){
                have_discount="yes";
                calculateCoins();
            }
        });

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
            if (!videoId.isEmpty()&&coinsModel!=null){
                addVideo(v);
            }
        });

        getViewSecond();


    }

    private void getViewSecond(){
        Api.getService(Tags.base_url)
                .getViewSeconds("Bearer "+userModel.getToken())
                .enqueue(new Callback<ViewsSecondsModel>() {
                    @Override
                    public void onResponse(Call<ViewsSecondsModel> call, Response<ViewsSecondsModel> response) {

                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getData() != null ) {

                                activity.runOnUiThread(() -> {


                                    viewsList.clear();
                                    viewsList.addAll(response.body().getData().getVideo_view_numbers());
                                    viewsAdapter.notifyDataSetChanged();

                                    secondsList.clear();
                                    secondsList.addAll(response.body().getData().getSeconds());
                                    secondsAdapter.notifyDataSetChanged();



                                });
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
                    public void onFailure(Call<ViewsSecondsModel> call, Throwable t) {


                    }
                });
    }

    private void calculateCoins(){
        if (views.isEmpty()||seconds.isEmpty()){
            return;
        }
        Api.getService(Tags.base_url)
                .calculateCoin("Bearer "+userModel.getToken(),views,seconds)
                .enqueue(new Callback<CoinsDataModel>() {
                    @Override
                    public void onResponse(Call<CoinsDataModel> call, Response<CoinsDataModel> response) {

                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getData() != null ) {
                                updateCoins(response.body());

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
                    public void onFailure(Call<CoinsDataModel> call, Throwable t) {


                    }
                });
    }

    private void updateCoins(CoinsDataModel body) {
        discount_coins = 0;
        total_coins =0;
        coinsModel = body.getData();
        if (have_discount.equals("yes")){
            discount_coins = (int) (Integer.parseInt(coinsModel.getCampaign_coins())*.10);
        }

        total_coins = Integer.parseInt(coinsModel.getCampaign_coins())-discount_coins;
        binding.setCoins(total_coins+"");

    }

    private void  addVideo(View view){
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .addVideo("Bearer "+userModel.getToken(),userModel.getId(),videoId,seconds,views,coinsModel.getCampaign_coins(),coinsModel.getProfit_coins(),have_discount,discount_coins+"")
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {

                            if (response.body().getStatus()==200){
                                Navigation.findNavController(view).popBackStack();
                                Common.CreateDialogAlert(activity,getString(R.string.admin_review));

                            }else if (response.body().getStatus()==408){
                                Toast.makeText(activity, R.string.not_enough_coins, Toast.LENGTH_SHORT).show();
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
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        dialog.dismiss();

                    }
                });
    }




}