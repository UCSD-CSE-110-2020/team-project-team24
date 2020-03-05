package com.cse110team24.walkwalkrevolution.mockedservices;

import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseServiceFactory;

public class TestDatabaseServiceFactory implements DatabaseServiceFactory {
    @Override
    public DatabaseService createDatabaseService(DatabaseService.Service service) {
        return null;
    }
}
