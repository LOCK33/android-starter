package net.bndy.ad.framework.http;

import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApiRequest<T> extends JsonRequest<T> {

    protected Class<T> resultType;

    /**
     * Creates a new request.
     *
     * @param method        the HTTP method to use
     * @param url           URL to fetch the JSON from
     * @param requestData   A {@link T} to post with the request. Null indicates no
     *                      parameters will be posted along with request.
     * @param listener      Listener to receive the JSON response
     * @param errorListener Error listener, or null to ignore errors.
     */
    public ApiRequest(
            Class<T> resultType,
            int method,
            String url,
            @Nullable Map<String, Object> requestData,
            Response.Listener<T> listener,
            @Nullable Response.ErrorListener errorListener) {
        super(method,
                url,
                map2RequestData(requestData),
                listener,
                errorListener);

        this.resultType = resultType;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString =
                    new String(
                            response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            T entity = new Gson().fromJson(jsonString, resultType);
            return Response.success(entity, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    private static String map2RequestData(Map<String, Object> data) {
        List<String> requestDataList = new ArrayList<>();
        if (data != null) {
            for (String key : data.keySet()) {
                Object value = data.get(key);
                requestDataList.add(key + "=" + (value == null ? "" : value.toString()));
            }
        }
        return requestDataList.size() == 0
                ? null : (Build.VERSION.SDK_INT >= 26
                ? String.join("&", requestDataList) : TextUtils.join("&", requestDataList));
    }
}
