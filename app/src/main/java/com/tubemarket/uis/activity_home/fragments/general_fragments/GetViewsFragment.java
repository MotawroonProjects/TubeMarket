package com.tubemarket.uis.activity_home.fragments.general_fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.tubemarket.R;
import com.tubemarket.adapters.SpinnerAdCostAdapter;
import com.tubemarket.adapters.SpinnerCountAdapter;
import com.tubemarket.databinding.FragmentGetSubscribersBinding;
import com.tubemarket.databinding.FragmentGetViewsBinding;
import com.tubemarket.models.AdCostDataModel;
import com.tubemarket.models.AdCostModel;
import com.tubemarket.models.AdPayModel;
import com.tubemarket.models.CostResultModel;
import com.tubemarket.models.MessageResponseModel;
import com.tubemarket.models.UserModel;
import com.tubemarket.models.VideoModel;
import com.tubemarket.preferences.Preferences;
import com.tubemarket.remote.Api;
import com.tubemarket.share.Common;
import com.tubemarket.tags.Tags;
import com.tubemarket.uis.activity_home.HomeActivity;
import com.tubemarket.uis.activity_view.ViewActivity;

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
    private String second = "0",view_num = "0",total="0",day="0";
    private ActivityResultLauncher<Intent> launcher;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_get_views,container,false);
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
                    Navigation.findNavController(binding.getRoot()).popBackStack();

                }
            }
        });
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
    /*    binding.spinnerDays.setAdapter(dayAdapter);

        binding.spinnerDays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                day = dayList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/
        binding.spinnerSeconds.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                second = secondsList.get(position).getSeconds();
                if (!view_num.equals("0")){
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
                    if (!second.equals("0")&&!view_num.equals("0")){
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
            if (vidId!=null&&!total.equals("0")&&!second.equals("0")&&!view_num.equals("0")){
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
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.tube_base_url)
                .getYouTubeVideoById("snippet,contentDetails", vidId, Tags.tubeKey)
                .enqueue(new Callback<VideoModel>() {
                    @Override
                    public void onResponse(Call<VideoModel> call, Response<VideoModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()){
                            if (response.body()!=null&&response.body().getItems()!=null&&response.body().getItems().size()>0){

                                String channel_id = response.body().getItems().get(0).getSnippet().getChannelId();
                                if (channel_id!=null){
                                    getChannelById(channel_id,vidId,dialog);

                                }
                            }else {
                                Toast.makeText(activity, R.string.in_url, Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            Toast.makeText(activity, R.string.in_url, Toast.LENGTH_SHORT).show();

                            try {
                                Log.e("error", response.code()+"__"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<VideoModel> call, Throwable t) {
                        dialog.dismiss();

                        Log.e("failed", t.getMessage()+"__");
                    }
                });
    }

    private void getChannelById(String channelId,String vidId,ProgressDialog dialog) {
        Api.getService(Tags.tube_base_url)
                .getYouTubeChannelById("snippet", channelId, Tags.tubeKey)
                .enqueue(new Callback<VideoModel>() {
                    @Override
                    public void onResponse(Call<VideoModel> call, Response<VideoModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getItems() != null) {

                                if (response.body().getItems().size() > 0){
                                    channelModel = response.body();
                                    UserModel.ChannelModel userChannel = new UserModel.ChannelModel(channelModel.getItems().get(0).getId(), channelModel.getItems().get(0).getSnippet().getLocalized().getTitle(), channelModel.getItems().get(0).getSnippet().getLocalized().getDescription(), channelModel.getItems().get(0).getSnippet().getThumbnails().getMedium().getUrl());

                                    addViews(vidId,dialog,userChannel.getTitle(),userChannel.getUrl());

                                }


                            }else {
                                dialog.dismiss();
                                Toast.makeText(activity, R.string.in_url, Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            dialog.dismiss();
                            Toast.makeText(activity, R.string.in_url, Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<VideoModel> call, Throwable t) {
                        dialog.dismiss();

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
                                total = response.body().getData();
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

    private void addViews(String vidId, ProgressDialog dialog, String channel_name, String channel_image) {


        Api.getService(Tags.base_url)
                .addViews("Bearer "+userModel.getToken(),userModel.getId(),view_num,day,total,vidId,second,channel_name,channel_image)
                .enqueue(new Callback<AdPayModel>() {
                    @Override
                    public void onResponse(Call<AdPayModel> call, Response<AdPayModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()&&response.body()!=null&&response.body().getStatus()==200){
                            Intent intent = new Intent(activity, ViewActivity.class);
                            intent.putExtra("url", response.body().getData().getPay_link());
                            intent.putExtra("data",new MessageResponseModel.Data());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            launcher.launch(intent);

                        }else {
                            try {
                                Log.e("error", response.code()+"__"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AdPayModel> call, Throwable t) {
                        dialog.dismiss();

                        Log.e("failed", t.getMessage()+"__");
                    }
                });

    }


}