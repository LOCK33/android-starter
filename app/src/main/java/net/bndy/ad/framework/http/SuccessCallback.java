package net.bndy.ad.framework.http;

public interface SuccessCallback<T> {
    void onSuccessResponse(T response);
}
