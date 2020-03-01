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

    private Invitation() {}

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
    public String fromEmail() {
        return mFrom.getEmail();
    }

    @Override
    public String toEmail() {
        return mTo.getEmail();
    }

    @Override
    public String fromName() {
        return mFrom.getDisplayName();
    }

    @Override
    public String toName() {
        return mTo.getDisplayName();
    }

    @Override
    public String uid() {
        return mUid;
    }

    @Override
    public Map<String, Object> invitationData() {
        Map<String, Object> data = new HashMap<>();
        data.put(INVITATION_UID_SET_KEY, mUid);
        data.put(INVITATION_FROM_SET_KEY, fromName() + " " + fromEmail());
        data.put(INVITATION_TO_SET_KEY, toName() + " " + toEmail());
        return data;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    public static InvitationBuilder builder() {
        return new Builder();
    }

    public static class Builder implements InvitationBuilder {
        private Invitation mInvitation;

        public Builder() {
            mInvitation = new Invitation();
        }

        @Override
        public InvitationBuilder addFromUser(IUser user) {
            mInvitation.mFrom = user;
            return this;
        }

        @Override
        public InvitationBuilder addToUser(IUser user) {
            mInvitation.mTo = user;
            return this;
        }

        @Override
        public InvitationBuilder addUid(String uid) {
            mInvitation.mUid = uid;
            return this;
        }

        @Override
        public Invitation build() {
            return mInvitation;
        }
    }

}
