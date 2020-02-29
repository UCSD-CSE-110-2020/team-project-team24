package com.cse110team24.walkwalkrevolution.Firebase.auth;

import android.app.Activity;
import android.util.Log;

import com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthAdapter implements AuthService {
    private static String TAG = "FirebaseAuthAdapter";

    private FirebaseAuth mAuth;
    private FirebaseUserAdapter mUser;
    private Activity mActivity;

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
                        Log.i(TAG, "onComplete: user creation successful");
                        mUser.setFirebaseUser(mAuth.getCurrentUser());
                    } else {
                        Log.e(TAG, "signUp: user creation failed", task.getException());
                        mUser = null;
                    }
                });
        return mUser;
    }

    @Override
    public IUser signUp(String displayName, String email, String password) {
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
        return mUser != null || mUser.getFirebaseUser() != null;
    }
}
