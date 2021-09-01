package com.app.tubemarket.uis.activity_home.fragments.general_fragments;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.app.tubemarket.R;
import com.app.tubemarket.adapters.SpinnerAdCostAdapter;
import com.app.tubemarket.adapters.SpinnerCountAdapter;
import com.app.tubemarket.databinding.FragmentGetSubscribersBinding;
import com.app.tubemarket.databinding.FragmentGetViewsBinding;
import com.app.tubemarket.models.AdCostDataModel;
import com.app.tubemarket.models.AdCostModel;
import com.app.tubemarket.models.CampaignDataModel;
import com.app.tubemarket.models.CoinsDataModel;
import com.app.tubemarket.models.CostResultModel;
import com.app.tubemarket.models.UserModel;
import com.app.tubemarket.models.VideoModel;
import com.app.tubemarket.preferences.Preferences;
import com.app.tubemarket.remote.Api;
import com.app.tubemarket.tags.Tags;
import com.app.tubemarket.uis.activity_home.HomeActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetViewsFragment extends Fragment {

    private FragmentGetViewsBinding binding;
    private HomeActivity activity;
    private UserModel userModel;
    private Preferences preferences;
    private VideoModel channelModel = null;
    private SpinnerCountAdapter dayAdapter;
    private List<String> dayList;
    private List<AdCostModel>secondsList;
    private SpinnerAdCostAdapter secondsAdapter;
    private String second = "0",view_num = "0";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_get_views,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        secondsList = new ArrayList<>();
        dayList = new ArrayList<>();
        binding.setCost("0");
        binding.tvCalc.setPaintFlags(binding.tvCalc.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        for (int index=1;index<9;index++)
        {
            if (index==1){
                dayList.add("45");
            }else {
                int day = index*30;
                dayList.add(day+"");



            }
        }
        secondsAdapter = new SpinnerAdCostAdapter(secondsList,activity);
        binding.spinnerSeconds.setAdapter(secondsAdapter);

        dayAdapter = new SpinnerCountAdapter(dayList,activity);
        binding.spinnerDays.setAdapter(dayAdapter);

        binding.spinnerSeconds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                second = secondsList.get(position).getSeconds();
                if (view_num.equals("0")){
                    binding.tvCalc.setVisibility(View.VISIBLE);
                    calculateCost();
                }else {
                    binding.tvCalc.setVisibility(View.GONE);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.edtNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()){
                    view_num = s.toString();
                    if (!second.equals("0")){
                        binding.tvCalc.setVisibility(View.VISIBLE);

                    }else {
                        binding.tvCalc.setVisibility(View.GONE);

                    }
                }else{
                    view_num = "0";
                    binding.tvCalc.setVisibility(View.GONE);
                    binding.setCost("0");
                }

            }
        });

        binding.tvCalc.setOnClickListener(v -> {
            if (!second.equals("0")&&!view_num.equals("0")){
                calculateCost();
            }
        });
        binding.btnAdd.setOnClickListener(v -> {
            String url = binding.edtUrl.getText().toString();
            String vidId = extractYTId(url);
            if (vidId!=null){
                getVideoById(vidId);
            }
        });



        getViewCost();

    }

    private void getViewCost() {
        Api.getService(Tags.base_url)
                .getViewsCost("Bearer " + userModel.getToken())
                .enqueue(new Callback<AdCostDataModel>() {
                    @Override
                    public void onResponse(Call<AdCostDataModel> call, Response<AdCostDataModel> response) {

                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                secondsList.clear();
                                activity.runOnUiThread(()->{
                                    secondsList.addAll(response.body().getData());
                                    secondsAdapter.notifyDataSetChanged();
                                });
                            }
                        } else {
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AdCostDataModel> call, Throwable t) {
                        Log.e("fail", t.getMessage());
                    }
                });

    }

    private void getVideoById(String vidId) {

        Api.getService(Tags.tube_base_url)
                .getYouTubeVideoById("snippet,contentDetails", vidId, Tags.tubeKey)
                .enqueue(new Callback<VideoModel>() {
                    @Override
                    public void onResponse(Call<VideoModel> call, Response<VideoModel> response) {
                        if (response.isSuccessful()){
                            if (response.body()!=null&&response.body().getItems()!=null&&response.body().getItems().size()>0){
                                addViews();
                            }
                        }else {
                            try {
                                Log.e("error", response.code()+"__"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<VideoModel> call, Throwable t) {
                        Log.e("failed", t.getMessage()+"__");
                    }
                });
    }

    private void calculateCost(){
        Api.getService(Tags.base_url)
                .calculateViewCost("Bearer "+userModel.getToken(),view_num+"",second+"")
                .enqueue(new Callback<CostResultModel>() {
                    @Override
                    public void onResponse(Call<CostResultModel> call, Response<CostResultModel> response) {

                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getData() != null ) {
                                binding.setCost(response.body().getData());
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
                    public void onFailure(Call<CostResultModel> call, Throwable t) {

                        Log.e("failed", t.getMessage()+"__");

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

    private void addViews() {



    }


}