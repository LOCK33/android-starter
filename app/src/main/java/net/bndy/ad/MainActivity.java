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
import net.bndy.ad.sample.TableFragment;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity {

    private static final String KEY_TAB_CURRENT = "tab.current";

    private OAuthLoginService oAuthLoginService;
    private Map<Integer, Fragment> mFragmentMap;

    @ViewInject(R.id.splash_tabs)
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //utils.setLocale(null); //java.util.Locale.CHINA);  // set default locale,  must be before at setContentView method
        setContentView(R.layout.activity_main);

        x.view().inject(this);

        setActionMenu(R.menu.main);
        registerProgressBar();

        mFragmentMap = new HashMap<>();
        mFragmentMap.put(R.string.form, new FormFragment());
        mFragmentMap.put(R.string.image, new ImagesFragment());
        mFragmentMap.put(R.string.photo, new PictureFragment());
        mFragmentMap.put(R.string.barcode, new BarcodeFragment());
        mFragmentMap.put(R.string.table, new TableFragment());
        mTabLayout.setItems(R.id.splash_content_container, mFragmentMap, this);
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

    @Override
    protected void onResume() {
        initView();
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_TAB_CURRENT, mTabLayout.getCurrentTab());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mTabLayout.setCurrentTab(savedInstanceState.getInt(KEY_TAB_CURRENT, 0));
        }
        super.onRestoreInstanceState(savedInstanceState);
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
            } else {
                setIcon(null);
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
