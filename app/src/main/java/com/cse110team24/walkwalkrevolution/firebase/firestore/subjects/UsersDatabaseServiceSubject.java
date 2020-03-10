package com.cse110team24.walkwalkrevolution.firebase.firestore.subjects;

import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.users.UsersDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.utils.Subject;

import java.util.Map;

public interface UsersDatabaseServiceSubject extends Subject<UsersDatabaseServiceObserver> {
    /**
     * Notify this subject's observers of type {@link com.cse110team24.walkwalkrevolution.firebase.firestore.observers.users.UsersUserDataObserver}
     * that the user data being requested is ready for reading.
     * <p>See also: {@link com.cse110team24.walkwalkrevolution.firebase.firestore.observers.users.UsersUserDataObserver#onUserData(Map)}.</p>
     * @param userDataMap the user data being requested
     */
    void notifyObserversUserData(Map<String, Object> userDataMap);

    /**
     * Notify this subject's observers of type {@link com.cse110team24.walkwalkrevolution.firebase.firestore.observers.users.UsersUserExistsObserver}
     * whether a user exists in the database.
     * <p>See also: {@link com.cse110team24.walkwalkrevolution.firebase.firestore.observers.users.UsersUserExistsObserver#onUserExists(IUser)}}
     * and {@link com.cse110team24.walkwalkrevolution.firebase.firestore.observers.users.UsersUserExistsObserver#onUserDoesNotExist()}</p>
     * @param exists true if the user exists in the database
     * @param otherUser the user whose existence was checked, if they exist.
     */
    void notifyObserversIfUserExists(boolean exists, IUser otherUser);
}
