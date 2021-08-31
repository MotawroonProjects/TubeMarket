package com.app.tubemarket.uis.activity_home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tubemarket.R;
import com.app.tubemarket.databinding.ActivityHomeBinding;
import com.app.tubemarket.language.Language;
import com.app.tubemarket.models.InterestsModel;
import com.app.tubemarket.models.LoginRegisterModel;
import com.app.tubemarket.models.StatusResponse;
import com.app.tubemarket.models.UserModel;
import com.app.tubemarket.preferences.Preferences;
import com.app.tubemarket.remote.Api;
import com.app.tubemarket.share.Common;
import com.app.tubemarket.tags.Tags;
import com.app.tubemarket.uis.activity_home.fragments.bottom_nav_fragment.CoinsFragment;
import com.app.tubemarket.uis.activity_login.LoginActivity;
import com.app.tubemarket.uis.activity_splash.SplashActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private NavController navController;
    private String lang;
    private CircleImageView imageView;
    private TextView tvName, tvEmail, tvCoins;


    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.setLifecycleOwner(this);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent.getData()!=null){
            String inviteCode = intent.getData().getLastPathSegment();
            Log.e("code", inviteCode);

        }
    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        View headerView = binding.navView.getHeaderView(0);
        imageView = headerView.findViewById(R.id.image);
        tvName = headerView.findViewById(R.id.tvName);
        tvEmail = headerView.findViewById(R.id.tvEmail);
        tvCoins = headerView.findViewById(R.id.tvCoins);

        updateUi();
        setSupportActionBar(binding.toolBar);
        navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(binding.bottomNav, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout);
        NavigationUI.setupWithNavController(binding.navView, navController);
        binding.bottomNav.getMenu().findItem(R.id.empty).setEnabled(false);

        binding.toolBar.getNavigationIcon().setColorFilter(ContextCompat.getColor(HomeActivity.this, R.color.white), PorterDuff.Mode.SRC_ATOP);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            String name = destination.getLabel().toString();
            binding.toolBar.setTitle("");
            binding.setTitle(name);

        });

        binding.cardAdAds.setOnClickListener(v -> {
            navController.navigate(R.id.addAdsFragment);
        });

        binding.navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            switch (id) {
                case R.id.profileFragment:
                    navController.navigate(R.id.profileFragment);
                    break;

                case R.id.goldProfileFragment:
                    navController.navigate(R.id.goldProfileFragment);

                    break;
                case R.id.invite:
                    navController.navigate(R.id.inviteFragment);

                    break;
                case R.id.changeLang:
                    navController.navigate(R.id.changeLang);

                    break;
                case R.id.faq:
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "1");
                    navController.navigate(R.id.faqActivity, bundle);
                    break;

                case R.id.rateApp:
                    final String appPackageName = getPackageName();
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                    break;

                case R.id.policy:
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("type", "2");
                    navController.navigate(R.id.faqActivity, bundle2);
                    break;

                case R.id.logout:
                    new Handler(Looper.myLooper())
                            .postDelayed(this::logout, 500);
                    break;
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        if (userModel == null) {
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            finish();
        }

        updateFirebaseToken();

    }

    private void updateUi() {
        Picasso.get().load(Uri.parse(userModel.getImage())).into(imageView);
        tvName.setText(userModel.getName());
        tvEmail.setText(userModel.getEmail());
        tvCoins.setText(userModel.getCoins());
    }

    private void navigateToSignInActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, binding.drawerLayout);
    }


    @Override
    public void onBackPressed() {
        int currentFragmentId = navController.getCurrentDestination().getId();
        if (currentFragmentId == R.id.coinsFragment) {
            Fragment fragment = getSupportFragmentManager().getPrimaryNavigationFragment();

            Fragment childFragment = fragment.getChildFragmentManager().getFragments().get(0);
            if (childFragment != null) {
                if (childFragment instanceof CoinsFragment) {
                    CoinsFragment coinsFragment = (CoinsFragment) childFragment;
                    if (coinsFragment.getDialogVisibility()) {
                        coinsFragment.closeDialog();
                    } else {
                        super.onBackPressed();

                    }
                } else {
                    super.onBackPressed();

                }


            } else {
                super.onBackPressed();

            }
        } else if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
    }

    public void refreshActivity(String lang) {
        Paper.book().write("lang", lang);
        Language.setNewLocale(this, lang);
        new Handler()
                .postDelayed(() -> {

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }, 500);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (userModel != null) {
            getUserProfile();
        }
    }

    public void getUserProfile() {
        Api.getService(Tags.base_url)
                .getProfile("Bearer " + userModel.getToken(), userModel.getId())
                .enqueue(new Callback<LoginRegisterModel>() {
                    @Override
                    public void onResponse(Call<LoginRegisterModel> call, Response<LoginRegisterModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Log.e("code", response.body().getStatus()+"_");
                            if (response.body().getStatus() == 200) {
                                LoginRegisterModel model = response.body();

                                UserModel.ChannelModel channelModel = null;
                                UserModel.VideoModel videoModel = null;
                                InterestsModel interestsModel = null;
                                if (model.getData().getChannel_id() != null) {
                                    channelModel = new UserModel.ChannelModel(model.getData().getChannel_id(), model.getData().getChannel_name(), model.getData().getChannel_description(), model.getData().getChannel_image());

                                }

                                if (model.getData().getChannel_video_link() != null) {
                                    videoModel = new UserModel.VideoModel(model.getData().getChannel_video_link(), model.getData().getChannel_video_name(), model.getData().getChannel_video_description(), model.getData().getChannel_video_image());

                                }

                                if (model.getData().getInterested() != 0) {
                                    interestsModel = new InterestsModel(model.getData().getInterested(), "");

                                }

                                userModel = new UserModel(model.getData().getId(), model.getData().getGoogle_id(), model.getData().getEmail(), model.getData().getName(), model.getData().getImage(), model.getData().getCoins(), model.getData().getCode(), model.getData().getUser_type(), model.getData().getIs_vip(), model.getData().getToken(), channelModel, videoModel, interestsModel);

                                preferences.create_update_userdata(HomeActivity.this, userModel);

                                updateUi();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginRegisterModel> call, Throwable t) {

                    }
                });

    }

    private void updateFirebaseToken() {
        FirebaseMessaging.getInstance()
                .getToken()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String token = task.getResult();
                        try {
                            Api.getService(Tags.base_url)
                                    .updateFirebaseToken("Bearer " + userModel.getToken(), userModel.getId(), token, "android")
                                    .enqueue(new Callback<StatusResponse>() {
                                        @Override
                                        public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                                            if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 200) {
                                                userModel.setFirebase_token(token);
                                                preferences.create_update_userdata(HomeActivity.this, userModel);

                                                Log.e("token", "updated successfully");
                                            } else {
                                                try {

                                                    Log.e("errorToken", response.code() + "_" + response.errorBody().string());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<StatusResponse> call, Throwable t) {
                                            try {

                                                if (t.getMessage() != null) {
                                                    Log.e("errorToken2", t.getMessage());

                                                }

                                            } catch (Exception e) {
                                            }
                                        }
                                    });
                        } catch (Exception e) {

                        }
                    }
                });
    }

    public void logout() {

        Log.e("ddd", "fff");
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .logout("Bearer " + userModel.getToken(), userModel.getId(), userModel.getFirebaseToken())
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getStatus() == 200) {
                                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                manager.cancel(Tags.not_tag,Tags.not_id);
                                preferences.clear(HomeActivity.this);
                                navigateToSignInActivity();
                            }

                        } else {
                            dialog.dismiss();
                            try {
                                Log.e("error", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (response.code() == 500) {
                            } else {
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            if (t.getMessage() != null) {
                                Log.e("error", t.getMessage() + "__");

                                if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                } else {
                                }
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.getMessage() + "__");
                        }
                    }
                });

    }


}