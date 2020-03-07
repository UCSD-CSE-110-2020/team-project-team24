package com.cse110team24.walkwalkrevolution.firebase.firestore;

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
