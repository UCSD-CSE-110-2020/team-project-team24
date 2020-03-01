package com.cse110team24.walkwalkrevolution.models.invitation;

import com.cse110team24.walkwalkrevolution.models.user.IUser;

import java.util.HashMap;
import java.util.Map;

public class Invitation implements IInvitation {
    public static final String INVITATION_UID_SET_KEY = "invitationUid";
    public static final String INVITATION_FROM_SET_KEY = "from";
    public static final String INVITATION_TO_SET_KEY = "to";

    private IUser mFrom;
    private IUser mTo;
    private String mUid;

    public Invitation(IUser from, IUser to) {
        mFrom = from;
        mTo = to;
    }

    public IUser fromUser() {
        return mFrom;
    }

    public IUser toUser() {
        return mTo;
    }

    @Override
    public String uid() {
        return mUid;
    }

    @Override
    public Map<String, Object> invitationData() {
        Map<String, Object> data = new HashMap<>();
        data.put(INVITATION_UID_SET_KEY, mUid);
        data.put(INVITATION_FROM_SET_KEY, mFrom.getEmail());
        data.put(INVITATION_TO_SET_KEY, toUser().getEmail());
        return data;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

}
