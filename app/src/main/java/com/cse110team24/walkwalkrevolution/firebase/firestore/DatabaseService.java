package com.cse110team24.walkwalkrevolution.firebase.firestore;

import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.google.firebase.firestore.DocumentReference;

public interface DatabaseService {

    DocumentReference createUserInDatabase(IUser user);
    DocumentReference updateUserTeam(IUser user, String teamUid);
    DocumentReference createTeamInDatabase(ITeam team);
    DocumentReference updateTeamMembers(ITeam team);
    ITeam getUserTeam(IUser user);
}
