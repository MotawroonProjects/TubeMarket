package com.tubemarket.uis.activity_home.fragments.general_fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

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
import android.widget.Toast;

import com.tubemarket.R;
import com.tubemarket.databinding.FragmentChannelProfitBinding;
import com.tubemarket.databinding.FragmentGetSubscribersBinding;
import com.tubemarket.models.AdPayModel;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChannelProfitFragment extends Fragment {
    private FragmentChannelProfitBinding binding;
    private HomeActivity activity;
    private UserModel userModel;
    private Preferences preferences;
    private UserModel.ChannelModel userChannel;
    private ActivityResultLauncher<Intent> launcher;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_channel_profit, container, false);
        initView();
        return binding.getRoot();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Navigation.findNavController(binding.getRoot()).popBackStack();

            }
        });
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);

        binding.btnConfirm.setOnClickListener(v -> {
            String url = binding.edtUrl.getText().toString().trim();
            String channelId = extractChannelId(url);
            if (channelId != null) {
                binding.btnConfirm.setEnabled(false);
                getChannelById(channelId);

            }
        });

        binding.fab.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=+201090184189"));
            startActivity(intent);
        });
        binding.btnAdd.setOnClickListener(v -> {
            String phone = binding.edtNumber.getText().toString();
            if (userChannel != null && !phone.isEmpty()) {
                addLink(phone);
            }
        });


    }

    private void addLink(String phone) {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .addProfitChannel("Bearer " + userModel.getToken(), userModel.getId(), phone, userChannel.getId(), userChannel.getTitle(), userChannel.getUrl())
                .enqueue(new Callback<AdPayModel>() {
                    @Override
                    public void onResponse(Call<AdPayModel> call, Response<AdPayModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                            Intent intent = new Intent(activity, ViewActivity.class);
                            intent.putExtra("url", response.body().getData().getPay_link());
                            intent.putExtra("data", new MessageResponseModel.Data());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            launcher.launch(intent);

                        } else {
                            Log.e("rasdasd", response.body().getStatus() + "__");
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AdPayModel> call, Throwable t) {
                        dialog.dismiss();

                        //yusufseries
                        Log.e("failed", t.getMessage() + "__");
                    }
                });

    }


    private void getChannelById(String channelId) {
        Api.getService(Tags.tube_base_url)
                .getYouTubeChannelById("snippet", channelId, Tags.tubeKey)
                .enqueue(new Callback<VideoModel>() {
                    @Override
                    public void onResponse(Call<VideoModel> call, Response<VideoModel> response) {
                        binding.btnConfirm.setEnabled(true);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getItems() != null) {

                                if (response.body().getItems().size() > 0) {
                                    VideoModel channelModel = response.body();
                                    userChannel = new UserModel.ChannelModel(channelModel.getItems().get(0).getId(), channelModel.getItems().get(0).getSnippet().getLocalized().getTitle(), channelModel.getItems().get(0).getSnippet().getLocalized().getDescription(), channelModel.getItems().get(0).getSnippet().getThumbnails().getMedium().getUrl());
                                    binding.setModel(userChannel);
                                }


                            } else {
                                Toast.makeText(activity, R.string.in_url, Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(activity, R.string.in_url, Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<VideoModel> call, Throwable t) {
                        binding.btnConfirm.setEnabled(true);

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

}