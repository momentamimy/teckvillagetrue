package com.developer.whocaller.net;

import android.app.Application;
import android.content.Context;

import com.developer.whocaller.net.Controller.LocaleHelper;

public class MainApp extends Application {

    private static Context mContext;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
}
