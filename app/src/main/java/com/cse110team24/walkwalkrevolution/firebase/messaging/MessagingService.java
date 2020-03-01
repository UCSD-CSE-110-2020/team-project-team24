package com.cse110team24.walkwalkrevolution.firebase.messaging;

import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;

public interface MessagingService {

    void subscribeToNotificationsTopic(String topic);
    void sendInvitation(Invitation invitation);
}
