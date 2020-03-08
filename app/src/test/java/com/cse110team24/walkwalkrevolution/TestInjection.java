package com.cse110team24.walkwalkrevolution;

import android.app.Activity;

import com.cse110team24.walkwalkrevolution.application.FirebaseApplicationWWR;
import com.cse110team24.walkwalkrevolution.firebase.auth.Auth;
import com.cse110team24.walkwalkrevolution.firebase.auth.FirebaseAuthFactory;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.FirestoreDatabaseServiceFactory;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.InvitationsDatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.TeamsDatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.services.UsersDatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.messaging.FirebaseMessagingFactory;
import com.cse110team24.walkwalkrevolution.firebase.messaging.Messaging;
import com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter;
import com.cse110team24.walkwalkrevolution.models.user.IUser;


import org.mockito.Mockito;

public class TestInjection {
    protected FirebaseAuthFactory asf;
    protected FirestoreDatabaseServiceFactory dsf;
    protected FirebaseMessagingFactory msf;
    protected Auth mAuth;
    protected DatabaseService mDb;
    protected Messaging mMsg;
    protected Activity activity;


    protected UsersDatabaseService usersDatabaseService;
    protected TeamsDatabaseService teamsDatabaseService;
    protected InvitationsDatabaseService invitationsDatabaseService;

    IUser aTestUser = FirebaseUserAdapter.builder()
            .addDisplayName("Ival")
            .addEmail("tester@gmail.com")
            .addUid("4")
            .build();

    IUser testUser = FirebaseUserAdapter.builder()
            .addDisplayName("tester")
            .addEmail("test@gmail.com")
            .addTeamUid("666")
            .addUid("0")
            .build();

    IUser otherUser = FirebaseUserAdapter.builder()
            .addDisplayName("cheery")
            .addEmail("amara@gmail.com")
            .build();

    void setup() {
        asf = Mockito.mock(FirebaseAuthFactory.class);
        dsf = Mockito.mock(FirestoreDatabaseServiceFactory.class);
        msf = Mockito.mock(FirebaseMessagingFactory.class);
        mAuth = Mockito.mock(Auth.class);
        mDb = Mockito.mock(DatabaseService.class);
        mMsg = Mockito.mock(Messaging.class);

        Mockito.when(asf.createAuthService()).thenReturn(mAuth);
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.LEGACY)).thenReturn(mDb);
        Mockito.when(msf.createMessagingService(activity, mDb)).thenReturn(mMsg);

        FirebaseApplicationWWR.setDatabaseServiceFactory(dsf);
        FirebaseApplicationWWR.setAuthServiceFactory(asf);
        FirebaseApplicationWWR.setMessagingServiceFactory(msf);


        usersDatabaseService = Mockito.mock(UsersDatabaseService.class);
        teamsDatabaseService = Mockito.mock(TeamsDatabaseService.class);
        invitationsDatabaseService = Mockito.mock(InvitationsDatabaseService.class);

        Mockito.when(mAuth.getUser()).thenReturn(testUser);
    }
}
