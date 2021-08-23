package com.app.tubemarket.uis.activity_web_view;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.app.tubemarket.R;
import com.app.tubemarket.databinding.ActivityLoginBinding;
import com.app.tubemarket.databinding.ActivityWebViewBinding;
import com.app.tubemarket.language.Language;
import com.app.tubemarket.models.UserModel;
import com.app.tubemarket.preferences.Preferences;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

import io.paperdb.Paper;

public class WebViewActivity extends AppCompatActivity {

    private ActivityWebViewBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String url="",vidUrl ="";


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
    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);

        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.loadUrl(url);
        binding.webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                if (url.contains("https://myaccount.google.com/")){
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
                if (url.contains("https://myaccount.google.com/")){
                    binding.webView.setVisibility(View.INVISIBLE);
                    webview.loadUrl(vidUrl);
                }else if (url.contains("https://m.youtube.com/watch")){
                    binding.webView.setVisibility(View.VISIBLE);
                    binding.flLoading.setVisibility(View.GONE);
                    startCounter();
                }else {
                    binding.flLoading.setVisibility(View.GONE);

                    binding.webView.setVisibility(View.VISIBLE);

                }

            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (url.contains("https://m.youtube.com/youtubei/v1/subscription/unsubscribe")){
                    Log.e("response", "UnSubscribed");
                }else if (url.contains("https://m.youtube.com/youtubei/v1/subscription/subscribe")){
                    Log.e("response", "Subscribed");

                }else if (url.contains("https://m.youtube.com/youtubei/v1/like/like")){
                    Log.e("response", "like");

                }else if (url.contains("https://m.youtube.com/youtubei/v1/like/removelike")){
                    Log.e("response", "removeLike");

                }else if (url.contains("https://m.youtube.com/youtubei/v1/like/dislike")){
                    Log.e("response", "disLike");

                }
                return null;

            }
        });
        binding.webView.setWebChromeClient(new WebChromeClient(){

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onPermissionRequest(PermissionRequest request) {
                super.onPermissionRequest(request);
                request.grant(request.getResources());
            }



        });





    }

    private void startCounter() {
        binding.llCounter.setVisibility(View.VISIBLE);
    }

    private  void onCounterFinished(){
        binding.viewLayer.setClickable(false);
        binding.viewLayer.setFocusable(false);
    }


}