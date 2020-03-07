package com.cse110team24.walkwalkrevolution;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.ListView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.UsersDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingObserver;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.models.user.IUser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
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
    private List<Invitation> mInvitations = new ArrayList<>();
    ListView invitationsList;
    InvitationsListViewAdapter listViewAdapter;
    Context appContext;
    //In<IUser> namesList = new List<IUser>;

    @Before
    public void setup() {
        super.setup();
        sp = ApplicationProvider.getApplicationContext().getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE);
        sp.edit().putString(IUser.EMAIL_KEY, testUser.getEmail())
                .putString(IUser.USER_NAME_KEY, testUser.getDisplayName())
                .commit();
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.USERS)).thenReturn(usersDatabaseService);
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.INVITATIONS)).thenReturn(invitationsDatabaseService);
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.TEAMS)).thenReturn(teamsDatabaseService);
        Mockito.when(msf.createMessagingService(Mockito.any(), eq(invitationsDatabaseService))).thenReturn(mMsg);
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
       // Invitation invitation = new Invitation(testUser);
        Invitation invitation = Invitation.builder()
                .addFromUser(testUser)
                .addToEmail("amara@gmail.com")
                .addToDisplayName("Amara")
                .build();

        mInvitations.add(invitation);
        listViewAdapter = new InvitationsListViewAdapter(appContext, mInvitations);
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
        setup();
        scenario = ActivityScenario.launch(InvitationsActivity.class);
        scenario.onActivity(activity -> {
            getUIFields(activity);
            invitationsList.setAdapter(listViewAdapter);
            assertEquals(mInvitations.get(0), listViewAdapter.getItem(0));
           assertEquals(2, listViewAdapter.getCount());
//            View itemView = listViewAdapter.getView(1, null, invitationsList);
//            invitationsList.performItemClick(itemView, 1, listViewAdapter.getItemId(1));
          // invitationsList.performItemClick(invitationsList.getAdapter().getView(0, null, null), 0, invitationsList.getAdapter().getItemId(0));
           // invitationsList.performItemClick(invitationsList.getChildAt(0), 0, invitationsList.getAdapter().getItemId(0));

        });
    }



}