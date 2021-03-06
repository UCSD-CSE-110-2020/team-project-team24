package com.cse110team24.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cse110team24.walkwalkrevolution.activities.userroutes.RouteDetailsActivity;
import com.cse110team24.walkwalkrevolution.activities.userroutes.RoutesActivity;
import com.cse110team24.walkwalkrevolution.activities.userroutes.SaveRouteActivity;
import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;
import com.cse110team24.walkwalkrevolution.firebase.auth.Auth;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.users.UsersUserDataObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.TeamsDatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.UsersDatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.messaging.Messaging;
import com.cse110team24.walkwalkrevolution.fitness.FitnessService;
import com.cse110team24.walkwalkrevolution.fitness.FitnessServiceFactory;

import com.cse110team24.walkwalkrevolution.models.route.Route;

import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.activities.teams.TeamActivity;
import com.cse110team24.walkwalkrevolution.utils.RoutesManager;
import com.cse110team24.walkwalkrevolution.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.cse110team24.walkwalkrevolution.models.route.WalkStats;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Handles Daily Steps and distance, latest steps, distance, and time, recording a walk.
 * <p>Integrates {@link Auth}, {@link TeamsDatabaseService}, {@link UsersDatabaseService}, and
 * {@link Messaging}.</p>
 * <ol>
 *     <li>Saves the user's height locally, passed by {@link LoginActivity}</li>
 *     <li>Creates instance of {@link FitnessService}</li>
 *     <li>Subscribes the currently signed in user to their invitations collection in order to receive notifications</li>\
 *     <li>If saving a newly recorded route, adds it to local file</li>
 *     <ul>
 *         <li>If the signed in user has a team, adds the route to the user's team in the database</li>
 *     </ul>
 *     <li>If recording an existing route, when stopped, will automatically update local save file</li>
 *     <ul>
 *         <li>If the signed in user has a team, updates the route in the user's team in the database</li>
 *     </ul>
 * </ol>
 */
public class HomeActivity extends AppCompatActivity implements UsersUserDataObserver {
    private static final String TAG = "WWR_HomeActivity";
    private static final String DECIMAL_FMT = "#0.00";
    private static final long UPDATE_PERIOD = 60_000;

    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    public static final String HEIGHT_FT_KEY = "Height Feet";
    public static final String HEIGHT_IN_KEY = "Height Remainder Inches";
    public static final String APP_PREF = "height_preferences";

    private FitnessService fitnessService;

    private TeamsDatabaseService mTeamsDbService;
    private UsersDatabaseService mUsersDbService;
    private Messaging mMessaging;

    private SharedPreferences preferences;

    private IUser mUser;

    private Handler handler = new Handler();
    private Runnable runUpdateSteps = new Runnable() {
        @Override
        public void run() {
            fitnessService.updateDailyStepCount();
            handler.postDelayed(this, UPDATE_PERIOD);
        }
    };

    private WalkStats latestStats;

    private boolean mocking;
    private boolean endTimeSet;
    private boolean recordingExistingRoute;
    private boolean saved;
    private Intent data;
    private Intent myIntent = null;

    private int heightFeet;
    private float heightRemainderInches;

    private TextView dailyStepsTv;
    private TextView dailyDistanceTv;
    private TextView recentStepsTv;
    private TextView recentDistanceTv;
    private TextView recentTimeElapsedTv;
    private TextView noRecentWalkPromptTv;
    private Button startWalkBtn;
    private Button stopWalkBtn;
    private Button launchMockActivityBtn;
    private Button saveRouteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        preferences = getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
        getUIFields();
        saveHeight();
        setFitnessService();
        firebaseUserSetup();
        subscribeToReceiveInvitations();
        setButtonOnClickListeners();

        handler.post(runUpdateSteps);
        Log.i(TAG, "onCreate: handler posted");
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void getUIFields() {
        dailyStepsTv = findViewById(R.id.tv_daily_steps);
        dailyDistanceTv = findViewById(R.id.tv_daily_distance);
        recentStepsTv = findViewById(R.id.tv_recent_steps);
        recentDistanceTv = findViewById(R.id.tv_recent_distance);
        recentTimeElapsedTv = findViewById(R.id.tv_recent_time_elapsed);
        noRecentWalkPromptTv = findViewById(R.id.tv_no_recent_walk_prompt);
        startWalkBtn = findViewById(R.id.btn_start_walk);
        stopWalkBtn = findViewById(R.id.btn_stop_walk);
        launchMockActivityBtn = findViewById(R.id.btn_mock_values);
        saveRouteBtn = findViewById(R.id.btn_save_this_route);
        toggleBtn(stopWalkBtn);
        toggleBtn(saveRouteBtn);
    }

    private void saveHeight() {
        SharedPreferences.Editor editor = preferences.edit();
        heightFeet = getIntent().getIntExtra(HEIGHT_FT_KEY, -1);
        heightRemainderInches =  getIntent().getFloatExtra(HEIGHT_IN_KEY, -1);
        editor.putInt(HomeActivity.HEIGHT_FT_KEY, heightFeet);
        editor.putFloat(HomeActivity.HEIGHT_IN_KEY, heightRemainderInches);
        editor.apply();
        Log.i(TAG, "saveHeight: saved height (feet: " + heightFeet + ", inches: " + heightRemainderInches + ").");
    }

    private void setFitnessService() {
        String fitnessServiceKey = getServiceKey();
        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        fitnessService.setup();
    }

    private String getServiceKey() {
        return getIntent().getStringExtra(FITNESS_SERVICE_KEY);
    }

    private void firebaseUserSetup() {
        Auth auth = FirebaseApplicationWWR.getAuthFactory().createAuthService();
        mUsersDbService = (UsersDatabaseService) FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.USERS);
        mUsersDbService.register(this);
        mTeamsDbService = (TeamsDatabaseService) FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.TEAMS);
        mMessaging = FirebaseApplicationWWR.getMessagingFactory().createMessagingService(this, mUsersDbService);

        SharedPreferences preferences = getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
        String email = preferences.getString(IUser.EMAIL_KEY, null);
        if (email != null) {
            mUser = auth.getUser();
            mUser.setEmail(email);
            mUsersDbService.getUserData(mUser);
        }
    }

    private void subscribeToReceiveInvitations() {
        if (mUser != null) {
            mMessaging.subscribeToNotificationsTopic(mUser.documentKey() + "invitations");
        }
    }

    private void setButtonOnClickListeners() {
        setStartWalkBtnOnClickListener();
        setStopWalkBtnOnClickListener();
        setLaunchMockActivityBtnOnClickListener();
        setBottomNavigationOnClickListener();
        setSaveRouteBtnOnClickListener();
    }

    private void setStartWalkBtnOnClickListener() {
        startWalkBtn.setOnClickListener(view -> {
            toggleStartAndStopBtns();
            if (!mocking) {
                fitnessService.setStartRecordingTime(System.currentTimeMillis());
            } else {
                showSetEndTimeToast();
            }
            fitnessService.startRecording();
        });
    }

    private void setStopWalkBtnOnClickListener() {
        stopWalkBtn.setOnClickListener(view -> {
            if (mocking && !endTimeSet) {
                showSetEndTimeToast();
                return;
            }
            toggleStartAndStopBtns();
            if (!mocking) {
                fitnessService.setEndRecordingTime(System.currentTimeMillis());
            }
            endTimeSet = false;
            mocking = false;
            fitnessService.stopRecording();
            noRecentWalkPromptTv.setVisibility(View.INVISIBLE);
            if (recordingExistingRoute) {
                recordingExistingRoute  = false;
                return;
            }
            if (!saved) {
                toggleBtn(saveRouteBtn);
                saved = true;
            }
        });
    }

    private void setLaunchMockActivityBtnOnClickListener() {
        launchMockActivityBtn.setOnClickListener(view -> {
            if(!mocking && startWalkBtn.getVisibility() == View.INVISIBLE) {
                showNoStartTimeToast();
                return;
            }
            launchMockActivity();
        });
    }

    private void setBottomNavigationOnClickListener() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            if(menuItem.getItemId() == R.id.action_routes_list) {
                myIntent = new Intent(getApplicationContext(), RoutesActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(myIntent, RoutesActivity.REQUEST_CODE);
            }
            if(menuItem.getItemId() == R.id.action_team) {
                myIntent = new Intent(getApplicationContext(), TeamActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(myIntent, TeamActivity.REQUEST_CODE);
            }
            return true;
        });
    }

    private void setSaveRouteBtnOnClickListener() {
        saveRouteBtn.setOnClickListener(view -> {
            launchSaveRouteActivity();
        });
    }

    private void toggleStartAndStopBtns() {
        toggleBtn(startWalkBtn);
        toggleBtn(stopWalkBtn);
    }

    private void toggleBtn(Button btn) {
        btn.setEnabled(!btn.isEnabled());
        int visibility = (btn.getVisibility() == View.VISIBLE) ? View.INVISIBLE : View.VISIBLE;
        btn.setVisibility(visibility);
    }

    private void launchMockActivity() {
        Intent intent = new Intent(this, MockActivity.class)
                .putExtra(MockActivity.START_WALK_BTN_VISIBILITY_KEY, startWalkBtn.getVisibility());
        startActivityForResult(intent, MockActivity.REQUEST_CODE);
    }

    /*
    public void launchGoToRoutesActivity(Intent intent) {
        startActivityForResult(intent, RoutesActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
    public void launchGoToTeamActivity(Intent intent) {
        startActivityForResult(intent, RoutesActivity.REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
    */

    private void launchSaveRouteActivity() {
        Log.i(TAG, "launchSaveRouteActivity: route stopped, going to save");
        Intent saveRouteIntent = new Intent(this, SaveRouteActivity.class)
                .putExtra(SaveRouteActivity.WALK_STATS_KEY, latestStats);
        startActivityForResult(saveRouteIntent, SaveRouteActivity.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == fitnessService.getRequestCode()) {
            if (resultCode == Activity.RESULT_OK) {
                fitnessService.updateDailyStepCount();
            } else {
                Log.e(TAG, "onActivityResult: error with fitness result code: " + resultCode);
            }
        } else if (requestCode == MockActivity.REQUEST_CODE && data != null) {
            setMockedExtras(data);
        } else if ((requestCode == RoutesActivity.REQUEST_CODE || requestCode == TeamActivity.REQUEST_CODE) && resultCode == Activity.RESULT_OK) {
            this.data = data;
            startRecordingExistingRoute();
        } else if (requestCode == SaveRouteActivity.REQUEST_CODE && resultCode == RESULT_OK ) {
            this.data = data;
            saved = false;
            handleNewRouteRecorded(data);
        }
    }

    private void handleNewRouteRecorded(Intent data) {
        toggleBtn(saveRouteBtn);
        Route route = (Route) data.getSerializableExtra(SaveRouteActivity.NEW_ROUTE_KEY);
        Log.d(TAG, "handleNewRouteRecorded: new route recorded " + route);
        uploadRouteToTeamIfExists(route);
        saveIntoList(route);
        showRouteSavedToast();
    }

    // if the user's team exists, upload the route to the routes collection of the Team
    private void uploadRouteToTeamIfExists(Route route) {
        String teamUid = Utils.getString(preferences, IUser.TEAM_UID_KEY);

        if (teamUid != null) {
            mTeamsDbService.uploadRoute(teamUid, route);
        }
    }

    private void showRouteSavedToast() {
        Toast.makeText(this, "Route saved!", Toast.LENGTH_LONG).show();
    }

    private void startRecordingExistingRoute() {
        recordingExistingRoute = true;
        startWalkBtn.performClick();
    }

    public void setDailyStats(long stepCount) {
        Log.i(TAG, "setDailyStats: setting daily stats with steps: " + stepCount);
        dailyStepsTv.setText(String.valueOf(stepCount));
        double distance = calculateDistance(stepCount);
        NumberFormat numberFormat = new DecimalFormat(DECIMAL_FMT);
        dailyDistanceTv.setText(numberFormat.format(distance));
    }

    private double calculateDistance(long stepCount) {
        return fitnessService.getDistanceFromHeight(stepCount, heightFeet, heightRemainderInches);
    }

    public void setLatestWalkStats(long stepCount, long timeElapsed) {
        double distanceTraveled = calculateDistance(stepCount);
        latestStats = new WalkStats(stepCount, timeElapsed, distanceTraveled, Calendar.getInstance());
        recentStepsTv.setText(String.valueOf(stepCount));
        recentDistanceTv.setText(latestStats.formattedDistance());
        recentTimeElapsedTv.setText(latestStats.formattedTime());
        checkIfRouteExisted(latestStats);
    }

    private void checkIfRouteExisted(WalkStats stats) {
        if (recordingExistingRoute) {
            Log.i(TAG, "checkIfRouteExisted: returning to route details view for automatic recording");
            Route existingRoute = (Route) data.getSerializableExtra(RouteDetailsActivity.ROUTE_KEY);
            existingRoute.setStats(stats);

            // update existing route in db
            if (routeBelongsToUser(existingRoute)) {
                updateRouteToTeamIfExists(existingRoute);
                saveIntoList(existingRoute);
                showRouteUpdatedToast();
            } else {
                try {
                    Log.d(TAG, "checkIfRouteExisted: route uid: " + existingRoute.getRouteUid());
                    RoutesManager.writeSingle(existingRoute, existingRoute.getRouteUid(), this);
                    Utils.showToast(this, "Saved your stats for a teammate's route", Toast.LENGTH_LONG);
                } catch (IOException e) {
                    Log.e(TAG, "checkIfRouteExisted: could not save teammate route", e);
                }
            }

        }
    }

    private boolean routeBelongsToUser(Route route) {
        Log.d(TAG, "routeBelongsToUser: route's creator name " + route.getCreatorName());
        Log.d(TAG, "routeBelongsToUser: mUser.getDisplayName() " + mUser.getDisplayName());
        return route.getCreatorName().equals(Utils.getString(preferences, IUser.USER_NAME_KEY));
    }

    // if the user's team exists, upload the route to the routes collection of the Team
    private void updateRouteToTeamIfExists(Route route) {
        String teamUid = Utils.getString(preferences, IUser.TEAM_UID_KEY);

        if (teamUid != null && route.getRouteUid() != null) {
            mTeamsDbService.updateRoute(teamUid, route);
        }
    }

    private void saveIntoList(Route route) {
        int idx = data.getIntExtra(RouteDetailsActivity.ROUTE_IDX_KEY, -1);
        Log.d(TAG, "saveIntoList: saving new route with idx " + idx + " " + route);
        try {
            if (idx == -1) {
                RoutesManager.appendToList(route, RoutesActivity.LIST_SAVE_FILE, this);
            } else {
                RoutesManager.replaceInList(route, idx, RoutesActivity.LIST_SAVE_FILE, this);
            }
        } catch (IOException e) {
            Log.e(TAG, "saveIntoList: failed to replace route in list", e);
        }
    }

    private void showRouteUpdatedToast() {
        Toast.makeText(this, "Route updated!", Toast.LENGTH_LONG).show();
    }

    private void setMockedExtras(Intent data) {
        setMockedTime(data);
        setMockedSteps(data);
        fitnessService.updateDailyStepCount();
    }

    private void setMockedSteps(Intent data) {
        long stepsToAdd = data.getLongExtra(MockActivity.ADDED_STEPS_KEY, 0);
        Log.d(TAG, "setMockedExtras: steps to add " + stepsToAdd);
        fitnessService.setStepsToAdd(stepsToAdd);
    }

    private void setMockedTime(Intent data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(MockActivity.TIME_FMT);
        String time = data.getStringExtra(MockActivity.INPUT_TIME_KEY);
        Date dateTime = parseMockedTime(dateFormat, time);

        boolean settingStartTime = data.getBooleanExtra(MockActivity.SETTING_START_TIME_KEY, false);
        long timeMillis = dateTime.getTime();
        if (settingStartTime) {
            mocking = true;
            fitnessService.setStartRecordingTime(timeMillis);
        } else {
            fitnessService.setEndRecordingTime(timeMillis);
            endTimeSet = true;
        }
    }

    private Date parseMockedTime(SimpleDateFormat dateFormat, String time) {
        Date dateTime = null;
        try {
            dateTime = dateFormat.parse(time);
        } catch (ParseException e) {
            Log.e(TAG, "setMockedExtras: there was a problem parsing the time string " + time, e);
        }
        Log.i(TAG, "setMockedExtras: time string " + time + " correctly parsed with value " + dateTime);
        return dateTime;
    }

    private void showSetEndTimeToast() {
        Toast.makeText(this, "Remember to set an end time for your walk!", Toast.LENGTH_SHORT).show();
    }

    private void showNoStartTimeToast() {
        Toast.makeText(this, "You didn't mock a start time for this walk!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserData(Map<String, Object> userDataMap) {
        if (userDataMap != null) {
            String teamUid = (String) userDataMap.get(IUser.TEAM_UID_KEY);
            if (teamUid != null) {
                mMessaging.subscribeToNotificationsTopic(teamUid);
            }
            Utils.saveString(preferences, IUser.TEAM_UID_KEY, teamUid);
        }
    }
}
