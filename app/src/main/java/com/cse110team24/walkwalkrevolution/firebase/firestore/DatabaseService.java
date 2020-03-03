package com.cse110team24.walkwalkrevolution.firebase.firestore;

import com.cse110team24.walkwalkrevolution.models.team.ITeam;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import org.w3c.dom.Document;

import java.util.List;

// TODO: 3/3/20 split this interface into the three specialized interface services 
public interface DatabaseService{

    enum Service {
        USERS,
        TEAMS,
        INVITATIONS
    }
}
