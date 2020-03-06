package com.cse110team24.walkwalkrevolution.firebase.auth;

import com.cse110team24.walkwalkrevolution.models.user.IUser;

/**
 * Handles user authentication given the implementing type's provider
 */
public interface AuthService extends AuthServiceSubject {
    /**
     * Signs in a user to this service's provider, given email and password. <p>On success, a call to
     * {@link AuthServiceSubject#notifyObserversSignedIn(IUser)} is made with the sign in user</p>. <p>On
     * failure, a call to {@link AuthServiceSubject#notifyObserversSignInError(AuthService.AuthError)} is made
     * with the AuthError that occurred.</p>
     * @param email the existing user's email
     * @param password the existing user's password
     */
    void signIn(String email, String password);

    /**
     * Signs up a user to this service's provider given email, password, and display name and automatically
     * signs the user into the provider. <p>On success, a call to {@link AuthServiceSubject#notifyObserversSignedUp(IUser)}
     * is made with the sign in user.</p> <p>On failure, a call to {@link AuthServiceSubject#notifyObserversSignUpError(AuthService.AuthError)}
     * is made with the AuthError that occurred.</p>
     * @param email the new user's email
     * @param password the new user's password
     * @param displayName the new user's displayName
     */
    void signUp(String email, String password, String displayName);

    /**
     * @return the currently signed in user
     */
    IUser getUser();

    /**
     * @return the last AuthError that occurred during sign-in or sign-up
     */
    AuthError getAuthError();

    /**
     *
     * @return true if a user is currently signed in to this service's provider
     */
    boolean isUserSignedIn();

    /**
     * Various errors that can occur during authentication
     */
    enum AuthError {
        USER_COLLISION,
        DOES_NOT_EXIST,
        INVALID_PASSWORD,
        NETWORK_ERROR,
        OTHER
    }
}
