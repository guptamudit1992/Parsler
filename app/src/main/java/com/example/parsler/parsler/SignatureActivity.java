package com.example.parsler.parsler;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.example.parsler.parsler.Commons.StaticConstants;
import com.example.parsler.parsler.Commons.StringConstants;
import com.example.parsler.parsler.Services.AWSServices;
import com.example.parsler.parsler.Utility.ActionBarUtils;
import com.example.parsler.parsler.Utility.NetworkChangeReceiver;

import java.io.File;
import java.io.FileOutputStream;

public class SignatureActivity extends AppCompatActivity {

    static TextView internetConnect;
    private BroadcastReceiver receiver;
    private static Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        // Fetch bundle arguments
        bundle = getIntent().getExtras();

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
        getMenuInflater().inflate(R.menu.menu_signature, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
     * Function to capture digital signature
     * @param view
     */
    public void saveSignature(View view) {

        try {
            GestureOverlayView gestureView = (GestureOverlayView) findViewById(R.id.signaturePad);
            gestureView.setDrawingCacheEnabled(true);
            Bitmap bm = Bitmap.createBitmap(gestureView.getDrawingCache());
            File file = new File(
                    Environment.getExternalStorageDirectory() + File.separator + StaticConstants.SUFFIX_FILENAME);
            file.createNewFile();
            FileOutputStream os = new FileOutputStream(file);

            bm.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.close();

            // Upload digital signature to S3
            AWSServices.uploadS3Bucket(getApplicationContext(), file, bundle);

        } catch (Exception e) {

            Log.e(StringConstants.EXCEPTION_ERROR, e.toString());
            Crashlytics.logException(e);
        }
    }

}
