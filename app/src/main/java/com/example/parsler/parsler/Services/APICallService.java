package com.example.parsler.parsler.Services;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.crashlytics.android.Crashlytics;
import com.example.parsler.parsler.Commons.StringConstants;
import com.example.parsler.parsler.Utility.APIInterface;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class APICallService {

    /**
     * Volley Service to interact with REST APIs
     * @param context
     * @param method
     * @param url
     * @param data
     * @param callback
     */
    public static void APICall(final Context context,
                               final int method, final String url,
                               final HashMap<String, String> data,
                               final APIInterface callback) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, url,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    callback.onSuccess(response);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Crashlytics.logException(error);
                    callback.onError(error);
                }
            })  {

            // set body
            protected Map<String, String> getParams() {
                Map<String, String> params = data;
                return params;
            }

            // set headers
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put(StringConstants.KEY_CONTENT_TYPE, StringConstants.KEY_URLENCODED);
                return params;
            }
        };

        // Push request in queue
        APIInstanceRequestService.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
