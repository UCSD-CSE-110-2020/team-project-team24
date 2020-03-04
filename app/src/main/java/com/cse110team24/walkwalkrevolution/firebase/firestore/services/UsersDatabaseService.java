package com.cse110team24.walkwalkrevolution.firebase.firestore.services;

import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.subjects.UsersDatabaseServiceSubject;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.firebase.firestore.DocumentReference;

public interface UsersDatabaseService extends UsersDatabaseServiceSubject, DatabaseService {
    void createUserInDatabase(IUser user);
    void setUserTeam(IUser user, String teamUid);
    DocumentReference addUserMessagingRegistrationToken(IUser user, String token);
    void getUserData(IUser user);

}
