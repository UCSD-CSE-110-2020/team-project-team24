package com.cse110team24.walkwalkrevolution.Firebase.auth;

import com.cse110team24.walkwalkrevolution.models.user.IUser;

public interface AuthService {
    IUser signIn(String email, String password);
    IUser signUp(String email, String password);
    boolean isUserSignedIn();
}
