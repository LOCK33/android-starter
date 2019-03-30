package net.bndy.ad.framework.http;

import com.android.volley.VolleyError;

import java.util.Collections;
import java.util.Map;

public class RequestOptions {

    public Map<String, String> getHeaders() {
        return Collections.emptyMap();
    }

    public void onError(VolleyError error) {
    }
}
