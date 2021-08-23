package com.app.tubemarket.uis.activity_home.fragments.bottom_nav_fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.tubemarket.R;
import com.app.tubemarket.adapters.SpinnerInterestsAdapter;
import com.app.tubemarket.databinding.FragmentNewAdditionBinding;
import com.app.tubemarket.databinding.FragmentProfileBinding;
import com.app.tubemarket.models.InterestsModel;
import com.app.tubemarket.models.UserModel;
import com.app.tubemarket.models.VideoModel;
import com.app.tubemarket.preferences.Preferences;
import com.app.tubemarket.share.Common;
import com.app.tubemarket.tags.Tags;
import com.app.tubemarket.uis.activity_home.HomeActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class NewAdditionFragment extends Fragment {
    private FragmentNewAdditionBinding binding;
    private HomeActivity activity;
    private UserModel userModel;
    private Preferences preferences;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_new_addition,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);

        binding.btnAdd.setOnClickListener(v -> {
            String vidLink = binding.edtUrl.getText().toString().trim();
            if (!vidLink.isEmpty()){
                String vidId = extractYTId(vidLink);
                if (vidId!=null){
                    Common.CloseKeyBoard(activity,binding.edtUrl);
                    Bundle bundle  = new Bundle();
                    bundle.putString("videoId", vidId);
                    Navigation.findNavController(v).navigate(R.id.newAdditionSetUpFragment,bundle);

                }
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
}