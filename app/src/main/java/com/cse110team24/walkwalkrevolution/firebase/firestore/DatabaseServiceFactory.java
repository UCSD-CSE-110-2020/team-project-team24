package com.cse110team24.walkwalkrevolution.firebase.firestore;

public class DatabaseServiceFactory {

    public DatabaseService createDatabaseService() {
        return new FirebaseFirestoreAdapter();
    }
}
