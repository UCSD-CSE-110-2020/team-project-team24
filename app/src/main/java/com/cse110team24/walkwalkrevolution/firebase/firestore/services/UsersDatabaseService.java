package com.cse110team24.walkwalkrevolution.firebase.firestore.services;

import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.subjects.UsersDatabaseServiceSubject;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.firebase.firestore.DocumentReference;

import java.util.Map;

/**
 * Handles provider database interactions with user-related documents and collections.
 */
public interface UsersDatabaseService extends UsersDatabaseServiceSubject, DatabaseService {
    /**
     * Create a document in this service's provider database.
     * @param user the signed in user whose document is being created. User must have a defined email.
     */
    void createUserInDatabase(IUser user);

    /**
     * Update an existing user document's teamUid field in this service's provider database
     * @param user the currently signed in user, who already has an existing user document. User must
     *             have a defined email.
     * @param teamUid the teamUid that will be set to the document's field
     */
    void updateUserTeamUidInDatabase(IUser user, String teamUid);

    /**
     * Query this service's provider database for the given user's data. On success, a call to
     * {@link UsersDatabaseServiceSubject#notifyObserversUserData(Map) is made with the requested user's data}.
     *
     * @param user the user whose data is being requested. User must have a defined email.
     */
    void getUserData(IUser user);

    /**
     * Query this service's provider database for a user given userDocumentKey. If user exists, a call to
     * {@link UsersDatabaseServiceSubject#notifyObserversIfUserExists(boolean, IUser)} with value true
     * and the requested user's data, otherwise, with value false and null user data.
     * @param userDocumentKey the requested user's documentKey. Must be in the form of a an email with
     *                        non-alphanumeric characters removed.
     */
    void checkIfOtherUserExists(String userDocumentKey);
}
