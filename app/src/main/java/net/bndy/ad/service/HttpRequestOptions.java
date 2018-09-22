package net.bndy.ad.service;

import com.android.volley.VolleyError;

import java.util.Collections;
import java.util.Map;

public class HttpRequestOptions {

    public Map<String, String> getHeaders() {
        return Collections.emptyMap();
    }

    public void onError(VolleyError error) {
    }
}
