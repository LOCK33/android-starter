package net.bndy.ad;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import net.bndy.ad.framework.BaseScanActivity;
import net.bndy.ad.model.AppUser;
import net.bndy.ad.model.GoogleUser;
import net.bndy.ad.lib.oauth.OAuthLoginService;
import net.bndy.ad.service.HttpResponseSuccessCallback;
import net.openid.appauth.AuthState;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends BaseScanActivity {

    @BindView(R.id.logout_btn)
    Button btnLogout;
    @OnClick(R.id.logout_btn)
    void onLogout() {
        logout();
        refreshUI();
    }

    @BindView(R.id.login_google_btn)
    Button btnLoginGoogle;
    @OnClick(R.id.login_google_btn)
    void onLoginWithGoogle() {
        this.oAuthLoginService.doAuth(OAuthLoginService.GoogleConfiguration);
    }

    @BindView(R.id.login_face_btn)
    Button btnLoginFace;
    @OnClick(R.id.login_face_btn)
    void onLoginWithFace() {
        startScan();
    }

    @BindView(R.id.hello_view)
    TextView viewHello;
    @BindView(R.id.user_img)
    CircularImageView userImg;

    private ActionBar actionBar;
    private OAuthLoginService oAuthLoginService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //applicationUtils.setLocale(Locale.CHINA);  // set default locale,  must be before at setContentView method
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        actionBar = getSupportActionBar();
        oAuthLoginService = new OAuthLoginService(this, GoogleUser.class).setLogTag(Application.LOG_TAG);

        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setIcon(R.drawable.github);

        refreshUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == OAuthLoginService.REQUEST_CODE) {
            oAuthLoginService.handleAuthorizationResponse(data, new HttpResponseSuccessCallback<GoogleUser>() {
                @Override
                public void onSuccessResponse(GoogleUser response) {
                    viewHello.setText(response.getName());
                    Picasso.get().load(response.getPicture()).into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            Drawable drawable = new BitmapDrawable(getBaseContext().getResources(), bitmap);
                            actionBar.setIcon(drawable);
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
                    refreshUI();
                }
            }, null);
        }
    }

    @Override
    protected void scanCallback(String scanResult) {
        applicationUtils.info(scanResult);
    }

    private void refreshUI() {
        AuthState authState = this.oAuthLoginService.getAuthState();
        if (authState != null && authState.isAuthorized() && this.oAuthLoginService.getUser() != null) {
            AppUser user = this.oAuthLoginService.getUser();
            this.viewHello.setText(user.getName());
            if (user.getMale() != null) {
                if (user.getMale().booleanValue()) {
                    viewHello.setTextColor(getResources().getColor(R.color.colorBlue));
                } else {
                    viewHello.setTextColor(getResources().getColor(R.color.colorPink));
                }
            }
            Picasso.get().load(user.getAvatar()).into(userImg);
            this.btnLoginGoogle.setVisibility(View.GONE);
            this.btnLogout.setVisibility(View.VISIBLE);
        } else {
            this.btnLoginGoogle.setVisibility(View.VISIBLE);
            this.btnLogout.setVisibility(View.GONE);
            this.viewHello.setText(R.string.hello);
            this.userImg.setImageResource(R.drawable.image_placeholder);
        }
    }

    private void logout() {
        applicationUtils.confirm(R.string.sign_out, R.string.sign_out_confirmation, new ApplicationUtils.Action() {
            @Override
            public void execute(Object... args) {
                oAuthLoginService.logout();
                refreshUI();
                applicationUtils.info(R.string.sign_out_success);
            }
        }, null);
    }
}
