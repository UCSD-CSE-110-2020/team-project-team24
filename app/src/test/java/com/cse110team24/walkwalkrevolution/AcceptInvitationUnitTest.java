package com.cse110team24.walkwalkrevolution;

import android.content.Context;

import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.models.user.IUser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.mockito.ArgumentMatchers.eq;

@RunWith(AndroidJUnit4.class)
public class AcceptInvitationUnitTest extends TestInjection {
    @Before
    public void setup() {
        super.setup();
        ApplicationProvider.getApplicationContext().getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE)
                .edit()
                .putString(IUser.EMAIL_KEY, "test@gmail.com")
                .putString(IUser.USER_NAME_KEY, "tester")
                .commit();
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.USERS)).thenReturn(usersDatabaseService);
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.INVITATIONS)).thenReturn(invitationsDatabaseService);
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.TEAMS)).thenReturn(teamDatabaseService);
        Mockito.when(msf.createMessagingService(Mockito.any(), eq(invitationsDatabaseService))).thenReturn(mMsg);
    }

    @Test
    public void testUpdateTeamInDatabase() {

    }
}
