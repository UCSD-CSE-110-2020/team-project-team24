package com.cse110team24.walkwalkrevolution.firebase.firestore;

public class FirestoreDatabaseServiceFactory implements DatabaseServiceFactory{

    @Override
    public DatabaseService createDatabaseService() {
        return new FirebaseFirestoreAdapter();
    }
}
