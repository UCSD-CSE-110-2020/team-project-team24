package com.cse110team24.walkwalkrevolution.firebase.messaging;

import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.observer.Subject;

public interface MessagingService extends Subject<MessagingObserver> {

    void subscribeToNotificationsTopic(String topic);
    void sendInvitation(Invitation invitation);
}
