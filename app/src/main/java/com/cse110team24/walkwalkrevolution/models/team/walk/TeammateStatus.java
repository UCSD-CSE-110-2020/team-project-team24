package com.cse110team24.walkwalkrevolution.models.team.walk;

import java.util.HashMap;
import java.util.Map;

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

    public Map<String, Object> dataInMapForm() {
        Map<String, Object> data = new HashMap<>();
        data.put("status", getReason());
        return data;
    }

    public static TeammateStatus get(String value) {
        return lookup.get(value);
    }

    private static final Map<String, TeammateStatus> lookup = new HashMap<>();
    static {
        for (TeammateStatus status : TeammateStatus.values()) {
            lookup.put(status.getReason(), status);
        }
    }
}
