package com.cse110team24.walkwalkrevolution.models.user;

import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;

import java.util.List;
import java.util.Map;

public interface IUser {
    String getDisplayName();
    String getEmail();
    String getUid();
    String teamUid();
    String documentKey();
    void updateTeamUid(String teamUid);
    Map<String, Object> userData();
    void updateDisplayName(String displayName);
    void signOut();

    void addInvitation(Invitation invitation);
    List<Invitation> invitations();
}
