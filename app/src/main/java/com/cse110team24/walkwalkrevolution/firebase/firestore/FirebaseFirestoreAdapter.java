package com.cse110team24.walkwalkrevolution.firebase.firestore;

import android.util.Log;

import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.cse110team24.walkwalkrevolution.models.team.TeamAdapter.MEMBERS_KEY;
import static com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter.TEAM_UID_KEY;

/** TODO: 2/28/20 flow for a team should be
 * get the user's account (sign in or up if necessary)
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
public class FirebaseFirestoreAdapter implements DatabaseService {
    private static final String TAG = "FirebaseFirestoreAdapter";
    public static final String USERS_COLLECTION_KEY = "users";
    public static final String ROUTES_COLLECTION_KEY = "routes";
    public static final String INVITATIONS_ROOT_COLLECTION_KEY = "invitations";
    public static final String USER_INVITATIONS_SUB_COLLECTION_KEY = "invitations";
    public static final String TEAMS_COLLECTION_KEY = "teams";

    private CollectionReference usersCollection;
    private CollectionReference teamsCollection;
    private CollectionReference invitationsRootCollection;

    private FirebaseFirestore firebaseFirestore;

    public FirebaseFirestoreAdapter() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        usersCollection = firebaseFirestore.collection(USERS_COLLECTION_KEY);
        teamsCollection = firebaseFirestore.collection(TEAMS_COLLECTION_KEY);
        invitationsRootCollection = firebaseFirestore.collection(INVITATIONS_ROOT_COLLECTION_KEY);
    }

    @Override
    public DocumentReference createUserInDatabase(IUser user) {
        Map<String, Object> userData = user.userData();
        DocumentReference userDocument= usersCollection.document(user.documentKey());
        userDocument.set(userData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, "createUserInDatabase: successfully created document in \"users\" collection for user " + user.getDisplayName());
            } else {
                Log.e(TAG, "createUserInDatabase: failed to create document", task.getException());
            }
        });
        return usersCollection.document(user.documentKey());
    }

    @Override
    public DocumentReference setUserTeam(IUser user, String teamUid) {
        user.updateTeamUid(teamUid);
        DocumentReference documentReference = usersCollection.document(user.documentKey());
        documentReference.update(TEAM_UID_KEY, teamUid).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, "updateUserTeam: successfully updated user's team uid");
            } else {
                Log.e(TAG, "updateUserTeam: error updating team uid", task.getException());
            }
        });
        return usersCollection.document(user.documentKey());
    }

    @Override
    public DocumentReference createTeamInDatabase(ITeam team) {
        DocumentReference teamDocument = teamsCollection.document();
        team.setUid(teamDocument.getId());
        Map<String, Object> teamData = team.teamData();
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
        DocumentReference documentReference = teamsCollection.document(team.documentKey());
        documentReference.update(MEMBERS_KEY, team.getTeam()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, "updateTeamMembers: successfully updated team member list");
            } else {
                Log.e(TAG, "updateTeamMembers: error updating team member list", task.getException());
            }
        });
        return teamsCollection.document(team.getUid());
    }

    @Override
    public CollectionReference addInvitationForReceivingUser(Invitation invitation) {
        IUser receiver = invitation.toUser();
        DocumentReference receiverDoc = usersCollection.document(receiver.documentKey());
        CollectionReference receiverInvitationsCollection = receiverDoc.collection(USER_INVITATIONS_SUB_COLLECTION_KEY);
        receiverInvitationsCollection.add(invitation.invitationData());
        return receiverInvitationsCollection;
    }

    @Override
    public DocumentReference createRootInvitationDocument(Invitation invitation) {
        DocumentReference rootInvitationDoc = invitationsRootCollection.document();
        invitation.setUid(rootInvitationDoc.getId());

        rootInvitationDoc.set(invitation.invitationData()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, "createRootInvitationDocument: success creating new invitation document");
            } else {
                Log.e(TAG, "createRootInvitationDocument: failed creating invitation doc", task.getException());
            }
        });
        return rootInvitationDoc;
    }

    @Override
    public List<Invitation> getUserInvitations(IUser user) {
        Task<QuerySnapshot> task  = usersCollection
                .document(user.documentKey())
                .collection(USER_INVITATIONS_SUB_COLLECTION_KEY)
                .get();
        List<DocumentSnapshot> invitationDocuments = task.getResult().getDocuments();
        List<Invitation> invitations = new ArrayList<>(invitationDocuments.size());
        invitationDocuments.forEach(document -> {
            invitations.add(buildInvitation(document, user));
        });
        return invitations;
    }

    private Invitation buildInvitation(DocumentSnapshot invitationDocument, IUser user) {
        IUser from = buildUserFromInvitation(invitationDocument, Invitation.INVITATION_FROM_SET_KEY);
        String uid = invitationDocument.getString(Invitation.INVITATION_UID_SET_KEY);
        return Invitation.builder()
                .addFromUser(from)
                .addToUser(user)
                .addUid(uid)
                .build();
    }

    private IUser buildUserFromInvitation(DocumentSnapshot invitationDocument, String documentKey) {
        String[] fromUserInfo = invitationDocument.getString(documentKey).split("\\s");
        return FirebaseUserAdapter.builder()
                    .addDisplayName(fromUserInfo[0])
                    .addEmail(fromUserInfo[1])
                    .build();
    }

    // TODO: 2/28/20 need to determine if this will be real time
    @Override
    public ITeam getUserTeam(IUser user) {
        return null;
    }
}
