package com.cse110team24.walkwalkrevolution.models.user;

import java.util.Map;

public interface IUser {
    String getDisplayName();
    String getEmail();
    String getUid();
    String getTeamUid();
    String getDocumentKey();
    void setTeamUid(String teamUid);
    Map<String, Object> getDBFields();
    void updateDisplayName(String name);
    void signOut();
}
