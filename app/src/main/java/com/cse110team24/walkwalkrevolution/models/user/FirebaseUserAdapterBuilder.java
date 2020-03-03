package com.cse110team24.walkwalkrevolution.models.user;

import com.google.firebase.auth.FirebaseUser;

public interface FirebaseUserAdapterBuilder extends UserBuilder {
    UserBuilder addFirebaseUser(FirebaseUser firebaseUser);
}
