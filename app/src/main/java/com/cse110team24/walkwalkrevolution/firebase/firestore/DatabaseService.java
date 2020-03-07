package com.cse110team24.walkwalkrevolution.firebase.firestore;

import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import org.w3c.dom.Document;

import java.util.List;

/**
 * Associates various database handlers with a common type.
 * <p>See also: {@link com.cse110team24.walkwalkrevolution.firebase.firestore.services.UsersDatabaseService}</p>
 * <p>See also: {@link com.cse110team24.walkwalkrevolution.firebase.firestore.services.TeamsDatabaseService}</p>
 * <p>See also: {@link com.cse110team24.walkwalkrevolution.firebase.firestore.services.InvitationsDatabaseService}</p>
 */
public interface DatabaseService{

    /**
     * Various observer interfaces that can be instantiated.
     */
    enum Service {
        USERS,
        TEAMS,
        INVITATIONS,
        LEGACY
    }
}
