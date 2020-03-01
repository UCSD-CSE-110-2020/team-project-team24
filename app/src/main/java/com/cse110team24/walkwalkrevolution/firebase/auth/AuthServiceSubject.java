package com.cse110team24.walkwalkrevolution.firebase.auth;

import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.utils.Subject;

public interface AuthServiceSubject extends Subject<AuthServiceObserver> {
    void notifyObserversSignedIn(IUser user);
    void notifyObserversSignedUp(IUser user);
    void notifyObserversSignInError(AuthError error);
    void notifyObserversSignUpError(AuthError error);
}
