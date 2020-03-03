package com.cse110team24.walkwalkrevolution.firebase.firestore.observers;

import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;

import java.util.List;

public interface InvitationsDatabaseServiceObserver {
    void onUserPendingInvitations(List<Invitation> invitations);
}
