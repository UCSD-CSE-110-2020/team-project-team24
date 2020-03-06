package com.cse110team24.walkwalkrevolution.firebase.firestore;

import com.cse110team24.walkwalkrevolution.firebase.firestore.adapters.FireBaseFireStoreAdapterTeams;
import com.cse110team24.walkwalkrevolution.firebase.firestore.adapters.FirebaseFirestoreAdapterInvitations;
import com.cse110team24.walkwalkrevolution.firebase.firestore.adapters.FirebaseFirestoreAdapterUsers;

/**
 * Instantiates one of 4 Cloud Firestore implementations of {@link DatabaseService}
 */
public class FirestoreDatabaseServiceFactory implements DatabaseServiceFactory{

    @Override
    public DatabaseService createDatabaseService(DatabaseService.Service service) {
        switch (service) {
            case USERS:
                return new FirebaseFirestoreAdapterUsers();
            case TEAMS:
                return new FireBaseFireStoreAdapterTeams();
            case INVITATIONS:
                return new FirebaseFirestoreAdapterInvitations();
        }
        return new FirebaseFirestoreAdapter();
    }
}
