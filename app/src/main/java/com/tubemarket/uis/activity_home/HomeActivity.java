package com.tubemarket.uis.activity_home;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.android.gms.ads.rewarded.RewardedAd;
import com.tubemarket.R;
import com.tubemarket.databinding.ActivityHomeBinding;
import com.tubemarket.databinding.DialogCoinsBinding;
import com.tubemarket.language.Language;
import com.tubemarket.models.AdsViewModel;
import com.tubemarket.models.GeneralAdsModel;
import com.tubemarket.models.InterestsModel;
import com.tubemarket.models.LoginRegisterModel;
import com.tubemarket.models.StatusResponse;
import com.tubemarket.models.UserModel;
import com.tubemarket.preferences.Preferences;
import com.tubemarket.remote.Api;
import com.tubemarket.share.Common;
import com.tubemarket.tags.Tags;
import com.tubemarket.uis.activity_home.fragments.bottom_nav_fragment.CoinsFragment;
import com.tubemarket.uis.activity_login.LoginActivity;
import com.tubemarket.uis.activity_splash.SplashActivity;
import com.tubemarket.uis.activity_web_view.WebViewActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements OnUserEarnedRewardListener {

    private ActivityHomeBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private NavController navController;
    private String lang;
    private CircleImageView imageView;
    private TextView tvName, tvEmail, tvCoins;
    private AbstractYouTubePlayerListener listener;
    private YouTubePlayer youTubePlayer;
    private Timer timer, timerClose;
    private TimerTask timerTask;
    private TaskAds timerTaskClose;
    private int seconds = 0;
    private AdsViewModel adsViewModel;
    private String videoId = "";
    private int adsSeconds = 6;//seconds
    private boolean canCloseAds = true;
    private AdsViewModel adsViewModelSubscription;
    private ActivityResultLauncher<Intent> launcher;



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
        if (intent.getData() != null) {
            String inviteCode = intent.getData().getLastPathSegment();
            Log.e("code", inviteCode);

        }
    }

    private void initView() {




        Paper.init(this);
        lang = Paper.book().read("lang", "ar");
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        View headerView = binding.navView.getHeaderView(0);
        imageView = headerView.findViewById(R.id.image);
        tvName = headerView.findViewById(R.id.tvName);
        tvEmail = headerView.findViewById(R.id.tvEmail);
        tvCoins = headerView.findViewById(R.id.tvCoins);

        ////////////////////////////////////////////////////////////////////
        binding.youtubePlayerView.enableBackgroundPlayback(true);
        getLifecycle().addObserver(binding.youtubePlayerView);

        ////////////////////////////////////////////////////////////////////
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

        binding.imgMessage.setOnClickListener(v -> {
            navController.navigate(R.id.userMessagesFragment);
        });

        binding.navView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            NavOptions navOptions = new NavOptions.Builder()
                    .setEnterAnim(0)
                    .setExitAnim(0)
                    .setPopEnterAnim(0)
                    .setPopExitAnim(0)
                    .build();
            switch (id) {
                case R.id.profileFragment:

                    navController.navigate(R.id.profileFragment, null, navOptions);
                    break;

                case R.id.goldProfileFragment:
                    navController.navigate(R.id.goldProfileFragment, null, navOptions);

                    break;
                case R.id.invite:
                    navController.navigate(R.id.inviteFragment, null, navOptions);

                    break;
                case R.id.myAdsFragment:
                    navController.navigate(R.id.myAdsFragment, null, navOptions);

                    break;
                case R.id.changeLang:
                    navController.navigate(R.id.changeLang, null, navOptions);
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


                    new Handler()
                            .postDelayed(() -> {
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("type", "2");

                                navController.navigate(R.id.faqActivity, bundle2, navOptions);
                                overridePendingTransition(0, 0);
                            }, 300);


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
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);

        }


        binding.drawerLayout.setScrimColor(getResources().getColor(R.color.transparent));
        binding.drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                float halfWidth = drawerView.getWidth() / 2;
                if (slideX < halfWidth) {
                    binding.cardAdAds.setVisibility(View.VISIBLE);

                } else {
                    binding.cardAdAds.setVisibility(View.INVISIBLE);

                }

                if (lang.equals("ar")) {
                    binding.consData.setTranslationX(-slideX);

                } else {
                    binding.consData.setTranslationX(slideX);
                }

            }
        });

        binding.cardClose.setOnClickListener(v -> {
            if (canCloseAds) {
                slideDown();
            }
        });
        binding.flSubscribe.setOnClickListener(v -> {
            String channelId = "https://youtube.com/channel/"+ adsViewModelSubscription.getAdvertisement_fk().getLink();
            String url = "https://accounts.google.com/ServiceLogin?service=youtube";
            Bundle bundle = new Bundle();
            bundle.putString("url", url);
            bundle.putString("vidUrl", channelId);
            bundle.putString("type", "channel");

            String watchTime = "0";
            if (adsViewModelSubscription.getAdvertisement_fk().getType().equals("get_subscription")) {
                watchTime = adsViewModelSubscription.getAdvertisement_fk().getTimer_limit();
            } else if (adsViewModelSubscription.getAdvertisement_fk().getType().equals("get_subscription_and_views")) {
                watchTime = adsViewModelSubscription.getAdvertisement_fk().getWatch_time();
            }

            GeneralAdsModel generalAdsModel = new GeneralAdsModel(adsViewModelSubscription.getId(), watchTime, adsViewModelSubscription.getAdvertisement_fk().getProfit_coins(), Tags.CUSTOM_AD);
            bundle.putSerializable("data", generalAdsModel);

            Intent intent = new Intent(HomeActivity.this, WebViewActivity.class);
            intent.putExtras(bundle);
            launcher.launch(intent);

            //getPlaylistByChannelId(channelId);
        });
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                getUserProfile();
                adsViewModelSubscription = null;
                slideDown();


            }
        });
        updateFirebaseToken();


    }

    public void adMob() {

        RewardedInterstitialAd.load(this, "ca-app-pub-4674117082406701/2505254239", new AdRequest.Builder().build(), new RewardedInterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull RewardedInterstitialAd rewardedInterstitialAd) {
                super.onAdLoaded(rewardedInterstitialAd);
                rewardedInterstitialAd.show(HomeActivity.this,HomeActivity.this);
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.e("failed", loadAdError.getMessage()+"__");
            }
        });
    }

    @Override
    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
        Log.e("fff", rewardItem.getAmount()+"__"+rewardItem.getType());

    }






    private void updateUi() {
        if (userModel.getImage()!=null&&!userModel.getImage().isEmpty()){
            Picasso.get().load(Uri.parse(userModel.getImage())).into(imageView);

        }
        tvName.setText(userModel.getName());
        tvEmail.setText(userModel.getEmail());
        tvCoins.setText(userModel.getCoins());
        binding.setCoins(userModel.getCoins());
    }

    public void loadVideoAds(AdsViewModel adsViewModel) {
        Log.e("ddd", adsViewModel.getAdvertisement_fk().getLink());
        binding.llViewAds.setVisibility(View.VISIBLE);
        binding.llSubscriptionAds.setVisibility(View.GONE);
        this.adsViewModel = adsViewModel;
        adsSeconds = 6;
        binding.setTimer(adsSeconds + " s");
        seconds = 0;
        canCloseAds = false;
        slideUp();

        startTimeClose();

        videoId = adsViewModel.getAdvertisement_fk().getLink();
        binding.setVidCoins(adsViewModel.getAdvertisement_fk().getProfit_coins());
        binding.setSecond(adsViewModel.getAdvertisement_fk().getWatch_time());
        seconds = adsViewModel.getAdvertisement_fk().getWatch_time() != null ? Integer.parseInt(adsViewModel.getAdvertisement_fk().getWatch_time()) : 0;
        listener = new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {

                HomeActivity.this.youTubePlayer = youTubePlayer;
                youTubePlayer.loadVideo(videoId, 0);


            }

            @Override
            public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {
                super.onStateChange(youTubePlayer, state);

                if (state.name().equals(PlayerConstants.PlayerState.PLAYING.name())) {

                    startTime();
                } else {
                    stopTimer();
                }
            }
        };

        if (youTubePlayer != null) {
            Log.e("ff", "fdgdgg");

            listener.onReady(youTubePlayer);

        } else {
            Log.e("ff", "fdgd");
            binding.youtubePlayerView.addYouTubePlayerListener(listener);

        }


    }

    public void loadSubscriptionAds(AdsViewModel adsViewModel) {
        adsViewModelSubscription = adsViewModel;
        binding.llViewAds.setVisibility(View.GONE);
        binding.llSubscriptionAds.setVisibility(View.VISIBLE);
        binding.setModel(adsViewModel);
        adsSeconds = 6;
        seconds = 0;
        binding.setTimer(adsSeconds + " s");
        canCloseAds = false;
        slideUp();

        startTimeClose();


    }


    private void slideUp() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        binding.scrollViewVideosAds.clearAnimation();
        binding.scrollViewVideosAds.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.scrollViewVideosAds.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void slideDown() {
        stopTimer();
        stopTimerClose();

        if (youTubePlayer != null) {
            youTubePlayer.pause();

        }
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        binding.scrollViewVideosAds.clearAnimation();
        binding.scrollViewVideosAds.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.scrollViewVideosAds.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void stopTimer() {
        if (timer != null && timerTask != null) {
            timerTask.cancel();
            timer.cancel();
            timer.purge();
            timer = null;
            timerTask = null;
        }

    }

    private void startTime() {
        if (seconds > 0) {
            timer = new Timer();
            timerTask = new Task();
            timer.scheduleAtFixedRate(timerTask, 1000, 1000);
        }

    }

    private void stopTimerClose() {
        if (timerClose != null && timerTaskClose != null) {
            timerTaskClose.cancel();
            timerClose.cancel();
            timerClose.purge();
            timerClose = null;
            timerTaskClose = null;
        }

    }

    private void startTimeClose() {
        if (adsSeconds > 0) {
            timerClose = new Timer();
            timerTaskClose = new TaskAds();
            timerClose.scheduleAtFixedRate(timerTaskClose, 1000, 1000);
        }

    }

    private void viewAds() {
        Api.getService(Tags.base_url)
                .viewAds("Bearer " + userModel.getToken(), userModel.getId(), adsViewModel.getId(), adsViewModel.getAdvertisement_fk().getWatch_time())
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Log.e("ddd", response.body().getStatus() + "__");
                            if (response.body().getStatus() == 200) {
                                getUserProfile();

                                createDialog(adsViewModel.getAdvertisement_fk().getProfit_coins());
                            }
                        } else {
                            try {
                                Log.e("resCode", response.code() + "__" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        Log.e("ex", t.getMessage());
                    }
                });
    }

    private void createDialog(String coins) {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .create();

        DialogCoinsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_coins, null, false);

        binding.tvMsg.setText(coins + " " + getString(R.string.currency));
        binding.cardCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.inset_dialog);
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();

        new Handler().postDelayed(dialog::dismiss, 3000);
    }




    public class Task extends TimerTask {

        @Override
        public void run() {
            if (seconds > 0) {
                seconds--;
                binding.setSecond(seconds + "");
            } else {
                stopTimer();
                viewAds();

            }
        }
    }

    public class TaskAds extends TimerTask {

        @Override
        public void run() {
            if (adsSeconds > 0) {
                adsSeconds--;
                binding.setTimer(adsSeconds + " s");
            } else {
                canCloseAds = true;
                binding.setTimer(getString(R.string.close));

                stopTimer();

            }
        }
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
        if (binding.scrollViewVideosAds.getVisibility() == View.VISIBLE) {
            if (canCloseAds) {
                slideDown();

            }
        } else {
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


    }

    public void refreshActivity(String lang) {
        Paper.book().write("lang", lang);
        Language.setNewLocale(this, lang);
        new Handler()
                .postDelayed(() -> {

                    Intent intent = getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                    overridePendingTransition(0, 0);

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
                            Log.e("code", response.body().getStatus() + "_");
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
                                                userModel.setFirebase_token(task.getResult());
                                                preferences.create_update_userdata(HomeActivity.this, userModel);

                                                Log.e("token",userModel.getFirebaseToken());
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
                            Log.e("res", response.body().getStatus()+"_");
                            if (response.body() != null && response.body().getStatus() == 200) {
                                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                manager.cancel(Tags.not_tag, Tags.not_id);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopTimer();
        stopTimerClose();
    }


}