package com.cse110team24.walkwalkrevolution.mockedservices;

import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.users.UsersDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.users.UsersUserDataObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.users.UsersUserExistsObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.UsersDatabaseService;
import com.cse110team24.walkwalkrevolution.models.user.IUser;

import java.util.Map;

/**
 * Test implementation of {@link UsersDatabaseService}.
 */
public class TestUsersDatabaseService implements UsersDatabaseService {
    public UsersDatabaseServiceObserver observer;

    /**
     * Set this when testing if an invitation was sent, because the activity will first check if
     * the user being invited exists.
     *
     */
    public static IUser testOtherUser;

    /**
     * Set this when a user's data is being retrieved. This is called in order to check if the user has a teamUid
     * on the database.
     *
     * <p>Do testCurrentUserData.put("teamUid", [some test uid]) when sending an invitation after a team already exists.</p>
     * <p>Don't set teamUid or simply don't set this map when testing if a team will get created first.</p>
     */
    public static Map<String, Object> testCurrentUserData;

    /**
     * Set this to true when testing invitation sent. Set this to false when testing invitation errors.
     *
     * <p>If true, when {@link UsersDatabaseService#checkIfOtherUserExists(String)} is called,
     * then the {@link TestUsersDatabaseService} will call {@link UsersUserExistsObserver#onUserExists(IUser)} with
     * {@link TestUsersDatabaseService#testOtherUser} </p>
     *
     * <p>If false, {@link UsersUserExistsObserver#onUserDoesNotExist()} will be called.</p>
     */
    public static boolean testOtherUserExits;

    @Override
    public void createUserInDatabase(IUser user) {
    }

    @Override
    public void updateUserTeamUidInDatabase(IUser user, String teamUid) {

    }

    @Override
    public void getUserData(IUser user) {
        // TODO: 3/5/20 should call [observer].onUserData(Map<String, Object> data) with user's data
        ((UsersUserDataObserver) observer).onUserData(testCurrentUserData);
    }

    @Override
    public void checkIfOtherUserExists(String userDocumentKey) {
        /** TODO: 3/5/20 should call either
         *  [observer].onUserExists(testOtherUser)
         *  [observer].onUserDoesNotExist()
         */
        if (testOtherUserExits) {
            ((UsersUserExistsObserver) observer).onUserExists(testOtherUser);
        } else {
            ((UsersUserExistsObserver) observer).onUserDoesNotExist();
        }
    }

    @Override
    public void notifyObserversUserData(Map<String, Object> userDataMap) {

    }

    @Override
    public void notifyObserversIfUserExists(boolean exists, IUser otherUser) {

    }

    @Override
    public void register(UsersDatabaseServiceObserver usersDatabaseServiceObserver) {
        observer = usersDatabaseServiceObserver;
    }

    @Override
    public void deregister(UsersDatabaseServiceObserver usersDatabaseServiceObserver) {

    }
}