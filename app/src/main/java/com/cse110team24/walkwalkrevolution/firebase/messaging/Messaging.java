package com.cse110team24.walkwalkrevolution.firebase.messaging;

import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.models.invitation.InvitationStatus;

/**
 * Handles notifications and messages for this service's Messaging provider.
 */
public interface Messaging extends MessagingSubject {

    /**
     * Subscribes client to the specified topic, to be used as a trigger for cloud functions [push notifications]
     * @param topic the topic the client is subscribing to
     */
    void subscribeToNotificationsTopic(String topic);

    /**
     * Send an invitation to another user. Updates sending user's 'sent' notifications using
     * {@link com.cse110team24.walkwalkrevolution.firebase.firestore.services.InvitationsDatabaseService#addInvitationForSendingUser(Invitation)}
     * and receiving user's 'received' notifications using
     * {@link com.cse110team24.walkwalkrevolution.firebase.firestore.services.InvitationsDatabaseService#addInvitationForReceivingUser(Invitation)}
     *
     * @param invitation the invitation to be sent. Must include the sender and receiver's names, emails, a
     *                   and also include the sender's teamUid.
     */
    void sendInvitation(Invitation invitation);
    void updateInvitationStatus(Invitation invitation, InvitationStatus status);
}
