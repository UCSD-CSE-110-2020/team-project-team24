package com.cse110team24.walkwalkrevolution.models.user;

import java.util.Map;

public interface IUser {
    String getDisplayName();
    String getEmail();
    String getUid();
    String teamUid();
    String invitationsUid();
    String documentKey();
    void updateTeamUid(String teamUid);
    void updateInvitationsUid(String invitationsUid);
    Map<String, Object> userData();
    void updateDisplayName(String displayName);
    void signOut();
}
