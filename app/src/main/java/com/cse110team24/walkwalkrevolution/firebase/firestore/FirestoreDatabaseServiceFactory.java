package com.cse110team24.walkwalkrevolution.firebase.firestore;

import com.cse110team24.walkwalkrevolution.firebase.firestore.adapters.FireBaseFireStoreAdapterTeams;
import com.cse110team24.walkwalkrevolution.firebase.firestore.adapters.FirebaseFirestoreAdapterInvitations;
import com.cse110team24.walkwalkrevolution.firebase.firestore.adapters.FirebaseFirestoreAdapterUsers;

// TODO: 3/3/20 return one of three database services
public class FirestoreDatabaseServiceFactory implements DatabaseServiceFactory{

    // TODO: 3/3/20 return correct type
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
