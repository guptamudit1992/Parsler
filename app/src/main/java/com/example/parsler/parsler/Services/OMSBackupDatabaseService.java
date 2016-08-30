package com.example.parsler.parsler.Services;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.parsler.parsler.Commons.BuildProperties;
import com.example.parsler.parsler.Commons.StringConstants;
import com.example.parsler.parsler.Database.OMSDBHelper;
import com.example.parsler.parsler.Models.PickupSummaryObject;

public class OMSBackupDatabaseService {

    /**
     * Function to insert tuple in table Order Management
     * @param dbHelper
     * @param pickupSummaryObject
     */
    public static void insertOrder(OMSDBHelper dbHelper, PickupSummaryObject pickupSummaryObject) {

        // Gets the data repository in write mode
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues contentValues = new ContentValues();
        contentValues.put(BuildProperties.KEY_PICKUP_ID, pickupSummaryObject.getPickupId());
        contentValues.put(BuildProperties.KEY_IS_ACTIVE, pickupSummaryObject.getIsActive());
        contentValues.put(BuildProperties.KEY_IS_READ, pickupSummaryObject.getIsRead());
        contentValues.put(BuildProperties.KEY_IS_CANCELLED,pickupSummaryObject.getIsCancelled());
        contentValues.put(BuildProperties.KEY_PICKUP_IS_COMPLETED, pickupSummaryObject.getPickupIsCompleted());
        contentValues.put(BuildProperties.KEY_DROP_IS_COMPLETED, pickupSummaryObject.getDropIsCompleted());

        // Insert row
        sqLiteDatabase.insert(BuildProperties.TABLE_NAME, null, contentValues);
    }


    /**
     * Function to read tuple from table Order Management
     * @param dbHelper
     * @param pickupId
     */
    public static Cursor readOrder(OMSDBHelper dbHelper, int pickupId) {

        // Gets the data repository in read mode
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        // Define projection that specifies which columns from the database to extract
        String[] projection = {
                BuildProperties.KEY_PICKUP_ID,
                BuildProperties.KEY_IS_ACTIVE,
                BuildProperties.KEY_IS_READ,
                BuildProperties.KEY_PICKUP_IS_COMPLETED,
                BuildProperties.KEY_DROP_IS_COMPLETED,
                BuildProperties.KEY_IS_CANCELLED

        };

        // Define sorting order
        String sortOrder = StringConstants.KEY_PICKUP_ID + " DESC";

        // Set cursor
        Cursor cursor = sqLiteDatabase.query(BuildProperties.TABLE_NAME, projection, null, null, null, null, sortOrder);
        cursor.moveToFirst();
        return cursor;
    }


    /**
     * Function to update tuple from table Order Management
     * @param dbHelper
     * @param pickupId
     * @param key
     * @param value
     */
    public static void updateOrder(OMSDBHelper dbHelper, int pickupId, String key, Boolean value) {

        // Gets the data repository in write mode
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        // Updated values
        ContentValues values = new ContentValues();
        values.put(key, value);

        // Search tuple parameter
        String selection = BuildProperties.KEY_PICKUP_ID + " LIKE ?";
        String[] selectionArgs = {
                String.valueOf(pickupId)
        };

        // Update the tuple
        sqLiteDatabase.update(
                BuildProperties.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }


    /**
     * Function to delete tuple from table Order Management
     * @param dbHelper
     */
    public static void deleteOrder(OMSDBHelper dbHelper, int pickupId) {

        // Gets the data repository in write mode
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        // Search tuple parameters
        String selection = BuildProperties.KEY_PICKUP_ID + " LIKE ?";
        String[] selectionArgs = {
                String.valueOf(pickupId)
        };

        // Delete the tuple
        sqLiteDatabase.delete(BuildProperties.TABLE_NAME, selection, selectionArgs);
    }
}
