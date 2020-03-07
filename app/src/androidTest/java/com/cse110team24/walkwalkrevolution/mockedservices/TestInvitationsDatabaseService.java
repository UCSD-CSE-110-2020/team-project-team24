package com.cse110team24.walkwalkrevolution.mockedservices;

import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.InvitationsDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.InvitationsDatabaseService;
import com.cse110team24.walkwalkrevolution.invitations.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.android.gms.tasks.Task;

import java.util.List;

/**
 * Test implementation of {@link InvitationsDatabaseService}.
 */
public class TestInvitationsDatabaseService implements InvitationsDatabaseService {

    /**
     * The single observer registered to this service.
     */
    public InvitationsDatabaseServiceObserver mObserver;

    /**
     * Set this when testing invitations received.
     * <p>This list is sent to observers by {@link TestInvitationsDatabaseService#getUserPendingInvitations(IUser)}
     * when {@link InvitationsDatabaseService#getUserPendingInvitations(IUser) is called}</p>
     */
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
