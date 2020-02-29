package com.cse110team24.walkwalkrevolution.models.user;

public class Invitation {
    private String mFromUid;
    private String mToUid;
    private String mUid;

    public Invitation(String fromUid, String toUid) {
        mFromUid = fromUid;
        mToUid = toUid;
    }
    public String getFromUid() {
        return mFromUid;
    }


    public String getToUid() {
        return mToUid;
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }
}
