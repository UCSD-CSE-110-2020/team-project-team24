package com.cse110team24.walkwalkrevolution.firebase.firestore;

import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public interface DatabaseService {

    DocumentReference createUserInDatabase(IUser user);
    DocumentReference setUserTeam(IUser user, String teamUid);
    DocumentReference createTeamInDatabase(ITeam team);
    DocumentReference updateTeamMembers(ITeam team);
    CollectionReference addInvitationForReceivingUser(Invitation invitation);
    DocumentReference createRootInvitationDocument(Invitation invitation);
    List<Invitation> getUserInvitations(IUser user);
    ITeam getUserTeam(IUser user);

}
