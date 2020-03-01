package com.cse110team24.walkwalkrevolution.firebase.auth;

import com.cse110team24.walkwalkrevolution.models.user.IUser;

public interface AuthService extends AuthServiceSubject {
    void signIn(String email, String password);
    void signUp(String email, String password);
    IUser getUser();
    AuthError getAuthError();
    boolean isUserSignedIn();
}
