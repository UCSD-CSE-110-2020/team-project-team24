package com.cse110team24.walkwalkrevolution.firebase.messaging;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingAdapter implements MessagingService {
    private FirebaseMessagingService messagingService;

    public FirebaseMessagingAdapter(FirebaseMessagingService service) {
        messagingService = service;
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        messagingService.onMessageReceived(remoteMessage);
    }
}
