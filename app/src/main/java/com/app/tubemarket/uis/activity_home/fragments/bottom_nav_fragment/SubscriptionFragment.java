package com.app.tubemarket.uis.activity_home.fragments.bottom_nav_fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.tubemarket.R;
import com.app.tubemarket.databinding.FragmentNewAdditionBinding;
import com.app.tubemarket.databinding.FragmentSubscriptionBinding;
import com.app.tubemarket.models.UserModel;
import com.app.tubemarket.preferences.Preferences;
import com.app.tubemarket.share.Common;
import com.app.tubemarket.uis.activity_home.HomeActivity;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.paperdb.Paper;


public class SubscriptionFragment extends Fragment {

    private FragmentSubscriptionBinding binding;
    private HomeActivity activity;
    private UserModel userModel;
    private Preferences preferences;
    private String videoId="";
    private String lang;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_subscription,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        videoId = "QPaI8xVWBjY";
        Paper.init(activity);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);


        binding.flSubscribe.setOnClickListener(v -> {
            String vidUrl ="https://youtu.be/"+videoId;
            String url = "https://accounts.google.com/ServiceLogin?service=youtube";
            Bundle bundle = new Bundle();
            bundle.putString("url",url);
            bundle.putString("vidUrl",vidUrl);

            Navigation.findNavController(v).navigate(R.id.webView,bundle);
        });


    }


}