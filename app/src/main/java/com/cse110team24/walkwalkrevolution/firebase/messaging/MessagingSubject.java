package com.cse110team24.walkwalkrevolution.firebase.messaging;

import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.utils.Subject;
import com.google.android.gms.tasks.Task;

public interface MessagingSubject extends Subject<MessagingObserver> {
    /**
     * Notify this subject's observers that the given invitation was sent successfully.
     * <p>See also: {@link MessagingObserver#onInvitationSent(Invitation)}</p>
     * @param invitation the invitation that was sent
     */
    void notifyObserversInvitationSent(Invitation invitation);

    /**
     * Notify this subject's observers that an invitation could not be sent.
     * <p>See also: {@link MessagingObserver#onFailedInvitationSent(Task)}</p>
     * @param task the task containing the result of trying to send the invitation
     */
    void notifyObserversFailedInvitationSent(Task<?> task);
}
