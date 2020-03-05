package com.cse110team24.walkwalkrevolution;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cse110team24.walkwalkrevolution.firebase.auth.AuthService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowToast;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class LoginActivityUnitTest extends TestInjection {
    private LoginActivity testActivity;
    private Button finishBtn;
    private Button signInAsGuestBtn;
    private TextView signUpTV;
    private EditText feetEt;
    private EditText inchesEt;
    private EditText gmail;
    private EditText username;
    private EditText password;


    private static final String TOAST_MSG_NO_EMAIL = "Please enter an email!";
    private static final String TOAST_MSG_NO_PASSWORD = "Please enter your password!";
    private static final String TOAST_MSG_NOT_GMAIL = "Please enter a valid gmail address!";
    private static final String TOAST_MSG_NO_USERNAME = "Please enter your name!";

    private static final String TOAST_MSG_NO_HEIGHT = "Please enter a valid height!";

    @Before
    public void setup() {
        super.setup();


        ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class);
        scenario.onActivity(activity -> {
            testActivity = activity;
            finishBtn = testActivity.findViewById(R.id.btn_height_finish);
            feetEt = testActivity.findViewById(R.id.et_height_feet);
            inchesEt = testActivity.findViewById(R.id.et_height_remainder_inches);
            gmail = testActivity.findViewById(R.id.enter_gmail_address);
            password = testActivity.findViewById(R.id.enter_password);
            signUpTV = testActivity.findViewById(R.id.sign_up_tv);
            username = testActivity.findViewById(R.id.enter_username);
            signInAsGuestBtn = testActivity.findViewById(R.id.no_login_btn);
        });
    }

    @Test
    public void loginWithoutHeight() {
        gmail.setText("amber@gmail.com");
        password.setText("testpw");
        finishBtn.performClick();
        assertEquals(ShadowToast.getTextOfLatestToast(), TOAST_MSG_NO_HEIGHT);
    }

    @Test
    public void loginWithoutEmail() {
        feetEt.setText("5");
        inchesEt.setText("3");
        finishBtn.performClick();
        assertEquals(ShadowToast.getTextOfLatestToast(), TOAST_MSG_NO_EMAIL);
    }

    @Test
    public void loginWithoutPassword() {
        feetEt.setText("5");
        inchesEt.setText("3");
        gmail.setText("amber@gmail.com");
        finishBtn.performClick();
        assertEquals(ShadowToast.getTextOfLatestToast(),TOAST_MSG_NO_PASSWORD);
    }

    @Test
    public void loginWithoutGmailAddress() {
        feetEt.setText("5");
        inchesEt.setText("3");
        gmail.setText("amber@yahoo.com");
        password.setText("testpw");
        finishBtn.performClick();
        when(mAuth.getAuthError()).thenReturn(AuthService.AuthError.DOES_NOT_EXIST);
        when(mAuth.isUserSignedIn()).thenReturn(false);
    }

    @Test
    public void signUpWithoutGmailAddress() {
        signUpTV.performClick();
        feetEt.setText("5");
        inchesEt.setText("3");
        gmail.setText("amber@yahoo.com");
        password.setText("testpw");
        username.setText("Cheery");
        finishBtn.performClick();
        assertEquals(ShadowToast.getTextOfLatestToast(), TOAST_MSG_NOT_GMAIL);
    }

    @Test
    public void signUpWithoutUsername() {
        signUpTV.performClick();
        feetEt.setText("5");
        inchesEt.setText("3");
        gmail.setText("amber@gmail.com");
        password.setText("testpw");
        finishBtn.performClick();
        assertEquals(ShadowToast.getTextOfLatestToast(), TOAST_MSG_NO_USERNAME);
    }

    @Test
    public void userCollision() {
        mAuth.signUp("amber@gmail.com", "testpw", "Cheery");
        signUpTV.performClick();
        feetEt.setText("5");
        inchesEt.setText("3");
        gmail.setText("amber@gmail.com");
        password.setText("testpw");
        username.setText("Cheery");
        finishBtn.performClick();
        when(mAuth.getAuthError()).thenReturn(AuthService.AuthError.USER_COLLISION);
        when(mAuth.isUserSignedIn()).thenReturn(false);
    }

//    @Test
//    public void signInAsGuest() {
//        signInAsGuestBtn.performClick();
//        feetEt.setText("5");
//        inchesEt.setText("3");
//        finishBtn.performClick();
//        Intent intent = Shadows.shadowOf(testActivity).peekNextStartedActivity();
//        assertEquals(HomeActivity.class.getCanonicalName(), intent.getComponent().getClassName());
//    }

    @Test
    public void invalidPassword() {
        mAuth.signUp("amber@gmail.com", "testpw", "Cheery");
        feetEt.setText("5");
        inchesEt.setText("3");
        gmail.setText("amber@gmail.com");
        password.setText("password");
        finishBtn.performClick();
        when(mAuth.getAuthError()).thenReturn(AuthService.AuthError.INVALID_PASSWORD);
        when(mAuth.isUserSignedIn()).thenReturn(false);
    }

    @Test
    public void loginSuccess() {
        mAuth.signUp("amber@gmail.com", "testpw", "Cheery");
        feetEt.setText("5");
        inchesEt.setText("3");
        gmail.setText("amber@gmail.com");
        password.setText("testpw");
        finishBtn.performClick();
        when(mAuth.isUserSignedIn()).thenReturn(true);
    }

    @Test
    public void signUpSuccess() {
        signUpTV.performClick();
        feetEt.setText("5");
        inchesEt.setText("3");
        gmail.setText("amber@gmail.com");
        password.setText("testpw");
        username.setText("Cheery");
        finishBtn.performClick();
        when(mAuth.isUserSignedIn()).thenReturn(true);
    }
}
