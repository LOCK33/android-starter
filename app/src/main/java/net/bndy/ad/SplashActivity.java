package net.bndy.ad;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import net.bndy.ad.framework.ApplicationUtils;
import net.bndy.ad.framework.BaseActivity;
import net.bndy.ad.model.AppUser;
import net.bndy.ad.model.GoogleUser;
import net.bndy.ad.oauth.OAuthLoginService;
import net.bndy.ad.sample.DrawableListActivity;
import net.bndy.ad.sample.FormActivity;
import net.bndy.ad.sample.GenerateBarcodeActivity;
import net.bndy.ad.sample.ScanBarcodeActivity;
import net.bndy.ad.sample.TakePhotoActivity;
import net.bndy.ad.service.HttpResponseSuccessCallback;
import net.openid.appauth.AuthState;

import org.xutils.common.Callback;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class SplashActivity extends BaseActivity {

    @ViewInject(R.id.logout_btn)
    private Button btnLogout;
    @Event(R.id.logout_btn)
    private void onLogout(View view) {
        logout();
        refreshUI();
    }

    @ViewInject(R.id.login_google_btn)
    private Button btnLoginGoogle;
    @Event(R.id.login_google_btn)
    private void onLoginWithGoogle(View view) {
        this.oAuthLoginService.doAuth(OAuthLoginService.GoogleConfiguration);
    }

    @Event(R.id.scan_barcode_btn)
    private void onScanBarcode(View view) {
        startActivity(ScanBarcodeActivity.class);
    }

    @Event(R.id.generate_barcode_btn)
    private void onGenerateBarcode(View view) {
        startActivity(GenerateBarcodeActivity.class);
    }

    @Event(R.id.form_btn)
    private void onForm(View view) {
        startActivity(FormActivity.class);
    }

    @Event(R.id.show_progress_bar)
    private void onShowProgressBar(View view) {
        showProgressBar();
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(5000);
                    hideProgressBar();
                } catch (Exception ex) { }

            }
        }.start();
    }

    @Event(R.id.show_drawable)
    private void onShowDrawable(View view) {
        startActivity(DrawableListActivity.class);
    }

    @Event(R.id.take_photo)
    private void onTakePhoto(View view) {
        startActivity(TakePhotoActivity.class);
    }

    private OAuthLoginService oAuthLoginService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //mApplicationUtils.setLocale(null); //java.util.Locale.CHINA);  // set default locale,  must be before at setContentView method

        x.view().inject(this);
        oAuthLoginService = new OAuthLoginService(this, GoogleUser.class).setLogTag(Application.LOG_TAG);

        setActionMenu(R.menu.main);
        registerProgressBar();

        refreshUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == OAuthLoginService.REQUEST_CODE) {
            oAuthLoginService.handleAuthorizationResponse(data, new HttpResponseSuccessCallback<GoogleUser>() {
                @Override
                public void onSuccessResponse(GoogleUser response) {
                    refreshUI();
                }
            }, null);
        }
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

    private void refreshUI() {
        AuthState authState = this.oAuthLoginService.getAuthState();
        if (authState != null && authState.isAuthorized() && this.oAuthLoginService.getUser() != null) {
            AppUser user = this.oAuthLoginService.getUser();
            setTitle(" Hi " + user.getName());
            x.image().loadDrawable(user.getAvatar(), new ImageOptions() { }, new Callback.CommonCallback<Drawable>() {
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
            this.btnLoginGoogle.setVisibility(View.GONE);
            this.btnLogout.setVisibility(View.VISIBLE);
        } else {
            this.btnLoginGoogle.setVisibility(View.VISIBLE);
            this.btnLogout.setVisibility(View.GONE);
            setIcon(null);
            setTitle(R.string.app_name);
        }
    }

    private void logout() {
        confirm(R.string.sign_out, R.string.sign_out_confirmation, new ApplicationUtils.Action() {
            @Override
            public void execute(Object... args) {
                oAuthLoginService.logout();
                refreshUI();
                info(R.string.sign_out_success);
            }
        }, null);
    }
}
