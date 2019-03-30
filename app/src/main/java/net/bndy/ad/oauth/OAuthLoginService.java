package net.bndy.ad.oauth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import net.bndy.ad.model.AppUser;
import net.bndy.ad.model.AppUserInteface;
import net.bndy.ad.framework.http.RequestOptions;
import net.bndy.ad.framework.http.Caller;
import net.bndy.ad.framework.http.ErrorCallback;
import net.bndy.ad.framework.http.SuccessCallback;
import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.TokenResponse;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class OAuthLoginService<TUser extends AppUserInteface> {

    private static final String SHARED_PREFERENCES_NAME = "AuthStatePreference";
    private static final String AUTH_STATE = "AUTH_STATE";
    public static final int REQUEST_CODE = 1;
    public static final OAuthConfiguration GoogleConfiguration = new OAuthConfiguration()
            .setAuthEndpoint("https://accounts.google.com/o/oauth2/v2/auth")
            .setTokenEndpoint("https://www.googleapis.com/oauth2/v4/token")
            .setClientID("511828570984-fuprh0cm7665emlne3rnf9pk34kkn86s.apps.googleusercontent.com")
            .setRedirectUri("com.google.codelabs.appauth:/oauth2callback")   // also be specified in AndroidManifest.xml
            .setScopes("profile")
            .setUserInfoEndpoint("https://www.googleapis.com/oauth2/v3/userinfo")
            ;

    private static AppUser sUser;
    private static AuthorizationService sAuthorizationService;

    private String logTag = "OAuth";
    private Activity activity;
    private OAuthConfiguration configuration;
    private Class<TUser> userClazz;

    public OAuthLoginService setLogTag(String tag) {
        logTag = tag;
        return this;
    }

    public AppUser getUser() {
        return sUser;
    }

    public void setUser(AppUser user) {
        sUser = user;
    }

    public AuthorizationService getAuthorizationService() {
        return sAuthorizationService;
    }

    public OAuthLoginService(Activity activity, Class<TUser> userClazz) {
        this.activity = activity;
        this.userClazz = userClazz;
        if (sAuthorizationService == null) {
            sAuthorizationService = new AuthorizationService(this.activity);
        }
    }

    public void doAuth(OAuthConfiguration configuration) {
        this.configuration = configuration;
        Log.d(this.logTag, String.format("Start authorizing with client %s...", configuration.getClientID()));

        AuthorizationServiceConfiguration serviceConfiguration = new AuthorizationServiceConfiguration(
                Uri.parse(configuration.getAuthEndpoint()),
                Uri.parse(configuration.getTokenEndpoint())
        );
        AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(
                serviceConfiguration,
                configuration.getClientID(),
                ResponseTypeValues.CODE,
                Uri.parse(configuration.getRedirectUri())
        );
        builder.setScopes(configuration.getScopes());

        AuthorizationRequest request = builder.build();
        Intent authIntent = sAuthorizationService.getAuthorizationRequestIntent(request);
        this.activity.startActivityForResult(authIntent, REQUEST_CODE);
    }

    public void logout() {
        Log.i(this.logTag, "Logging out...");
        sUser = null;
        this.clearAuthState();
    }

    @Nullable
    public AuthState getAuthState() {
        String jsonString = this.activity.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                .getString(AUTH_STATE, null);
        if (!TextUtils.isEmpty(jsonString)) {
            try {
                return AuthState.jsonDeserialize(jsonString);
            } catch (JSONException jsonException) {
                // should never happen
            }
        }
        return null;
    }

    /**
     * Exchanges the code, for the {@link TokenResponse}.
     *
     * @param intent represents the {@link Intent} from the Custom Tabs or the System Browser.
     */
    public void handleAuthorizationResponse(@NonNull Intent intent, final SuccessCallback<TUser> callback, final ErrorCallback errorCallback) {
        AuthorizationResponse response = AuthorizationResponse.fromIntent(intent);
        AuthorizationException error = AuthorizationException.fromIntent(intent);
        final AuthState authState = new AuthState(response, error);
        if (response != null) {
            Log.i(this.logTag, String.format("Handled Authorization Response %s ", authState.jsonSerializeString()));
            sAuthorizationService.performTokenRequest(response.createTokenExchangeRequest(), new AuthorizationService.TokenResponseCallback() {
                @Override
                public void onTokenRequestCompleted(@Nullable TokenResponse tokenResponse, @Nullable AuthorizationException exception) {
                    if (exception != null) {
                        Log.w(logTag, "Token Exchange failed", exception);
                    } else {
                        if (tokenResponse != null) {
                            authState.update(tokenResponse, exception);
                            persistAuthState(authState);
                            getUserInfo(callback, errorCallback);
                            Log.i(logTag, String.format("Token Response [ Access Token: %s, ID Token: %s ]", tokenResponse.accessToken, tokenResponse.idToken));
                        }
                    }
                }
            });
        }
    }

    public void getUserInfo(final SuccessCallback<TUser> callback, final ErrorCallback errorCallback) {
        Log.i(logTag, String.format("Getting user info from %s...", configuration.getUserInfoEndpoint()));
        this.getAuthState().performActionWithFreshTokens(this.getAuthorizationService(), new AuthState.AuthStateAction() {
            @Override
            public void execute(@Nullable final String accessToken, @Nullable String idToken, @Nullable AuthorizationException ex) {
                Caller requestService = new Caller(activity, new RequestOptions(){
                    @Override
                    public Map<String, String> getHeaders() {
                        Log.i(logTag, String.format("Setting header Authorization: Bearer %s...", accessToken));

                        Map<String, String> headers = new HashMap<>();
                        headers.put("Authorization", String.format("Bearer %s", accessToken));
                        return headers;
                    }
                });
                requestService.apiGet(userClazz, configuration.getUserInfoEndpoint(), new SuccessCallback<TUser>() {
                    @Override
                    public void onSuccessResponse(TUser response) {
                        sUser = response.toAppUser();
                        callback.onSuccessResponse(response);
                    }
                }, errorCallback);
            }
        });
    }

    public void dispose() {
        if (sAuthorizationService != null) {
            sAuthorizationService.dispose();
            sAuthorizationService = null;
        }
    }

    private void persistAuthState(@NonNull AuthState authState) {
        this.activity.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
                .putString(AUTH_STATE, authState.jsonSerializeString())
                .commit();
    }

    private void clearAuthState() {
        this.activity.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
                .edit()
                .remove(AUTH_STATE)
                .apply();
    }
}

