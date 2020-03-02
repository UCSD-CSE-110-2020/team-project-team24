package com.cse110team24.walkwalkrevolution;

import android.app.Activity;

import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;
import com.cse110team24.walkwalkrevolution.firebase.auth.AuthService;
import com.cse110team24.walkwalkrevolution.firebase.auth.AuthServiceFactory;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseServiceFactory;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingService;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingServiceFactory;

import org.mockito.Mockito;

public class TestInjection {
    protected AuthServiceFactory asf;
    protected DatabaseServiceFactory dsf;
    protected MessagingServiceFactory msf;
    protected AuthService mAuth;
    protected DatabaseService mDb;
    protected MessagingService mMsg;
    protected Activity activity;

    void setup() {
        asf = Mockito.mock(AuthServiceFactory.class);
        dsf = Mockito.mock(DatabaseServiceFactory.class);
        msf = Mockito.mock(MessagingServiceFactory.class);
        mAuth = Mockito.mock(AuthService.class);
        mDb = Mockito.mock(DatabaseService.class);
        mMsg = Mockito.mock(MessagingService.class);

        Mockito.when(asf.createAuthService()).thenReturn(mAuth);
        Mockito.when(dsf.createDatabaseService()).thenReturn(mDb);
        Mockito.when(msf.createMessagingService(activity, mDb)).thenReturn(mMsg);

        FirebaseApplicationWWR.setDatabaseServiceFactory(dsf);
        FirebaseApplicationWWR.setAuthServiceFactory(asf);
        FirebaseApplicationWWR.setMessagingServiceFactory(msf);

    }
}
