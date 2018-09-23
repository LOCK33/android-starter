package net.bndy.ad.service;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class HttpRequestService {

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    private RequestQueue requestQueue;

    public HttpRequestOptions getRequestOptions() {
        return requestOptions;
    }

    private HttpRequestOptions requestOptions;

    public HttpRequestService(Context context, HttpRequestOptions requestOptions) {
        this.requestQueue = Volley.newRequestQueue(context);
        this.requestOptions = requestOptions;
    }

    public void getString(String url, final HttpResponseSuccessCallback responseSuccess) {
        this.getString(url, responseSuccess, null);
    }

    public void getString(String url, @NonNull final HttpResponseSuccessCallback<String> responseSuccess, final HttpResponseErrorCallback responseError) {
        final StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                responseSuccess.onSuccessResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (responseError != null) {
                    responseError.onErrorResponse(error);
                } else {
                    if (requestOptions != null) {
                        requestOptions.onError(error);
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (requestOptions != null) {
                    return requestOptions.getHeaders();
                }
                return super.getHeaders();
            }
        };

        this.requestQueue.add(request);
    }

    public void getObject(String url, @NonNull final HttpResponseSuccessCallback<JSONObject> responseSuccess, final HttpResponseErrorCallback responseError) {
        jsonRequest(Request.Method.GET, url, null, responseSuccess, responseError);
    }

    public void getList(String url, @NonNull final HttpResponseSuccessCallback<JSONArray> responseSuccess, final HttpResponseErrorCallback responseError) {
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                responseSuccess.onSuccessResponse(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (responseError != null) {
                    responseError.onErrorResponse(error);
                } else {
                    if (requestOptions != null) {
                        requestOptions.onError(error);
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (requestOptions != null) {
                    return requestOptions.getHeaders();
                }
                return super.getHeaders();
            }
        };

        this.requestQueue.add(request);
    }

    public void post(String url, Map<String, Object> requestData, HttpResponseSuccessCallback<JSONObject> responseSuccess, HttpResponseErrorCallback responseError) {
        jsonRequest(Request.Method.POST, url, requestData, responseSuccess, responseError);
    }

    public void delete(String url, HttpResponseSuccessCallback<JSONObject> responseSuccess, HttpResponseErrorCallback responseError) {
        jsonRequest(Request.Method.DELETE, url, null, responseSuccess, responseError);
    }

    public void put(String url, Map<String, Object> requestData, HttpResponseSuccessCallback<JSONObject> responseSuccess, HttpResponseErrorCallback responseError) {
        jsonRequest(Request.Method.PUT, url, requestData, responseSuccess, responseError);
    }

    public void jsonRequest(int method,
                            String url,
                            Map<String, Object> requestData,
                            final HttpResponseSuccessCallback<JSONObject> responseSuccess,
                            final HttpResponseErrorCallback responseError) {
        JSONObject jsonObject = new JSONObject(requestData);
        JsonObjectRequest request = new JsonObjectRequest(method, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (responseSuccess != null) {
                    responseSuccess.onSuccessResponse(response);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (responseError != null) {
                    responseError.onErrorResponse(error);
                } else {
                    if (requestOptions != null) {
                        requestOptions.onError(error);
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (requestOptions != null) {
                    return requestOptions.getHeaders();
                }
                return super.getHeaders();
            }
        };

        this.requestQueue.add(request);
    }


    public <T> void apiGet(Class<T> resultType, String url, final HttpResponseSuccessCallback<T> responseSuccess, final HttpResponseErrorCallback responseError) {
        this.apiRequest(resultType, Request.Method.GET, url, null, responseSuccess, responseError);
    }
    public <T> void apiPost(Class<T> resultType, String url, Map<String, Object> postData, final  HttpResponseSuccessCallback<T> responseSuccess,  final HttpResponseErrorCallback responseError) {
       this.apiRequest(resultType, Request.Method.POST, url, postData, responseSuccess, responseError);
    }
    public <T> void apiPut(Class<T> resultType, String url, Map<String, Object> postData, final  HttpResponseSuccessCallback<T> responseSuccess,  final HttpResponseErrorCallback responseError) {
        this.apiRequest(resultType, Request.Method.PUT, url, postData, responseSuccess, responseError);
    }
    public <T> void apiDelete(Class<T> resultType, String url, final  HttpResponseSuccessCallback<T> responseSuccess,  final HttpResponseErrorCallback responseError) {
        this.apiRequest(resultType, Request.Method.DELETE, url, null, responseSuccess, responseError);
    }
    public <T> void apiRequest(Class<T> resultType,
                            int method,
                            String url,
                            Map<String, Object> requestData,
                            final HttpResponseSuccessCallback<T> responseSuccess,
                            final HttpResponseErrorCallback responseError) {
        ApiRequest<T> request = new ApiRequest<T>(resultType, method, url, requestData, new Response.Listener<T>() {
            @Override
            public void onResponse(T responseBody) {
                if (responseSuccess != null) {
                    responseSuccess.onSuccessResponse(responseBody);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (responseError != null) {
                    responseError.onErrorResponse(error);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (requestOptions != null) {
                    return requestOptions.getHeaders();
                }
                return super.getHeaders();
            }
        };

        this.requestQueue.add(request);
    }
}
