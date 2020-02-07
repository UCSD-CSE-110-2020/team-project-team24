package com.cse110team24.walkwalkrevolution.fitness;

import android.util.Log;

import com.cse110team24.walkwalkrevolution.HomeActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;

public class GoogleFitAdapter implements FitnessService {
    private static final String TAG = "GoogleFitAdapter";
    private static final double STRIDE_LEN_CONST = 0.413;
    private static final int FEET_IN_MILE = 5280;
    private static final int INCHES_IN_FEET = 12;

    private final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = System.identityHashCode(this) & 0xFFFF;
    private GoogleSignInAccount account;
    private HomeActivity activity;

    public GoogleFitAdapter(HomeActivity activity) {
        this.activity = activity;
    }

    @Override
    public void setup() {
        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .build();

        GoogleSignInAccount lastAcct = GoogleSignIn.getLastSignedInAccount(activity);
        account = (lastAcct == null) ? GoogleSignIn.getAccountForExtension(activity, fitnessOptions) : lastAcct;
        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    activity,
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    account,
                    fitnessOptions);
        }
    }

    @Override
    public void updateDailyStepCount() {
        if (account == null) {
            return;
        }

        Fitness.getHistoryClient(activity, account)
                .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(dataSet -> {
                    long steps = dataSet.isEmpty()
                            ? 0 : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
                    activity.setStepCount(steps);
                })
                .addOnFailureListener(e ->
                    Log.e(TAG, "updateDailyStepCount: there was a problem getting the daily step count.", e)
                );
    }


    @Override
    public int getRequestCode() {
        return GOOGLE_FIT_PERMISSIONS_REQUEST_CODE;
    }

    @Override
    public double getDistanceFromHeight(long steps, int heightFeet, float heightRemainderInches) {
        double totalHeightInches = (INCHES_IN_FEET * heightFeet) + heightRemainderInches;
        double avgStrideLen = (totalHeightInches * STRIDE_LEN_CONST) / INCHES_IN_FEET;
        double stepsPerMile = FEET_IN_MILE / avgStrideLen;
        return steps / stepsPerMile;
    }
}
