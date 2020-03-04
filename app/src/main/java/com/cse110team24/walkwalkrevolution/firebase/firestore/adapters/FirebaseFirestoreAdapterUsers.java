package com.cse110team24.walkwalkrevolution.firebase.firestore.adapters;

import android.util.Log;

import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.UsersDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.UsersDatabaseService;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter.TEAM_UID_KEY;

public class FirebaseFirestoreAdapterUsers implements UsersDatabaseService {
    private static final String TAG = "FirebaseFirestoreAdapterUsers";

    public static final String USERS_COLLECTION_KEY = "users";
    public static final String USER_REGISTRATION_TOKENS_COLLECTION_KEY = "tokens";
    public static final String TOKEN_SET_KEY = "token";

    private CollectionReference usersCollection;
    private FirebaseFirestore firebaseFirestore;

    public FirebaseFirestoreAdapterUsers() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        usersCollection = firebaseFirestore.collection(USERS_COLLECTION_KEY);
    }

    @Override
    public void createUserInDatabase(IUser user) {
        Map<String, Object> userData = user.userData();
        DocumentReference userDocument= usersCollection.document(user.documentKey());
        userDocument.set(userData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, "createUserInDatabase: successfully created document in \"users\" collection for user " + user.getDisplayName());
            } else {
                Log.e(TAG, "createUserInDatabase: failed to create document", task.getException());
            }
        });
    }

    @Override
    public void updateUserTeamUidInDatabase(IUser user, String teamUid) {
        DocumentReference documentReference = usersCollection.document(user.documentKey());
        documentReference.update(TEAM_UID_KEY, teamUid).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, "updateUserTeam: successfully updated user's team uid");
            } else {
                Log.e(TAG, "updateUserTeam: error updating team uid", task.getException());
            }
        });
    }

    @Override
    public DocumentReference addUserMessagingRegistrationToken(IUser user, String token) {
        DocumentReference userDoc = usersCollection.document(user.documentKey());
        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put(TOKEN_SET_KEY, token);
        userDoc.collection(USER_REGISTRATION_TOKENS_COLLECTION_KEY)
                .add(tokenData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.i(TAG, "addUserMessagingRegistrationToken: success adding user registration token");
                    } else {
                        Log.e(TAG, "addUserMessagingRegistrationToken: error adding user registration token", task.getException());
                    }
                });
        return userDoc;
    }

    @Override
    public void getUserData(IUser user) {
        DocumentReference documentReference = usersCollection.document(user.documentKey());
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                notifyObserversUserData(task.getResult().getData());
            }
        });
    }

    @Override
    public void notifyObserversUserData(Map<String, Object> userDataMap) {
        observers.forEach(observer -> {
            observer.onUserData(userDataMap);
        });
    }

    @Override
    public void checkIfOtherUserExists(String userDocumentKey) {
        DocumentReference otherUserDoc = usersCollection.document(userDocumentKey);
        otherUserDoc.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                notifyObserversIfUserExists(task.getResult().exists());
            }
        });
    }

    @Override
    public void notifyObserversIfUserExists(boolean exists) {
        if (exists) {
            observers.forEach(observer -> observer.onUserExists());
        } else {
            observers.forEach(observer -> observer.onUserDoesNotExist());
        }
    }

    List<UsersDatabaseServiceObserver> observers = new ArrayList<>();
    @Override
    public void register(UsersDatabaseServiceObserver usersDatabaseServiceObserver) {
        observers.add(usersDatabaseServiceObserver);
    }

    @Override
    public void deregister(UsersDatabaseServiceObserver usersDatabaseServiceObserver) {
        observers.remove(usersDatabaseServiceObserver);
    }
}
