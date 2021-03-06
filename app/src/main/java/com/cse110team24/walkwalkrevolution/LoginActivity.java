package com.cse110team24.walkwalkrevolution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;
import com.cse110team24.walkwalkrevolution.firebase.auth.Auth;
import com.cse110team24.walkwalkrevolution.firebase.auth.AuthObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.DatabaseService;

import com.cse110team24.walkwalkrevolution.firebase.firestore.services.UsersDatabaseService;
import com.cse110team24.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.cse110team24.walkwalkrevolution.fitness.GoogleFitAdapter;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.utils.Utils;

/**
 * Handles user authentication UI, integrating {@link Auth} and {@link UsersDatabaseService}.
 * Asks for user height information in any mode.
 * <ol>
 *     <li>On successful sign in or sign up, the user's name, email, and display name are saved locally
 *     to the device. See {@link IUser} for the SharedPreferences keys.</li>
 *     <li>Implements {@link AuthObserver} in order to detect authentication changes.</li>
 *     <li>Starts in sign-in mode. Asks for User email and password.</li>
 *     <ul>
 *         <li>If there is no error, launches {@link HomeActivity}</li>
 *         <li>If there is a sign in error, displays appropriate error as Toast</li>
 *     </ul>
 *     <li>Click "sign up here" displays additional displayName field.</li>
 *     <ul>
 *         <li>Only allows sign-up click if email is valid gmail, password is >= 6 characters in length,
 *         and displayName is not blank</li>
 *         <li>If there is no error, launches {@link HomeActivity}</li>
 *         <li>If there is a sign up error, displays appropriate error as Toast</li>
 *     </ul>
 * </ol>
 */
public class LoginActivity extends AppCompatActivity implements AuthObserver {
    private static final String TAG = "WWR_LoginActivity";
    private static final String INVALID_GMAIL_TOAST = "Please enter a valid gmail address!";
    private static final String INVALID_PASSWORD_TOAST = "Please enter a password at least 6 characters long!";
    private static final String INVALID_SIGN_IN = "Incorrect email or password";

    public static final int MAX_FEET = 8;
    public static final float MAX_INCHES = 11.99f;
    public static final float INVALID_VAL = -1.0f;

    private String fitnessServiceKey = "GOOGLE_FIT";

    private Intent homeIntent;

    private Button withoutLoginBtn;
    private TextView promptTv;
    private EditText feetEditText;
    private EditText inchesEditText;
    private TextView feetTextView;
    private TextView inchesTextView;
    private EditText nameEditText;
    private EditText gmailEditText;
    private EditText passwordEditText;
    private Button loginBtn;
    private TextView signUpTv;

    SharedPreferences preferences;

    // firebase dependencies
    private Auth mAuth;
    private IUser mUser;
    private UsersDatabaseService mDb;
    private ProgressBar progressBar;

    private int feet;
    private float inches;
    private boolean loginMode = true;
    private boolean guestMode = false;

    private String gmailAddress;
    private String password;
    private String username;

    // This was a gmail regex I found: (\W|^)[\w.\-]{0,25}@(gmail)\.com(\W|$)
    //https://support.google.com/a/answer/1371417

    //Also added method to check minimum length of password
    //https://stackoverflow.com/questions/10518979/setting-a-minimum-maximum-character-count-for-any-character-using-a-regular-expr

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(TAG, "onCreate: login activity created");
        preferences = getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE);
        homeIntent = new Intent(this, HomeActivity.class);

        mAuth = FirebaseApplicationWWR.getAuthFactory().createAuthService();
        mAuth.register(this);
        mDb = (UsersDatabaseService) FirebaseApplicationWWR.getDatabaseServiceFactory().createDatabaseService(DatabaseService.Service.USERS);

        getConfiguredFields();
        checkLogin(preferences);
        hideName();
        FitnessServiceFactory.put(fitnessServiceKey, homeActivity -> new GoogleFitAdapter(homeActivity));
        signUpTvOnClickListener();
        loginBtnOnClickListener();
    }

    public void setFitnessServiceKey(String fitnessServiceKey) {
        this.fitnessServiceKey = fitnessServiceKey;
    }

    private void loginBtnOnClickListener() {
        loginBtn.setOnClickListener(view -> {
            username = nameEditText.getText().toString();
            password = passwordEditText.getText().toString();
            gmailAddress = gmailEditText.getText().toString();
            String btnText = loginBtn.getText().toString();
            Log.i(TAG, "loginBtnOnClickListener: button text is " + btnText);
            if(btnText.equals("Finish")) {
                launchHomeActivityFromGuestMode();
            } else if(btnText.equals("Sign Up")) {
                signUp();
            } else {
                logIn();
            }
        });
    }

    private void launchHomeActivityFromGuestMode() {
        Log.i(TAG, "launchHomeActivityFromGuestMode: valid height params found; launching home.");
        if (validateFeet() && validateInches()) {
            launchHome();
        }
    }

    private void signUp() {
        if (!validateSignUpInfo()) {
            Log.i(TAG, "Invalid sign up info entered");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        Log.i(TAG, "signUp: with email " + gmailAddress);
        mAuth.signUp(gmailAddress, password, username);
    }


    private void logIn() {
        if (!validateSignInInfo()) {
            Log.i(TAG, "Invalid sign in info entered");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        Log.i(TAG, "logIn: with email: " + gmailAddress);
        mAuth.signIn(gmailAddress, password);
    }

    private boolean validateSignUpInfo() {
        if(!Utils.isValidGmail(gmailAddress)) {
            Toast.makeText(this, INVALID_GMAIL_TOAST, Toast.LENGTH_LONG).show();
            return false;
        } else if(!checkValidPassword()) {
            Toast.makeText(this, INVALID_PASSWORD_TOAST, Toast.LENGTH_LONG).show();
            return false;
        } else if(username.equals("")) {
            Toast.makeText(this, "Please enter your name!", Toast.LENGTH_LONG).show();
            return false;
        } else if(!validateFeet() || !validateInches()) {
            Toast.makeText(this, "Please enter a valid height!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean validateSignInInfo() {
        if(gmailAddress.isEmpty()) {
            Toast.makeText(this, "Please enter an email!", Toast.LENGTH_LONG).show();
            return false;
        } else if(password.isEmpty()) {
            Toast.makeText(this, "Please enter your password!", Toast.LENGTH_LONG).show();
            return false;
        } else if(!validateFeet() || !validateInches()) {
            Toast.makeText(this, "Please enter a valid height!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void launchHome() {
        homeIntent.putExtra(HomeActivity.FITNESS_SERVICE_KEY, fitnessServiceKey)
                .putExtra(HomeActivity.HEIGHT_FT_KEY, feet)
                .putExtra(HomeActivity.HEIGHT_IN_KEY, inches);
        finish();
        startActivity(homeIntent);
    }

    private void signUpTvOnClickListener() {
        signUpTv.setOnClickListener(view -> {
            Log.i(TAG, "signUpTvOnClickListener: sign up click detected");
            if(loginMode) {
                signUpTv.setText(R.string.login_tv);
                loginBtn.setText(R.string.sign_up);
                showEmailPassword();
                showNameAndHeight();
                loginMode = false;
            } else {
                signUpTv.setText(R.string.sign_up_tv);
                loginBtn.setText(R.string.login);
                hideName();
                showEmailPassword();
                loginMode = true;
            }
        });
    }

    private void withoutLoginOnClickListener() {
        withoutLoginBtn.setOnClickListener(view -> {
            if(!guestMode) {
                withoutLoginBtn.setText(R.string.with_login);
                showHeight();
                hideEmailPasswordName();
                loginBtn.setText(R.string.finish_btn_text);
                loginBtn.setEnabled(false);
                feetEditText.setText("");
                inchesEditText.setText("");
                TextWatcher textWatcher = getTextWatcher();
                feetEditText.addTextChangedListener(textWatcher);
                inchesEditText.addTextChangedListener(textWatcher);
                guestMode = true;
            } else {
                withoutLoginBtn.setText(R.string.without_login);
                showEmailPassword();
                loginBtn.setText(R.string.login);
                loginBtn.setEnabled(true);
                guestMode = false;
            }
        });
    }

    private void getConfiguredFields() {
        withoutLoginBtn = findViewById(R.id.no_login_btn);
        gmailEditText = findViewById(R.id.enter_gmail_address);
        passwordEditText = findViewById(R.id.enter_password);
        promptTv = findViewById(R.id.tv_prompt);
        feetEditText = findViewById(R.id.et_height_feet);
        inchesEditText = findViewById(R.id.et_height_remainder_inches);
        feetTextView = findViewById(R.id.tv_feet);
        inchesTextView = findViewById(R.id.tv_height_remainder_inches);
        loginBtn = findViewById(R.id.btn_height_finish);
        nameEditText = findViewById(R.id.enter_username);
        signUpTv = findViewById(R.id.sign_up_tv);
        progressBar = findViewById(R.id.progressBar);
        withoutLoginBtn.setVisibility(View.GONE); // Guest mode not currently supported
    }

    private void showEmailPassword() {
        gmailEditText.setVisibility(View.VISIBLE);
        passwordEditText.setVisibility(View.VISIBLE);
        nameEditText.setVisibility(View.INVISIBLE);
    }

    private void hideEmailPasswordName() {
        gmailEditText.setVisibility(View.INVISIBLE);
        passwordEditText.setVisibility(View.INVISIBLE);
        nameEditText.setVisibility(View.INVISIBLE);
    }

    private void hideName() {
        nameEditText.setVisibility(View.INVISIBLE);
    }

    private void hideHeight() {
        promptTv.setVisibility(View.INVISIBLE);
        feetEditText.setVisibility(View.INVISIBLE);
        inchesEditText.setVisibility(View.INVISIBLE);
        feetTextView.setVisibility(View.INVISIBLE);
        inchesTextView.setVisibility(View.INVISIBLE);
    }

    private void showNameAndHeight() {
        nameEditText.setVisibility(View.VISIBLE);
        showHeight();
    }

    private void showHeight() {
        promptTv.setVisibility(View.VISIBLE);
        feetEditText.setVisibility(View.VISIBLE);
        inchesEditText.setVisibility(View.VISIBLE);
        feetTextView.setVisibility(View.VISIBLE);
        inchesTextView.setVisibility(View.VISIBLE);
    }

    private TextWatcher getTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                loginBtn.setEnabled(validateFeet() && validateInches());
            }
        };
    }

    private boolean checkHeight(SharedPreferences preferences) {
        Log.i(TAG, "checkHeight: checking preferences if height already exists");
        feet = preferences.getInt(HomeActivity.HEIGHT_FT_KEY, (int) INVALID_VAL);
        inches = preferences.getFloat(HomeActivity.HEIGHT_IN_KEY, INVALID_VAL);
        return feet > 0 && inches > -.00001 ;
    }

    private void checkLogin(SharedPreferences preferences) {
        if (checkHeight(preferences) && mAuth.isUserSignedIn()) {
            Log.i(TAG, "checkHeight: valid height in preferences already exists (feet: " + feet + ", inches: " + inches + ").");
            launchHome();
        }
    }

    private boolean validateInches() {
        try {
            inches = Float.parseFloat(inchesEditText.getText().toString());
        } catch (NumberFormatException e) {
            inches = INVALID_VAL;
        }
        return inches <= MAX_INCHES && inches >= 0;
    }

    private boolean validateFeet() {
        try {
            feet = Integer.parseInt(feetEditText.getText().toString());
        } catch (NumberFormatException e) {
            feet = (int) INVALID_VAL;
        }
        return feet <= MAX_FEET && feet > 0;
    }

    private boolean checkValidPassword() {
        return password.length() >= 6;
    }

    @Override
    public void onAuthSignInError(Auth.AuthError error) {
        String errorString = "";
        switch (error) {
            case DOES_NOT_EXIST:
                errorString = "user does not exist!";
                break;
            case INVALID_PASSWORD:
                errorString = "password is incorrect";
                break;
            case NETWORK_ERROR:
                errorString = "network error occurred";
                break;
            case OTHER:
                errorString = "unknown error occurred";
                break;
        }
        progressBar.setVisibility(View.INVISIBLE);
        Log.i(TAG, "onAuthSignInError: " + error.toString());
        Toast.makeText(this, errorString, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUserSignedUp(IUser user) {
        Log.i(TAG, "onUserSignedUp: user signed up");
        if(mAuth.isUserSignedIn()) {
            user.updateDisplayName(username);
            saveUserInfo(user);
            mDb.createUserInDatabase(user);
            launchHome();
        }
    }

    @Override
    public void onUserSignedIn(IUser user) {
        Log.i(TAG, "onUserSignedIn: user signed in");
        if (validateFeet() && validateInches() && mAuth.isUserSignedIn()) {
            saveUserInfo(user);
            launchHome();
        }
    }

    private void saveUserInfo(IUser user) {
        preferences.edit()
                .putString(IUser.USER_NAME_KEY, user.getDisplayName())
                .putString(IUser.EMAIL_KEY, user.getEmail())
                .putString(IUser.UID_KEY, user.getUid())
                .apply();
    }

    @Override
    public void onAuthSignUpError(Auth.AuthError error) {
        String errorString = "";
        switch (error) {
            case USER_COLLISION:
                errorString = "user already exists!";
                break;
            case NETWORK_ERROR:
                errorString = "network error occurred";
                break;
            case OTHER:
                errorString = "unknown error occurred";
                break;
        }
        progressBar.setVisibility(View.INVISIBLE);
        Log.i(TAG, "onAuthSignUpError: " + errorString);
        Toast.makeText(this, errorString, Toast.LENGTH_LONG).show();
    }
}