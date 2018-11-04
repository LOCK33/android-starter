package net.bndy.ad;

import android.support.v7.app.AppCompatDelegate;

import net.bndy.ad.framework.BaseApplication;
import net.bndy.ad.framework.helper.SharedPreferencesHelper;

import org.xutils.x;

public class Application extends BaseApplication {

    public static final String LOG_TAG = "net.bndy.ad";
    public static final String URL_UPGRADE = "";
    public static final ShowSplashPage showSplashPage = ShowSplashPage.ONCE;

    public static final String SP_KEY_SKIP_SPLASH = "splash.skip";
    public static final String SP_KEY_UPGRADE_ID = "upgrade.downloadId";

    public static SharedPreferencesHelper SP;

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

        SP = new SharedPreferencesHelper(getApplicationContext(), getResources().getString(R.string.app_name));
        sApplication = this;
    }

    public void enableSplash() {
        SP.set(SP_KEY_SKIP_SPLASH, false);
    }

    public void disableSplash() {
        SP.set(SP_KEY_SKIP_SPLASH, true);
    }


    public enum ShowSplashPage {
        ONCE,
        AlWAYS,
        NEVER,
    }
}
