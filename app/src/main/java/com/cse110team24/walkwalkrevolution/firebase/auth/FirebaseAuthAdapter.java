package com.cse110team24.walkwalkrevolution.firebase.auth;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter.FirebaseUserAdapterBuilder;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class FirebaseAuthAdapter implements AuthService, FirebaseAuth.AuthStateListener {
    private static String TAG = "FirebaseAuthAdapter";

    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseUserAdapterBuilder mUserAdapterBuilder;
    private Activity mActivity;
    private AuthError mAuthError;

    private boolean signUp;

    private List<AuthServiceObserver> observers;

    public FirebaseAuthAdapter(Activity activity) {
        mAuth = FirebaseAuth.getInstance();
        mActivity = activity;
        mFirebaseUser = mAuth.getCurrentUser();
        mUserAdapterBuilder = new FirebaseUserAdapterBuilder();
        mAuth.addAuthStateListener(this);
        observers = new ArrayList<>();
    }

    @Override
    public void signIn(String email, String password) {
        Log.i(TAG, "signIn: beginning sign in process");
        signUp = false;
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(mActivity, task -> {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "onComplete: user sign-in successful");
                        buildUserEssentials(email);
                    } else {
                        Log.e(TAG, "signUp: user sign-in failed", task.getException());
                        detectErrorType(task);
                        notifyObserversSignInError();
                    }
                });
    }

    @Override
    public void signUp(String email, String password) {
        Log.i(TAG, "signUp: beginning sign up process");
        signUp = true;
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(mActivity, task -> {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "signUp: user creation successful");
                        buildUserEssentials(email);
                    } else {
                        Log.e(TAG, "signUp: user creation failed", task.getException());
                        detectErrorType(task);
                        notifyObserversSignUpError();
                    }
                });
    }

    @Override
    public IUser getUser() {
        return mUserAdapterBuilder.build();
    }

    @Override
    public boolean isUserSignedIn() {
        return mFirebaseUser != null;
    }

    @Override
    public AuthError getAuthError() {
        return mAuthError;
    }

    private void detectErrorType(Task<AuthResult> task) {
        Exception exception = task.getException();
        if (exception instanceof FirebaseAuthUserCollisionException) {
            mAuthError = AuthError.USER_COLLISION;
        } else if (exception instanceof FirebaseAuthInvalidUserException) {
            mAuthError = AuthError.DOES_NOT_EXIST;
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            mAuthError = AuthError.INVALID_PASSWORD;
        } else if (exception instanceof FirebaseNetworkException){
            mAuthError = AuthError.NETWORK_ERROR;
        }
        else {
            mAuthError = AuthError.OTHER;
        }
    }

    private void buildUserEssentials(String email) {
        mUserAdapterBuilder.addEmail(email);
        mFirebaseUser = mAuth.getCurrentUser();
        mUserAdapterBuilder.addFirebaseUser(mFirebaseUser);
        mUserAdapterBuilder.addUid(mFirebaseUser.getUid());
    }

    @Override
    public void register(AuthServiceObserver authServiceObserver) {
        observers.add(authServiceObserver);
    }

    @Override
    public void deregister(AuthServiceObserver authServiceObserver) {
        observers.remove(authServiceObserver);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        mFirebaseUser = firebaseAuth.getCurrentUser();
        if (isUserSignedIn()) {
            buildUserEssentials(mFirebaseUser.getEmail());
        }

        if (signUp) {
            notifyObserversSignedUp();
        } else {
            notifyObserversSignedIn();
        }
    }


    private void notifyObserversSignedIn() {
        observers.forEach(observer -> {
            observer.onUserSignedIn(mUserAdapterBuilder.build());
        });
    }

    private void notifyObserversSignedUp() {
        observers.forEach(observer -> {
            observer.onUserSignedUp(mUserAdapterBuilder.build());
        });
    }

    private void notifyObserversSignInError() {
        observers.forEach(observer -> {
            observer.onAuthSignInError(mAuthError);
        });
    }

    private void notifyObserversSignUpError() {
        observers.forEach(observer -> {
            observer.onAuthSignUpError(mAuthError);
        });
    }
}
