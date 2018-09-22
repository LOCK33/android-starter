package net.bndy.ad.service;

public interface HttpResponseSuccessCallback<T> {
    void onSuccessResponse(T response);
}
