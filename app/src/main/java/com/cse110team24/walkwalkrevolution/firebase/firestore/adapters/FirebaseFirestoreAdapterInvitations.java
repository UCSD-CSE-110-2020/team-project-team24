package com.cse110team24.walkwalkrevolution.firebase.firestore.adapters;

import android.util.Log;

import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.InvitationsDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.InvitationsDatabaseService;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.models.invitation.InvitationStatus;
import com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * {@inheritDoc}
 * The database provider for this type is Cloud Firestore.
 */
public class FirebaseFirestoreAdapterInvitations implements InvitationsDatabaseService {
    private static final String TAG = "WWR_FirebaseFirestoreAdapterInvitations";

    public static final String INVITATIONS_ROOT_COLLECTION_KEY = "invitations";
    public static final String USER_RECEIVED_INVITATIONS_COLLECTION = "received";
    public static final String USER_SENT_INVITATIONS_COLLECTION = "sent";

    private CollectionReference invitationsRootCollection;
    private FirebaseFirestore firebaseFirestore;

    public FirebaseFirestoreAdapterInvitations() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        invitationsRootCollection = firebaseFirestore.collection(INVITATIONS_ROOT_COLLECTION_KEY);
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

    public void updateInvitationForReceivingUser(Invitation invitation) {
        DocumentReference userInvitationDoc = invitationsRootCollection.document(invitation.toDocumentKey() + "invitations");
        CollectionReference receivedInvitations = userInvitationDoc.collection(USER_RECEIVED_INVITATIONS_COLLECTION);
        DocumentReference invitationDoc = receivedInvitations.document(invitation.uid());
        invitationDoc.update(invitation.invitationData());
    }

    public void updateInvitationForSendingUser(Invitation invitation) {
        DocumentReference userInvitationDoc = invitationsRootCollection.document(invitation.fromDocumentKey() + "invitations");
        CollectionReference sentInvitations = userInvitationDoc.collection(USER_SENT_INVITATIONS_COLLECTION);
        DocumentReference invitationDoc = sentInvitations.document(invitation.uid());
        invitationDoc.update(invitation.invitationData());
    }

    @Override
    public void getUserPendingInvitations(IUser user) {
        invitationsRootCollection
                .document(user.documentKey() + "invitations")
                .collection(USER_RECEIVED_INVITATIONS_COLLECTION)
                .whereEqualTo(Invitation.INVITATION_STATUS_SET_KEY, InvitationStatus.PENDING
                        .toString()
                        .toLowerCase(Locale.getDefault()))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Log.i(TAG, "getUserPendingInvitations: success retrieving invitations");
                        List<Invitation> invitations = createInvitationsListFromDoc(user, task.getResult().getDocuments());
                        notifyObserversPendingInvitations(invitations);
                    } else {
                        Log.e(TAG, "getUserPendingInvitations: error getting pending invitations", task.getException());
                    }
                });
    }
    private List<Invitation> createInvitationsListFromDoc(IUser user, List<DocumentSnapshot> invitationDocuments) {
        List<Invitation> invitations = new ArrayList<>(invitationDocuments.size());
        invitationDocuments.forEach(document -> invitations.add(buildInvitation(document, user)));
        return invitations;
    }

    private Invitation buildInvitation(DocumentSnapshot invitationDocument, IUser user) {
        IUser from = buildUserFromInvitation(invitationDocument);
        String uid = invitationDocument.getString(Invitation.INVITATION_UID_SET_KEY);
        String teamUid = invitationDocument.getString(Invitation.INVITATION_TEAM_UID_SET_KEY);
        return Invitation.builder()
                .addFromUser(from)
                .addToEmail(user.getEmail())
                .addToDisplayName(user.getDisplayName())
                .addUid(uid)
                .addTeamUid(teamUid)
                .build();
    }

    private IUser buildUserFromInvitation(DocumentSnapshot invitationDocument) {
        Map<String, Object> fromData =(Map<String, Object>) invitationDocument.get(Invitation.INVITATION_FROM_SET_KEY);
        return FirebaseUserAdapter.builder()
                .addDisplayName((String) fromData.get("name"))
                .addEmail((String) fromData.get("identifier"))
                .build();
    }

    // TODO: 3/3/20 change to get correct document from invitations collection
    @Override
    public void addInvitationsSnapshotListener(IUser user) {
//        usersCollection
//                .document(user.documentKey())
//                .collection(USER_RECEIVED_INVITATIONS_COLLECTION)
//                .addSnapshotListener((queryDocumentSnapshots, error) -> {
//                    if (error != null) {
//                        Log.e(TAG, "addInvitationsSnapshotListener: error adding snapshot", error);
//                        return;
//                    }
//
//                    if (queryDocumentSnapshots != null) {
//                        List<DocumentChange> documentChanges = queryDocumentSnapshots.getDocumentChanges();
//                        documentChanges.forEach(documentChange -> {
//                            Invitation invitation = buildInvitation(documentChange.getDocument(), user);
//                            user.addInvitation(invitation);
//                        });
//                    }
//                });
    }

    @Override
    public void notifyObserversPendingInvitations(List<Invitation> invitations) {
        observers.forEach(observer -> observer.onUserPendingInvitations(invitations));
    }

    List<InvitationsDatabaseServiceObserver> observers = new ArrayList<>();
    @Override
    public void register(InvitationsDatabaseServiceObserver invitationsDatabaseServiceObserver) {
        observers.add(invitationsDatabaseServiceObserver);
    }

    @Override
    public void deregister(InvitationsDatabaseServiceObserver invitationsDatabaseServiceObserver) {
        observers.remove(invitationsDatabaseServiceObserver);
    }
}
