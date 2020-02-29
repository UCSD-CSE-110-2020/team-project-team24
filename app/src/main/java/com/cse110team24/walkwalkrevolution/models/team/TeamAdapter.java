package com.cse110team24.walkwalkrevolution.models.team;

import com.cse110team24.walkwalkrevolution.models.user.IUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cse110team24.walkwalkrevolution.models.user.FirebaseUserAdapter.UID_KEY;

public class TeamAdapter implements ITeam {
    public static final String MEMBERS_KEY = "teamMembers";

    private List<IUser> team;
    private String uid;

    public TeamAdapter(List<IUser> team) {
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

    @Override
    public String getUid() {
        return uid;
    }

    @Override
    public Map<String, Object> getDBFields() {
        Map<String, Object> teamData = new HashMap<>();
        teamData.put(UID_KEY, uid);
        teamData.put(MEMBERS_KEY, team);
        return teamData;
    }
}