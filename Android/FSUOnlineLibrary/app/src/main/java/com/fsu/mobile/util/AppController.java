package com.fsu.mobile.util;

import android.app.Application;
import android.content.SharedPreferences;
import android.text.TextUtils;


/**
 * 
 */
public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    public static String PREFS = "PREFS";
    public static SharedPreferences sharedPreferences ;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        sharedPreferences = getSharedPreferences(PREFS,MODE_PRIVATE);
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

}
