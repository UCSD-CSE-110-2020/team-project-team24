package com.cse110team24.walkwalkrevolution.mockedservices;

import android.app.Activity;

import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingObserver;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingService;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingServiceFactory;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.models.invitation.InvitationStatus;
import com.google.android.gms.tasks.Task;

public class TestMessage {

    // TODO: 3/5/20 set these when you need to
    public static boolean invitationSentSuccess;

    public static class TestMessagingServiceFactory implements MessagingServiceFactory {

        @Override
        public MessagingService createMessagingService(Activity activity, DatabaseService databaseService) {
            return new TestMessagingService();
        }
    }

    public static class TestMessagingService implements MessagingService {

        public MessagingObserver mObserver;

        @Override
        public void subscribeToNotificationsTopic(String topic) {

        }

        @Override
        public void sendInvitation(Invitation invitation) {
            if (invitationSentSuccess) {
                mObserver.onInvitationSent(invitation);
            } else {
                mObserver.onFailedInvitationSent(null);
            }
        }

        @Override
        public void updateInvitationStatus(Invitation invitation, InvitationStatus status) {

        }

        @Override
        public void notifyObserversInvitationSent(Invitation invitation) {

        }

        @Override
        public void notifyObserversFailedInvitationSent(Task<?> task) {

        }

        @Override
        public void register(MessagingObserver observer) {
            mObserver = observer;
        }

        @Override
        public void deregister(MessagingObserver observer) {

        }
    }
}
