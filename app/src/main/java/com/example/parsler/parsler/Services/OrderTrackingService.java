package com.example.parsler.parsler.Services;

import android.app.IntentService;
import android.content.Intent;

import com.example.parsler.parsler.Commons.BuildProperties;
import com.example.parsler.parsler.Commons.StringConstants;
import com.example.parsler.parsler.Utility.MessageController;
import com.example.parsler.parsler.Utility.OMSController;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class OrderTrackingService extends IntentService {

    private static Firebase firebase;
    private static String firebaseUrl;


    // Default Constructor
    public OrderTrackingService() {
        super("OrderTrackingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // Initialize and configure Firebase
        Firebase.setAndroidContext(this);
        firebaseUrl = BuildProperties.FIREBASE_ORDER_MANAGEMENT_SERVICE_URL
                .concat(StringConstants.KEY_EMPLOYEE)
                .concat(SharedPreferenceService.getValue(getApplicationContext(), StringConstants.KEY_IMEI));
        firebase = new Firebase(firebaseUrl);


        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                OMSController.updateOrderList(getApplicationContext(), firebase, dataSnapshot);
                MessageController.updateMessageList(getApplicationContext(), firebase, dataSnapshot);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
