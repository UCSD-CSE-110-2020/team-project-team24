package com.cse110team24.walkwalkrevolution;

import android.app.Activity;

import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;
import com.cse110team24.walkwalkrevolution.firebase.auth.AuthService;
import com.cse110team24.walkwalkrevolution.firebase.auth.FirebaseAuthServiceFactory;
import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.FirestoreDatabaseServiceFactory;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingService;
import com.cse110team24.walkwalkrevolution.firebase.messaging.FirebaseMessagingServiceFactory;

import org.mockito.Mockito;

public class TestInjection {
    protected FirebaseAuthServiceFactory asf;
    protected FirestoreDatabaseServiceFactory dsf;
    protected FirebaseMessagingServiceFactory msf;
    protected AuthService mAuth;
    protected DatabaseService mDb;
    protected MessagingService mMsg;
    protected Activity activity;

    void setup() {
        asf = Mockito.mock(FirebaseAuthServiceFactory.class);
        dsf = Mockito.mock(FirestoreDatabaseServiceFactory.class);
        msf = Mockito.mock(FirebaseMessagingServiceFactory.class);
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
