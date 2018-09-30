package net.bndy.ad;

import android.support.v7.app.AppCompatActivity;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity  {

    private Locale mCurrentLocale;

    protected ApplicationUtils applicationUtils;

    public BaseActivity() {
        super();
        applicationUtils = new ApplicationUtils(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCurrentLocale = getResources().getConfiguration().locale;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Locale locale = applicationUtils.getLocale();

        if (!locale.equals(mCurrentLocale)) {
            mCurrentLocale = locale;
            recreate();
        }
    }
}
