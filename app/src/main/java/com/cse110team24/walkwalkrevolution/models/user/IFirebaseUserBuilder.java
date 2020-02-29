package com.cse110team24.walkwalkrevolution.models.user;

import com.google.firebase.auth.FirebaseUser;

public interface IFirebaseUserBuilder extends IUserBuilder {
    IUserBuilder addFirebaseUser(FirebaseUser firebaseUser);
}
