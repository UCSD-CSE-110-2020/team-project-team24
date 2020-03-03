package com.cse110team24.walkwalkrevolution.firebase.firestore.services;

import com.cse110team24.walkwalkrevolution.firebase.firestore.subjects.TeamsDatabaseServiceSubject;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.firebase.firestore.DocumentReference;

public interface TeamDatabaseService extends TeamsDatabaseServiceSubject {
    String createTeamInDatabase(IUser user);
    DocumentReference updateTeamMembers(ITeam team);
    void getUserTeam(String teamUid);

}
