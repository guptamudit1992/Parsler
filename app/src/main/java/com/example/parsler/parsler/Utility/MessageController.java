package com.example.parsler.parsler.Utility;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.parsler.parsler.Adapters.Adapter;
import com.example.parsler.parsler.Commons.StaticConstants;
import com.example.parsler.parsler.Commons.StringConstants;
import com.example.parsler.parsler.Models.PickupSummaryObject;
import com.example.parsler.parsler.PickupSummaryActivity;
import com.example.parsler.parsler.R;
import com.example.parsler.parsler.databinding.MessageCardBinding;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

import java.util.ArrayList;

public class MessageController {

    private static Context context;
    private static Firebase firebase;
    private static ArrayList<PickupSummaryObject> messageObjects = new ArrayList<>();
    private static LinearLayout linearLayout;
    private static RecyclerView recyclerView;
    private static TextView textViewNoMessage;
    private static ProgressBar progressBar;


    /**
     * Function to fetch message from OMS Background Service
     * @param _context
     * @param _firebase
     * @param dataSnapshot
     */
    public static void updateMessageList(Context _context,
                                         Firebase _firebase,
                                         DataSnapshot dataSnapshot) {
        context = _context;
        firebase = _firebase;
        messageObjects = new ArrayList<>();

        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

            PickupSummaryObject pickupSummaryObject = snapshot.getValue(PickupSummaryObject.class);
            pickupSummaryObject.setPickupKey(dataSnapshot.getKey());
            messageObjects.add(pickupSummaryObject);

            // Check if notification flag has been already set, which states it is read
            if (snapshot.hasChild(StringConstants.KEY_IS_READ) &&
                    !((Boolean) snapshot.child(StringConstants.KEY_IS_READ).getValue())) {

                sendNotification(snapshot);
            }

            // Bind message list to recycler view
            bindMessageList(context);
        }
    }


    /**
     * Function to send notification to FE
     * @param snapshot
     */
    private static void sendNotification(DataSnapshot snapshot) {

        if(snapshot.hasChild(StringConstants.KEY_PICKUP_ID)) {

            // Create an intent that will be fired when the user clicks the notification.
            Intent intent = new Intent(context, PickupSummaryActivity.class);
            intent.putExtra(StringConstants.KEY_PICKUP_ID,
                    snapshot.child(StringConstants.KEY_PICKUP_ID).getValue().toString());
            intent.putExtra(StringConstants.KEY_IS_ACTIVE,
                    Boolean.valueOf(snapshot.child(StringConstants.KEY_IS_ACTIVE).getValue().toString()));

            // Building TaskStackTrace to navigate once inside app with notification
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(PickupSummaryActivity.class);
            stackBuilder.addNextIntent(intent);
            PendingIntent pendingIntent = stackBuilder
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            // Set Notification Priority
            builder.setPriority(StaticConstants.NOTIFICATION_PRIORITY);
            builder.setVisibility(StaticConstants.VISIBILITY_PUBLIC);
            builder.setSmallIcon(R.drawable.ic_local_post_office_white_36dp);
            // Notification Sound
            builder.setSound(RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_ALARM));

            // Set the intent that will fire when the user taps the notification.
            builder.setContentIntent(pendingIntent);

            // Set the notification to auto-cancel.
            // This means that the notification will disappear after the user taps it,
            // rather than remaining until it's explicitly dismissed.
            builder.setAutoCancel(true);

            builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ic_local_post_office_white_36dp));
            builder.setContentTitle(StringConstants.NOTIFICATION_TITLE);
            builder.setContentText(StringConstants.NOTIFICATION_MESSAGE_PREFIX
                    .concat(snapshot.child(StringConstants.KEY_PICKUP_ID).getValue().toString())
                    .concat(StringConstants.NOTIFICATION_MESSAGE_SUFFIX));
            builder.setSubText(StringConstants.NOTIFICATION_SUBTEXT);

            // Send the notification
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            notificationManager.notify(StaticConstants.NOTIFICATION_ID, builder.build());

            firebase.child(snapshot.getKey()).child(StringConstants.KEY_IS_READ).setValue(true);
        }
    }


    /**
     * Function to return list of messages
     * @return
     */
    public static ArrayList<PickupSummaryObject> fetchMessagesList() {
        return messageObjects;
    }


    /**
     * Function to initialize message list
     * @param _recyclerView
     * @param _textView
     */
    public static void initializeMessageList(RecyclerView _recyclerView,
                                             TextView _textView,
                                             ProgressBar _progressBar,
                                             LinearLayout _linearLayout) {
        recyclerView = _recyclerView;
        textViewNoMessage = _textView;
        progressBar = _progressBar;
        linearLayout = _linearLayout;
    }

    /**
     * Function to bind message list to recycler view
     * @param context
     */
    public static void bindMessageList(Context context) {

        // Fetch and update data via adapter and object holder
        ArrayList<PickupSummaryObject> messageObjects = fetchMessagesList();
        Adapter<PickupSummaryObject, MessageCardBinding> messageCardBindingAdapter =
                new Adapter<>(messageObjects, R.layout.message_card, StringConstants.MESSAGES_ADAPTER);

        // Set adapter
        if(recyclerView != null) {
            showContent();
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(messageCardBindingAdapter);

            // Update view
            if(messageObjects.size() > 0) {

                textViewNoMessage.setVisibility(View.GONE);

            } else {

                textViewNoMessage.setVisibility(View.VISIBLE);
            }
        }
    }


    /**
     * Function to hide loader and show content
     */
    public static void showContent() {

        if(progressBar != null) {
            progressBar.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }
    }
}
