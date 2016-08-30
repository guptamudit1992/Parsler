package com.example.parsler.parsler.Utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ValidateService {

    /**
     * Function to check if device has internet connection
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null;
    }


    /**
     * Function to check if a string is null
     * @param string
     * @return
     */
    public static boolean isNull(String string) {
        return (string == null || string.isEmpty());
    }
}


