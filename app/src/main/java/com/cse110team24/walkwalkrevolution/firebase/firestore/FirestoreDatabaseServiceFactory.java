package com.cse110team24.walkwalkrevolution.firebase.firestore;

public class FirestoreDatabaseServiceFactory {

    public DatabaseService createDatabaseService() {
        return new FirebaseFirestoreAdapter();
    }
}
