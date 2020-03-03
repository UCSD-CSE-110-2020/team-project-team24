package com.cse110team24.walkwalkrevolution.firebase.firestore;

import android.util.Log;

import com.cse110team24.walkwalkrevolution.models.invitation.InvitationStatus;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.team.TeamAdapter;
import com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
    public static final String USER_RECEIVED_INVITATIONS_COLLECTION = "received";
    public static final String USER_SENT_INVITATIONS_COLLECTION = "sent";
    public static final String TEAMS_COLLECTION_KEY = "teams";
    public static final String TEAMMATES_SUB_COLLECTION = "teammates";
    public static final String USER_REGISTRATION_TOKENS_COLLECTION_KEY = "tokens";
    public static final String TOKEN_SET_KEY = "token";
    public static final String TEAM_ID_KEY = "teamUid";

    List<DatabaseServiceObserver> observers = new ArrayList<>();

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
    public void setUserTeam(IUser user, String teamUid) {
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
    public String createTeamInDatabase(IUser user) {
        // create new team document and update user's teamUid
        DocumentReference teamDocument = teamsCollection.document();
        String teamUid = teamDocument.getId();

        // create the teammates collection and the individual member document
        CollectionReference teamSubCollection = teamDocument.collection(TEAMMATES_SUB_COLLECTION);
        DocumentReference memberDocument = teamSubCollection.document(user.documentKey());

        memberDocument.set(user.getDisplayName()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i(TAG, "createTeamInDatabase: successfully created team document");
            } else {
                Log.e(TAG, "createTeamInDatabase: error creating team document", task.getException());
            }
        });

        return teamUid;
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
    public Task<?> addInvitationForReceivingUser(Invitation invitation) {
        DocumentReference userSpecificInvitationsDoc = invitationsRootCollection.document(invitation.toDocumentKey() + "invitations");
        CollectionReference receivedInvitations = userSpecificInvitationsDoc.collection(USER_RECEIVED_INVITATIONS_COLLECTION);
        DocumentReference invitationDoc = receivedInvitations.document();
        invitation.setUid(invitationDoc.getId());
        Task<Void> result = invitationDoc.set(invitation.invitationData());
        return result;
    }

    @Override
    public Task<?> addInvitationForSendingUser(Invitation invitation) {
        DocumentReference userSpecificInvitationsDoc = invitationsRootCollection.document(invitation.fromDocumentKey() + "invitations");
        CollectionReference sentInvitations = userSpecificInvitationsDoc.collection(USER_SENT_INVITATIONS_COLLECTION);
        DocumentReference invitationDoc = sentInvitations.document(invitation.uid());
        Task<?> result = invitationDoc.set(invitation.invitationData());
        return result;
    }

    // TODO: 2/28/20 need to determine if this will be real time
    @Override
    public void getUserTeam(String teamUid) {
        DocumentReference documentReference = teamsCollection.document(teamUid);
        CollectionReference teammatesCollection = documentReference.collection(TEAMMATES_SUB_COLLECTION);
        teammatesCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                Log.i(TAG, "getUserTeam: team successfully retrieved");
                List<DocumentSnapshot> documents = task.getResult().getDocuments();
                ITeam team = getTeamList(documents);
                notifyObserversTeamRetrieved(team);
            } else {
                Log.e(TAG, "getUserTeam: error getting user team", task.getException());
            }
        });
    }

    private ITeam getTeamList(List<DocumentSnapshot> documents) {
        ITeam team = new TeamAdapter(new ArrayList<>());
        for (DocumentSnapshot member : documents) {
            String displayName = (String) member.get("displayName");
            IUser user = FirebaseUserAdapter.builder()
                    .addDisplayName(displayName)
                    .build();
            team.addMember(user);
        }
        return team;
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
    public List<Invitation> getUserPendingInvitations(IUser user) {
        Task<QuerySnapshot> task  = usersCollection
                .document(user.documentKey())
                .collection(USER_RECEIVED_INVITATIONS_COLLECTION)
                .whereEqualTo(Invitation.INVITATION_STATUS_SET_KEY, InvitationStatus.PENDING
                                .toString()
                                .toLowerCase(Locale.getDefault()))
                .get();
        List<DocumentSnapshot> invitationDocuments = task.getResult().getDocuments();
        List<Invitation> invitations = new ArrayList<>(invitationDocuments.size());
        invitationDocuments.forEach(document -> {
            invitations.add(buildInvitation(document, user));
        });
        return invitations;
    }

    @Override
    public void addInvitationsSnapshotListener(IUser user) {
        usersCollection
                .document(user.documentKey())
                .collection(USER_RECEIVED_INVITATIONS_COLLECTION)
                .addSnapshotListener((queryDocumentSnapshots, error) -> {
                    if (error != null) {
                        Log.e(TAG, "addInvitationsSnapshotListener: error adding snapshot", error);
                        return;
                    }

                    if (queryDocumentSnapshots != null) {
                        List<DocumentChange> documentChanges = queryDocumentSnapshots.getDocumentChanges();
                        documentChanges.forEach(documentChange -> {
                            Invitation invitation = buildInvitation(documentChange.getDocument(), user);
                            user.addInvitation(invitation);
                        });
                    }
                });
    }

    private Invitation buildInvitation(DocumentSnapshot invitationDocument, IUser user) {
        IUser from = buildUserFromInvitation(invitationDocument, Invitation.INVITATION_FROM_SET_KEY);
        String uid = invitationDocument.getString(Invitation.INVITATION_UID_SET_KEY);
        return Invitation.builder()
                .addFromUser(from)
                .addToEmail(user.getEmail())
                .addToDisplayName(user.getDisplayName())
                .addUid(uid)
                .build();
    }

    private IUser buildUserFromInvitation(DocumentSnapshot invitationDocument, String documentKey) {
        String[] fromUserInfo = invitationDocument.getString(documentKey).split("@");
        return FirebaseUserAdapter.builder()
                    .addDisplayName(fromUserInfo[0].trim())
                    .addEmail(fromUserInfo[1].trim())
                    .build();
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
    public void register(DatabaseServiceObserver observer) {
        observers.add(observer);
    }

    @Override
    public void deregister(DatabaseServiceObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObserversTeamRetrieved(ITeam team) {
        observers.forEach(observer -> {
            observer.onTeamRetrieved(team);
        });
    }

    @Override
    public void notifyObserversFieldRetrieved(Object field) {
        observers.forEach(observer -> {
            observer.onFieldRetrieved(field);
        });
    }

    @Override
    public void notifyObserversUserData(Map<String, Object> userDataMap) {
        observers.forEach(observer -> {
            observer.onUserData(userDataMap);
        });
    }

}
