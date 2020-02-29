package com.cse110team24.walkwalkrevolution.firebase.firestore;

import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.firebase.firestore.DocumentReference;

public interface DatabaseService {

    DocumentReference createUserInDatabase(IUser user);
    DocumentReference updateUserTeam(IUser user, String teamUid);
    DocumentReference createTeamInDatabase(ITeam team);
    DocumentReference updateTeamMembers(ITeam team);

    /**
     * creates new invitations document for user, and automatically updates user's invitationsUid by
     * calling setUserInvitations()
     * @param user user whose invitations are being created
     * @return document reference to invitations for given user
     */
    DocumentReference createUserInvitationsInDatabase(IUser user);

    /**
     * updates user's invitationsUid field locally and in database
     * @param user the user whose invitationsUid field is being set
     * @param invitationsUid the uid of the invitations document
     * @return document reference to user document
     */
    DocumentReference setUserInvitations(IUser user, String invitationsUid);

    /**
     * adds new invitation to given user's invitations documen
     * @param user the user whose invitations are being updated
     * @param inviteUserUid uid of the user who is inviting given user
     * @return
     */
    DocumentReference updateUserInvitations(IUser user, String inviteUserUid);
    ITeam getUserTeam(IUser user);
}
