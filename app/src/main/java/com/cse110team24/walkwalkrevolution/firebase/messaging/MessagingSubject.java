package com.cse110team24.walkwalkrevolution.firebase.messaging;

import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.utils.Subject;
import com.google.android.gms.tasks.Task;

public interface MessagingSubject extends Subject<MessagingObserver> {
    void notifyObserversInvitationSent(Invitation invitation);
    void notifyObserversFailedInvitationSent(Task<?> task);
}
