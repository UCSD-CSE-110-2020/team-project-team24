package com.cse110team24.walkwalkrevolution.models.user;

import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.models.team.walk.TeammateStatus;

import java.util.List;
import java.util.Map;

public interface IUser {
    String USER_NAME_KEY = "name";
    String EMAIL_KEY = "email";
    String UID_KEY = "uid";
    String TEAM_UID_KEY = "teamUid";
    TeammateStatus getLatestWalkStatus();
    String getDisplayName();
    String getEmail();
    String getUid();
    String teamUid();
    String documentKey();
    void setEmail(String email);
    void setDisplayName(String email);
    void setUid(String uid);
    void updateTeamUid(String teamUid);
    Map<String, Object> userData();
    void updateDisplayName(String displayName);
    void signOut();

    void addInvitation(Invitation invitation);
    List<Invitation> invitations();
}
