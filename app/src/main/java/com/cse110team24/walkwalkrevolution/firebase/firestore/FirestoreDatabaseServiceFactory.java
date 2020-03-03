package com.cse110team24.walkwalkrevolution.firebase.firestore;

import com.cse110team24.walkwalkrevolution.firebase.firestore.adapters.FirebaseFirestoreAdapterUsers;

// TODO: 3/3/20 return one of three database services
public class FirestoreDatabaseServiceFactory implements DatabaseServiceFactory{

    // TODO: 3/3/20 return correct type
    @Override
    public DatabaseService createDatabaseService(DatabaseService.Service service) {
        switch (service) {
            case USERS:
                break;
            case TEAMS:
                break;
            case INVITATIONS:
                break;
        }
        return new FirebaseFirestoreAdapter();
    }
}
