package com.cse110team24.walkwalkrevolution.firebase.firestore;

import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import org.w3c.dom.Document;

import java.util.List;

public interface DatabaseService extends DatabaseServiceSubject {

    void createUserInDatabase(IUser user);
    void setUserTeam(IUser user, String teamUid);
    String createTeamInDatabase(IUser user);

    DocumentReference updateTeamMembers(ITeam team);
    Task<?> addInvitationForReceivingUser(Invitation invitation);
    Task<?> addInvitationForSendingUser(Invitation invitation);
    List<Invitation> getUserPendingInvitations(IUser user);
    void getUserTeam(String teamUid);
    void addInvitationsSnapshotListener(IUser user);
    DocumentReference addUserMessagingRegistrationToken(IUser user, String token);

    void getUserData(IUser user);
}
