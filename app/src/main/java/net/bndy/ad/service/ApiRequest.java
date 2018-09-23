package net.bndy.ad.service;

import android.support.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;


import java.io.UnsupportedEncodingException;
import java.util.Map;

public class ApiRequest<T> extends JsonRequest<T> {

    protected Class<T> resultType;
    /**
     * Creates a new request.
     *
     * @param method the HTTP method to use
     * @param url URL to fetch the JSON from
     * @param requestData A {@link T} to post with the request. Null indicates no
     *     parameters will be posted along with request.
     * @param listener Listener to receive the JSON response
     * @param errorListener Error listener, or null to ignore errors.
     */
    public ApiRequest(
            Class<T> resultType,
            int method,
            String url,
            @Nullable Map<String, Object> requestData,
            Response.Listener<T> listener,
            @Nullable Response.ErrorListener errorListener) {
        super(
                method,
                url,
                (requestData == null) ? null : requestData.toString(), // TODO: convert to string
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
}
