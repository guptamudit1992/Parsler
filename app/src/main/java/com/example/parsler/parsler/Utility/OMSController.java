package com.example.parsler.parsler.Utility;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.parsler.parsler.Adapters.Adapter;
import com.example.parsler.parsler.Commons.BuildProperties;
import com.example.parsler.parsler.Commons.StringConstants;
import com.example.parsler.parsler.Database.OMSDBHelper;
import com.example.parsler.parsler.Models.PickupSummaryObject;
import com.example.parsler.parsler.R;
import com.example.parsler.parsler.Services.OMSBackupDatabaseService;
import com.example.parsler.parsler.databinding.PickupCardBinding;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

import java.util.ArrayList;

public class OMSController {

    private static Context context;
    private static Firebase firebase;
    private static DataSnapshot dataSnapshot;
    private static ArrayList<PickupSummaryObject> activePickupSummaryObjects = new ArrayList<>();
    private static ArrayList<PickupSummaryObject> historyPickupSummaryObjects = new ArrayList<>();
    private static LinearLayout activeOrderLinearLayout;
    private static RecyclerView activeOrderRecyclerView;
    private static ScrollView activeScrollViewOrderContainer;
    private static TextView activeOrderCount;
    private static TextView noActiveOrderCount;
    private static ProgressBar activeOrderProgressBar;
    private static LinearLayout historyOrderLinearLayout;
    private static RecyclerView historyOrderRecyclerView;
    private static ScrollView historyScrollViewOrderContainer;
    private static TextView historyOrderCount;
    private static TextView noHistoryOrderCount;
    private static ProgressBar historyOrderProgressBar;


    /**
     * Function to fetch orders from OMS Background Service
     * @param _context
     * @param _firebase
     * @param _dataSnapshot
     */
    public static void updateOrderList(Context _context,
                                       Firebase _firebase,
                                       DataSnapshot _dataSnapshot) {

        context = _context;
        firebase = _firebase;
        dataSnapshot = _dataSnapshot;
        activePickupSummaryObjects = new ArrayList<>();
        historyPickupSummaryObjects = new ArrayList<>();

        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            categorizeOrder(snapshot);
        }
    }


    /**
     * Function to categorize order into active or completed
     * @param dataSnapshot
     */
    public static void categorizeOrder(DataSnapshot dataSnapshot) {

        PickupSummaryObject pickupSummaryObject = dataSnapshot.getValue(PickupSummaryObject.class);
        pickupSummaryObject.setPickupKey(dataSnapshot.getKey());

        // Sort orders into active and completed
        if (dataSnapshot.hasChild(StringConstants.KEY_IS_ACTIVE) &&
                ((Boolean) dataSnapshot.child(StringConstants.KEY_IS_ACTIVE).getValue())) {

            activePickupSummaryObjects.add(pickupSummaryObject);

        } else {

            historyPickupSummaryObjects.add(pickupSummaryObject);
        }

        // Binding updated order list
        bindOrderList(context, StringConstants.KEY_ACTIVE_ORDERS);
        bindOrderList(context, StringConstants.KEY_HISTORY_ORDERS);
    }


    /**
     * Function to return list of orders
     * @return
     */
    public static ArrayList<PickupSummaryObject> fetchOrderList(String key) {

        if(key.equalsIgnoreCase(StringConstants.KEY_ACTIVE_ORDERS)) {

            return activePickupSummaryObjects;
        } else {

            return historyPickupSummaryObjects;
        }
    }


    /**
     * Function to initialize list of orders
     * @param _recyclerView
     */
    public static void initializeOrderList(RecyclerView _recyclerView,
                                           ScrollView scrollViewOrderContainer,
                                           TextView textViewOrderCount,
                                           TextView textViewNoOrder,
                                           String key, ProgressBar progressBar,
                                           LinearLayout linearLayout) {


        if(key.equalsIgnoreCase(StringConstants.KEY_ACTIVE_ORDERS)) {
            activeOrderRecyclerView = _recyclerView;
            activeScrollViewOrderContainer = scrollViewOrderContainer;
            activeOrderCount = textViewOrderCount;
            noActiveOrderCount = textViewNoOrder;
            activeOrderProgressBar = progressBar;
            activeOrderLinearLayout = linearLayout;

        } else {
            historyOrderRecyclerView = _recyclerView;
            historyScrollViewOrderContainer = scrollViewOrderContainer;
            historyOrderCount = textViewOrderCount;
            noHistoryOrderCount = textViewNoOrder;
            historyOrderProgressBar = progressBar;
            historyOrderLinearLayout = linearLayout;
        }
    }


    /**
     * Function to bind order list to recyclerview
     * @param context
     * @param key
     */
    public static void bindOrderList(Context context, String key) {

        ArrayList<PickupSummaryObject> pickupSummaryObjects;

        // Fetch and update data via adapter and object holder
        if(key.equalsIgnoreCase(StringConstants.KEY_ACTIVE_ORDERS)) {

            pickupSummaryObjects = fetchOrderList(StringConstants.KEY_ACTIVE_ORDERS);
            updateView(context, activeOrderRecyclerView, pickupSummaryObjects,
                    activeScrollViewOrderContainer, activeOrderCount, noActiveOrderCount,
                    StringConstants.ACTIVE_ORDERS_COUNT);

        } else {

            pickupSummaryObjects = fetchOrderList(StringConstants.KEY_HISTORY_ORDERS);
            updateView(context, historyOrderRecyclerView, pickupSummaryObjects,
                    historyScrollViewOrderContainer, historyOrderCount, noHistoryOrderCount,
                    StringConstants.HISTORY_ORDERS_COUNT);
        }
    }


    /**
     * Function to update display
     * @param context
     * @param recyclerView
     * @param pickupSummaryObjects
     * @param scrollView
     * @param textViewCount
     * @param textViewError
     */
    public static void updateView(Context context, RecyclerView recyclerView,
                                  ArrayList<PickupSummaryObject> pickupSummaryObjects,
                                  ScrollView scrollView, TextView textViewCount, TextView textViewError,
                                  String countText) {

        Adapter<PickupSummaryObject, PickupCardBinding> pickupCardBindingAdapter;

        // Bind adapter views
        if(recyclerView != null) {

            showContent();

            pickupCardBindingAdapter =
                    new Adapter<>(pickupSummaryObjects, R.layout.pickup_card, StringConstants.PICKUP_SUMMARY_ADAPTER);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(pickupCardBindingAdapter);

            textViewCount.setText(pickupSummaryObjects.size() + countText);

            // Hide / show elements
            if(pickupSummaryObjects.size() > 0) {

                scrollView.setVisibility(View.VISIBLE);
                textViewError.setVisibility(View.GONE);

            } else {

                scrollView.setVisibility(View.GONE);
                textViewError.setVisibility(View.VISIBLE);
            }
        }
    }


    /**
     * Function to hide loader and show content
     */
    public static void showContent() {

        if(activeOrderProgressBar != null) {
            activeOrderProgressBar.setVisibility(View.GONE);
            activeOrderLinearLayout.setVisibility(View.VISIBLE);
        }

        if(historyOrderProgressBar != null) {
            historyOrderProgressBar.setVisibility(View.GONE);
            historyOrderLinearLayout.setVisibility(View.VISIBLE);
        }
    }


    /**
     * Function to fetch pickup summary object based on pickup id
     * @param pickupId
     * @return
     */
    public static PickupSummaryObject getOrderDetails(String pickupId) {
        PickupSummaryObject pickupSummaryObject = null;

        // Check for pickup id in active orders list
        for (int i = 0; i < activePickupSummaryObjects.size(); i++) {
            if (activePickupSummaryObjects.get(i).getPickupId().equalsIgnoreCase(pickupId)) {
                pickupSummaryObject = activePickupSummaryObjects.get(i);
                break;
            }
        }

        // Check for pickup id in order history list
        if(pickupSummaryObject == null) {
            for (int i = 0; i < historyPickupSummaryObjects.size(); i++) {
                if (historyPickupSummaryObjects.get(i).getPickupId().equals(pickupId)) {
                    pickupSummaryObject = historyPickupSummaryObjects.get(i);
                    break;
                }
            }
        }

        return pickupSummaryObject;
    }


    /**
     * Function to update status of order in Firebase
     * @param bundle
     */
    public static void updateOrderStatus(Bundle bundle) {

        PickupSummaryObject pickupSummaryObject =
                getOrderDetails(bundle.getString(StringConstants.KEY_PICKUP_ID));

        // Backup Data in SQLite
        OMSDBHelper dbHelper = new OMSDBHelper(context);

        // Check if order is being cancelled
        if(bundle.getString(StringConstants.KEY_UPDATE_STATUS)
                .equalsIgnoreCase(StringConstants.KEY_IS_CANCELLED)) {

            // If order is cancelled

            // Update isCancelled flag in SQLite
            OMSBackupDatabaseService.updateOrder(dbHelper, Integer.valueOf(pickupSummaryObject.getPickupId()),
                    bundle.getString(StringConstants.KEY_UPDATE_STATUS), true);

            // Update isCancelled flag status in Firebase
            firebase.child(pickupSummaryObject.getPickupKey())
                    .child(bundle.getString(StringConstants.KEY_UPDATE_STATUS)).setValue(true);


            updateOrderStatusFinish(dbHelper, pickupSummaryObject);

        } else {

            //OMSBackupDatabaseService.updateOrder(dbHelper, pickupSummaryObject.getPickupId(), bundle.getString(StringConstants.KEY_UPDATE_STATUS).concat(StringConstants.KEY_IS_COMPLETED), true);

            // Update status in Firebase
            firebase.child(pickupSummaryObject.getPickupKey())
                    .child(bundle.getString(StringConstants.KEY_UPDATE_STATUS))
                    .child(StringConstants.KEY_IS_COMPLETE).setValue(true);


            // Check status of the order
            if (bundle.getString(StringConstants.KEY_UPDATE_STATUS).equalsIgnoreCase(StringConstants.KEY_DROP) &&
                    Boolean.valueOf(dataSnapshot.child(pickupSummaryObject.getPickupKey())
                            .child(StringConstants.KEY_PAYMENT).child(StringConstants.KEY_IS_COMPLETE).getValue().toString())
                    || bundle.getString(StringConstants.KEY_UPDATE_STATUS).equalsIgnoreCase(StringConstants.KEY_PAYMENT)) {


                updateOrderStatusFinish(dbHelper, pickupSummaryObject);
            }
        }
    }


    /**
     * Function to update order isActive flag to false in Firebase and SQLite
     * @param dbHelper
     * @param pickupSummaryObject
     */
    public static void updateOrderStatusFinish(OMSDBHelper dbHelper, PickupSummaryObject pickupSummaryObject) {

        OMSBackupDatabaseService.updateOrder(dbHelper,
                Integer.valueOf(pickupSummaryObject.getPickupId()),
                BuildProperties.KEY_IS_ACTIVE, false);

        firebase.child(pickupSummaryObject.getPickupKey())
                .child(StringConstants.KEY_IS_ACTIVE).setValue(false);
    }

    /**
     * Function to update customer signature link to s3
     * @param bundle
     * @param url
     */
    public static void updateDigitalSignature(Bundle bundle, String url) {

        PickupSummaryObject pickupSummaryObject =
                getOrderDetails(bundle.getString(StringConstants.KEY_PICKUP_ID));

        // Update status in Firebase
        firebase.child(pickupSummaryObject.getPickupKey())
                .child(bundle.getString(StringConstants.KEY_UPDATE_STATUS))
                .child(StringConstants.KEY_CUSTOMER_SIGNATURE).setValue(url);

    }
}
