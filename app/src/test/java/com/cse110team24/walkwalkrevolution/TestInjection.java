package com.cse110team24.walkwalkrevolution;

import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;
import com.cse110team24.walkwalkrevolution.firebase.auth.AuthService;
import com.cse110team24.walkwalkrevolution.firebase.auth.AuthServiceFactory;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseServiceFactory;

import org.mockito.Mockito;

public class TestInjection {
    protected AuthServiceFactory asf;
    protected DatabaseServiceFactory dsf;
    protected AuthService mAuth;
    protected DatabaseService mDb;

    void setup() {
        asf = Mockito.mock(AuthServiceFactory.class);
        dsf = Mockito.mock(DatabaseServiceFactory.class);
        mAuth = Mockito.mock(AuthService.class);
        mDb = Mockito.mock(DatabaseService.class);

        Mockito.when(asf.createAuthService()).thenReturn(mAuth);
        Mockito.when(dsf.createDatabaseService()).thenReturn(mDb);

        FirebaseApplicationWWR.setDatabaseServiceFactory(dsf);
        FirebaseApplicationWWR.setAuthServiceFactory(asf);

    }
}
