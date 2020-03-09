package com.cse110team24.walkwalkrevolution.firebase.firestore.services;

import com.cse110team24.walkwalkrevolution.firebase.firestore.subjects.TeamsDatabaseServiceSubject;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.team.TeamWalk;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

/**
 * Handles provider database interactions with team-related documents and collections.
 */
public interface TeamsDatabaseService extends TeamsDatabaseServiceSubject, DatabaseService {
    /**
     * Create a team document in this service's provider database.
     * @param user the user whose team is being created.
     * @return the newly created document's ID, to be used as the user's teamUid.
     */
    String createTeamInDatabase(IUser user);

    /**
     * Updates the team document in this service's provider database to include the new user as a
     * teammate.
     * @param user the user who is being added as a teammate to the team
     * @param teamUid the already-existing team's ID
     */
    void addUserToTeam(IUser user, String teamUid);

    /**
     * Query this service's provider database for the teammates in the specified team document.
     * All teammates' data are returned except the currently signed in user's.
     * <p>On complete, a call to {@link TeamsDatabaseServiceSubject#notifyObserversTeamRetrieved(ITeam)} is made
     * to notify observers that the team is ready.</p>
     * @param teamUid the already-existing team's ID
     * @param currentUserDisplayName the currently signed-in user's displayName
     */
    void getUserTeam(String teamUid, String currentUserDisplayName);

    /**
     * Query this service's provider database for the routes in the specified team document, limited
     * by the amount of routes requested. All teammates's routes are given except those of the currently
     * signed in user.
     * <p>On complete, a call to {@link TeamsDatabaseServiceSubject#notifyObserversTeamRoutesRetrieved(List, DocumentSnapshot)}
     * is made containing up to the amount of requested routes and a DocumentSnapshot to the last document retrieved.</p>
     * @param teamUid the already-existing team's ID
     * @param currentUserDisplayName the currently signed in user's display name.
     * @param routeLimitCount the amount of routes to Query the database for. List of routes may be <=
     *                        this number.
     * @param lastRoute the last retrieved route's document, used to determine where to start querying this
     *                  service's provider database from given the last request. Should be null if ruquesting
     *                  routes for the first time.
     */
    void getUserTeamRoutes(String teamUid, String currentUserDisplayName, int routeLimitCount, DocumentSnapshot lastRoute);

    /**
     * Creates a route document in this service's provider database for the given team's routes.
     * @param teamUid the already-existing team's ID
     * @param route the route whose document is being added to the specified team's routes
     */
    void uploadRoute(String teamUid, Route route);

    /**
     * Updates a route document in this service's provider database for the given team's routes.
     * @param teamUid the already-existing team's ID
     * @param route the route whose document is being updated in the specified team's routes
     */
    void updateRoute(String teamUid, Route route);

    /**
     * Update current Team Walk in database or create it if it DNE
     * @param teamWalk team walk that is being proposed, scheduled, cancelled, or withdrawn
     * @return the team walk's uid whether it was created or updated
     */
    String updateCurrentTeamWalk(TeamWalk teamWalk);

    /**
     * Query this service's provider database for up to teamWalkLimitCt amount of team walks,
     * in descending order by timestamp of day walk was proposed
     * @param teamUid the uid of the team whose walks are being requested
     * @param teamWalkLimitCt the amount of team walks to query from database.
     * @return a Task with the result of this operation, to be handled by caller
     */
    Task<?> getLatestTeamWalksDescendingOrder(String teamUid, int teamWalkLimitCt);
}
