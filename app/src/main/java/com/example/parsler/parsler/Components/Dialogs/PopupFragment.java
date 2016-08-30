package com.example.parsler.parsler.Components.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.example.parsler.parsler.Commons.StringConstants;
import com.example.parsler.parsler.MainActivity;
import com.example.parsler.parsler.R;
import com.example.parsler.parsler.SignatureActivity;
import com.example.parsler.parsler.Utility.OMSController;

public class PopupFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final Bundle arguments = getArguments();

        if(arguments.getString(StringConstants.KEY_UPDATE_STATUS).equalsIgnoreCase(
                StringConstants.KEY_IS_CANCELLED)) {

            builder.setView(inflater.inflate(R.layout.confirmation_cancellation_popup, null));
        } else{

            builder.setView(inflater.inflate(R.layout.confirmation_popup, null));
        }

        builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (!(arguments.getString(StringConstants.KEY_UPDATE_STATUS).equalsIgnoreCase(
                        StringConstants.KEY_IS_CANCELLED))) {

                    Intent intent =
                            new Intent(getActivity().getApplicationContext(), SignatureActivity.class);
                    intent.putExtras(getArguments());
                    startActivity(intent);

                } else {

                    OMSController.updateOrderStatus(arguments);

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
            }
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        // Create the AlertDialog object and return it
        AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }
}
