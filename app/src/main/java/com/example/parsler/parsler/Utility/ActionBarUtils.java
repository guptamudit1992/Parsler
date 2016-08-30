package com.example.parsler.parsler.Utility;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.example.parsler.parsler.Commons.StringConstants;
import com.example.parsler.parsler.LoginActivity;
import com.example.parsler.parsler.Services.SharedPreferenceService;

public class ActionBarUtils {

    /**
     * Function to contact area manager
     * @return
     */
    public static boolean callManager(Context context) {

        Intent intent = new Intent(Intent.ACTION_CALL,
                Uri.parse("tel:".concat(SharedPreferenceService.getValue(context,
                        StringConstants.KEY_MANAGER_MOBILE_NUMBER))));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(context,
                    StringConstants.PHONECALL_CANNOT_BE_MADE, Toast.LENGTH_SHORT).show();
        }

        return true;
    }


    /**
     * Function to logout, stop background services and destroy session cache
     * @return
     */
    public static boolean logout(Context context) {

        // Stop background services
        BackgroundServiceController.stopBackgroundServices(context);

        // Destroy user session
        SharedPreferenceService.destroyUserSession(context);

        // Navigate to Login Page
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        return true;
    }
}
