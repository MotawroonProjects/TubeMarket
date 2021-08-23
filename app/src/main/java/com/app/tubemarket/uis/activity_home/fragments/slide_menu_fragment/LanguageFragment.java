package com.app.tubemarket.uis.activity_home.fragments.slide_menu_fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.tubemarket.R;
import com.app.tubemarket.adapters.SpinnerInterestsAdapter;
import com.app.tubemarket.databinding.FragmentLanguageBinding;
import com.app.tubemarket.models.InterestsModel;
import com.app.tubemarket.models.UserModel;
import com.app.tubemarket.models.VideoModel;
import com.app.tubemarket.preferences.Preferences;
import com.app.tubemarket.share.Common;
import com.app.tubemarket.uis.activity_home.HomeActivity;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class LanguageFragment extends Fragment {

    private FragmentLanguageBinding binding;
    private HomeActivity activity;
    private String lang = "";
    private String selectedLang;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_language,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        lang = Paper.book().read("lang", "ar");
        selectedLang = lang;

        if (lang.equals("ar")) {
            binding.flAr.setBackgroundResource(R.drawable.small_stroke_primary);
            binding.flEn.setBackgroundResource(0);

        } else {
            binding.flAr.setBackgroundResource(0);
            binding.flEn.setBackgroundResource(R.drawable.small_stroke_primary);

        }

        binding.cardAr.setOnClickListener(view -> {
            selectedLang = "ar";

            if (!selectedLang.equals(lang)) {
                binding.btnNext.setVisibility(View.VISIBLE);

            } else {
                binding.btnNext.setVisibility(View.INVISIBLE);

            }
            binding.flAr.setBackgroundResource(R.drawable.small_stroke_primary);
            binding.flEn.setBackgroundResource(0);

        });

        binding.cardEn.setOnClickListener(view -> {
            selectedLang = "en";

            Log.e("lang",selectedLang+"__"+lang);
            if (!selectedLang.equals(lang)) {
                binding.btnNext.setVisibility(View.VISIBLE);

            } else {
                binding.btnNext.setVisibility(View.INVISIBLE);

            }
            binding.flAr.setBackgroundResource(0);
            binding.flEn.setBackgroundResource(R.drawable.small_stroke_primary);
        });


        binding.btnNext.setOnClickListener(view -> {

            activity.refreshActivity(selectedLang);

        });
    }
}