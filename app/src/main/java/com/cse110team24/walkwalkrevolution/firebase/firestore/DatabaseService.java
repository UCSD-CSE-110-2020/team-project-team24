package com.cse110team24.walkwalkrevolution.firebase.firestore;

import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import org.w3c.dom.Document;

import java.util.List;

public interface DatabaseService {

    DocumentReference createUserInDatabase(IUser user);
    DocumentReference setUserTeam(IUser user, String teamUid);
    DocumentReference createTeamInDatabase(ITeam team);
    DocumentReference updateTeamMembers(ITeam team);
    Task<DocumentReference> addInvitationForReceivingUser(Invitation invitation);
    DocumentReference createRootInvitationDocument(Invitation invitation);
    List<Invitation> getUserPendingInvitations(IUser user);
    ITeam getUserTeam(IUser user);
    Object getField(String path, String fieldKey);
    void addInvitationsSnapshotListener(IUser user);
    DocumentReference addUserMessagingRegistrationToken(IUser user, String token);
}
