package net.bndy.ad.framework.system;

import android.app.Activity;

public abstract class BaseHelper {
    protected Activity mActivity;

    public BaseHelper(Activity activity) {
        mActivity = activity;
    }
}
