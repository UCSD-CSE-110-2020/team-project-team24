package com.cse110team24.walkwalkrevolution.Firebase.Firestore;

import android.app.Activity;

import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseFirestoreAdapter implements DatabaseService{
    public static final String USERS_COLLECTION_KEY = "users";
    public static final String ROUTES_COLLECTION_KEY = "routes";
    public static final String TEAM_COLLECTION_KEY = "team";

    private CollectionReference usersCollection;
    private FirebaseFirestore firebaseFirestore;

    FirebaseFirestoreAdapter() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        usersCollection = firebaseFirestore.collection(USERS_COLLECTION_KEY);
    }

    @Override
    public DocumentReference createUserInDatabase(IUser user) {
        return usersCollection.document(user.getDisplayName());
    }

    @Override
    public CollectionReference createCollection(String collectionKey) {
        return firebaseFirestore.collection(collectionKey);
    }

    @Override
    public CollectionReference createSubCollection(String collectionKey, DocumentReference parentDocument) {
        return parentDocument.collection(collectionKey);
    }

    @Override
    public DocumentReference createDocument(String documentKey) {
        return firebaseFirestore.document(documentKey);
    }

    @Override
    public DocumentReference createSubDocument(String documentKey, CollectionReference parentCollection) {
        return parentCollection.document(documentKey);
    }


}
