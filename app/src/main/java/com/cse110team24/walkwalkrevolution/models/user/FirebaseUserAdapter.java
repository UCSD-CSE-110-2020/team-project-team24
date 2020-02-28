package com.cse110team24.walkwalkrevolution.models.user;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class FirebaseUserAdapter implements IUser {
    private static final String TAG = "FirebaseUserAdapter";
    private FirebaseUser mFirebaseUser;

    public FirebaseUserAdapter(FirebaseUser firebaseUser) {
        mFirebaseUser = firebaseUser;
    }

    public void setFirebaseUser(FirebaseUser firebaseUser) {
        mFirebaseUser = firebaseUser;
    }

    public FirebaseUser getFirebaseUser() {
        return mFirebaseUser;
    }

    @Override
    public String getDisplayName() {
        return mFirebaseUser.getDisplayName();
    }

    @Override
    public String getEmail() {
        return mFirebaseUser.getEmail();
    }

    @Override
    public String getUid() {
        return mFirebaseUser.getUid();
    }

    @Override
    public void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void updateDisplayName(String name) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        mFirebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "updateDisplayName: display name update successful");
                    } else {
                        Log.e(TAG, "updateDisplayName: could not update display name", task.getException());
                    }
                });
    }

}
