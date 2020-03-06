package com.cse110team24.walkwalkrevolution.firebase.auth;

import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.utils.Subject;

public interface AuthServiceSubject extends Subject<AuthServiceObserver> {
    /**
     * Notify this subject's observers that the user was signed in successfully
     * @param user the user that was signed in
     */
    void notifyObserversSignedIn(IUser user);

    /**
     * Notify this subject's observers that the user was signed up successfully
     * @param user the user that was signed up
     */
    void notifyObserversSignedUp(IUser user);

    /**
     * Notify this subject's observers that an AuthError was encountered during sign-in
     * @param error the error that was encountered
     */
    void notifyObserversSignInError(AuthService.AuthError error);

    /**
     * Notify this subject's observers that an AuthError was encountered during sign-up
     * @param error the error that was encountered
     */
    void notifyObserversSignUpError(AuthService.AuthError error);
}
