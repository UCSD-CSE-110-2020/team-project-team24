package com.cse110team24.walkwalkrevolution.mockedservices;

import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseServiceFactory;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseServiceObserver;
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
    public UsersDatabaseServiceObserver observer;

    // TODO: 3/5/20 set these in your tests
    public static IUser testOtherUser;
    public static Map<String, Object> testCurrentUserData;
    public static boolean testOtherUserExits;

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
        observer.onUserData(testCurrentUserData);
    }

//    @Override
//    public void uploadRoute(String userDocumentKey, Route route) {
//
//    }
//
//    @Override
//    public void updateRoute(String userDocumentKey, Route route) {
//
//    }

    @Override
    public void checkIfOtherUserExists(String userDocumentKey) {
        /** TODO: 3/5/20 should call either
         *  [observer].onUserExists(testOtherUser)
         *  [observer].onUserDoesNotExist()
         */
        if (testOtherUserExits) {
            observer.onUserExists(testOtherUser);
        } else {
            observer.onUserDoesNotExist();
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