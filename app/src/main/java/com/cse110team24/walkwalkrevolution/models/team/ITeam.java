package com.cse110team24.walkwalkrevolution.models.team;

import com.cse110team24.walkwalkrevolution.models.user.IUser;

import java.util.List;

public interface ITeam {
    List<IUser> getTeam();
    boolean addMember(IUser user);
}