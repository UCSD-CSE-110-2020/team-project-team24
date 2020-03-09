package com.cse110team24.walkwalkrevolution.firebase.firestore.observers;

import com.cse110team24.walkwalkrevolution.models.user.IUser;

import java.util.Map;

public interface UsersDatabaseServiceObserver {

    /**
     * Called by the UsersDatabaseServiceSubject this observer is observing when the requested user
     * exists in the subject's provider database.
     * <p>See also: {@link com.cse110team24.walkwalkrevolution.firebase.firestore.subjects.UsersDatabaseServiceSubject#notifyObserversIfUserExists(boolean, IUser)}</p>
     * @param otherUser the user whose existence was checked, if they exist.
     */
    void onUserExists(IUser otherUser);

    /**
     * Called by the UsersDatabaseServiceSubject this observer is observing when the requested user
     * does not exist in the subject's provider database.
     * <p>See also: {@link com.cse110team24.walkwalkrevolution.firebase.firestore.subjects.UsersDatabaseServiceSubject#notifyObserversIfUserExists(boolean, IUser)}</p>
     */
    void onUserDoesNotExist();
}
