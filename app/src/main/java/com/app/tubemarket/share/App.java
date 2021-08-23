package com.app.tubemarket.share;


import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.app.tubemarket.language.Language;


public class App extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase,"ar"));
    }


    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceUtil.setDefaultFont(this, "DEFAULT", "fonts/font.ttf");
        TypefaceUtil.setDefaultFont(this, "MONOSPACE", "fonts/font.ttf");
        TypefaceUtil.setDefaultFont(this, "SERIF", "fonts/font.ttf");
        TypefaceUtil.setDefaultFont(this, "SANS_SERIF", "fonts/font.ttf");

    }
}

