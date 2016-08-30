package com.example.parsler.parsler.Services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.example.parsler.parsler.Commons.BuildProperties;
import com.example.parsler.parsler.Commons.StringConstants;
import com.example.parsler.parsler.PickupSummaryActivity;
import com.example.parsler.parsler.Utility.OMSController;

import java.io.File;
import java.net.URL;

public class AWSServices {

    private static Bundle bundle;
    private static Context applicationContext;

    /**
     * Function to upload digital signature to Amazon S3 Bucket
     * @param context
     * @param file
     */
    public static void uploadS3Bucket(Context context, File file, Bundle _bundle) {
        applicationContext = context;
        bundle = _bundle;

        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                BuildProperties.COGNITO_POOL_ID,
                Regions.US_EAST_1);

        //AmazonS3Client s3Client = new AmazonS3Client(credentialsProvider);

        AmazonS3Client s3Client = new AmazonS3Client(new BasicAWSCredentials(
                BuildProperties.AWS_ACCESS_KEY,
                BuildProperties.AWS_PRIVATE_KEY));
        TransferUtility transferUtility = new TransferUtility(s3Client, context);

        TransferObserver observer = transferUtility.upload(
                BuildProperties.BUCKET_NAME,                // The S3 bucket to upload to
                file.getName()
                        .concat(bundle.getString(StringConstants.KEY_PICKUP_ID))
                        .concat(bundle.getString(StringConstants.KEY_UPDATE_STATUS)),
                                                            // The key for the uploaded object
                file                                        // The location of the file to be uploaded
        );

        GeneratePresignedUrlRequest urlRequest =
                new GeneratePresignedUrlRequest(BuildProperties.BUCKET_NAME, file.getName());

        URL url = s3Client.generatePresignedUrl(urlRequest);
        OMSController.updateDigitalSignature(bundle, url.toString());


        if (!TransferState.COMPLETED.equals(observer.getState())
                && !TransferState.FAILED.equals(observer.getState())
                && !TransferState.CANCELED.equals(observer.getState())) {

            observer.setTransferListener(new UploadListener());
        }
    }


    /**
     * A TransferListener class that can listen to a upload task and be notified when the status changes.
     */
    private static class UploadListener implements TransferListener {

        // Simply updates the UI list when notified.
        @Override
        public void onError(int id, Exception e) {
            Toast.makeText(applicationContext,
                    StringConstants.AWS_S3_UPLOAD_ERROR, Toast.LENGTH_SHORT).show();
        }


        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) { }


        @Override
        public void onStateChanged(int id, TransferState newState) {

            if(newState.equals(TransferState.COMPLETED)) {

                Toast.makeText(applicationContext,
                        StringConstants.AWS_S3_UPLOAD_SUCCESS, Toast.LENGTH_SHORT).show();

                // Update order status
                OMSController.updateOrderStatus(bundle);

                // Navigate back to Order Summary Activity
                Intent intent = new Intent(applicationContext, PickupSummaryActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(StringConstants.KEY_PICKUP_ID,
                        bundle.getString(StringConstants.KEY_PICKUP_ID));
                intent.putExtra(StringConstants.KEY_IS_ACTIVE,
                        bundle.getBoolean(StringConstants.KEY_IS_ACTIVE));
                applicationContext.startActivity(intent);

            } else {

                Toast.makeText(applicationContext,
                        StringConstants.AWS_S3_UPLOAD_IN_PROGRESS, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
