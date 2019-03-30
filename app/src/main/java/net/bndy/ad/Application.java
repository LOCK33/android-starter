package net.bndy.ad;

import android.support.v7.app.AppCompatDelegate;

import net.bndy.ad.framework.ui.BaseApplication;
import net.bndy.ad.framework.utils.SharedPreferencesHelper;

import org.xutils.x;

public class Application extends BaseApplication {
    // url for upgrade which response {app_name, app_version, description, url} and the url is for downloading apk
    public static final String URL_UPGRADE_METADATA = "";
    // determine how to show splash page
    public static final Application.SPLASH_MODE SPLASH_MODE = Application.SPLASH_MODE.ONCE;
    // splash page images
    public static final int[] SPLASH_IMAGES = new int[]{R.drawable.splash_1, R.drawable.splash_2};

    public static final String LOG_TAG = "net.bndy.ad";
    public static final String SP_KEY_SKIP_SPLASH = "splash.skip";

    private static SharedPreferencesHelper sSharedPreferencesHelper;
    private static Application sApplication;

    public static Application getInstance() {
        return sApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);

        sSharedPreferencesHelper = new SharedPreferencesHelper(getApplicationContext(), getResources().getString(R.string.app_name));
        sApplication = this;
        this.enableSplash();
    }

    public void enableSplash() {
        sSharedPreferencesHelper.set(SP_KEY_SKIP_SPLASH, false);
    }

    public void disableSplash() {
        sSharedPreferencesHelper.set(SP_KEY_SKIP_SPLASH, true);
    }

    public SharedPreferencesHelper getSP() {
        return sSharedPreferencesHelper;
    }


    public enum SPLASH_MODE {
        ONCE,
        AlWAYS,
        NEVER,
    }
}
