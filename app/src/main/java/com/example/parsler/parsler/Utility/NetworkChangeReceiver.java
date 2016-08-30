package com.example.parsler.parsler.Utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

/**
 * Network Change Broadcast Receiver
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    TextView internetConnect;

    public NetworkChangeReceiver(TextView _internetConnect) {
        internetConnect = _internetConnect;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (ValidateService.isConnected(context)) {
            internetConnect.setVisibility(View.INVISIBLE);
        } else {
            internetConnect.setVisibility(View.VISIBLE);
        }
    }
}