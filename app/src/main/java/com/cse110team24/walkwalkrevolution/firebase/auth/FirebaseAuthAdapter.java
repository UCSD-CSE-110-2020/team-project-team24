package com.cse110team24.walkwalkrevolution.firebase.auth;

import android.app.Activity;
import android.util.Log;

import com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class FirebaseAuthAdapter implements AuthService {
    private static String TAG = "FirebaseAuthAdapter";
    private static String USER_COLLISION = "ERROR: a user with this email already exists";

    private FirebaseAuth mAuth;
    private FirebaseUserAdapter mUser;
    private Activity mActivity;
    private AuthError mError;

    public FirebaseAuthAdapter(Activity activity) {
        mAuth = FirebaseAuth.getInstance();
        mActivity = activity;
        mUser = new FirebaseUserAdapter(mAuth.getCurrentUser());
    }

    @Override
    public IUser signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(mActivity, task -> {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "onComplete: user sign-in successful");
                        mUser.setFirebaseUser(mAuth.getCurrentUser());
                    } else {
                        Log.e(TAG, "signUp: user sign-in failed", task.getException());
                        handleError(task);
                        mUser = null;
                    }
                });

        return mUser;
    }

    @Override
    public IUser signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(mActivity, task -> {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "signUp: user creation successful");
                        mUser.setFirebaseUser(mAuth.getCurrentUser());
                    } else {
                        Log.e(TAG, "signUp: user creation failed", task.getException());
                        handleError(task);
                        mUser = null;
                    }
                });
        return mUser;
    }

    private void handleError(Task<AuthResult> task) {
        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
            mError = AuthError.USER_COLLISION;
        } else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
            mError = AuthError.DOES_NOT_EXIST;
        } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
            mError = AuthError.INVALID_PASSWORD;
        } else {
            mError = AuthError.OTHER;
        }
    }

    @Override
    public IUser signUpWithName(String email, String password, String displayName) {
        signUp(email, password);
        if (isUserSignedIn()) {
            mUser.updateDisplayName(displayName);
        }
        return mUser;
    }

    @Override
    public IUser getUser() {
        return mUser;
    }

    @Override
    public boolean isUserSignedIn() {
        return mUser != null && mUser.firebaseUser() != null;
    }

    @Override
    public AuthError getAuthError() {
        return mError;
    }
}
