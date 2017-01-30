package com.nigeldbrown.wifiautomater;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.text.TextUtils;

import com.karumi.dexter.Dexter;

import java.util.HashMap;

/**
 * Created by nibrown on 1/30/2017.
 */

public class WifiAutomaterApplication extends Application {

    public static final String TAG = WifiAutomaterApplication.class
            .getSimpleName();
    private static WifiAutomaterApplication mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Dexter.initialize(this);

    }


}
