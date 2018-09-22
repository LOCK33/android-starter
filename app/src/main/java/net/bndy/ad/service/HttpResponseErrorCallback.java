package net.bndy.ad.service;

import com.android.volley.VolleyError;

public interface HttpResponseErrorCallback {
    void onErrorResponse(VolleyError error);
}
