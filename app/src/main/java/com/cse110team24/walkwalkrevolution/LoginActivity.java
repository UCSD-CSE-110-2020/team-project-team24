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
import android.widget.TextView;
import android.widget.Toast;

import com.cse110team24.walkwalkrevolution.firebase.auth.AuthServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.FirebaseFirestoreAdapter;
import com.cse110team24.walkwalkrevolution.firebase.auth.FirebaseAuthAdapter;
import com.cse110team24.walkwalkrevolution.firebase.auth.AuthError;

import com.cse110team24.walkwalkrevolution.fitness.FitnessServiceFactory;
import com.cse110team24.walkwalkrevolution.fitness.GoogleFitAdapter;
import com.cse110team24.walkwalkrevolution.models.user.IUser;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements AuthServiceObserver {
    private static final String TAG = "LoginActivity";
    private static final String INVALID_GMAIL_TOAST = "Please enter a valid gmail address!";
    private static final String INVALID_PASSWORD_TOAST = "Please enter a password at least 6 characters long!";

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
    private FirebaseAuthAdapter mAuth;
    private IUser mUser;
    private FirebaseFirestoreAdapter firebaseFirestore;

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

        final SharedPreferences preferences = getSharedPreferences(HomeActivity.HEIGHT_PREF, Context.MODE_PRIVATE);
        homeIntent = new Intent(this, HomeActivity.class);

        getConfiguredFields();
        // TODO: replace it with checkLogin()
        //checkHeight(preferences);
        hideNameAndHeight();
        FitnessServiceFactory.put(fitnessServiceKey, homeActivity -> new GoogleFitAdapter(homeActivity));
        signUpTvOnClickListener();
        withoutLoginOnClickListener();
        loginBtnOnClickListener();
    }

    public void setFitnessServiceKey(String fitnessServiceKey) {
        this.fitnessServiceKey = fitnessServiceKey;
    }

    private void loginBtnOnClickListener() {
        loginBtn.setOnClickListener(view -> {
            String btnText = loginBtn.getText().toString();
            Log.i(TAG, "loginBtnOnClickListener: button text is " + btnText);
            if(btnText.equals("Finish")) {
                launchHomeActivityFromGuestMode();
            } else if(btnText.equals("Sign Up")) {
                launchHomeActivityFromSignUp();
            } else {
                launchHomeActivityFromLogIn();
            }
        });
    }

    private void signUp() {
        mAuth = new FirebaseAuthAdapter(LoginActivity.this);
        mAuth.register(LoginActivity.this);
        Log.i(TAG, "signUp: with email " + gmailAddress);
        mUser = mAuth.signUp(gmailAddress, password);
    }


    private void logIn() {
        mAuth = new FirebaseAuthAdapter(LoginActivity.this);
        mAuth.register(LoginActivity.this);
        Log.i(TAG, "logIn: with email: " + gmailAddress);
        mUser = mAuth.signIn(gmailAddress, password);
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
                hideNameAndHeight();
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
                hideHeight();
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

    private void hideNameAndHeight() {
        nameEditText.setVisibility(View.INVISIBLE);
        hideHeight();
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

    private void launchHomeActivityFromGuestMode() {
        Log.i(TAG, "launchHomeActivityFromGuestMode: valid height params found; launching home.");
        homeIntent.putExtra(HomeActivity.FITNESS_SERVICE_KEY, fitnessServiceKey)
                    .putExtra(HomeActivity.HEIGHT_FT_KEY, feet)
                    .putExtra(HomeActivity.HEIGHT_IN_KEY, inches);
        finish();
        startActivity(homeIntent);
    }

    private void launchHomeActivityFromSignUp() {
        username = nameEditText.getText().toString();
        password = passwordEditText.getText().toString();

        if(!checkForGmailAddress()) {
            Toast.makeText(this, INVALID_GMAIL_TOAST, Toast.LENGTH_LONG).show();
            return;
        } else if(!checkValidPassword()) {
            Toast.makeText(this, INVALID_PASSWORD_TOAST, Toast.LENGTH_LONG).show();
            return;
        } else if(username.equals("")) {
            Toast.makeText(this, "Please enter your name!", Toast.LENGTH_LONG).show();
            return;
        } else if(!validateFeet() || !validateInches()) {
            Toast.makeText(this, "Please enter a valid height!", Toast.LENGTH_LONG).show();
            return;
        }
        signUp();
    }

    private void launchHomeActivityFromLogIn() {
        if(!checkForGmailAddress()) {
            Toast.makeText(this, INVALID_GMAIL_TOAST, Toast.LENGTH_LONG).show();
            return;
        } else if(!checkValidPassword()) {
            Toast.makeText(this, INVALID_PASSWORD_TOAST, Toast.LENGTH_LONG).show();
            return;
        }
        logIn();
    }

    private void checkHeight(SharedPreferences preferences) {
        Log.i(TAG, "checkHeight: checking preferences if height already exists");
        feet = preferences.getInt(HomeActivity.HEIGHT_FT_KEY, (int) INVALID_VAL);
        inches = preferences.getFloat(HomeActivity.HEIGHT_IN_KEY, INVALID_VAL);
        if (feet > 0 && inches > 0) {
            Log.i(TAG, "checkHeight: valid height in preferences already exists (feet: " + feet + ", inches: " + inches + ").");
            launchHomeActivityFromGuestMode();
        }
    }

    private boolean validateInches() {
        try {
            inches = Float.parseFloat(inchesEditText.getText().toString());
        } catch (NumberFormatException e) {
            inches = INVALID_VAL;
        }
        return inches <= MAX_INCHES && inches > 0;
    }

    private boolean validateFeet() {
        try {
            feet = Integer.parseInt(feetEditText.getText().toString());
        } catch (NumberFormatException e) {
            feet = (int) INVALID_VAL;
        }
        return feet <= MAX_FEET && feet > 0;
    }

    private boolean checkForGmailAddress() {
        gmailAddress = gmailEditText.getText().toString();
        Log.i(TAG, "checkForGmailAddress: " + gmailAddress);
        return Pattern.matches("(\\W|^)[\\w.\\-]{0,25}@(gmail)\\.com(\\W|$)", gmailAddress);
    }

    private boolean checkValidPassword() {
        password = passwordEditText.getText().toString();
        Log.i(TAG, "checkValidPassword: " + password);
        return Pattern.matches("^[:;,\\-@0-9a-zA-Zâéè'.\\s]{6,}$", password);
    }

    @Override
    public void onAuthSignInError(AuthError error) {
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
        Toast.makeText(this, errorString, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUserSignedUp(IUser user) {
        if(mAuth.isUserSignedIn()) {
            firebaseFirestore = new FirebaseFirestoreAdapter();
            user.updateDisplayName(username);
            firebaseFirestore.createUserInDatabase(user);
        }
        homeIntent.putExtra(HomeActivity.FITNESS_SERVICE_KEY, fitnessServiceKey);
        finish();
        startActivity(homeIntent);
    }

    @Override
    public void onUserSignedIn(IUser user) {

    }

    @Override
    public void onAuthSignUpError(AuthError error) {
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
        Toast.makeText(this, errorString, Toast.LENGTH_LONG).show();
    }
}