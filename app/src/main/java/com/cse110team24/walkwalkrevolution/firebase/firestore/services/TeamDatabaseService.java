package com.cse110team24.walkwalkrevolution.firebase.firestore.services;

import com.cse110team24.walkwalkrevolution.firebase.firestore.DatabaseService;
import com.cse110team24.walkwalkrevolution.firebase.firestore.subjects.TeamsDatabaseServiceSubject;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.firebase.firestore.DocumentReference;

public interface TeamDatabaseService extends TeamsDatabaseServiceSubject, DatabaseService {
    String createTeamInDatabase(IUser user);
    public void addUserToTeam(IUser user);
    void getUserTeam(String teamUid);

}
