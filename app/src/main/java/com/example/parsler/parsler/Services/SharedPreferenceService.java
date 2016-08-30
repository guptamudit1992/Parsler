package com.example.parsler.parsler.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.example.parsler.parsler.Commons.StaticConstants;
import com.example.parsler.parsler.Commons.StringConstants;
import com.example.parsler.parsler.Utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;


public class SharedPreferenceService {

    /**
     * Function to initialize user session in shared preference memory
     * @param context
     * @param data
     */
    public static void storeUserDetails(Context context, JSONObject data) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(StaticConstants.USER_DETAILS, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();

        try {
            editor.putString(StringConstants.KEY_USERHASH,
                    data.get(StringConstants.KEY_USERHASH).toString());
            editor.putString(StringConstants.KEY_USERNAME,
                    data.get(StringConstants.KEY_USERNAME).toString());
            editor.putString(StringConstants.KEY_RUNNER_MOBILE_NUMBER,
                    data.get(StringConstants.KEY_RUNNER_MOBILE_NUMBER).toString());
            editor.putString(StringConstants.KEY_RATING,
                    data.get(StringConstants.KEY_RATING).toString());
            editor.putString(StringConstants.KEY_IMEI,
                    Utils.getIMEINumber(context));
            editor.putString(StringConstants.KEY_MANAGER_MOBILE_NUMBER,
                    data.get(StringConstants.KEY_MANAGER_MOBILE_NUMBER).toString());
            editor.putString(StringConstants.KEY_IS_ONDUTY, StringConstants.VALUE_FALSE);

        } catch (JSONException error) {
            Log.e(StringConstants.JSON_EXCEPTION_ERROR, error.toString());

        } catch (Exception error) {
            Log.e(StringConstants.EXCEPTION_ERROR, error.toString());
        }

        // Commit to Shared Preference Cache
        editor.commit();
    }


    /**
     * Function to update on duty status of runner
     * @param context
     * @param status
     */
    public static void updateOnDutyStatus(Context context, String status) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(StaticConstants.USER_DETAILS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(StringConstants.KEY_IS_ONDUTY, status);
        editor.commit();
    }


    /**
     * Function to fetch value corresponding to key in Shared Preference
     * @param context
     * @param key
     * @return
     */
    public static String getValue(Context context, String key) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(StaticConstants.USER_DETAILS, Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(key, null);
        return value;
    }


    /**
     * Function to clear user session from shared preference memory
     * @param context
     */
    public static void destroyUserSession(Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences(StaticConstants.USER_DETAILS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}
