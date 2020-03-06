package com.cse110team24.walkwalkrevolution.firebase.firestore.services;

import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.subjects.TeamsDatabaseServiceSubject;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.firebase.firestore.DocumentReference;

public interface TeamDatabaseService extends TeamsDatabaseServiceSubject, DatabaseService {
    String createTeamInDatabase(IUser user);
    void addUserToTeam(IUser user, String teamUid);
    void getUserTeam(String teamUid, String currentUserDisplayName);
    void getUserTeamRoutes(String teamUid, String currentUserDisplay, int routeLimitCount);
}
