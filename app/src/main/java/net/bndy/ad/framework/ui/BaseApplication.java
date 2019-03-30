package net.bndy.ad.framework.ui;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;

import net.bndy.ad.framework.utils.ApplicationUtils;

public abstract class BaseApplication extends Application {

    protected ApplicationUtils applicationUtils;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationUtils = new ApplicationUtils(this);
        initLocale();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initLocale();
    }

    private void initLocale() {
        final Resources resources = getResources();
        final Configuration configuration = resources.getConfiguration();
        if (!configuration.locale.equals(applicationUtils.getLocale())) {
            applicationUtils.setLocale(applicationUtils.getLocale());
        }
    }
}
