package com.example.parsler.parsler;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.example.parsler.parsler.Commons.BuildProperties;
import com.example.parsler.parsler.Commons.StaticConstants;
import com.example.parsler.parsler.Commons.StringConstants;
import com.example.parsler.parsler.Components.Fonts.FontsOverride;
import com.example.parsler.parsler.Services.APICallService;
import com.example.parsler.parsler.Utility.APIInterface;
import com.example.parsler.parsler.Utility.BackgroundServiceController;
import com.example.parsler.parsler.Utility.NetworkChangeReceiver;
import com.example.parsler.parsler.Services.SharedPreferenceService;
import com.example.parsler.parsler.Utility.Utils;
import com.example.parsler.parsler.Utility.ValidateService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.fabric.sdk.android.Fabric;

public class LoginActivity extends AppCompatActivity {

    static TextView internetConnect;
    private BroadcastReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initiate Fabric Crashlytics
        Fabric.with(this, new Crashlytics());

        // Set Default Font
        FontsOverride.setDefaultFont(this,
                StaticConstants.TYPEFACE_FONT_MONOSPACE,
                StaticConstants.TYPEFACE_FONT_COPPERPLATE_REGULAR);

        setContentView(R.layout.activity_login);

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
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        // Do nothing to avoid back button login
    }


    /**
     * Function to validate and authenticate user
     * @param view
     */
    public void signinRequest(View view) {

        final String username = ((EditText) findViewById(R.id.username)).getText().toString();
        final String password = ((EditText) findViewById(R.id.password)).getText().toString();
        final String url = BuildProperties.BASE_URL.concat(StringConstants.API_SIGNIN);

        // Check internet connectivity
        if (ValidateService.isConnected(getApplicationContext())) {

            if (ValidateService.isNull(username)) {
                Toast.makeText(getApplicationContext(),
                        StringConstants.USERNAME_NOT_ENTERED, Toast.LENGTH_SHORT).show();

            } else if (ValidateService.isNull(password)) {
                Toast.makeText(getApplicationContext(),
                        StringConstants.PASSWORD_NOT_ENTERED, Toast.LENGTH_SHORT).show();

            } else {

                // Progress Dialog Popup
                final ProgressDialog popup = new ProgressDialog(this);
                popup.setMessage(StringConstants.SIGNIN_LOADER_MESSAGE);
                popup.show();


                // Constructing request body
                final HashMap<String, String> data = new HashMap<>();
                data.put(StringConstants.KEY_USERNAME, username);
                data.put(StringConstants.KEY_PASSWORD, password);
                data.put(StringConstants.KEY_IMEI, Utils.getIMEINumber(getApplicationContext()));


                // API Call
                APICallService.APICall(this, Request.Method.POST, url, data, new APIInterface() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {

                            // Dismiss loader
                            popup.dismiss();

                            if (response.has(StringConstants.RESPONSE_STATUS) &&
                                    response.get(StringConstants.RESPONSE_STATUS).toString()
                                            .equalsIgnoreCase(StringConstants.RESPONSE_SUCCESS)) {

                                // Store user hash in private Shared Preference Memory
                                SharedPreferenceService.storeUserDetails(getApplicationContext(),
                                        (JSONObject) response.get(StringConstants.KEY_DATA));

                                Toast.makeText(getApplicationContext(),
                                        StringConstants.WELCOME_TO_PARSLER, Toast.LENGTH_SHORT).show();


                                // Start background services
                                BackgroundServiceController.startBackgroundServices(getApplicationContext());

                                // Navigate to Main Activity on success
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {

                            Toast.makeText(getApplicationContext(),
                                    StringConstants.PLEASE_TRY_AGAIN_IN_SOMETIME, Toast.LENGTH_LONG).show();

                            // Log error
                            Crashlytics.logException(e);
                            Log.e(StringConstants.JSON_EXCEPTION_ERROR, e.toString());

                        } catch (Exception e) {

                            Toast.makeText(getApplicationContext(),
                                    StringConstants.PLEASE_TRY_AGAIN_IN_SOMETIME, Toast.LENGTH_LONG).show();

                            // Log error
                            Crashlytics.logException(e);
                            Log.e(StringConstants.EXCEPTION_ERROR, e.toString());
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {

                        // Dismiss loader
                        popup.dismiss();

                        Toast.makeText(getApplicationContext(),
                                StringConstants.USERNAME_PASSWORD_INVALID, Toast.LENGTH_LONG).show();
                        // Log error
                        Log.e(StringConstants.RESPONSE_ERROR, error.toString());
                    }
                });
            }
        } else {

            // In case of no internet connectivity
            Toast.makeText(getApplicationContext(),
                    StringConstants.CHECK_NETWORK_CONNECTION, Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onPause() {
        if (receiver != null) {
            this.unregisterReceiver(receiver);
        }
        super.onPause();
    }

}



