package com.cse110team24.walkwalkrevolution.firebase.auth;

import com.cse110team24.walkwalkrevolution.models.user.IUser;

public interface AuthObserver {

    /**
     * Called when the AuthServiceSubject this observer is observing signs in the user
     * <p>See also: {@link AuthSubject#notifyObserversSignedIn(IUser)}</p>
     * @param user the user that was signed in
     */
    void onUserSignedIn(IUser user);

    /**
     * Called when the AuthServiceSubject this observer is observing signs up the user
     * <p>See also: {@link AuthSubject#notifyObserversSignedUp(IUser)}</p>
     * @param user the user that was signed up
     */
    void onUserSignedUp(IUser user);

    /**
     * Called when the AuthServiceSubject this observer is observing fails to sign in the user
     * <p>See also: {@link AuthSubject#notifyObserversSignInError(Auth.AuthError)}</p>
     * @param error the error that was encountered during sign in
     */
    void onAuthSignInError(Auth.AuthError error);

    /**
     * Called when the AuthServiceSubject this observer is observing fails to sign up the user
     * <p>See also: {@link AuthSubject#notifyObserversSignUpError(Auth.AuthError)}</p>
     * @param error the error that was encountered during sign up
     */
    void onAuthSignUpError(Auth.AuthError error);
}
