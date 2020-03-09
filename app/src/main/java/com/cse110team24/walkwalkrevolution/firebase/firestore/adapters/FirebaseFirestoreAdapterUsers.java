package com.cse110team24.walkwalkrevolution.firebase.firestore.adapters;

import android.util.Log;

import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.UsersDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.users.UsersUserDataObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.users.UsersUserExistsObserver;
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

/**
 * {@inheritDoc}
 * This type's database provider is Cloud Firestore. The document path for a user is
 * users/\{user\}.
 */
public class FirebaseFirestoreAdapterUsers implements UsersDatabaseService {
    private static final String TAG = "WWR_FirebaseFirestoreAdapterUsers";

    public static final String USERS_COLLECTION_KEY = "users";
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
            if(observer instanceof UsersUserDataObserver) {
                ((UsersUserDataObserver) observer).onUserData(userDataMap);
            }
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
        observers.forEach(observer -> {
            if (observer instanceof UsersUserExistsObserver) {
                UsersUserExistsObserver userExistsObserver = (UsersUserExistsObserver) observer;

                if (exists) {
                    userExistsObserver.onUserExists(otherUser);
                } else {
                    userExistsObserver.onUserDoesNotExist();
                }
            }
        });
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
