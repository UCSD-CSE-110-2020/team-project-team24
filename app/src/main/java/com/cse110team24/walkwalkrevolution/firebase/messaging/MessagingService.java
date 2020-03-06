package com.cse110team24.walkwalkrevolution.firebase.messaging;

import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.models.invitation.InvitationStatus;

/**
 * Handles notifications and messages for this service's Messaging provider. 
 */
public interface MessagingService extends MessagingSubject {

    void subscribeToNotificationsTopic(String topic);
    void sendInvitation(Invitation invitation);
    void updateInvitationStatus(Invitation invitation, InvitationStatus status);
}
