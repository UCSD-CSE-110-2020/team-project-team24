package com.cse110team24.walkwalkrevolution.firebase.firestore;

import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.models.user.Invitation;
import com.google.firebase.firestore.DocumentReference;

public interface DatabaseService {

    DocumentReference createUserInDatabase(IUser user);

    /**
     * updates user's teamUid field locally and in database
     * @param user
     * @param teamUid
     * @return
     */
    DocumentReference setUserTeam(IUser user, String teamUid);
    DocumentReference createTeamInDatabase(ITeam team);
    DocumentReference updateTeamMembers(ITeam team);
    ITeam getUserTeam(IUser user);

    /**
     * add new invitation to receiving user's invitations list and create given document
     * @param invitation
     * @return
     */
    DocumentReference addInvitationForReceivingUser(Invitation invitation);

    DocumentReference createRootInvitationDocument(Invitation invitation);


}
