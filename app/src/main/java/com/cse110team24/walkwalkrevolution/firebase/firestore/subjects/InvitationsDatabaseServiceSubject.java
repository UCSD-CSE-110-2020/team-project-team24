package com.cse110team24.walkwalkrevolution.firebase.firestore.subjects;

import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.InvitationsDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.utils.Subject;

import java.util.List;

public interface InvitationsDatabaseServiceSubject extends Subject<InvitationsDatabaseServiceObserver> {

    /**
     * Notify this subject's observers that the requested received invitations are ready to be read.
     * <p>See also: {@link InvitationsDatabaseServiceObserver#onUserPendingInvitations(List)}</p>
     * @param invitations the requested pending invitations list.
     */
    void notifyObserversPendingInvitations(List<Invitation> invitations);
}
