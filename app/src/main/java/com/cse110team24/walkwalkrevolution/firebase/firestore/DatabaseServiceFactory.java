package com.cse110team24.walkwalkrevolution.firebase.firestore;

import com.cse110team24.walkwalkrevolution.firebase.firestore.services.DatabaseService;

public interface DatabaseServiceFactory {
    DatabaseService createDatabaseService(DatabaseService.Service service);
}
