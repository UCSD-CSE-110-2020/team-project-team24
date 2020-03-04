package com.cse110team24.walkwalkrevolution.firebase.firestore.subjects;

import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.InvitationsDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.utils.Subject;

import java.util.List;

public interface InvitationsDatabaseServiceSubject extends Subject<InvitationsDatabaseServiceObserver> {
    void notifyObserversPendingInvitations(List<Invitation> invitations);
}
