package com.cse110team24.walkwalkrevolution.mockedservices;

import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseServiceFactory;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.UsersDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.UsersDatabaseService;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.firebase.firestore.DocumentReference;

import java.util.Map;

/**
 * TODO - for the observer methods that should be called, you call the notify methods manually
 */
public class TestUsersDatabaseService implements UsersDatabaseService {

    public static IUser testOtherUser;
    public static boolean testOtherUserExists;
    public static Map<String, Object> testCurrentUserData;

    @Override
    public void createUserInDatabase(IUser user) {
    }

    @Override
    public void updateUserTeamUidInDatabase(IUser user, String teamUid) {

    }

    @Override
    public DocumentReference addUserMessagingRegistrationToken(IUser user, String token) {
        return null;
    }

    @Override
    public void getUserData(IUser user) {
        // TODO: 3/5/20 should call [observer].onUserData(Map<String, Object> data) with user's data
    }

    @Override
    public void uploadRoute(String userDocumentKey, Route route) {

    }

    @Override
    public void updateRoute(String userDocumentKey, Route route) {

    }

    @Override
    public void checkIfOtherUserExists(String userDocumentKey) {
        /** TODO: 3/5/20 should call either
         *  [observer].onUserExists(testOtherUser)
         *  [observer].onUserDoesNotExist()
         */
    }

    @Override
    public void notifyObserversUserData(Map<String, Object> userDataMap) {

    }

    @Override
    public void notifyObserversIfUserExists(boolean exists, IUser otherUser) {

    }

    @Override
    public void register(UsersDatabaseServiceObserver usersDatabaseServiceObserver) {

    }

    @Override
    public void deregister(UsersDatabaseServiceObserver usersDatabaseServiceObserver) {

    }
}
