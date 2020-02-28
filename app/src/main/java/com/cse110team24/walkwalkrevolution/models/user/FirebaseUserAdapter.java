package com.cse110team24.walkwalkrevolution.models.user;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseUserAdapter implements IUser {
    private FirebaseUser mFirebaseUser;

    public void setFirebaseUser(FirebaseUser firebaseUser) {
        mFirebaseUser = firebaseUser;
    }

    public FirebaseUser getFirebaseUser() {
        return mFirebaseUser;
    }

    @Override
    public String getName() {
        return mFirebaseUser.getDisplayName();
    }

    @Override
    public String getEmail() {
        return mFirebaseUser.getEmail();
    }

    @Override
    public void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

}
