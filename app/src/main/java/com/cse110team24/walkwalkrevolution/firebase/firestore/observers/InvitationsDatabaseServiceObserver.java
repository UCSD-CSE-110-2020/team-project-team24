package com.cse110team24.walkwalkrevolution.firebase.firestore.observers;

import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;

import java.util.List;

public interface InvitationsDatabaseServiceObserver {

    /**
     * Called by the InvitationsDatabaseServiceSubject this observer is observing when the requested invitations
     * list is ready to be read.
     * @param invitations the requested pending invitations list.
     */
    void onUserPendingInvitations(List<Invitation> invitations);
}
