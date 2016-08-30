package com.example.parsler.parsler.Commons;

public class BuildProperties {

    // Local Firebase Environment endpoints
    public static final String FIREBASE_GEOLOCATION_URL = "https://popping-torch-1459.firebaseio.com/";
    public static final String FIREBASE_ORDER_MANAGEMENT_SERVICE_URL = "https://orderservice.firebaseio.com/";

    // Staging Firebase Environment endpoints
    //public static final String FIREBASE_GEOLOCATION_URL = "https://boiling-torch-3635.firebaseio.com/";
    //public static final String FIREBASE_ORDER_MANAGEMENT_SERVICE_URL = "https://order-management-pyc.firebaseio.com/";

    // API endpoint
    public static final String BASE_URL = "http://private-4434e-parsler.apiary-mock.com/";

    // AWS credentials
    public static final String AWS_ACCESS_KEY = "AKIAIHETZSKWVKXHTU7Q";
    public static final String AWS_PRIVATE_KEY = "lEtetNnQQJNwG4jfEmds/0AABiPatemxLWKgnJ4R";
    public static final String COGNITO_POOL_ID = "us-east-1:ad62312a-957c-47a0-9b16-e1e80747c433";
    public static final String BUCKET_NAME = "signaturebucket";

    // SQLite Database credentials
    public static final String DATABASE_NAME = "OMS_Backup.db";
    public static final int DATABASE_VERSION = 3;
    public static final String TABLE_NAME = "order_backup";

    // Order Management Service Database keys
    public static final String KEY_PICKUP_ID = "pickupId";
    public static final String KEY_IS_ACTIVE = "isActive";
    public static final String KEY_IS_READ = "isRead";
    public static final String KEY_IS_CANCELLED = "isCancelled";
    public static final String KEY_PICKUP_IS_COMPLETED = "pickupDataIsCompleted";
    public static final String KEY_DROP_IS_COMPLETED = "dropDataIsCompleted";
    public static final String KEY_PAYMENT_IS_COMPLETED = "paymentDataIsCompleted";

}
