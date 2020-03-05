package com.cse110team24.walkwalkrevolution;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.ListView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.UsersDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingObserver;
import com.cse110team24.walkwalkrevolution.models.user.IUser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.shadows.ShadowToast;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(AndroidJUnit4.class)
public class InvitationsActivityUnitTest extends TestInjection {

    ActivityScenario<InvitationsActivity> scenario;
    MessagingObserver messagingObserver;
    UsersDatabaseServiceObserver userDbObserver;
    SharedPreferences sp;
    Button acceptBtn;
    Button declineBtn;
    ListView invitationsList;

    @Before
    public void setup() {
        super.setup();
        sp = ApplicationProvider.getApplicationContext().getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE);
        sp.edit().putString(IUser.EMAIL_KEY, testUser.getEmail())
                .putString(IUser.USER_NAME_KEY, testUser.getDisplayName())
                .commit();
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.USERS)).thenReturn(usersDatabaseService);
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.INVITATIONS)).thenReturn(invitationsDatabaseService);
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.TEAMS)).thenReturn(teamDatabaseService);
        Mockito.when(msf.createMessagingService(Mockito.any(), eq(invitationsDatabaseService))).thenReturn(mMsg);
        mockInvitationSent();
    }

    private void getUIFields(Activity activity) {
        acceptBtn = activity.findViewById(R.id.buttonAccept);
        declineBtn = activity.findViewById(R.id.buttonDecline);
        invitationsList = activity.findViewById(R.id.invitationList);
    }

    private void mockUserDbRegister() {
        Mockito.doAnswer(invocation -> {
            userDbObserver = invocation.getArgument(0);
            return invocation;
        }).when(usersDatabaseService).register(any());
    }

    private void mockOtherUserExists() {
        Mockito.doAnswer(invocation -> {
            userDbObserver.onUserExists(otherUser);
            return null;
        }).when(usersDatabaseService).checkIfOtherUserExists(Mockito.any());
    }

    private void mockInvitationSent() {
        sp.edit().putString(IUser.TEAM_UID_KEY, testUser.teamUid()).commit();
        mockUserDbRegister();
        mockOtherUserExists();

        Mockito.doAnswer(invocation -> {
            messagingObserver = invocation.getArgument(0);
            return invocation;
        }).when(mMsg).register(any());

        Mockito.doAnswer(invocation -> {
            messagingObserver.onInvitationSent(null);
            return null;
        }).when(mMsg).sendInvitation(Mockito.any());
    }

    @Test
    public void approveInvitation() {
        scenario = ActivityScenario.launch(InvitationsActivity.class);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            assertNotNull(invitationsList);

        });
    }



}