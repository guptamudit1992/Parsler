package com.example.parsler.parsler.Utility;

import android.content.Context;
import android.content.Intent;

import com.example.parsler.parsler.Services.GeoLocationService;
import com.example.parsler.parsler.Services.OrderTrackingService;

public class BackgroundServiceController {

    /**
     * Function to start Background Services - Geolocation Service and Order Tracking Service
     * @param context
     */
    public static void startBackgroundServices(Context context) {

        // Initiate the GeoLocation Service
        Intent geoLocationIntent = new Intent(context, GeoLocationService.class);
        geoLocationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(geoLocationIntent);

        // Initiate Order Tracking Service
        Intent orderTrackingServiceIntent = new Intent(context, OrderTrackingService.class);
        orderTrackingServiceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(orderTrackingServiceIntent);
    }


    /**
     * Function to stop Background Services - Geolocation Service and Order Tracking Service
     * @param context
     */
    public static void stopBackgroundServices(Context context) {

        // Terminate the GeoLocation Service
        Intent geoLocationIntent = new Intent(context, GeoLocationService.class);
        geoLocationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.stopService(geoLocationIntent);

        // terminate Order Tracking Service
        Intent orderTrackingServiceIntent = new Intent(context, OrderTrackingService.class);
        orderTrackingServiceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.stopService(orderTrackingServiceIntent);
    }
}
