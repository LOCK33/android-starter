package net.bndy.ad.lib.oauth;

public class OAuthConfiguration {

    private String authEndpoint;
    private String tokenEndpoint;
    private String userInfoEndpoint;
    private String clientID;
    private String clientSecret;
    private String redirectUri;
    private String scopes;

    public String getAuthEndpoint() {
        return authEndpoint;
    }

    public OAuthConfiguration setAuthEndpoint(String authEndpoint) {
        this.authEndpoint = authEndpoint;
        return this;
    }

    public String getTokenEndpoint() {
        return tokenEndpoint;
    }

    public OAuthConfiguration setTokenEndpoint(String tokenEndpoint) {
        this.tokenEndpoint = tokenEndpoint;
        return this;
    }

    public String getUserInfoEndpoint() {
        return userInfoEndpoint;
    }

    public OAuthConfiguration setUserInfoEndpoint(String userInfoEndpoint) {
        this.userInfoEndpoint = userInfoEndpoint;
        return this;
    }

    public String getClientID() {
        return clientID;
    }

    public OAuthConfiguration setClientID(String clientID) {
        this.clientID = clientID;
        return this;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public OAuthConfiguration setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public OAuthConfiguration setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }

    public String getScopes() {
        return scopes;
    }

    public OAuthConfiguration setScopes(String scopes) {
        this.scopes = scopes;
        return this;
    }
}

