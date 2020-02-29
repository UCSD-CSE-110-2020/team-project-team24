package com.cse110team24.walkwalkrevolution.models.user;

public class Invitation {
    private IUser mFrom;
    private IUser mTo;
    private String mUid;

    public Invitation(IUser from, IUser to) {
        mFrom = from;
        mTo = to;
    }
    public String getFromUser() {
        return getUserIdentifier(mFrom);
    }


    public String getToUser() {
        return getUserIdentifier(mTo);
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    private String getUserIdentifier(IUser user) {
        return user.getUid();
    }
}
