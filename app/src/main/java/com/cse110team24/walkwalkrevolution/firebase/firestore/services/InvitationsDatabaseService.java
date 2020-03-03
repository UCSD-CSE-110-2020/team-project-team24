package com.cse110team24.walkwalkrevolution.firebase.firestore.services;

import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.subjects.InvitationsDatabaseServiceSubject;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.android.gms.tasks.Task;

public interface InvitationsDatabaseService extends InvitationsDatabaseServiceSubject, DatabaseService {
    Task<?> addInvitationForReceivingUser(Invitation invitation);
    Task<?> addInvitationForSendingUser(Invitation invitation);
    void getUserPendingInvitations(IUser user);
    void addInvitationsSnapshotListener(IUser user);
}
