package com.cse110team24.walkwalkrevolution.Firebase.Firestore;

import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

public interface DatabaseService {

    CollectionReference createCollection(String collectionKey);
    CollectionReference createSubCollection(String collectionKey, DocumentReference parentDocument);
    DocumentReference createDocument(String documentKey);
    DocumentReference createSubDocument(String documentKey, CollectionReference parentCollection);
    DocumentReference createUserInDatabase(IUser user);
}
