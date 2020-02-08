package com.cse110team24.walkwalkrevolution.fitness;

import android.util.Log;

import com.cse110team24.walkwalkrevolution.HomeActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.request.SessionReadRequest;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GoogleFitAdapter implements FitnessService {
    private static final String TAG = "GoogleFitAdapter";
    private static final String RECORDING_SESSION_NAME = "Recording a walk";
    private static final double STRIDE_LEN_CONST = 0.413;
    private static final int FEET_IN_MILE = 5280;
    private static final int INCHES_IN_FEET = 12;

    private final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = System.identityHashCode(this) & 0xFFFF;
    private GoogleSignInAccount account;
    private HomeActivity activity;
    private Session recordingSession;

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
        } else {
            updateDailyStepCount();
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
                    activity.setDailyStats(steps);
                    Log.i(TAG, "updateDailyStepCount: successful steps update: " + steps);
                })
                .addOnFailureListener(e ->
                    Log.e(TAG, "updateDailyStepCount: there was a problem getting the daily step count.", e)
                );
    }

    @Override
    public void startRecording() {
        if (account == null) {
            return;
        }
        subscribeRecordingClient();
        recordingSession = new Session.Builder()
                .setName(RECORDING_SESSION_NAME)
                .setIdentifier("recording session")
                .setStartTime(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .build();
        Task<Void> response = Fitness.getSessionsClient(activity, account).startSession(recordingSession);
    }

    @Override
    public void stopRecording() {
        Task<List<Session>> response = Fitness.getSessionsClient(activity, account).stopSession(recordingSession.getIdentifier());
        readSessionData();
        unsubscribeRecordingClient();
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

    private void subscribeRecordingClient() {
        Fitness.getRecordingClient(activity, account)
                .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .addOnSuccessListener(datSet -> {
                    Log.i(TAG, "startRecording: recording session started!");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "startRecording: recording session could not be started", e);
                });
    }

    private void unsubscribeRecordingClient() {
        Fitness.getRecordingClient(activity, account)
                .unsubscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .addOnSuccessListener(dataSet -> {
                    Log.i(TAG, "unsubscribeRecordingClient: sucessfully unsubscribed client");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "unsubscribeRecordingClient: could not unsubscribe from recording client", e);
                });
    }

    private void readSessionData() {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        SessionReadRequest readRequest = new SessionReadRequest.Builder()
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS)
                .read(DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .setSessionName(RECORDING_SESSION_NAME)
                .build();

        Fitness.getSessionsClient(activity, account)
                .readSession(readRequest)
                .addOnSuccessListener(sessionReadResponse -> {
                    List<Session> sessions = sessionReadResponse.getSessions();

                    sessions.forEach(session -> {
                        List<DataSet> dataSets = sessionReadResponse.getDataSet(session);
                        dataSets.forEach(dataSet -> {
                            long steps = dataSet.isEmpty()
                                    ? 0 : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
                            Log.i(TAG, "readSessionData: success reading session data: " + steps);
                        });
                    });
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "readSessionData: Error reading session data", e);
                });
    }
}
