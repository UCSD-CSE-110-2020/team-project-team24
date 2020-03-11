package com.cse110team24.walkwalkrevolution.mockedservices;

import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.teams.TeamsDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.teams.TeamsRoutesObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.teams.TeamsTeammatesObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.TeamsDatabaseService;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.team.walk.TeamWalk;
import com.cse110team24.walkwalkrevolution.models.team.walk.TeammateStatus;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;
import java.util.SortedMap;

/**
 * Test implementation of {@link TestTeamsDatabaseService}
 */
public class TestTeamsDatabaseService implements TeamsDatabaseService {

    public TeamsDatabaseServiceObserver mObserver;

    /**
     * contains a list of {@link IUser} teammates. Set this testTeam when testing things to do with Team.
     *
     * <p>When {@link TestTeamsDatabaseService} is called, this ITeam is sent to the observer by calling
     * {@link TeamsTeammatesObserver#onTeamRetrieved(ITeam)}</p>
     */
    public static ITeam testTeam;

    /**
     * Set this when testing if a user's team is created. Before the user has a team, their teamUid is null.
     * After their team is created, their teamUid should match this testTeamUid.
     */
    public static String testTeamUid;

    /**
     * Set this when sending routes to an observer
     */
    public static List<Route> testTeamRoutes;

    @Override
    public String createTeamInDatabase(IUser user) {
        return testTeamUid;
    }

    @Override
    public void addUserToTeam(IUser user, String teamUid) {

    }

    @Override
    public void getUserTeam(String teamUid, String currentUserDisplayName) {
        ((TeamsTeammatesObserver) mObserver).onTeamRetrieved(testTeam);
    }

    @Override
    public void notifyObserversTeamRetrieved(ITeam team) {

    }

    @Override
    public void register(TeamsDatabaseServiceObserver observer) {
        mObserver = observer;
    }

    @Override
    public void deregister(TeamsDatabaseServiceObserver observer) {

    }

    @Override
    public void getUserTeamRoutes(String teamUid, String currentUserDisplay, int routeLimitCount, DocumentSnapshot lastRoute) {
        ((TeamsRoutesObserver) mObserver).onRoutesRetrieved(testTeamRoutes, null);
    }

    @Override
    public void uploadRoute(String teamUid, Route route) {

    }

    @Override
    public void updateRoute(String teamUid, Route route) {

    }

    @Override
    public String updateCurrentTeamWalk(TeamWalk teamWalk) {
        return null;
    }

    @Override
    public void getLatestTeamWalksDescendingOrder(String teamUid, int teamWalkLimitCt) {

    }

    @Override
    public void notifyObserversTeamRoutesRetrieved(List<Route> routes, DocumentSnapshot lastRoute) {

    }

    @Override
    public void notifyObserversTeamWalksRetrieved(List<TeamWalk> walks) {

    }

    @Override
    public void changeTeammateStatusForLatestWalk(IUser user, TeamWalk teamWalk, TeammateStatus changedStatus) {

    }

    @Override
    public void getTeammateStatusesForTeamWalk(TeamWalk teamWalk, String teamUid) {

    }

    @Override
    public void notifyObserversTeamWalkStatusesRetrieved(SortedMap<String, String> statusData) {

    }
}
