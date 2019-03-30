package net.bndy.ad;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import net.bndy.ad.framework.ui.BaseActivity;
import net.bndy.ad.framework.utils.ImageUtils;
import net.bndy.ad.framework.http.Caller;
import net.bndy.ad.model.AppUser;
import net.bndy.ad.model.GoogleUser;
import net.bndy.ad.oauth.OAuthLoginService;
import net.bndy.ad.service.DbHelper;
import net.bndy.ad.framework.http.ErrorCallback;
import net.bndy.ad.framework.http.SuccessCallback;
import net.openid.appauth.AuthState;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class LoginActivity extends BaseActivity {

    private OAuthLoginService<GoogleUser> mOAuthLoginService;
    private Caller mApiCaller;
    private Context mContext;

    @ViewInject(R.id.login_main_layout)
    private LinearLayout mLayoutMain;
    @ViewInject(R.id.login_username_input)
    private EditText mEditTextUsername;
    @ViewInject(R.id.login_wel_layout)
    private LinearLayout mLayoutWel;
    @ViewInject(R.id.login_user_avatar_img)
    private ImageView mImageViewUserAvatar;
    @ViewInject(R.id.login_username_tv)
    private TextView mTextViewUsername;
    @ViewInject(R.id.login_copyright)
    private TextView mTextViewCopyright;
    @ViewInject(R.id.login_sign_in_with_google_btn)
    private TextView mTextViewLoginWithGoogle;

    @ViewInject(R.id.login_layout)
    private ConstraintLayout mLayout;

    @Event(R.id.login_sign_in_btn)
    private void onLogin(View view) {
        if (true) { // checkRequired(R.id.login_username_input, R.string.required) && checkRequired(R.id.login_password_input, R.string.required)) {
            final AppUser appUser = new AppUser();
            if (this.mEditTextUsername.getText() != null) {
                appUser.setName(this.mEditTextUsername.getText().toString());
            }

            // TODO: logic to authorize your username and password
            Toast.makeText(mContext, "Calling api to authorize...", Toast.LENGTH_SHORT).show();
            mApiCaller.getString("https://api.github.com/users/bndynet", new SuccessCallback<String>() {
                @Override
                public void onSuccessResponse(String response) {
                    Toast.makeText(mContext, "Response: " + response, Toast.LENGTH_LONG).show();
                    mOAuthLoginService.setUser(appUser);
                    DbHelper dbHelper = new DbHelper(mContext, AppUser.class);
                    dbHelper.insert(appUser);
                    startActivity(MainActivity.class);
                }
            });
        }
    }

    @Event(R.id.login_sign_in_with_google_btn)
    private void onLoginWithGoogle(View view) {
        showProgressBar();
        this.mOAuthLoginService.doAuth(OAuthLoginService.GoogleConfiguration);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        x.view().inject(this);
        registerProgressBar();

        mContext = this.getBaseContext();
        mApiCaller = new Caller(this, null);
        mOAuthLoginService = new OAuthLoginService(this, GoogleUser.class).setLogTag(Application.LOG_TAG);
        utils.setTextViewAsLink(mTextViewLoginWithGoogle);
        utils.enableLinksInTextView(mTextViewCopyright);

        mLayout.setBackground(ImageUtils.blur(this, mLayout.getBackground()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == OAuthLoginService.REQUEST_CODE) {
            mOAuthLoginService.handleAuthorizationResponse(data, new SuccessCallback<GoogleUser>() {
                @Override
                public void onSuccessResponse(GoogleUser response) {
                    refreshUI();
                    startActivity(MainActivity.class);
                }
            }, new ErrorCallback() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideProgressBar();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        mOAuthLoginService.dispose();
        super.onDestroy();
    }

    private void refreshUI() {
        AuthState authState = this.mOAuthLoginService.getAuthState();
        if (authState != null && authState.isAuthorized() && this.mOAuthLoginService.getUser() != null) {
            AppUser user = this.mOAuthLoginService.getUser();
            mTextViewUsername.setText(user.getName());
            x.image().bind(mImageViewUserAvatar, user.getAvatar());
            mLayoutMain.setVisibility(View.GONE);
            mLayoutWel.setVisibility(View.VISIBLE);
        } else {
            mLayoutMain.setVisibility(View.VISIBLE);
            mLayoutWel.setVisibility(View.GONE);
        }
    }
}
