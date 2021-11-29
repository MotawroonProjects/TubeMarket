package com.tubemarket.uis.activity_home.fragments.general_fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tubemarket.R;
import com.tubemarket.adapters.UserMessageAdapter;
import com.tubemarket.databinding.FragmentUserMessagesBinding;
import com.tubemarket.interfaces.Listeners;
import com.tubemarket.models.UserMessageModel;
import com.tubemarket.models.UserMessagesDataModel;
import com.tubemarket.models.UserModel;
import com.tubemarket.preferences.Preferences;
import com.tubemarket.remote.Api;
import com.tubemarket.tags.Tags;
import com.tubemarket.uis.activity_home.HomeActivity;
import com.tubemarket.uis.activity_view.ViewActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserMessagesFragment extends Fragment implements Listeners.UserMessageListener {
    private FragmentUserMessagesBinding binding;
    private HomeActivity activity;
    private UserModel userModel;
    private Preferences preferences;
    private List<UserMessageModel> list;
    private UserMessageAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_messages, container, false);
        initView();
        return binding.getRoot();
    }


    private void initView() {
        list = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);

        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new UserMessageAdapter(activity, list, this);
        binding.recView.setAdapter(adapter);


        getMessages();
    }

    private void getMessages() {
        binding.progBar.setVisibility(View.VISIBLE);
        list.clear();
        adapter.notifyDataSetChanged();
        Api.getService(Tags.base_url)
                .getUserMessages("Bearer " + userModel.getToken(), userModel.getId())
                .enqueue(new Callback<UserMessagesDataModel>() {
                    @Override
                    public void onResponse(Call<UserMessagesDataModel> call, Response<UserMessagesDataModel> response) {
                        binding.progBar.setVisibility(View.GONE);

                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                if (response.body().getData().size() > 0) {
                                    list.clear();
                                    list.addAll(response.body().getData());

                                    adapter.notifyDataSetChanged();
                                    binding.tvNoMessage.setVisibility(View.GONE);

                                } else {
                                    binding.tvNoMessage.setVisibility(View.VISIBLE);
                                }


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
                    public void onFailure(Call<UserMessagesDataModel> call, Throwable t) {
                        binding.progBar.setVisibility(View.GONE);
                        Log.e("fail", t.getMessage());
                    }
                });
    }


    @Override
    public void onUserMessage(UserMessageModel model, View itemView) {
        Intent intent = new Intent(activity, ViewActivity.class);
        intent.putExtra("url", model.getBuy_message().getLink());
        startActivity(intent);
    }
}