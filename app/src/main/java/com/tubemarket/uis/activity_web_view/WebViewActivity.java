package com.tubemarket.uis.activity_web_view;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.tubemarket.R;
import com.tubemarket.databinding.ActivityWebViewBinding;
import com.tubemarket.language.Language;
import com.tubemarket.models.GeneralAdsModel;
import com.tubemarket.models.StatusResponse;
import com.tubemarket.models.UserModel;
import com.tubemarket.preferences.Preferences;
import com.tubemarket.remote.Api;
import com.tubemarket.share.Common;
import com.tubemarket.tags.Tags;

import java.io.IOException;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebViewActivity extends AppCompatActivity {

    private ActivityWebViewBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String url = "", vidUrl = "";
    private GeneralAdsModel generalAdsModel;
    private int seconds = 0;
    private CountDownTimer timer;
    private String type = "video";
    private String action = "";


    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_view);
        binding.setLifecycleOwner(this);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        url = intent.getExtras().getString("url");
        vidUrl = intent.getExtras().getString("vidUrl");
        type = intent.getExtras().getString("type", "video");
        generalAdsModel = (GeneralAdsModel) intent.getExtras().getSerializable("data");
        Log.e("vide", vidUrl);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);

        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.loadUrl(url);
        seconds = generalAdsModel.getTimer_limit() != null ? Integer.parseInt(generalAdsModel.getTimer_limit()) : 0;

        binding.setSeconds(generalAdsModel.getTimer_limit());
        binding.setCoins(generalAdsModel.getProfit_coins());

        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                if (url.contains("https://myaccount.google.com/")) {
                    binding.webView.setVisibility(View.INVISIBLE);
                    view.loadUrl(vidUrl);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);


            }

            @Override
            public void onPageCommitVisible(WebView webview, String url) {
                super.onPageCommitVisible(webview, url);
                Log.e("ddd", url);
                if (url.contains("https://myaccount.google.com/")) {
                    binding.webView.setVisibility(View.INVISIBLE);
                    webview.loadUrl(vidUrl);


                } else if (url.contains("https://support.google.com/accounts/answer/")) {
                    binding.webView.setVisibility(View.INVISIBLE);
                    webview.loadUrl(vidUrl);
                } else if (url.contains("https://m.youtube.com/watch")) {
                    binding.webView.setVisibility(View.VISIBLE);
                    binding.flLoading.setVisibility(View.GONE);
                    startCounter();
                } else if (url.contains("https://accounts.google.com/ServiceLogin?service=youtube")) {
                    binding.webView.setVisibility(View.VISIBLE);
                    binding.flLoading.setVisibility(View.GONE);
                    binding.viewLayer.setVisibility(View.GONE);
                    binding.viewLayer.setClickable(false);
                    binding.viewLayer.setFocusable(false);
                } else {
                    binding.flLoading.setVisibility(View.GONE);
                    binding.webView.setVisibility(View.VISIBLE);

                    if (type.equals("channel")) {
                        binding.viewLayer.setVisibility(View.GONE);
                        binding.viewLayer.setClickable(false);
                        binding.viewLayer.setFocusable(false);
                        binding.btnConfirm.setVisibility(View.VISIBLE);
                        binding.llCounter.setVisibility(View.VISIBLE);

                        binding.llCoins.setVisibility(View.INVISIBLE);
                        binding.llSeconds.setVisibility(View.INVISIBLE);
                        binding.btnConfirm.setVisibility(View.VISIBLE);
                    }

                }

            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (url.contains("https://m.youtube.com/youtubei/v1/subscription/unsubscribe")) {
                    Log.e("response", "UnSubscribed");
                    action = "subscribe";

                } else if (url.contains("https://m.youtube.com/youtubei/v1/subscription/subscribe")) {
                    Log.e("response", "Subscribed");

                    action = "subscribe";


                } else if (url.contains("https://m.youtube.com/youtubei/v1/like/like")) {
                    Log.e("response", "like");
                    action = "like";


                } else if (url.contains("https://m.youtube.com/youtubei/v1/like/removelike")) {
                    Log.e("response", "removeLike");
                    action = "remove_like";

                } else if (url.contains("https://m.youtube.com/youtubei/v1/like/dislike")) {
                    Log.e("response", "disLike");
                    action = "dislike";


                }
                return null;

            }
        });

        binding.webView.setWebChromeClient(new WebChromeClient() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onPermissionRequest(PermissionRequest request) {
                super.onPermissionRequest(request);
                request.grant(request.getResources());
            }


        });

        binding.viewLayer.setOnClickListener(v -> Toast.makeText(this, R.string.should_view, Toast.LENGTH_SHORT).show());

        binding.btnConfirm.setOnClickListener(v -> {
            if (action.equals("like")) {
                if (generalAdsModel.getAdType().equals(Tags.NORMAL_AD)) {
                    likeDislike("like");
                } else if (generalAdsModel.getAdType().equals(Tags.CUSTOM_AD)) {
                    likeDislikeAdVideo("like");
                }
            } else if (action.equals("dislike")) {
                if (generalAdsModel.getAdType().equals(Tags.NORMAL_AD)) {
                    likeDislike("remove_like");
                } else if (generalAdsModel.getAdType().equals(Tags.CUSTOM_AD)) {
                    likeDislikeAdVideo("remove_like");
                }
            } else if (action.equals("remove_like")) {
                if (generalAdsModel.getAdType().equals(Tags.NORMAL_AD)) {
                    likeDislike("dislike");
                } else if (generalAdsModel.getAdType().equals(Tags.CUSTOM_AD)) {
                    likeDislikeAdVideo("dislike");
                }
            } else if (action.equals("subscribe")) {

                if (generalAdsModel.getAdType().equals(Tags.NORMAL_AD)) {
                    subscribe();
                } else if (generalAdsModel.getAdType().equals(Tags.CUSTOM_AD)) {
                    subscribeAdChannel();
                }
            } else {
                finish();
            }
        });

        Log.e("vidurl", vidUrl);
    }

    private void updateSeconds() {
        if (seconds > 0) {
            seconds -= 1;
            binding.setSeconds(seconds + "");
        }


    }

    private void startCounter() {
        binding.viewLayer.setClickable(true);
        binding.viewLayer.setFocusable(true);
        binding.llCounter.setVisibility(View.VISIBLE);

        timer = new CountDownTimer(Long.parseLong(generalAdsModel.getTimer_limit() != null ? generalAdsModel.getTimer_limit() : "5") * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateSeconds();
            }

            @Override
            public void onFinish() {
                onCounterFinished();
            }
        };

        timer.start();
    }

    private void onCounterFinished() {
        binding.viewLayer.setClickable(false);
        binding.viewLayer.setFocusable(false);
        binding.llCoins.setVisibility(View.INVISIBLE);
        binding.llSeconds.setVisibility(View.INVISIBLE);
        binding.btnConfirm.setVisibility(View.VISIBLE);
    }

    private void likeDislike(String status) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .like("Bearer " + userModel.getToken(), userModel.getId(), generalAdsModel.getId(), generalAdsModel.getProfit_coins(), status)
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                setResult(RESULT_OK);
                                finish();
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
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        dialog.dismiss();
                        Log.e("dsa", t.getMessage());

                    }
                });

    }

    private void subscribe() {
        Log.e("cc", "bb");
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .subscribe("Bearer " + userModel.getToken(), userModel.getId(), generalAdsModel.getId(), generalAdsModel.getProfit_coins())
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();

                        if (response.isSuccessful() && response.body() != null) {
                            Log.e("ff", response.body().getStatus() + "_");
                            if (response.body().getStatus() == 200) {

                                setResult(RESULT_OK);
                                finish();
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
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        dialog.dismiss();
                        Log.e("sda", t.getMessage());
                    }
                });

    }

    private void likeDislikeAdVideo(String status) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .likeAdVideo("Bearer " + userModel.getToken(), userModel.getId(), generalAdsModel.getId(), status)
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                setResult(RESULT_OK);
                                finish();
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
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        dialog.dismiss();
                        Log.e("sada", t.getMessage());
                    }
                });

    }

    private void subscribeAdChannel() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .subscribeAdChannel("Bearer " + userModel.getToken(), userModel.getId(), generalAdsModel.getId())
                .enqueue(new Callback<StatusResponse>() {
                    @Override
                    public void onResponse(Call<StatusResponse> call, Response<StatusResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful() && response.body() != null) {
                            Log.e("ff", response.body().getStatus() + "_");

                            if (response.body().getStatus() == 200) {
                                setResult(RESULT_OK);
                                finish();
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
                    public void onFailure(Call<StatusResponse> call, Throwable t) {
                        dialog.dismiss();
                        Log.e("asda", t.getMessage());
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}