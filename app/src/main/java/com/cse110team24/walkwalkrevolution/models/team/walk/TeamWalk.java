package com.cse110team24.walkwalkrevolution.models.team.walk;

import androidx.annotation.NonNull;

import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.utils.Utils;
import com.google.firebase.Timestamp;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TeamWalk {

    private Route mProposedRoute;
    private String mProposedBy;
    private Timestamp mProposedDateAndTime;
    private String mTeamUid;
    private TeamWalkStatus mStatus;
    private Timestamp mProposedOn;
    private String mWalkUid;

    private TeamWalk() {
        mStatus = TeamWalkStatus.PROPOSED;
        mProposedOn = new Timestamp(Calendar.getInstance().getTime());
    }

    public TeamWalk(Route proposedRoute, String proposedBy, Timestamp proposedDateAndTime) {
        this();
        mProposedRoute = proposedRoute;
        mProposedBy = proposedBy;
        mProposedDateAndTime = proposedDateAndTime;
    }

    public void setProposedRoute(Route proposedRoute) {
        mProposedRoute = proposedRoute;
    }

    public Route getProposedRoute() {
        return mProposedRoute;
    }

    public void setProposedBy(String proposedBy) {
        mProposedBy = proposedBy;
    }

    public String getProposedBy() {
        return mProposedBy;
    }

    public void setProposedDateAndTime(Timestamp proposedDateAndTime) {
        mProposedDateAndTime = proposedDateAndTime;
    }

    public void setTeamUid(String teamUid) {
        mTeamUid = teamUid;
    }

    public String getTeamUid() {
        return mTeamUid;
    }

    public TeamWalkStatus getStatus() {
        return mStatus;
    }

    public void setStatus(TeamWalkStatus status) {
        mStatus = status;
    }

    public String getWalkUid() {
        return mWalkUid;
    }

    public void setWalkUid(String walkUid) {
        mWalkUid = walkUid;
    }

    public Map<String, Object> dataInMapForm() {
        Map<String, Object> data = new HashMap<>();
        data.put("proposedBy", mProposedBy);
        data.put("proposedDateAndTime", mProposedDateAndTime);
        data.put("proposedRoute", relevantRouteData(mProposedRoute));
        data.put("status", mStatus.toString());
        data.put("proposedOn", mProposedOn);
        return data;
    }

    private Map<String, Object> relevantRouteData(Route route) {
        Map<String, Object> data = new HashMap<>(route.routeData());
        data.remove("stats");
        data.remove("notes");
        return data;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        TeamWalk mTeamWalk = new TeamWalk();

        public Builder addProposedRoute(@NonNull Route proposedRoute) {
            mTeamWalk.mProposedRoute = proposedRoute;
            return this;
        }

        public Builder addProposedBy(@NonNull String proposedBy) {
            mTeamWalk.mProposedBy = proposedBy;
            return this;
        }

        public Builder addProposedDateAndTime(@NonNull Timestamp proposedDateAndTime) {
            mTeamWalk.mProposedDateAndTime = proposedDateAndTime;
            return this;
        }

        public Builder addTeamUid(@NonNull String teamUid) {
            mTeamWalk.setTeamUid(teamUid);
            return this;
        }

        public Builder addStatus(TeamWalkStatus status) {
            if (Utils.checkNotNull(status)) {
                mTeamWalk.setStatus(status);
            }
            return this;
        }

        public Builder addWalkUid(String walkUid) {
            mTeamWalk.setWalkUid(walkUid);
            return this;
        }

        public TeamWalk build() {
            if (Utils.checkNotNull(mTeamWalk.mProposedBy)
                    && Utils.checkNotNull(mTeamWalk.mProposedDateAndTime)
                    && Utils.checkNotNull(mTeamWalk.mProposedRoute)) {
                return mTeamWalk;
            }

            throw new IllegalStateException("Team Walk must have non-null fields");
        }
    }

}
