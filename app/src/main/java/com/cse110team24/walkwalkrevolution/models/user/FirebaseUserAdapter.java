package com.cse110team24.walkwalkrevolution.models.user;

import com.google.firebase.auth.FirebaseUser;

public class FirebaseUserAdapter implements IUser {
    FirebaseUser mUser;

    @Override
    public String getName() {
        return mUser.getDisplayName();
    }

    @Override
    public String getEmail() {
        return mUser.getEmail();
    }
}
