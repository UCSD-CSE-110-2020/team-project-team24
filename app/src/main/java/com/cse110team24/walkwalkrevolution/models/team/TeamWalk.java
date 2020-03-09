package com.cse110team24.walkwalkrevolution.models.team;

import androidx.annotation.NonNull;

import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.utils.Utils;
import com.google.firebase.Timestamp;

import java.util.Date;

public class TeamWalk {

    private Route mProposedRoute;
    private String mProposedBy;
    private Timestamp mProposedDateAndTime;

    private TeamWalk() {
    }

    public TeamWalk(Route proposedRoute, String proposedBy, Timestamp proposedDateAndTime) {
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
