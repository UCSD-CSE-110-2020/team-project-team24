package com.cse110team24.walkwalkrevolution.mockedservices;

import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.TeamsDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.TeamsDatabaseService;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class TestTeamsDatabaseService implements TeamsDatabaseService {

    public TeamsDatabaseServiceObserver mObserver;
    // TODO: 3/5/20 set these when you need to
    public static ITeam testTeam;
    public static String testTeamUid;

    @Override
    public String createTeamInDatabase(IUser user) {
        return testTeamUid;
    }

    @Override
    public void addUserToTeam(IUser user, String teamUid) {

    }

    @Override
    public void getUserTeam(String teamUid, String currentUserDisplayName) {
        mObserver.onTeamRetrieved(testTeam);
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

    }

    @Override
    public void uploadRoute(String teamUid, Route route) {

    }

    @Override
    public void updateRoute(String teamUid, Route route) {

    }

    @Override
    public void notifyObserversTeamRoutesRetrieved(List<Route> routes, DocumentSnapshot lastRoute) {

    }
}
