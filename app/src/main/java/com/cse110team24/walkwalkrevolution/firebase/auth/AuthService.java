package com.cse110team24.walkwalkrevolution.firebase.auth;

import com.cse110team24.walkwalkrevolution.models.user.IUser;

public interface AuthService {
    IUser signIn(String email, String password);
    IUser signUp(String email, String password);
    IUser signUpWithName(String email, String password, String displayName);
    IUser getUser();
    AuthError getAuthError();
    boolean isUserSignedIn();
}
