package net.bndy.ad.service;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;

public class LoginOnlineService {
    public void login(Context context) {
        AuthorizationServiceConfiguration serviceConfiguration =
                new AuthorizationServiceConfiguration(
                        Uri.parse("http://10.140.200.99:9110/oauth/authorize"), // authorization endpoint
                        Uri.parse("http://10.140.200.99:9110/oauth/token")); // token endpoint

        String clientId = "foo";
        Uri redirectUri = Uri.parse("net.bndy.ad:/oauth2redirect");
        AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(
                serviceConfiguration,
                clientId,
                ResponseTypeValues.CODE,
                redirectUri
        );
        builder.setScopes("user_info");
        AuthorizationRequest request = builder.build();
        AuthorizationService authorizationService = new AuthorizationService(context);

        String action = "com.google.codelabs.appauth.HANDLE_AUTHORIZATION_RESPONSE";
        Intent postAuthorizationIntent = new Intent(action);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, request.hashCode(), postAuthorizationIntent, 0);
        authorizationService.performAuthorizationRequest(request, pendingIntent);
    }
}
