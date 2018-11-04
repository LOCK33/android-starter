package net.bndy.ad;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import net.bndy.ad.framework.ApplicationUtils;
import net.bndy.ad.framework.BaseActivity;
import net.bndy.ad.framework.ui.TabLayout;
import net.bndy.ad.model.AppUser;
import net.bndy.ad.model.GoogleUser;
import net.bndy.ad.oauth.OAuthLoginService;
import net.bndy.ad.sample.FormFragment;
import net.bndy.ad.sample.BarcodeFragment;
import net.bndy.ad.sample.ImagesFragment;
import net.bndy.ad.sample.PictureFragment;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity {

    private OAuthLoginService oAuthLoginService;
    private Map<Integer, Fragment> mFragmentMap;

    @ViewInject(R.id.splash_tabs)
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //utils.setLocale(null); //java.util.Locale.CHINA);  // set default locale,  must be before at setContentView method

        // go to splash screen
        if (Application.showSplashPage == Application.ShowSplashPage.AlWAYS ||
                (Application.showSplashPage == Application.ShowSplashPage.ONCE && !getSP().getBoolean(Application.KEY_SKIP_SPLASH, false))) {
            startActivity(SplashActivity.class);
        }

        x.view().inject(this);

        setActionMenu(R.menu.main);
        registerProgressBar();

        initView();

        mFragmentMap = new HashMap<>();
        mFragmentMap.put(R.string.form, new FormFragment());
        mFragmentMap.put(R.string.image, new ImagesFragment());
        mFragmentMap.put(R.string.photo, new PictureFragment());
        mFragmentMap.put(R.string.barcode, new BarcodeFragment());
        mTabLayout.setItems(R.id.splash_content_container, mFragmentMap, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_logout:
                logout();
                break;

            case R.id.menu_main_exit:
                exitApplication();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        oAuthLoginService.dispose();
        super.onDestroy();
    }

    private void initView() {
        if (oAuthLoginService == null) {
            oAuthLoginService = new OAuthLoginService(this, GoogleUser.class).setLogTag(Application.LOG_TAG);
        }
        AppUser appUser = oAuthLoginService.getUser();
        if (oAuthLoginService.getUser() == null) {
            startActivity(LoginActivity.class);
        } else {
            if (appUser.getAvatar() != null && !appUser.getAvatar().isEmpty()) {
                x.image().loadDrawable(appUser.getAvatar(), new ImageOptions() {
                }, new Callback.CommonCallback<Drawable>() {
                    @Override
                    public void onSuccess(Drawable result) {
                        setIcon(result);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
            }
            setTitle(" " + oAuthLoginService.getUser().getName());
        }
    }

    private void logout() {
        confirm(R.string.sign_out, R.string.sign_out_confirmation, new ApplicationUtils.Action() {
            @Override
            public void execute(Object... args) {
                oAuthLoginService.logout();
                info(R.string.sign_out_success);
                startActivity(LoginActivity.class);
            }
        }, null);
    }
}
