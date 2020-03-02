package com.cse110team24.walkwalkrevolution.fitness;

import android.util.Log;
import com.cse110team24.walkwalkrevolution.HomeActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
//<<<<<<< Updated upstream

public class GoogleFitAdapter implements FitnessService {
    private static final String TAG = "GoogleFitAdapter";
    private static final double STRIDE_LEN_CONST = 0.413;
    private static final int FEET_IN_MILE = 5280;
    private static final int INCHES_IN_FEET = 12;
    private static final long MILLIS_IN_DAY = 86_400_000;

    private final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = System.identityHashCode(this) & 0xFFFF;
    private GoogleSignInAccount account;
    private HomeActivity activity;

    private long recordingStartTime;
    private long recordingEndTime;
    private long recordingInitSteps;
    private long updatedSteps;
    private long stepsToAdd;

    public GoogleFitAdapter(HomeActivity activity) {
        this.activity = activity;
    }

//=======
//import com.google.android.gms.fitness.data.Session;
//import com.google.android.gms.fitness.request.SessionInsertRequest;
//import com.google.android.gms.fitness.request.SessionReadRequest;
//import com.google.android.gms.tasks.Task;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//public class GoogleFitAdapter implements FitnessService {
//    private static final String TAG = "GoogleFitAdapter";
//    private static final String RECORDING_SESSION_NAME = "Recording a walk";
//    private static final double STRIDE_LEN_CONST = 0.413;
//    private static final int FEET_IN_MILE = 5280;
//    private static final int INCHES_IN_FEET = 12;
//    private final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = System.identityHashCode(this) & 0xFFFF;
//    private GoogleSignInAccount account;
//    private HomeActivity activity;
//    private Session recordingSession;
//    public GoogleFitAdapter(HomeActivity activity) {
//        this.activity = activity;
//    }
//>>>>>>> Stashed changes
    @Override
    public int getRequestCode() {
        return GOOGLE_FIT_PERMISSIONS_REQUEST_CODE;
    }
//<<<<<<< Updated upstream
//
//=======
//>>>>>>> Stashed changes
//    @Override
//    public double getDistanceFromHeight(long steps, int heightFeet, float heightRemainderInches) {
//        double totalHeightInches = (INCHES_IN_FEET * heightFeet) + heightRemainderInches;
//        double avgStrideLen = (totalHeightInches * STRIDE_LEN_CONST) / INCHES_IN_FEET;
//        double stepsPerMile = FEET_IN_MILE / avgStrideLen;
//        return steps / stepsPerMile;
//    }
//<<<<<<< Updated upstream

    @Override
    public void setStartRecordingTime(long startTime) {
        recordingStartTime = startTime;
    }

    @Override
    public void setEndRecordingTime(long startTime) {
        recordingEndTime = startTime;
    }

    @Override
    public void setStepsToAdd(long stepsToAdd) {
        this.stepsToAdd += stepsToAdd;
    }

//=======
//>>>>>>> Stashed changes
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
//<<<<<<< Updated upstream
                    updatedSteps = dataSet.isEmpty()
                            ? 0 : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
                    Log.i(TAG, "updateDailyStepCount: successful steps update: " + updatedSteps);
//=======
//                    long steps = dataSet.isEmpty()
//                            ? 0 : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
//                    activity.setDailyStats(steps);
//                    Log.i(TAG, "updateDailyStepCount: successful steps update: " + steps);
//>>>>>>> Stashed changes
                })
                .addOnFailureListener(e ->
                        Log.e(TAG, "updateDailyStepCount: there was a problem getting the daily step count.", e)
                );
//<<<<<<< Updated upstream
        activity.setDailyStats(updatedSteps + stepsToAdd);
    }

    @Override
    public void startRecording() {
        updateDailyStepCount();
        recordingInitSteps = updatedSteps + stepsToAdd;
    }


    @Override
    public void stopRecording() {
        long timeElapsed = recordingEndTime - recordingStartTime;
        if (timeElapsed < 0) {
            timeElapsed += MILLIS_IN_DAY;
        }
        updateDailyStepCount();
        long totalSteps = updatedSteps + stepsToAdd - recordingInitSteps;
        activity.setLatestWalkStats(totalSteps, timeElapsed);
//=======
//    }
//    @Override
//    public void startRecording() {
//        if (account == null) {
//            return;
//        }
//        subscribeRecordingClient();
//        recordingSession = new Session.Builder()
//                .setName(RECORDING_SESSION_NAME)
//                .setIdentifier("recording session")
//                .setStartTime(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
//                .build();
//        insertSession();
//        Task<Void> response = Fitness.getSessionsClient(activity, account).startSession(recordingSession);
//        Log.i(TAG, "startRecording: recording session started");
//    }
//    private void insertSession() {
//        DataSource dataSource = getDataSource();
//        DataSet dataSet = DataSet.create(dataSource);
//        DataPoint walkSteps = dataSet.createDataPoint();
//        SessionInsertRequest insertRequest = new SessionInsertRequest.Builder()
//                .setSession(recordingSession)
//                .addDataSet(dataSet)
//                .build();
//        Fitness.getSessionsClient(activity, account)
//                .insertSession(insertRequest)
//                .addOnSuccessListener(Void -> {
//                    Log.i(TAG, "insertSession: successful");
//                })
//                .addOnFailureListener(e -> {
//                    Log.e(TAG, "insertSession: error", e);
//                });
//    }
//    private DataSource getDataSource() {
//        return new DataSource.Builder()
//                .setAppPackageName(activity.getPackageName())
//                .setDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE)
//                .setType(DataSource.TYPE_RAW)
//                .build();
//    }
//    @Override
//    public void stopRecording() {
//        Task<List<Session>> response = Fitness.getSessionsClient(activity, account).stopSession(recordingSession.getIdentifier());
//        Log.i(TAG, "stopRecording: recordingSession name: " + recordingSession.getName());
//        readSessionData();
//        unsubscribeRecordingClient();
//        Log.i(TAG, "stopRecording: recording session stopped");
//    }
//    private void subscribeRecordingClient() {
//        Fitness.getRecordingClient(activity, account)
//                .subscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
//                .addOnSuccessListener(datSet -> {
//                    Log.i(TAG, "subscribeRecordingClient: successfully subscribed to recording client!");
//                })
//                .addOnFailureListener(e -> {
//                    Log.e(TAG, "subscribeRecordingClient: error subscribing to recording client!", e);
//                });
//    }
//    private void unsubscribeRecordingClient() {
//        Fitness.getRecordingClient(activity, account)
//                .unsubscribe(DataType.TYPE_STEP_COUNT_CUMULATIVE)
//                .addOnSuccessListener(dataSet -> {
//                    Log.i(TAG, "unsubscribeRecordingClient: sucessfully unsubscribed client");
//                })
//                .addOnFailureListener(e -> {
//                    Log.e(TAG, "unsubscribeRecordingClient: could not unsubscribe from recording client", e);
//                });
//    }
//    private void readSessionData() {
//        Log.i(TAG, "readSessionData: reading session data");
//        Calendar cal = Calendar.getInstance();
//        Date now = new Date();
//        cal.setTime(now);
//        long endTime = cal.getTimeInMillis();
//        cal.add(Calendar.WEEK_OF_YEAR, -1);
//        long startTime = cal.getTimeInMillis();
//        SessionReadRequest readRequest = new SessionReadRequest.Builder()
//                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS)
//                .read(DataType.TYPE_STEP_COUNT_CUMULATIVE)
//                .setSessionName(RECORDING_SESSION_NAME)
//                .build();
//        Fitness.getSessionsClient(activity, account)
//                .readSession(readRequest)
//                .addOnSuccessListener(sessionReadResponse -> {
//                    List<Session> sessions = sessionReadResponse.getSessions();
//                    Log.i(TAG, "readSessionData: session read successful, session count: " + sessions.size());
//                    sessions.forEach(session -> {
//                        List<DataSet> dataSets = sessionReadResponse.getDataSet(session);
//                        dataSets.forEach(dataSet -> {
//                            long steps = dataSet.isEmpty()
//                                    ? 0 : dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
//                            Log.i(TAG, "readSessionData: success reading session data: " + steps);
//                        });
//                    });
//                })
//                .addOnFailureListener(e -> {
//                    Log.e(TAG, "readSessionData: Error reading session data", e);
//                });
//        Log.i(TAG, "readSessionData: finished reading session data");
//>>>>>>> Stashed changes
    }

    @Override
    public double getDistanceFromHeight(long steps, int heightFeet, float heightRemainderInches) {
        return 0;
    }
}
