package com.cse110team24.walkwalkrevolution.models.user;

import com.cse110team24.walkwalkrevolution.invitations.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseUserAdapter implements IUser {

    public static final String NAME_KEY = "displayName";
    public static final String EMAIL_KEY = "email";
    public static final String UID_KEY = "uid";
    public static final String TEAM_UID_KEY = "teamUid";

    private FirebaseUser mFirebaseUser;
    private String mTeamUid;
    private String mDisplayName;
    private String mEmail;
    private String mUid;
    private List<Invitation> mInvitations;

    public FirebaseUserAdapter() {
        mInvitations = new ArrayList<>();
    }

    public void setFirebaseUser(FirebaseUser firebaseUser) {
        mFirebaseUser = firebaseUser;
    }

    public FirebaseUser firebaseUser() {
        return mFirebaseUser;
    }

    @Override
    public String getDisplayName() {
        return mDisplayName;
    }

    @Override
    public void setDisplayName(String displayName) {
        mDisplayName = displayName;
    }

    @Override
    public String getEmail() {
        return mEmail;
    }

    @Override
    public void setEmail(String email) {
        mEmail = email;
    }

    @Override
    public String getUid() {
        return mUid;
    }

    @Override
    public void setUid(String uid) {
        mUid = uid;
    }

    @Override
    public String documentKey() {
        return Utils.cleanEmail(getEmail());
    }

    @Override
    public String teamUid() {
        return mTeamUid;
    }

    @Override
    public void updateTeamUid(String teamUid) {
        mTeamUid = teamUid;
    }

    @Override
    public void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void updateDisplayName(String displayName) {
        mDisplayName = displayName;
    }

    @Override
    public Map<String, Object> userData() {
        Map<String, Object> userData = new HashMap<>();
        userData.put(NAME_KEY, getDisplayName());
        userData.put(EMAIL_KEY, getEmail());
        userData.put(UID_KEY, getUid());
        userData.put(TEAM_UID_KEY, teamUid());
        return userData;
    }

    @Override
    public List<Invitation> invitations() {
        return mInvitations;
    }

    @Override
    public void addInvitation(Invitation invitation) {
        mInvitations.add(invitation);
    }

    public static FirebaseUserAdapterBuilder builder() {
        return new Builder();
    }

    public static class Builder implements FirebaseUserAdapterBuilder {
        FirebaseUserAdapter mUser;

        public Builder() {
            mUser = new FirebaseUserAdapter();
        }
        @Override
        public UserBuilder addFirebaseUser(FirebaseUser firebaseUser) {
            mUser.setFirebaseUser(firebaseUser);
            return this;
        }

        @Override
        public UserBuilder addEmail(String email) {
            mUser.mEmail = email;
            return this;
        }

        @Override
        public UserBuilder addInvitationsList(List<Invitation> invitations) {
            mUser.mInvitations = invitations;
            return this;
        }

        @Override
        public UserBuilder addDisplayName(String displayName) {
            mUser.updateDisplayName(displayName);
            return this;
        }

        @Override
        public UserBuilder addUid(String uid) {
            mUser.mUid = uid;
            return this;
        }

        @Override
        public UserBuilder addTeamUid(String teamUid) {
            mUser.updateTeamUid(teamUid);
            return this;
        }

        @Override
        public IUser build() {
            return mUser;
        }
    }

}
