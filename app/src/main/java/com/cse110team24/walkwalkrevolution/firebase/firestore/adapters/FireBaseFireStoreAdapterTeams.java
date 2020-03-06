package com.cse110team24.walkwalkrevolution.firebase.firestore.adapters;


import android.util.Log;

import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.TeamsDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.TeamDatabaseService;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.team.TeamAdapter;
import com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static com.cse110team24.walkwalkrevolution.models.team.TeamAdapter.MEMBERS_KEY;

public class FireBaseFireStoreAdapterTeams implements TeamDatabaseService {
    private static final String TAG = "WWR_FirebaseFirestoreAdapterTeams";

    public static final String TEAMS_COLLECTION_KEY = "teams";
    public static final String TEAMMATES_SUB_COLLECTION = "teammates";

    private CollectionReference teamsCollection;
    private FirebaseFirestore firebaseFirestore;

    public FireBaseFireStoreAdapterTeams() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        teamsCollection = firebaseFirestore.collection(TEAMS_COLLECTION_KEY);
    }

    @Override
    public String createTeamInDatabase(IUser user) {
        Log.d(TAG, "createTeamInDatabase: creating team");
        // create new team document and update user's teamUid
        DocumentReference teamDocument = teamsCollection.document();
        String teamUid = teamDocument.getId();
        user.updateTeamUid(teamUid);

        // create the teammates collection and the individual member document
        CollectionReference teamSubCollection = teamDocument.collection(TEAMMATES_SUB_COLLECTION);
        DocumentReference memberDocument = teamSubCollection.document(user.documentKey());
        memberDocument.set(user.userData()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, "createTeamInDatabase: successfully created team document");
            } else {
                Log.e(TAG, "createTeamInDatabase: error creating team document", task.getException());
            }
        });

        return teamUid;
    }

    @Override
    public void addUserToTeam(IUser user, String teamUid) {
        // teamsCollection/teamDocument/teammatesCollection/userDocument
        DocumentReference teamDocument = teamsCollection.document(teamUid);
        CollectionReference teammatesCollection = teamDocument.collection(TEAMMATES_SUB_COLLECTION);
        teammatesCollection.document(user.documentKey()).set(user.userData()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, "addUserToTeam: successfully updated team");
            } else {
                Log.e(TAG, "addUserToTeam: error updating team", task.getException());
            }
        });
    }

    // TODO: 2/28/20 need to determine if this will be real time
    @Override
    public void getUserTeam(String teamUid, String currentUserDisplayName) {
        DocumentReference documentReference = teamsCollection.document(teamUid);
        CollectionReference teammatesCollection = documentReference.collection(TEAMMATES_SUB_COLLECTION);
        teammatesCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Log.i(TAG, "getUserTeam: team successfully retrieved");
                List<DocumentSnapshot> documents = task.getResult().getDocuments();
                ITeam team = getTeamList(documents, currentUserDisplayName);
                notifyObserversTeamRetrieved(team);
            } else {
                Log.e(TAG, "getUserTeam: error getting user team", task.getException());
            }
        });
    }

    @Override
    public void getUserTeamRoutes(String teamUid, String currentUserDisplay, int routeLimitCount, DocumentSnapshot lastRoute) {
        // todo get routeLimitCount amount of routes
    }

    @Override
    public void uploadRoute(String teamUid, Route route) {
        // TODO upload to teams/{team}/routes/{route}
    }

    @Override
    public void updateRoute(String teamUid, Route route) {
        // TODO: 3/6/20 update in teams/{team}/routes/{route}
    }
// todo something similar
//    @Override
//    public void uploadRoute(String userDocumentKey, Route route) {
//        CollectionReference userRoutesCollection = usersCollection.document(userDocumentKey).collection(USER_ROUTES_SUB_COLLECTION_KEY);
//        DocumentReference routeDoc = userRoutesCollection.document();
//        route.setRouteUid(routeDoc.getId());
//        routeDoc.set(route.routeData()).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                Log.i(TAG, "uploadRoute: success uploading route " + route);
//            } else {
//                Log.e(TAG, "uploadRoute: error uploading route.", task.getException());
//            }
//        });
//    }
// todo
//    @Override
//    public void updateRoute(String userDocumentKey, Route route) {
//        CollectionReference userRoutesCollection = usersCollection.document(userDocumentKey).collection(USER_ROUTES_SUB_COLLECTION_KEY);
//        DocumentReference routeDoc = userRoutesCollection.document(route.getRouteUid());
//        routeDoc.set(route.routeData()).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                Log.i(TAG, "uploadRoute: success updated route " + route);
//            } else {
//                Log.e(TAG, "uploadRoute: error updating route.", task.getException());
//            }
//        });
//    }

    private ITeam getTeamList(List<DocumentSnapshot> documents, String currentUserDisplayName) {
        ITeam team = new TeamAdapter(new ArrayList<>());
        for (DocumentSnapshot member : documents) {
            String displayName = (String) member.get("displayName");
            // skip the current user
            if (currentUserDisplayName.equals(displayName)) continue;
            IUser user = FirebaseUserAdapter.builder()
                    .addDisplayName(displayName)
                    .build();
            team.addMember(user);
        }
        return team;
    }

    List<TeamsDatabaseServiceObserver> observers = new ArrayList<>();
    @Override
    public void notifyObserversTeamRetrieved(ITeam team) {
        observers.forEach(observer -> {
            observer.onTeamRetrieved(team);
        });
    }

    @Override
    public void notifyObserversTeamRoutesRetrieved(List<Route> routes, DocumentSnapshot lastRoute) {
        observers.forEach(observer -> observer.onRoutesRetrieved(routes, lastRoute));
    }

    @Override
    public void register(TeamsDatabaseServiceObserver observer) {
        observers.add(observer);
    }


    @Override
    public void deregister(TeamsDatabaseServiceObserver observer) {
        observers.remove(observer);
    }
}
