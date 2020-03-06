package com.cse110team24.walkwalkrevolution.mockedservices;

import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseServiceFactory;

public class TestDatabaseServiceFactory implements DatabaseServiceFactory {
    @Override
    public DatabaseService createDatabaseService(DatabaseService.Service service) {
        switch (service) {
            case USERS:
                return new TestUsersDatabaseService();

            case TEAMS:
                return new TestTeamsDatabaseService();

            case INVITATIONS:
                return new TestInvitationsDatabaseService();
            default:
                return null;
        }
    }
}
