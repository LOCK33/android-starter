package net.bndy.ad;

import android.support.v7.app.AppCompatDelegate;

public class Application extends android.app.Application {

    public static final String LOG_TAG = "net.bndy.ad";

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}
