package net.bndy.ad;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.bndy.ad.domain.GoogleUser;
import net.bndy.ad.lib.oauth.OAuthLoginService;
import net.bndy.ad.service.HttpResponseSuccessCallback;
import net.openid.appauth.AuthState;

public class SplashActivity extends AppCompatActivity {

    private Button btnLogout;
    private Button btnLoginGoogle;
    private TextView viewHello;

    private OAuthLoginService oAuthLoginService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        btnLogout = (Button) findViewById(R.id.logout_btn);
        btnLoginGoogle = (Button) findViewById(R.id.login_google_btn);
        viewHello = (TextView)  findViewById(R.id.hello_view);
        oAuthLoginService = new OAuthLoginService(this, GoogleUser.class).setLogTag(Application.LOG_TAG);

        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithGoogle();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                logout();
                refreshUI();
            }
        });

        refreshUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == OAuthLoginService.REQUEST_CODE) {
            oAuthLoginService.handleAuthorizationResponse(data, new HttpResponseSuccessCallback<GoogleUser>() {
                @Override
                public void onSuccessResponse(GoogleUser response) {
                    viewHello.setText(response.getName());
                    refreshUI();
                }
            }, null);
        }
    }

    private void refreshUI() {
        AuthState authState = this.oAuthLoginService.getAuthState();
        if (authState != null && authState.isAuthorized()) {
            if (this.oAuthLoginService.getUser() != null) {
                this.viewHello.setText("Hi " + ((GoogleUser)this.oAuthLoginService.getUser()).getName());
            }
            this.btnLoginGoogle.setVisibility(View.GONE);
            this.btnLogout.setVisibility(View.VISIBLE);
        } else {
            this.btnLoginGoogle.setVisibility(View.VISIBLE);
            this.btnLogout.setVisibility(View.GONE);
            this.viewHello.setText(R.string.hello);
        }
    }

    private void loginWithGoogle() {
        this.oAuthLoginService.doAuth(OAuthLoginService.GoogleConfiguration);
    }

    private void logout() {
        this.oAuthLoginService.logout();
        refreshUI();
    }
}
