package com.tubemarket.uis.activity_home.fragments.slide_menu_fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tubemarket.R;
import com.tubemarket.databinding.FragmentInviteBinding;
import com.tubemarket.databinding.FragmentLanguageBinding;
import com.tubemarket.models.UserModel;
import com.tubemarket.preferences.Preferences;
import com.tubemarket.tags.Tags;
import com.tubemarket.uis.activity_home.HomeActivity;


public class InviteFragment extends Fragment {

    private FragmentInviteBinding binding;
    private HomeActivity activity;
    private UserModel userModel;
    private Preferences preferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_invite, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);

        String shareLink = Tags.base_url+"code/"+userModel.getCode();

        binding.tvLink.setText(shareLink);

        binding.tvLink.setOnClickListener(v -> {
            String link = binding.tvLink.getText().toString();
            if (!link.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

        });

        binding.btnCopy.setOnClickListener(v -> {
            String link = binding.tvLink.getText().toString();
            if (!link.isEmpty()) {
                ClipboardManager clipboardManager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("text", link);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(activity, R.string.copied, Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnInvite.setOnClickListener(v -> {
            String link = binding.tvLink.getText().toString();
            if (!link.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,link);
                startActivity(Intent.createChooser(intent,"Share"));
            }
        });
    }
}