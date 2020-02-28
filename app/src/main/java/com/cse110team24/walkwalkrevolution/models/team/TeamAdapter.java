package com.cse110team24.walkwalkrevolution.models.team;

import com.cse110team24.walkwalkrevolution.models.user.IUser;

import java.util.ArrayList;
import java.util.List;

public class TeamAdapter implements ITeam {
    private List<IUser> team;

    public TeamAdapter(ArrayList<IUser> team) {
        this.team = team;
    }

    @Override
    public List<IUser> getTeam() {
        return team;
    }

    @Override
    public boolean addMember(IUser user) {
        try {
            team.add(user);
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}