package com.cse110team24.walkwalkrevolution.firebase.firestore.services;

import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.subjects.InvitationsDatabaseServiceSubject;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.android.gms.tasks.Task;

import java.util.List;

/**
 * Handles provider database interactions with invitation-related documents and collections.
 */
public interface InvitationsDatabaseService extends InvitationsDatabaseServiceSubject, DatabaseService {

    /**
     * Create an invitation document for the receiving user's received invitations in
     * this service's provider database.
     *
     * <p>Updates the invitation's ID to that of the newly created document</p>
     * @param invitation the invitation being sent to the user.
     * @return a task containing the result of trying to create the invitation document.
     */
    Task<?> addInvitationForReceivingUser(Invitation invitation);

    /**
     * Create an invitation document for the sending user's sent invitations in
     * this service's provider database.
     * @param invitation the invitation being sent by the user.
     * @return a task containing the result of trying to create the invitation document.
     */
    Task<?> addInvitationForSendingUser(Invitation invitation);

    /**
     * Update the given invitation's document for the receiving user in this service's provider
     * database.
     * @param invitation the invitation that is being updated. Must have an invitation ID.
     */
    void updateInvitationForReceivingUser(Invitation invitation);

    /**
     * Update the given invitation's document for the sending user in this service's provider
     * database.
     * @param invitation the invitation that is being updated. Must have an invitation ID.
     */
    void updateInvitationForSendingUser(Invitation invitation);

    /**
     * Query this service's provider database for the given user's received invitations that have
     * {@link com.cse110team24.walkwalkrevolution.models.invitation.InvitationStatus} equal to PENDING.
     * <p>On complete, calls {@link InvitationsDatabaseServiceSubject#notifyObserversPendingInvitations(List)} to
     * notify observers that the invitations are ready to read.</p>
     * @param user
     */
    void getUserPendingInvitations(IUser user);
    void addInvitationsSnapshotListener(IUser user);
}
