package com.example.parsler.parsler;

import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parsler.parsler.Commons.StaticConstants;
import com.example.parsler.parsler.Commons.StringConstants;
import com.example.parsler.parsler.Components.Dialogs.PopupFragment;
import com.example.parsler.parsler.Database.OMSDBHelper;
import com.example.parsler.parsler.Models.PickupSummaryObject;
import com.example.parsler.parsler.Services.OMSBackupDatabaseService;
import com.example.parsler.parsler.Utility.ActionBarUtils;
import com.example.parsler.parsler.Utility.NetworkChangeReceiver;
import com.example.parsler.parsler.Utility.OMSController;
import com.example.parsler.parsler.databinding.ActivityPickupSummaryBinding;

public class PickupSummaryActivity extends AppCompatActivity {

    static TextView internetConnect;
    private BroadcastReceiver receiver;
    private static PickupSummaryObject pickupSummaryObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Fetch data for pickup id
        Intent intent = getIntent();
        String pickupId = intent.getStringExtra(StringConstants.KEY_PICKUP_ID);
        pickupSummaryObject = OMSController.getOrderDetails(pickupId);

        if(pickupSummaryObject != null) {

            // Bind pickup summary object to view
            ActivityPickupSummaryBinding binding =
                    DataBindingUtil.setContentView(this, R.layout.activity_pickup_summary);
            binding.setPickupSummaryObject(pickupSummaryObject);

            // Backup Data in SQLite
            OMSDBHelper dbHelper = new OMSDBHelper(getApplicationContext());
            OMSBackupDatabaseService.insertOrder(dbHelper, pickupSummaryObject);

        } else {

            setContentView(R.layout.activity_exception);
        }


        // Initialize broadcast receiver
        internetConnect = (TextView) findViewById(R.id.internet_connection);
        receiver =  new NetworkChangeReceiver(internetConnect);
    }


    @Override
    public void onResume() {
        super.onResume();
        if(receiver != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(receiver, filter);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order_summary, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        switch (item.getItemId()) {
            case R.id.action_call_manager:
                ActionBarUtils.callManager(getApplicationContext());
                return true;

            case R.id.action_settings:
                ActionBarUtils.logout(getApplicationContext());
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onPause() {
        if (receiver != null) {
            this.unregisterReceiver(receiver);
        }
        super.onPause();
    }


    /**
     * Function to submit when pickup completed
     * @param view
     */
    public void pickupComplete(View view) {

        stepComplete(StringConstants.KEY_PICKUP,
                String.valueOf(pickupSummaryObject.getPickupId()),
                Boolean.valueOf(pickupSummaryObject.getIsActive()));
    }


    /**
     * Function to submit when drop completed
     * @param view
     */
    public void dropComplete(View view) {

        stepComplete(StringConstants.KEY_DROP,
                pickupSummaryObject.getPickupId(),
                Boolean.valueOf(pickupSummaryObject.getIsActive()));
    }


    /**
     * Function to submit when payment completed
     * @param view
     */
    public void paymentComplete(View view) {

        stepComplete(StringConstants.KEY_PAYMENT,
                pickupSummaryObject.getPickupId(),
                Boolean.valueOf(pickupSummaryObject.getIsActive()));
    }


    /**
     * Function to cancel order
     * @param view
     */
    public void cancelOrder(View view) {

        stepComplete(StringConstants.KEY_IS_CANCELLED,
                pickupSummaryObject.getPickupId(),
                Boolean.valueOf(pickupSummaryObject.getIsActive()));
    }


    /**
     * Function to update completion status
     * @param status
     * @param pickupId
     * @param isActive
     */
    public void stepComplete(String status, String pickupId, Boolean isActive) {

        // Create bundle to set arguments
        Bundle bundle = new Bundle();
        bundle.putString(StringConstants.KEY_UPDATE_STATUS, status);
        bundle.putString(StringConstants.KEY_PICKUP_ID, pickupId);
        bundle.putBoolean(StringConstants.KEY_IS_ACTIVE, isActive);

        showPopupDialog(bundle);
    }


    /**
     * Function to show dialog popup
     * @param bundle
     */
    public void showPopupDialog(Bundle bundle) {
        DialogFragment popup = new PopupFragment();
        popup.setArguments(bundle);
        popup.show(getFragmentManager(), StringConstants.KEY_STEP_COMPLETE);
    }


    /**
     * Function to contact the merchant
     * @param view
     */
    public void callCustomer(View view) {

        Intent in=new Intent(Intent.ACTION_CALL,
                Uri.parse(StringConstants.KEY_TEL.concat(pickupSummaryObject.getCustomerContact())));
        try {
            startActivity(in);
        } catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(getApplicationContext(), StringConstants.PHONECALL_CANNOT_BE_MADE, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Function to fetch pickup point latitude and longitude
     * @param view
     */
    public void fetchPickupPoint(View view) {

        double latitudeDestination = Double.valueOf(pickupSummaryObject.getPickupLat());
        double longitudeDestination = Double.valueOf(pickupSummaryObject.getPickupLong());

        navigateMap(latitudeDestination, longitudeDestination);
    }


    /**
     * Function to fetch drop point latitude and longitude
     * @param view
     */
    public void fetchDropPoint(View view) {

        double latitudeDestination = Double.valueOf(pickupSummaryObject.getDropLat());
        double longitudeDestination = Double.valueOf(pickupSummaryObject.getDropLong());

        navigateMap(latitudeDestination, longitudeDestination);
    }


    /**
     * Function to deep link navigation tab with Google Maps
     *
     * @param latitudeDestination
     * @param longitudeDestination
     */
    public void navigateMap(double latitudeDestination, double longitudeDestination) {
        String mode = StaticConstants.MODE;

        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse(String.format("google.navigation:ll=%s,%s%s",
                        latitudeDestination, longitudeDestination, mode)));

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    /**
     * Function to navigate to homepage
     * @param view
     */
    public void goToHomepage(View view) {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

