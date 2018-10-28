package net.bndy.ad;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.flyco.tablayout.CommonTabLayout;

import net.bndy.ad.framework.ApplicationUtils;
import net.bndy.ad.framework.BaseActivity;
import net.bndy.ad.model.AppUser;
import net.bndy.ad.model.GoogleUser;
import net.bndy.ad.oauth.OAuthLoginService;
import net.bndy.ad.sample.DrawableListFragment;
import net.bndy.ad.sample.FormFragment;
import net.bndy.ad.sample.BarcodeFragment;
import net.bndy.ad.sample.TakePhotoFragment;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends BaseActivity {

//    @Event(R.id.scan_barcode_btn)
//    private void onScanBarcode(View view) {
//        startActivity(ScanBarcodeActivity.class);
//    }
//
//    @Event(R.id.generate_barcode_btn)
//    private void onGenerateBarcode(View view) {
//        startActivity(GenerateBarcodeActivity.class);
//    }
//
//    @Event(R.id.form_btn)
//    private void onForm(View view) {
//        startActivity(FormActivity.class);
//    }
//
//    @Event(R.id.show_progress_bar)
//    private void onShowProgressBar(View view) {
//        showProgressBar();
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    sleep(5000);
//                    hideProgressBar();
//                } catch (Exception ex) { }
//
//            }
//        }.start();
//    }
//
//    @Event(R.id.show_drawable)
//    private void onShowDrawable(View view) {
//        startActivity(DrawableListFragment.class);
//    }
//
//    @Event(R.id.take_photo)
//    private void onTakePhoto(View view) {
//        startActivity(TakePhotoActivity.class);
//    }

    @ViewInject(R.id.splash_tabs)
    private CommonTabLayout mTabLayout;

    private OAuthLoginService oAuthLoginService;
    private Map<Integer, Fragment> mFragmentMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //utils.setLocale(null); //java.util.Locale.CHINA);  // set default locale,  must be before at setContentView method

        x.view().inject(this);
        oAuthLoginService = new OAuthLoginService(this, GoogleUser.class).setLogTag(Application.LOG_TAG);
        setActionMenu(R.menu.main);
        registerProgressBar();

        initView();

        mFragmentMap = new HashMap<>();
        mFragmentMap.put(R.string.form, new FormFragment());
        mFragmentMap.put(R.string.show_drawable, new DrawableListFragment());
        mFragmentMap.put(R.string.take_photo, new TakePhotoFragment());
        mFragmentMap.put(R.string.barcode, new BarcodeFragment());
        setTabs(mTabLayout, R.id.splash_content_container, mFragmentMap);
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


    private void initView() {
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
