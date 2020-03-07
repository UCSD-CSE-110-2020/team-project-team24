package com.cse110team24.walkwalkrevolution.firebase.auth;

import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.utils.Subject;

public interface AuthSubject extends Subject<AuthObserver> {
    /**
     * Notify this subject's observers that the user was signed in successfully.
     * <p>See also: {@link AuthObserver#onUserSignedIn(IUser)}.</p>
     * @param user the user that was signed in
     */
    void notifyObserversSignedIn(IUser user);

    /**
     * Notify this subject's observers that the user was signed up successfully.
     * <p>See also: {@link AuthObserver#onUserSignedUp(IUser)}.</p>
     * @param user the user that was signed up
     */
    void notifyObserversSignedUp(IUser user);

    /**
     * Notify this subject's observers that an AuthError was encountered during sign-in.
     * <p>See also: {@link AuthObserver#onAuthSignInError(Auth.AuthError)}.</p>
     * @param error the error that was encountered
     */
    void notifyObserversSignInError(Auth.AuthError error);

    /**
     * Notify this subject's observers that an AuthError was encountered during sign-up
     * <p>See also: {@link AuthObserver#onAuthSignUpError(Auth.AuthError)}.</p>
     * @param error the error that was encountered
     */
    void notifyObserversSignUpError(Auth.AuthError error);
}
