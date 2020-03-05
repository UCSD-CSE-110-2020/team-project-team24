package com.cse110team24.walkwalkrevolution.firebase.firestore.adapters;

import android.util.Log;

import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.UsersDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.UsersDatabaseService;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter;
import com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapterBuilder;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter.TEAM_UID_KEY;

public class FirebaseFirestoreAdapterUsers implements UsersDatabaseService {
    private static final String TAG = "FirebaseFirestoreAdapterUsers";

    public static final String USERS_COLLECTION_KEY = "users";
    public static final String USER_ROUTES_SUB_COLLECTION_KEY = "routes";
    public static final String USER_REGISTRATION_TOKENS_COLLECTION_KEY = "tokens";
    public static final String TOKEN_SET_KEY = "token";

    private CollectionReference usersCollection;
    private FirebaseFirestore firebaseFirestore;
    List<UsersDatabaseServiceObserver> observers = new ArrayList<>();

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
    public void uploadRoute(String userDocumentKey, Route route) {
        CollectionReference userRoutesCollection = usersCollection.document(userDocumentKey).collection(USER_ROUTES_SUB_COLLECTION_KEY);
        DocumentReference routeDoc = userRoutesCollection.document();
        route.setRouteUid(routeDoc.getId());
        routeDoc.set(route.routeData()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, "uploadRoute: success uploading route " + route);
            } else {
                Log.e(TAG, "uploadRoute: error uploading route.", task.getException());
            }
        });
    }

    @Override
    public void updateRoute(String userDocumentKey, Route route) {
        CollectionReference userRoutesCollection = usersCollection.document(userDocumentKey).collection(USER_ROUTES_SUB_COLLECTION_KEY);
        DocumentReference routeDoc = userRoutesCollection.document(route.getRouteUid());
        routeDoc.set(route.routeData()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, "uploadRoute: success updated route " + route);
            } else {
                Log.e(TAG, "uploadRoute: error updating route.", task.getException());
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
                notifyObserversIfUserExists(task.getResult().exists(), buildUser(task.getResult()));
            }
        });
    }

    // build user, only gives display name and teamUid
    private IUser buildUser(DocumentSnapshot data) {
        if (data.exists()) {
            return FirebaseUserAdapter.builder()
                    .addDisplayName(data.getString("displayName"))
                    .addTeamUid(data.getString("teamUid"))
                    .build();
        } else {
            return null;
        }
    }

    @Override
    public void notifyObserversIfUserExists(boolean exists, IUser otherUser) {
        if (exists) {
            observers.forEach(observer -> observer.onUserExists(otherUser));
        } else {
            observers.forEach(observer -> observer.onUserDoesNotExist());
        }
    }
    @Override
    public void register(UsersDatabaseServiceObserver usersDatabaseServiceObserver) {
        observers.add(usersDatabaseServiceObserver);
    }

    @Override
    public void deregister(UsersDatabaseServiceObserver usersDatabaseServiceObserver) {
        observers.remove(usersDatabaseServiceObserver);
    }
}
