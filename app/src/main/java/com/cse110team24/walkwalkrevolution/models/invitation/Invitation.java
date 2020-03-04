package com.cse110team24.walkwalkrevolution.models.invitation;

import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.utils.Utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.grpc.okhttp.internal.Util;

public class Invitation implements IInvitation {
    public static final String INVITATION_UID_SET_KEY = "invitationUid";
    public static final String INVITATION_FROM_SET_KEY = "from";
    public static final String INVITATION_TO_SET_KEY = "to";
    public static final String USER_DATA_NAME = "name";
    public static final String USER_DATA_EMAIL = "identifier";
    public static final String INVITATION_STATUS_SET_KEY = "status";

    private IUser mFrom;
    private String mUid;
    private String mToEmail;
    private String mToDisplayName;
    private InvitationStatus mStatus;

    private Invitation() {
        mStatus = InvitationStatus.PENDING;
    }

    public Invitation(IUser from) {
        this();
        mFrom = from;
    }

    public IUser fromUser() {
        return mFrom;
    }

    @Override
    public String fromEmail() {
        return mFrom.getEmail();
    }

    @Override
    public String toEmail() {
        return mToEmail;
    }

    @Override
    public String fromName() {
        return mFrom.getDisplayName();
    }

    @Override
    public String toName() {
        return mToDisplayName;
    }

    @Override
    public String toDocumentKey() {
        return Utils.cleanEmail(toEmail());
    }

    @Override
    public String fromDocumentKey() {
        return mFrom.documentKey();
    }

    @Override
    public InvitationStatus status() {
        return mStatus;
    }

    @Override
    public void setStatus(InvitationStatus status) {
        this.mStatus = status;
    }

    @Override
    public String uid() {
        return mUid;
    }

    @Override
    public Map<String, Object> invitationData() {
        Map<String, Object> data = new HashMap<>();
        data.put(INVITATION_UID_SET_KEY, mUid);
        data.put(INVITATION_FROM_SET_KEY, fromData());
        data.put(INVITATION_TO_SET_KEY, toData());
        data.put(INVITATION_STATUS_SET_KEY, status().toString().toLowerCase(Locale.getDefault()));
        return data;
    }

    private Map<String, Object> fromData() {
        return userData(fromName(), fromEmail());
    }

    private Map<String, Object> toData() {
        return userData(toName(), toEmail());
    }

    private Map<String, Object> userData(String displayName, String email) {
        Map<String, Object> data = new HashMap<>();
        data.put(USER_DATA_NAME, displayName);
        data.put(USER_DATA_EMAIL, Utils.cleanEmail(email));
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
        public InvitationBuilder addToEmail(String email) {
            mInvitation.mToEmail = email;
            return this;
        }

        @Override
        public InvitationBuilder addToDisplayName(String displayName) {
            mInvitation.mToDisplayName = displayName;
            return this;
        }

        @Override
        public InvitationBuilder addUid(String uid) {
            mInvitation.mUid = uid;
            return this;
        }

        @Override
        public InvitationBuilder addStatus(InvitationStatus status) {
            mInvitation.mStatus = status;
            return this;
        }

        @Override
        public Invitation build() {
            return mInvitation;
        }
    }

}
