package net.bndy.ad.framework.http;

import com.android.volley.VolleyError;

public interface ErrorCallback {
    void onErrorResponse(VolleyError error);
}
