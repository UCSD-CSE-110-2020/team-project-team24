package com.cse110team24.walkwalkrevolution.fitness;

import android.renderscript.Element;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cse110team24.walkwalkrevolution.HomeActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;

public class GoogleFitAdapter implements FitnessService {

    private final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = System.identityHashCode(this) & 0xFFFF;
    private final String TAG = "GoogleFitAdapter";
    private GoogleSignInAccount account;

    private HomeActivity activity;

    public GoogleFitAdapter(HomeActivity activity) {
        this.activity = activity;
    }


    public void setup() {
        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(Element.DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(Element.DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .build();


        account = GoogleSignIn.getAccountForExtension(activity, fitnessOptions);
        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    activity,
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    account,
                    fitnessOptions);
        }
        // else {
        //     updateStepCount();
        //     startRecording();
        // }
    }

    // private void startRecording() {
    //     if (account == null) {
    //         return;
    //     }

    //     Fitness.getRecordingClient(activity, account)
    //             .subscribe(Element.DataType.TYPE_STEP_COUNT_CUMULATIVE)
    //             .addOnSuccessListener(new OnSuccessListener<Void>() {
    //                 @Override
    //                 public void onSuccess(Void aVoid) {
    //                     Log.i(TAG, "Successfully subscribed!");
    //                 }
    //             })
    //             .addOnFailureListener(new OnFailureListener() {
    //                 @Override
    //                 public void onFailure(@NonNull Exception e) {
    //                     Log.i(TAG, "There was a problem subscribing.");
    //                 }
    //     });
    // }


    /**
     * Reads the current daily step total, computed from midnight of the current day on the device's
     * current timezone.
    // */
    // public void updateStepCount() {
    //     if (account == null) {
    //         return;
    //     }

    //     Fitness.getHistoryClient(activity, account)
    //             .readDailyTotal(Element.DataType.TYPE_STEP_COUNT_DELTA)
    //             .addOnSuccessListener(
    //                     new OnSuccessListener<DataSet>() {
    //                         @Override
    //                         public void onSuccess(DataSet dataSet) {
    //                             Log.d(TAG, dataSet.toString());
    //                             long total =
    //                                     dataSet.isEmpty()
    //                                             ? 0
    //                                             : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();

    //                             activity.setStepCount(total);
    //                             Log.d(TAG, "Total steps: " + total);
    //                         }
    //                     })
    //             .addOnFailureListener(
    //                     new OnFailureListener() {
    //                         @Override
    //                         public void onFailure(@NonNull Exception e) {
    //                             Log.d(TAG, "There was a problem getting the step count.", e);
    //                         }
    //                     });
    // }


    @Override
    public int getRequestCode() {
        return GOOGLE_FIT_PERMISSIONS_REQUEST_CODE;
    }
}
