package com.cse110team24.walkwalkrevolution.firebase.messaging;

import com.cse110team24.walkwalkrevolution.invitations.invitation.Invitation;
import com.google.android.gms.tasks.Task;

public interface MessagingObserver {

    /**
     * Called by the MessagingSubject this observer is observing when the subject successfully
     * sent the invitation.
     * <p>See also: {@link MessagingSubject#notifyObserversInvitationSent(Invitation)}</p>
     * @param invitation the invitation that was sent
     */
    void onInvitationSent(Invitation invitation);

    /**
     * Called by the MessagingSubject this observer is observing when the subject failed to send an
     * invitation.
     * <p>See also: {@link MessagingSubject#notifyObserversFailedInvitationSent(Task)}</p>
     * @param task the task containing the result of trying to send the invitation
     */
    void onFailedInvitationSent(Task<?> task);

}
