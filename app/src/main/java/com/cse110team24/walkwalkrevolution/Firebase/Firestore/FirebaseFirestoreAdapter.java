package com.cse110team24.walkwalkrevolution.Firebase.Firestore;

import android.util.Log;

import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.Map;

import static com.cse110team24.walkwalkrevolution.models.team.TeamAdapter.MEMBERS_KEY;
import static com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter.TEAM_UID_KEY;

/** TODO: 2/28/20 flow for a team should be
 * get the user's account.
 * Check if they have a team.
 * check if user has team
 *      if yes, update UI
 *
 *      if not, when user sends invite,
 *          instantiate team,
 *          add user as member of team,
 *          create team in database
 *          set team's new UID as teamUID for user
 *
 */
public class FirebaseFirestoreAdapter implements DatabaseService{
    private static final String TAG = "FirebaseFirestoreAdapter";
    public static final String USERS_COLLECTION_KEY = "users";
    public static final String ROUTES_COLLECTION_KEY = "routes";
    public static final String TEAMS_COLLECTION_KEY = "teams";

    private CollectionReference usersCollection;
    private CollectionReference teamsCollection;
    private FirebaseFirestore firebaseFirestore;

    public FirebaseFirestoreAdapter() {
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public DocumentReference createUserInDatabase(IUser user) {
        Map<String, Object> userData = user.getDBFields();
        firebaseFirestore.collection(USERS_COLLECTION_KEY).document(user.getDocumentKey()).set(userData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, "createUserInDatabase: successfully created document in \"users\" collection for user " + user.getDisplayName());
            } else {
                Log.e(TAG, "createUserInDatabase: failed to create document", task.getException());
            }
        });
        return usersCollection.document(user.getDocumentKey());
    }

    @Override
    public DocumentReference updateUserTeam(IUser user, String teamUid) {
        DocumentReference documentReference = usersCollection.document(user.getDocumentKey());
        documentReference.update(TEAM_UID_KEY, teamUid).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, "updateUserTeam: successfully updated user's team uid");
            } else {
                Log.e(TAG, "updateUserTeam: error updating team uid", task.getException());
            }
        });
        return usersCollection.document(user.getDisplayName());
    }

    // TODO: 2/28/20 need to determine if this will be real time
    @Override
    public ITeam getUserTeam(IUser user) {
        return null;
    }

    @Override
    public DocumentReference createTeamInDatabase(ITeam team) {
        DocumentReference teamDocument = teamsCollection.document();
        team.setUid(teamDocument.getId());
        Map<String, Object> teamData = team.getDBFields();
        teamDocument.set(teamData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, "createTeamInDatabase: successfully created team document");
            } else {
                Log.e(TAG, "createTeamInDatabase: error creating team document", task.getException());
            }
        });
        return teamDocument;
    }

    @Override
    public DocumentReference updateTeamMembers(ITeam team) {
        DocumentReference documentReference = teamsCollection.document(team.getUid());
        documentReference.update(MEMBERS_KEY, team.getTeam()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, "updateTeamMembers: successfully updated team member list");
            } else {
                Log.e(TAG, "updateTeamMembers: error updating team member list", task.getException());
            }
        });
        return teamsCollection.document(team.getUid());
    }

}
