package com.cse110team24.walkwalkrevolution.firebase.firestore;

// TODO: 3/3/20 return one of three database services 
public class FirestoreDatabaseServiceFactory implements DatabaseServiceFactory{

    @Override
    public DatabaseService createDatabaseService() {
        return new FirebaseFirestoreAdapter();
    }
}
