package com.cse110team24.walkwalkrevolution.firebase.messaging;

import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.google.android.gms.tasks.Task;

public interface MessagingObserver {

    void onInvitationSent(Invitation invitation);
    void onFailedInvitationSent(Task<?> task);

}
