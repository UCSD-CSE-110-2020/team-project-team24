package com.cse110team24.walkwalkrevolution.firebase.firestore.adapters;


import android.util.Log;

import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.TeamsDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.TeamsDatabaseService;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.route.RouteBuilder;
import com.cse110team24.walkwalkrevolution.models.route.RouteEnvironment;
import com.cse110team24.walkwalkrevolution.models.route.WalkStats;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.team.TeamAdapter;
import com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * {@inheritDoc}
 * This type's database provider is Cloud Firestore. The document path for a team is
 * teams/\{team\}. The document path for a teammate is teams/{\team\}/teammates/\{teammate}.
 * The document path for a teammate route is teams/{\team\}/routes/\{route}.
 */
public class FireBaseFireStoreAdapterTeams implements TeamsDatabaseService {
    private static final String TAG = "WWR_FirebaseFirestoreAdapterTeams";

    public static final String TEAMS_COLLECTION_KEY = "teams";
    public static final String TEAMMATES_SUB_COLLECTION = "teammates";
    public static final String TEAM_ROUTES_SUB_COLLECTION_KEY = "routes";

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
        teammatesCollection.orderBy("displayName").get().addOnCompleteListener(task -> {
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
    public void getUserTeamRoutes(String teamUid, String currentUserDisplayName, int routeLimitCount, DocumentSnapshot lastRoute) {
        Log.d(TAG, "getUserTeamRoutes: teamUid " + teamUid + " currentDisplayName " + currentUserDisplayName);
        // return routes ordered by name, skipping routes that current user owns
        Query routesQuery = teamsCollection
                .document(teamUid)
                .collection(TEAM_ROUTES_SUB_COLLECTION_KEY)
//                .whereGreaterThan("createdBy", currentUserDisplayName)
//                .whereLessThan("createdBy", currentUserDisplayName)
//                .orderBy("createdBy")
                .limit(routeLimitCount);
        Log.d(TAG, "getUserTeamRoutes: query " + routesQuery);

        if (lastRoute != null) {
            routesQuery = routesQuery.startAfter(lastRoute);
        }

        routesQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                QuerySnapshot resultDocs = task.getResult();
                Log.i(TAG, "getUserTeamRoutes: " + resultDocs.size() + " routes retrieved");
                DocumentSnapshot lastVisible = null;
                if (resultDocs.size() > 0)
                    lastVisible = resultDocs.getDocuments().get(resultDocs.size() - 1);
                List<Route> routes = new ArrayList<>(resultDocs.size());
                resultDocs.getDocuments().forEach(documentSnapshot -> routes.add(buildRoute(documentSnapshot)));
                notifyObserversTeamRoutesRetrieved(routes, lastVisible);
            } else {
                Log.e(TAG, "getUserTeamRoutes: could not retrieve team routes", task.getException());
            }
        });
    }

    private Route buildRoute(DocumentSnapshot routeDoc) {
        WalkStats stats = null;
        Object data = routeDoc.get("stats");
        if (data != null) {
            stats = buildWalkStats((Map<String, Object>) data);
        }
        return new Route.Builder(routeDoc.getString("title"))
                .addCreatorDisplayName(routeDoc.getString("createdBy"))
                .addWalkStats(stats)
                .addRouteEnvironment(buildRouteEnvironment((Map<String, Object>) routeDoc.get("environment")))
                .addNotes(routeDoc.getString("notes"))
                .addStartingLocation(routeDoc.getString("startingLocation"))
                .build();
    }

    private WalkStats buildWalkStats(Map<String, Object> data) {
        long steps = (long) data.get("steps");
        double distance = (double) data.get("distance");
        long timeElapsed = (long) data.get("elapsedTimeMillis");
        Timestamp time = (Timestamp) data.get("date");
        Calendar.getInstance().setTimeInMillis((long) (time.getNanoseconds() * 10e6));
        return new WalkStats(steps, timeElapsed, distance, Calendar.getInstance());
    }

    private RouteEnvironment buildRouteEnvironment(Map<String, Object> data) {
        return RouteEnvironment.builder()
                .addDifficulty(RouteEnvironment.Difficulty.valueOf((String) data.get("difficulty")))
                .addRouteType(RouteEnvironment.RouteType.valueOf((String) data.get("routeType")))
                .addSurfaceType(RouteEnvironment.SurfaceType.valueOf((String) data.get("surfaceType")))
                .addTerrainType(RouteEnvironment.TerrainType.valueOf((String)data.get("terrainType")))
                .addTrailType(RouteEnvironment.TrailType.valueOf((String) data.get("trailType")))
                .build();
    }

    @Override
    public void uploadRoute(String teamUid, Route route) {
        // upload to teams/{team}/routes/{route}
        DocumentReference routeDoc = teamsCollection
                .document(teamUid)
                .collection(TEAM_ROUTES_SUB_COLLECTION_KEY)
                .document();
        route.setRouteUid(routeDoc.getId());
        routeDoc.set(route.routeData()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, "uploadRoute: route uploaded successfully");
            } else {
                Log.e(TAG, "uploadRoute: route failed to upload", task.getException());
            }
        });
    }

    @Override
    public void updateRoute(String teamUid, Route route) {
        // update in teams/{team}/routes/{route}
        DocumentReference routeDoc = teamsCollection
                .document(teamUid)
                .collection(TEAM_ROUTES_SUB_COLLECTION_KEY)
                .document(route.getRouteUid());
        routeDoc.set(route.routeData()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, "uploadRoute: success updated route " + route);
            } else {
                Log.e(TAG, "uploadRoute: error updating route.", task.getException());
            }
        });
    }

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
