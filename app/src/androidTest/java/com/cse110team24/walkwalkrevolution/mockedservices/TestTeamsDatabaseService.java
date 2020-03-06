package com.cse110team24.walkwalkrevolution.mockedservices;

import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.TeamsDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.TeamDatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.subjects.TeamsDatabaseServiceSubject;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.user.IUser;

public class TestTeamsDatabaseService implements TeamDatabaseService {

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
}
