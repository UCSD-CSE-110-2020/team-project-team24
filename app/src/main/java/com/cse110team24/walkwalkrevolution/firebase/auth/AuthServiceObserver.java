package com.cse110team24.walkwalkrevolution.firebase.auth;

import com.cse110team24.walkwalkrevolution.models.user.IUser;

public interface AuthServiceObserver {

    /**
     * Called when the AuthServiceSubject this observer is observing signs in the user
     * see also: {@link AuthServiceSubject#notifyObserversSignedIn(IUser)}
     * @param user the user that was signed in
     */
    void onUserSignedIn(IUser user);

    /**
     * Called when the AuthServiceSubject this observer is observing signs up the user
     * see also: {@link AuthServiceSubject#notifyObserversSignedUp(IUser)}
     * @param user the user that was signed up
     */
    void onUserSignedUp(IUser user);

    /**
     * Called when the AuthServiceSubject this observer is observing fails to sign in the user
     * see also: {@link AuthServiceSubject#notifyObserversSignInError(AuthService.AuthError)}
     * @param error the error that was encountered during sign in
     */
    void onAuthSignInError(AuthService.AuthError error);

    /**
     * Called when the AuthServiceSubject this observer is observing fails to sign up the user
     * see also: {@link AuthServiceSubject#notifyObserversSignUpError(AuthService.AuthError)}
     * @param error the error that was encountered during sign up
     */
    void onAuthSignUpError(AuthService.AuthError error);
}
