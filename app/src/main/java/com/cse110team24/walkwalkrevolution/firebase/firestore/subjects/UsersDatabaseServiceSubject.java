package com.cse110team24.walkwalkrevolution.firebase.firestore.subjects;

import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.UsersDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.utils.Subject;

import java.util.Map;

public interface UsersDatabaseServiceSubject extends Subject<UsersDatabaseServiceObserver> {
    /**
     * Notify this subject's observers that the user data being requested is ready for reading.
     * <p>See also: {@link UsersDatabaseServiceObserver#onUserData(Map)}.</p>
     * @param userDataMap the user data being requested
     */
    void notifyObserversUserData(Map<String, Object> userDataMap);

    /**
     * Notify this subject's observers whether a user exists in the database.
     * <p>See also: {@link UsersDatabaseServiceObserver#onUserExists(IUser)}} and {@link UsersDatabaseServiceObserver#onUserDoesNotExist()}</p>
     * @param exists true if the user exists in the database
     * @param otherUser the user whose existence was checked, if they exist.
     */
    void notifyObserversIfUserExists(boolean exists, IUser otherUser);
}
