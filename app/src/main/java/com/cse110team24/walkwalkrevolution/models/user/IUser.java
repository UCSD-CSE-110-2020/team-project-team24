package com.cse110team24.walkwalkrevolution.models.user;

import java.util.Map;

public interface IUser {
    String getDisplayName();
    String getEmail();
    String getUid();
    String teamUid();
    String documentKey();
    void setTeamUid(String teamUid);
    Map<String, Object> userData();
    void updateDisplayName(String displayName);
    void signOut();
}
