package net.bndy.ad;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;

import net.bndy.ad.framework.ApplicationUtils;
import net.bndy.ad.framework.BaseScanActivity;
import net.bndy.ad.model.AppUser;
import net.bndy.ad.model.GoogleUser;
import net.bndy.ad.oauth.OAuthLoginService;
import net.bndy.ad.sample.DrawableListActivity;
import net.bndy.ad.sample.FormActivity;
import net.bndy.ad.sample.GenerateBarcodeActivity;
import net.bndy.ad.sample.ScanBarcodeActivity;
import net.bndy.ad.service.HttpResponseSuccessCallback;
import net.openid.appauth.AuthState;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class SplashActivity extends BaseScanActivity {

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

    @ViewInject(R.id.user_name)
    TextView mUserName;
    @ViewInject(R.id.user_avatar)
    CircularImageView mUserAvatar;

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

    private ActionBar actionBar;
    private OAuthLoginService oAuthLoginService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setIcon(R.mipmap.ic_launcher);

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
                exitApplication();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void scanCallback(String scanResult) {
        info(scanResult);
    }

    private void refreshUI() {
        AuthState authState = this.oAuthLoginService.getAuthState();
        if (authState != null && authState.isAuthorized() && this.oAuthLoginService.getUser() != null) {
            AppUser user = this.oAuthLoginService.getUser();
            if (user.getMale() != null) {
                if (user.getMale().booleanValue()) {
                    mUserName.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    mUserName.setTextColor(getResources().getColor(R.color.colorAccent));
                }
            }
            bindObjectToView(user, "user_");
            this.btnLoginGoogle.setVisibility(View.GONE);
            this.btnLogout.setVisibility(View.VISIBLE);
        } else {
            this.btnLoginGoogle.setVisibility(View.VISIBLE);
            this.btnLogout.setVisibility(View.GONE);
            this.mUserName.setText(R.string.hello);
            this.mUserAvatar.setImageResource(R.drawable.image_placeholder);
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
