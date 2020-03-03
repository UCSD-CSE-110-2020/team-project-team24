package com.cse110team24.walkwalkrevolution.firebase.firestore;

import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.utils.Subject;

import java.util.List;
import java.util.Map;

public interface DatabaseServiceSubject extends Subject<DatabaseServiceObserver> {
    void notifyObserversFieldRetrieved(Object field);
    void notifyObserversPendingInvitations(List<Invitation> invitations);
}
