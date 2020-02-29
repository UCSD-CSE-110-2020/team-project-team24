package com.cse110team24.walkwalkrevolution.firebase.messaging;

import com.google.firebase.messaging.RemoteMessage;

public interface MessagingService {
    void onMessageReceived(RemoteMessage remoteMessage);
}
