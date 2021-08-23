package com.app.tubemarket.uis.activity_home.fragments.bottom_nav_fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.app.tubemarket.R;
import com.app.tubemarket.adapters.SpinnerCountAdapter;
import com.app.tubemarket.databinding.FragmentAddSubscriptionsBinding;
import com.app.tubemarket.databinding.FragmentNewAdditionBinding;
import com.app.tubemarket.models.UserModel;
import com.app.tubemarket.preferences.Preferences;
import com.app.tubemarket.uis.activity_home.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class AddSubscriptionsFragment extends Fragment {

    private FragmentAddSubscriptionsBinding binding;
    private HomeActivity activity;
    private UserModel userModel;
    private Preferences preferences;
    private SpinnerCountAdapter subscriptionsAdapter,secondsAdapter;
    private List<String> subscriptionsList,secondsList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_subscriptions,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        subscriptionsList =new ArrayList<>();
        secondsList = new ArrayList<>();
        binding.setModel(userModel.getChannelModel());
        for (int index=1;index<21;index++)
        {
            if (index==1){
                subscriptionsList.add("5");
                secondsList.add("45");
            }else {
                int second = index*30;
                secondsList.add(second+"");

                int sub;
                if (index<12){
                    sub = (index - 1) * 10;

                }else {
                    sub = ((index - 10)) * 100;

                }
                subscriptionsList.add(sub+"");

            }
        }
        subscriptionsAdapter = new SpinnerCountAdapter(subscriptionsList,activity);
        binding.spinnerSubscriptions.setAdapter(subscriptionsAdapter);
        secondsAdapter = new SpinnerCountAdapter(secondsList,activity);
        binding.spinnerSeconds.setAdapter(secondsAdapter);

        binding.spinnerSubscriptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.setValue(subscriptionsList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}