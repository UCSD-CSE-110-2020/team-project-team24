package com.cse110team24.walkwalkrevolution.firebase.auth;

import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.observer.Subject;

public interface AuthService extends Subject<AuthServiceObserver> {
    IUser signIn(String email, String password);
    IUser signUp(String email, String password);
    IUser signUpWithName(String email, String password, String displayName);
    IUser getUser();
    AuthError getAuthError();
    boolean isUserSignedIn();
}
