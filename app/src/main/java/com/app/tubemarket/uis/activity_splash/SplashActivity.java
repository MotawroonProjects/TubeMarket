package com.app.tubemarket.uis.activity_splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.databinding.DataBindingUtil;

import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Handler;

import com.app.tubemarket.R;
import com.app.tubemarket.databinding.ActivitySplashBinding;
import com.app.tubemarket.language.Language;
import com.app.tubemarket.models.UserModel;
import com.app.tubemarket.preferences.Preferences;
import com.app.tubemarket.uis.activity_home.HomeActivity;
import com.app.tubemarket.uis.activity_login.LoginActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash);
        binding.setLifecycleOwner(this);
        initView();
    }

    private void initView() {

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        new Handler().postDelayed(() -> {
            if (userModel==null){
               // navigateToHomeActivity();
                navigateToLoginActivity();
            }else {
                navigateToHomeActivity();
            }
        }, 3000);

    }

    private void navigateToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent );
        finish();
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent );
        finish();
    }
}