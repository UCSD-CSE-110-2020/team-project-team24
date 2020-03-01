package com.cse110team24.walkwalkrevolution.firebase.auth;

import com.cse110team24.walkwalkrevolution.models.user.IUser;

public interface AuthServiceObserver {

    void onUserSignedIn(IUser user);
    void onUserSignedUp(IUser user);
    void onAuthSignInError(AuthError error);
    void onAuthSignUpError(AuthError error);
}
