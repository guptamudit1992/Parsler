package com.example.parsler.parsler.Models;

import android.databinding.BaseObservable;

import com.example.parsler.parsler.Commons.StaticConstants;
import com.example.parsler.parsler.Commons.StringConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class PickupSummaryObject extends BaseObservable {

    private String pickupKey;
    private String pickupId;
    private String isActive;
    public String status;
    private long timestamp;
    private boolean isRead;
    public boolean isCancelled;
    HashMap<String, String> pickupData = new HashMap<>();
    HashMap<String, String> dropData = new HashMap<>();
    HashMap<String, String> paymentData = new HashMap<>();


    // Necessary for Firebase to be able to create a new instance of this class
    public PickupSummaryObject() {}

    public PickupSummaryObject(String pickupId, String isActive, boolean isRead,
                               String status, long timestamp, boolean isCancelled,
                               HashMap<String, String> pickupData,
                               HashMap<String, String> dropData,
                               HashMap<String, String> paymentData) {

        this.pickupId = pickupId;
        this.timestamp = timestamp;
        this.isRead = isRead;
        this.isActive = isActive;
		this.isCancelled = isCancelled;
        this.status = status;
        this.pickupData = pickupData;
        this.dropData = dropData;
        this.paymentData = paymentData;
    }




    public void setPickupKey(String _key) {
        this.pickupKey = _key;
    }


    public String getPickupKey() {
        return this.pickupKey;
    }


    public String getPickupId() {
        return this.pickupId;
    }


    public String getIsActive() {
        return this.isActive;
    }

    public Boolean getIsCancelled() {
        return this.isCancelled;
    }

    public String getTimestamp() {
        DateFormat df = new SimpleDateFormat(StaticConstants.DEFAULT_DATE_TIME_FORMAT);
        Date date = new Date(this.timestamp);
        return df.format(date);
    }


    public boolean getIsRead() {
        return this.isRead;
    }


    public String getStatus() {
        return this.status;
    }


    public HashMap<String, String> getPickupData() {
        return this.pickupData;
    }


    public HashMap<String, String> getDropData() {
        return this.dropData;
    }


    public HashMap<String, String> getPaymentData() {
        return this.paymentData;
    }


    public boolean getIsPaymentSuccess() {
        return Boolean.valueOf(this.paymentData.get(StringConstants.KEY_IS_COMPLETE));
    }


    public long getPaymentAmount() {
        return Long.valueOf(this.paymentData.get(StringConstants.KEY_PAYMENT_AMOUNT));
    }


    public String getPaymentCurrency() {
        return this.paymentData.get(StringConstants.KEY_PAYMENT_CURRENCY);
    }


    public String getPaymentMode() {
        return this.paymentData.get(StringConstants.KEY_PAYMENT_MODE);
    }


    public String getCollectionPoint() {
        return this.paymentData.get(StringConstants.KEY_COLLECTION_POINT);
    }


    public String getPickupTimestamp() {
        DateFormat df = new SimpleDateFormat(StaticConstants.DEFAULT_DATE_TIME_FORMAT);
        Date date = new Date(Long.valueOf(
                this.pickupData.get(StringConstants.KEY_TIMESTAMP).toString()));
        return df.format(date);
    }


    public String getDropTimestamp() {
        DateFormat df = new SimpleDateFormat(StaticConstants.DEFAULT_DATE_TIME_FORMAT);
        Date date = new Date(Long.valueOf(
                this.dropData.get(StringConstants.KEY_TIMESTAMP)));
        return df.format(date);
    }


    public String getPickupEntity() {
        return this.pickupData.get(StringConstants.KEY_ENTITY);
    }


    public String getDropEntity() {
        return this.dropData.get(StringConstants.KEY_ENTITY);
    }


    public String getPickupLocation() {
        return this.pickupData.get(StringConstants.KEY_LOC);
    }


    public String getDropLocation() {
        return this.dropData.get(StringConstants.KEY_LOC);
    }


    public String getPickupETA() {
        return this.pickupData.get(StringConstants.KEY_ETA);
    }


    public String getDropETA() {
        return this.dropData.get(StringConstants.KEY_ETA);
    }


    public String getCustomerContact() {
        return this.pickupData.get(StringConstants.KEY_CUSTOMER_CONTACT);
    }


    public String getPickupLat() {
        return this.pickupData.get(StringConstants.KEY_LAT);
    }


    public String getPickupLong() {
        return this.pickupData.get(StringConstants.KEY_LONG);
    }


    public String getDropLat() {
        return this.dropData.get(StringConstants.KEY_LAT);
    }


    public String getDropLong() {
        return this.dropData.get(StringConstants.KEY_LONG);
    }


    public Boolean getPickupIsCompleted() {
        return Boolean.valueOf(this.pickupData.get(StringConstants.KEY_IS_COMPLETE));
    }


    public Boolean getDropIsCompleted() {
        return Boolean.valueOf(this.dropData.get(StringConstants.KEY_IS_COMPLETE));
    }

}
