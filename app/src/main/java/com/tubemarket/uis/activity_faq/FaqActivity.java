package com.tubemarket.uis.activity_faq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tubemarket.R;
import com.tubemarket.databinding.ActivityFaqBinding;
import com.tubemarket.language.Language;

import io.paperdb.Paper;

public class FaqActivity extends AppCompatActivity {
    private ActivityFaqBinding binding;
    private String type="";
    private String lang;

    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_faq);
        getDataFromIntent();
        initView();
    }



    private void getDataFromIntent() {
        Intent intent = getIntent();
        type = intent.getExtras().getString("type");

    }

    private void initView() {
        Paper.init(this);
        lang = Paper.book().read("lang","ar");
        binding.setLang(lang);
        if (type.equals("1")){
            binding.setTitle(getString(R.string.faq));

        }else if (type.equals("2")){
            binding.setTitle(getString(R.string.privacy_policy));

        }

        binding.llBack.setOnClickListener(v -> {
            finish();
        });
    }
}