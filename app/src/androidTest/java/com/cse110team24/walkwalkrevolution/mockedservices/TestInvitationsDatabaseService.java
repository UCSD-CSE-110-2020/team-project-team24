package com.cse110team24.walkwalkrevolution.mockedservices;

import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.InvitationsDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.InvitationsDatabaseService;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class TestInvitationsDatabaseService implements InvitationsDatabaseService {

    public InvitationsDatabaseServiceObserver mObserver;
    // TODO: 3/5/20 set these when needed
    public static List<Invitation> testInvitationsList;

    @Override
    public Task<?> addInvitationForReceivingUser(Invitation invitation) {
        return null;
    }

    @Override
    public Task<?> addInvitationForSendingUser(Invitation invitation) {
        return null;
    }

    @Override
    public void updateInvitationForReceivingUser(Invitation invitation) {

    }

    @Override
    public void updateInvitationForSendingUser(Invitation invitation) {

    }

    @Override
    public void getUserPendingInvitations(IUser user) {
        mObserver.onUserPendingInvitations(testInvitationsList);
    }

    @Override
    public void addInvitationsSnapshotListener(IUser user) {

    }

    @Override
    public void notifyObserversPendingInvitations(List<Invitation> invitations) {

    }

    @Override
    public void register(InvitationsDatabaseServiceObserver invitationsDatabaseServiceObserver) {
        mObserver = invitationsDatabaseServiceObserver;
    }

    @Override
    public void deregister(InvitationsDatabaseServiceObserver invitationsDatabaseServiceObserver) {

    }
}
