package com.app.tubemarket.uis.activity_home.fragments.general_fragments;

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

import com.app.tubemarket.R;
import com.app.tubemarket.adapters.SpinnerCountAdapter;
import com.app.tubemarket.adapters.SpinnerInterestsAdapter;
import com.app.tubemarket.databinding.FragmentGetSubscribersBinding;
import com.app.tubemarket.databinding.FragmentProfileBinding;
import com.app.tubemarket.models.AdPayModel;
import com.app.tubemarket.models.ChannelUrlModel;
import com.app.tubemarket.models.CostResultModel;
import com.app.tubemarket.models.InterestsModel;
import com.app.tubemarket.models.MessageResponseModel;
import com.app.tubemarket.models.StatusResponse;
import com.app.tubemarket.models.UserModel;
import com.app.tubemarket.models.VideoDataModel;
import com.app.tubemarket.models.VideoModel;
import com.app.tubemarket.preferences.Preferences;
import com.app.tubemarket.remote.Api;
import com.app.tubemarket.share.Common;
import com.app.tubemarket.tags.Tags;
import com.app.tubemarket.uis.activity_home.HomeActivity;
import com.app.tubemarket.uis.activity_view.ViewActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetSubscribersFragment extends Fragment {
    private FragmentGetSubscribersBinding binding;
    private HomeActivity activity;
    private UserModel userModel;
    private Preferences preferences;
    private VideoModel channelModel = null;
    private UserModel.ChannelModel userChannel;
    private SpinnerCountAdapter secondsAdapter;
    private List<String> secondsList;
    private String subscribe_num = "0",seconds="0",total="0",channel_id="";
    private ActivityResultLauncher<Intent> launcher;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_get_subscribers, container, false);
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
        binding.setCost("0");
        binding.tvCalc.setPaintFlags(binding.tvCalc.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        for (int index = 1; index < 9; index++) {
            if (index == 1) {
                secondsList.add("45");
            } else {
                int second = index * 30;
                secondsList.add(second + "");


            }
        }
        secondsAdapter = new SpinnerCountAdapter(secondsList, activity);
        /*binding.spinnerSubscriptions.setAdapter(secondsAdapter);

        binding.spinnerSubscriptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seconds = secondsList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/
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
                    subscribe_num =s.toString();
                    binding.tvCalc.setVisibility(View.VISIBLE);

                }else{
                    subscribe_num = "0";
                    binding.tvCalc.setVisibility(View.GONE);
                    binding.setCost("0");
                }

            }
        });

        binding.tvCalc.setOnClickListener(v -> {
            if (!subscribe_num.equals("0")){
                calculateCost();
            }
        });

        binding.btnConfirm.setOnClickListener(v -> {
            String url = binding.edtUrl.getText().toString().trim();
            Log.e("dsaf", url+"__");

            if (isChannelUrl(url)){
                Log.e("xx","xxx");
                getChannelIdFromUrl(url);
            }else {
                String channelId = extractChannelId(url);
                if (channelId != null) {
                    binding.btnConfirm.setEnabled(false);
                    getChannelById(channelId);

                }
            }

        });

        binding.btnAdd.setOnClickListener(v -> {
            seconds = binding.edtSeconds.getText().toString();

            if (channelModel!=null&&!subscribe_num.equals("0")&&!total.equals("0")&&!seconds.equals("0")){
                addSubscribes();
            }
        });

    }

    private void addSubscribes() {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .addSubscribes("Bearer "+userModel.getToken(),userModel.getId(),subscribe_num,seconds,total,channel_id,userChannel.getTitle(),userChannel.getUrl(),seconds)
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
                            Log.e("rasdasd", response.body().getStatus()+"__");
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

                        //yusufseries
                        Log.e("failed", t.getMessage()+"__");
                    }
                });

    }

    private void getChannelById(String channelId) {
        binding.btnConfirm.setEnabled(false);
        channel_id = channelId;
        Api.getService(Tags.tube_base_url)
                .getYouTubeChannelById("snippet", channelId, Tags.tubeKey)
                .enqueue(new Callback<VideoModel>() {
                    @Override
                    public void onResponse(Call<VideoModel> call, Response<VideoModel> response) {
                        binding.btnConfirm.setEnabled(true);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getItems() != null) {

                                if (response.body().getItems().size() > 0){
                                    channelModel = response.body();
                                    userChannel = new UserModel.ChannelModel(channelModel.getItems().get(0).getId(), channelModel.getItems().get(0).getSnippet().getLocalized().getTitle(), channelModel.getItems().get(0).getSnippet().getLocalized().getDescription(), channelModel.getItems().get(0).getSnippet().getThumbnails().getMedium().getUrl());
                                    binding.setModel(userChannel);
                                }


                            }else {
                                Toast.makeText(activity, R.string.in_url, Toast.LENGTH_SHORT).show();

                            }
                        }else {
                            Toast.makeText(activity, R.string.in_url, Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<VideoModel> call, Throwable t) {
                        binding.btnConfirm.setEnabled(true);

                    }
                });
    }

    private void getChannelIdFromUrl(String url) {
        binding.btnConfirm.setEnabled(false);

        Api.getService(Tags.tube_search_base_url)
                .getChannelIdFromUrl("snippet",url,Tags.tubeKey)
                .enqueue(new Callback<ChannelUrlModel>() {
                    @Override
                    public void onResponse(Call<ChannelUrlModel> call, Response<ChannelUrlModel> response) {
                        binding.btnConfirm.setEnabled(true);

                        if (response.isSuccessful()){
                            Log.e("dd", "yyy");
                            if (response.body().getItems().size()>0){
                                Log.e("nn", "nn");

                                String channelId = response.body().getItems().get(0).getSnippet().getChannelId();
                                getChannelById(channelId);
                            }
                        }else {
                            try {
                                Log.e("errr", response.code()+"__"+response.errorBody().string()+"__");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ChannelUrlModel> call, Throwable t) {
                        Log.e("sdada", t.getMessage()+"_");
                    }
                });
    }


    private String extractChannelId(String url) {
        String channelId = null;
        Pattern pattern = Pattern.compile("^https?://.*(?:youtube\\.com/channel/)([^#&?]*)$",
                Pattern.CASE_INSENSITIVE);


        Matcher matcher = pattern.matcher(url);
        if (matcher.matches()) {
            channelId = matcher.group(1);
        }

        return channelId;

    }
    private boolean isChannelUrl(String url) {
        boolean isUrl = false;
        Pattern pattern = Pattern.compile("^https?://.*(?:youtube\\.com/c/)([^#&?]*)$",
                Pattern.CASE_INSENSITIVE);
        Pattern pattern2 = Pattern.compile("^https?://.*(?:youtube\\.com/user/)([^#&?]*)$",
                Pattern.CASE_INSENSITIVE);


        Matcher matcher = pattern.matcher(url);
        Matcher matcher2 = pattern2.matcher(url);

        if (matcher.matches()) {
            isUrl = true;
        }else if (matcher2.matches()){
            isUrl = true;
        }

        return isUrl;

    }


    private void calculateCost(){
        Api.getService(Tags.base_url)
                .calculateSubscribeCost("Bearer "+userModel.getToken(),subscribe_num+"")
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

}