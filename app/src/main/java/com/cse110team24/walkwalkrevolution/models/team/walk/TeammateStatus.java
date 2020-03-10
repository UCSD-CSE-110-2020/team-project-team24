package com.cse110team24.walkwalkrevolution.models.team.walk;

public enum TeammateStatus {
    DECLINED_SCHEDULING_CONFLICT("declined the walk due to a scheduling conflict"),
    DECLINED_NOT_GOOD("decline the walk because it's not good for them"),
    ACCEPTED("accepted the walk!");


    private String mReason;
    TeammateStatus(String reason) {
        mReason = reason;
    }

    public String getReason() {
        return mReason;
    }
}
