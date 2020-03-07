package com.cse110team24.walkwalkrevolution.mockedservices;

import android.app.Activity;

import com.cse110team24.walkwalkrevolution.firebase.firestore.services.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingObserver;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingService;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingServiceFactory;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.models.invitation.InvitationStatus;
import com.google.android.gms.tasks.Task;

/**
 * Test implementation of {@link MessagingService}.
 */
public class TestMessage {

    /**
     * Set to true when testing invitation sent; false when testing invitation sending errors.
     *
     * <p>When this is true, {@link TestMessagingService} will call {@link MessagingObserver#onInvitationSent(Invitation)}</p>
     * <p>When this is false, {@link TestMessagingService} will call {@link MessagingObserver#onFailedInvitationSent(Task)}</p>
     */
    public static boolean invitationSentSuccess;

    /**
     * Creates instances of {@link TestMessagingService}.
     */
    public static class TestMessagingServiceFactory implements MessagingServiceFactory {

        /**
         * @param activity unused
         * @param databaseService unused
         * @return an instance of {@link TestMessagingService}
         */
        @Override
        public MessagingService createMessagingService(Activity activity, DatabaseService databaseService) {
            return new TestMessagingService();
        }
    }

    /**
     * Test implementation of {@link MessagingService}.
     */
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
