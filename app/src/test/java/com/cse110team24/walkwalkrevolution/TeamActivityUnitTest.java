package com.cse110team24.walkwalkrevolution;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.observers.TeamsDatabaseServiceObserver;
import com.cse110team24.walkwalkrevolution.firebase.messaging.MessagingObserver;

import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.team.TeamAdapter;
import com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.teammates.TeamActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.ArrayList;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@RunWith(AndroidJUnit4.class)
public class TeamActivityUnitTest extends TestInjection {

    ActivityScenario<TeamActivity> scenario;
    MessagingObserver messagingObserver;
    TeamsDatabaseServiceObserver teamDbObserver;
    SharedPreferences sp;
    ListView teammatesList;
    TextView noTeammatesInTeamText;
    ITeam teamList;
    List<IUser> listOfUsers;

    @Before
    public void setup() {
        super.setup();
        sp = ApplicationProvider.getApplicationContext().getSharedPreferences(HomeActivity.APP_PREF, Context.MODE_PRIVATE);
        sp.edit().putString(IUser.EMAIL_KEY, testUser.getEmail())
                .putString(IUser.USER_NAME_KEY, testUser.getDisplayName())
                .commit();
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.USERS)).thenReturn(usersDatabaseService);
        //Mockito.when(dsf.createDatabaseService(DatabaseService.Service.INVITATIONS)).thenReturn(invitationsDatabaseService);
        Mockito.when(dsf.createDatabaseService(DatabaseService.Service.TEAMS)).thenReturn(teamsDatabaseService);
    }

    private void getUIFields(Activity activity) {
        teammatesList = activity.findViewById(R.id.list_members_in_team);
        noTeammatesInTeamText = activity.findViewById(R.id.text_no_teammates);
    }
    private void mockTeamDbRegister() {
        Mockito.doAnswer(invocation -> {
            teamDbObserver = invocation.getArgument(0);
            return invocation;
        }).when(teamsDatabaseService).register(any());
    }

    public void savedTeamUIDToPreferences() {
        sp.edit().putString("teamUid", "666")
                .commit();
    }

    @Test
    public void emptyTeamOnTeamRetrieved() {
        mockTeamDbRegister();

        listOfUsers = new ArrayList<IUser>();
        teamList = new TeamAdapter(listOfUsers);

        savedTeamUIDToPreferences();
        Mockito.doAnswer(invocation -> {
            teamDbObserver.onTeamRetrieved(teamList);
            return null;
        }).when(teamsDatabaseService).getUserTeam(any(), any());

        scenario = ActivityScenario.launch(TeamActivity.class);
        scenario.onActivity(activity -> {
            Mockito.verify(teamsDatabaseService).register(any());
            getUIFields(activity);
            assertEquals(View.VISIBLE, noTeammatesInTeamText.getVisibility());
            assertEquals(0, teammatesList.getCount());
        });
    }

    @Test
    public void TeamOfTwoOnTeamRetrieved() {
        mockTeamDbRegister();

        IUser userOne = FirebaseUserAdapter.builder()
                .addDisplayName("testerOne")
                .addEmail("testOne@gmail.com")
                .addTeamUid("666")
                .addUid("1")
                .build();
        IUser userTwo = FirebaseUserAdapter.builder()
                .addDisplayName("testerTwo")
                .addEmail("testTwo@gmail.com")
                .addTeamUid("666")
                .addUid("2")
                .build();
        listOfUsers = new ArrayList<IUser>();
        teamList = new TeamAdapter(listOfUsers);
        teamList.addMember(userOne);
        teamList.addMember(userTwo);

        savedTeamUIDToPreferences();
        Mockito.doAnswer(invocation -> {
            teamDbObserver.onTeamRetrieved(teamList);
            return null;
        }).when(teamsDatabaseService).getUserTeam(any(), any());

        scenario = ActivityScenario.launch(TeamActivity.class);
        scenario.onActivity(activity -> {
            Mockito.verify(teamsDatabaseService).register(any());
            getUIFields(activity);
            teamDbObserver.onTeamRetrieved(teamList);

            assertEquals(View.GONE, noTeammatesInTeamText.getVisibility());
            assertEquals(2, teammatesList.getCount());
        });
    }

    @Test
    public void TeamOfFourOnTeamRetrieved() {
        mockTeamDbRegister();

        IUser userOne = FirebaseUserAdapter.builder()
                .addDisplayName("testerOne")
                .addEmail("testOne@gmail.com")
                .addTeamUid("666")
                .addUid("1")
                .build();
        IUser userTwo = FirebaseUserAdapter.builder()
                .addDisplayName("testerTwo")
                .addEmail("testTwo@gmail.com")
                .addTeamUid("666")
                .addUid("2")
                .build();
        IUser userThree = FirebaseUserAdapter.builder()
                .addDisplayName("testerThree")
                .addEmail("testThree@gmail.com")
                .addTeamUid("666")
                .addUid("3")
                .build();
        IUser userFour = FirebaseUserAdapter.builder()
                .addDisplayName("testerFour")
                .addEmail("testFour@gmail.com")
                .addTeamUid("666")
                .addUid("4")
                .build();
        listOfUsers = new ArrayList<IUser>();
        teamList = new TeamAdapter(listOfUsers);
        teamList.addMember(userOne);
        teamList.addMember(userTwo);
        teamList.addMember(userThree);
        teamList.addMember(userFour);

        savedTeamUIDToPreferences();
        Mockito.doAnswer(invocation -> {
            teamDbObserver.onTeamRetrieved(teamList);
            return null;
        }).when(teamsDatabaseService).getUserTeam(any(), any());

        scenario = ActivityScenario.launch(TeamActivity.class);
        scenario.onActivity(activity -> {
            Mockito.verify(teamsDatabaseService).getUserTeam(any(), any());
            Mockito.verify(teamsDatabaseService).register(any());
            getUIFields(activity);
            assertEquals(View.GONE, noTeammatesInTeamText.getVisibility());
            assertEquals(4, teammatesList.getCount());
        });
    }

}
