package com.example.parsler.parsler.Services;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Singleton Service to implement Volley API request queue
 */
public class APIInstanceRequestService {

    private static APIInstanceRequestService apiInstanceRequestServiceInstance;
    private RequestQueue queue;

    // Constructor
    private APIInstanceRequestService(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public static synchronized APIInstanceRequestService getInstance(Context context) {
        if(apiInstanceRequestServiceInstance == null) {
            apiInstanceRequestServiceInstance = new APIInstanceRequestService(context);
        }

        return apiInstanceRequestServiceInstance;
    }


    public RequestQueue getRequestQueue() {
        return queue;
    }


    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }
}
