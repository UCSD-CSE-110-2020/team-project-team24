package com.cse110team24.walkwalkrevolution.firebase.firestore;

import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.user.IUser;

import java.util.List;
import java.util.Map;
import java.util.Observer;

public interface DatabaseServiceObserver {
    void onFieldRetrieved(Object field);
}
