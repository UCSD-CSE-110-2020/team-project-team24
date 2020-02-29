package com.cse110team24.walkwalkrevolution.models.team;

import com.cse110team24.walkwalkrevolution.models.user.IUser;

import java.util.List;
import java.util.Map;

public interface ITeam {
    List<IUser> getTeam();
    String getUid();
    void setUid(String uid);
    Map<String, Object> teamData();
    boolean addMember(IUser user);
}