package com.example.parsler.parsler.Services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.example.parsler.parsler.Commons.BuildProperties;
import com.example.parsler.parsler.Commons.StaticConstants;
import com.example.parsler.parsler.Commons.StringConstants;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.HashMap;


public class GeoLocationService extends IntentService implements ConnectionCallbacks,
        OnConnectionFailedListener, LocationListener  {

    private GoogleApiClient googleApiClient;
    private Location lastLocation;
    private LocationRequest locationRequest;

    // GeoFire plugin initialization
    private Firebase firebase;
    private GeoFire geoFire;
    private String firebaseEntry;

    // Historical datapoints
    private HashMap<String, Object> datapoints = new HashMap<>();



    // Default constructor
    public GeoLocationService() {
        super("GeoLocationService");
    }


    @Override
    protected void onHandleIntent(Intent workIntent) {
        buildGoogleApiClient();
    }


    /**
     * Function to build Google API client
     */
    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }


    /**
     * Function to create a latitude longitude request
     */
    protected void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(StaticConstants.UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(StaticConstants.DISPLACEMENT);
    }


    /**
     * Function to store location in Firebase
     */
    protected void fetchLocation() {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);



        if (lastLocation != null) {
            final double latitude = lastLocation.getLatitude();
            final double longitude = lastLocation.getLongitude();
            final Boolean isOnDuty = true;

            // Update the FireBase entry
            geoFire.setLocation(firebaseEntry, new GeoLocation(latitude, longitude), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, FirebaseError error) {
                    if (error != null) {

                        Toast.makeText(getApplicationContext(),
                                StringConstants.CHECK_NETWORK_CONNECTION, Toast.LENGTH_LONG).show();

                    } else {

                        // Additional Parameters - Status and historical datapoints
                        String timestamp = String.valueOf(System.currentTimeMillis());
                        datapoints.put(timestamp, new GeoLocation(latitude, longitude));
                        firebase.child(firebaseEntry).child(
                                StringConstants.KEY_IS_ONDUTY).setValue(isOnDuty);
                        firebase.child(firebaseEntry)
                                .child(StringConstants.KEY_HISTORICAL_DATAPOINTS)
                                .setValue(datapoints);
                    }
                }
            });
        }
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        // Setup and initialize FireBase and GeoFire instances
        Firebase.setAndroidContext(this);
        firebase = new Firebase(BuildProperties.FIREBASE_GEOLOCATION_URL);
        geoFire = new GeoFire(firebase);

        // Creating unique key for each FE Agent in FireBase DB
        firebaseEntry = StringConstants.KEY_EMPLOYEE
                .concat(SharedPreferenceService.getValue(getApplicationContext(), StringConstants.KEY_IMEI));

        // Start Geo Location updates
        startLocationUpdates();
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Toast.makeText(getApplicationContext(),
                StringConstants.CHECK_NETWORK_CONNECTION, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onConnectionSuspended(int arg0) {
        googleApiClient.connect();
    }


    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        fetchLocation();
    }


    /**
     * Function to start location updates
     */
    protected void startLocationUpdates() {
        createLocationRequest();
        LocationServices.FusedLocationApi
                .requestLocationUpdates(googleApiClient, locationRequest, this);
    }


    /**
     * Function to stop location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                googleApiClient, this);
    }
}

