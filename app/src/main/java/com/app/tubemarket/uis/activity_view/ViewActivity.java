package com.app.tubemarket.uis.activity_view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.app.tubemarket.R;
import com.app.tubemarket.databinding.ActivityViewBinding;
import com.app.tubemarket.databinding.ActivityWebViewBinding;
import com.app.tubemarket.language.Language;
import com.app.tubemarket.models.MessageResponseModel;
import com.app.tubemarket.models.MyVideosModel;
import com.app.tubemarket.models.UserModel;
import com.app.tubemarket.preferences.Preferences;
import com.app.tubemarket.tags.Tags;

import io.paperdb.Paper;

public class ViewActivity extends AppCompatActivity {
    private ActivityViewBinding binding;
    private String url = "";
    private MessageResponseModel.Data data;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view);
        binding.setLifecycleOwner(this);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        url = intent.getExtras().getString("url");
        if (intent.hasExtra("data")){
            data = (MessageResponseModel.Data) intent.getSerializableExtra("data");
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.loadUrl(url);

        binding.llBack.setOnClickListener(v -> finish());

        binding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (data!=null){
                    String successLink = Tags.base_url+"success-payment";

                    if (url.contains(successLink)){
                        new Handler()
                                .postDelayed(()->{
                                    setResult(RESULT_OK);
                                    finish();
                                }, 500);


                    }

                }

            }

            @Override
            public void onPageCommitVisible(WebView webview, String url) {
                super.onPageCommitVisible(webview, url);
                binding.progBar.setVisibility(View.GONE);

            }


        });
    }

}