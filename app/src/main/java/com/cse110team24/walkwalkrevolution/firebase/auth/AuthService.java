package com.cse110team24.walkwalkrevolution.firebase.auth;

import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.observer.Subject;

public interface AuthService extends Subject<AuthServiceObserver> {
    void signIn(String email, String password);
    void signUp(String email, String password);
    IUser getUser();
    AuthError getAuthError();
    boolean isUserSignedIn();
}
