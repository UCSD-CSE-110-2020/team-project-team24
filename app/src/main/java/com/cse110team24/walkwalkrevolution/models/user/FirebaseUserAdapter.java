package com.cse110team24.walkwalkrevolution.models.user;

import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseUserAdapter implements IUser {
    private static final String TAG = "FirebaseUserAdapter";

    public static final String NAME_KEY = "displayName";
    public static final String EMAIL_KEY = "email";
    public static final String UID_KEY = "uid";
    public static final String TEAM_UID_KEY = "teamUid";
    public static final String INVITATIONS_LIST_KEY = "invitationsUid";

    private FirebaseUser mFirebaseUser;
    private String mTeamUid;
    private String mDisplayName;
    private String mEmail;
    private String mUid;
    private List<Invitation> mInvitations;

    public FirebaseUserAdapter() {}

    public FirebaseUserAdapter(FirebaseUser firebaseUser, List<Invitation> invitations) {
        mFirebaseUser = firebaseUser;
        mInvitations = invitations;
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
    public String getEmail() {
        return mEmail;
    }

    @Override
    public String getUid() {
        return mUid;
    }

    @Override
    public String documentKey() {
        return getEmail();
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

    public static IFirebaseUserAdapterBuilder builder() {
        return new FirebaseUserAdapterBuilder();
    }

    @Override
    public void addInvitation(Invitation invitation) {
        mInvitations.add(invitation);
    }

    // TODO: 2/29/20 implement FirebaseUserBuilder - don't use firebase to get the fields. Store them immediately

    public static class FirebaseUserAdapterBuilder implements IFirebaseUserAdapterBuilder {
        FirebaseUserAdapter mUser;

        public FirebaseUserAdapterBuilder() {
            mUser = new FirebaseUserAdapter();
        }
        @Override
        public IUserBuilder addFirebaseUser(FirebaseUser firebaseUser) {
            mUser.setFirebaseUser(firebaseUser);
            return this;
        }

        @Override
        public IUserBuilder addEmail(String email) {
            mUser.mEmail = email;
            return this;
        }

        @Override
        public IUserBuilder addInvitationsList(List<Invitation> invitations) {
            mUser.mInvitations = invitations;
            return this;
        }

        @Override
        public IUserBuilder addDisplayName(String displayName) {
            mUser.updateDisplayName(displayName);
            return this;
        }

        @Override
        public IUserBuilder addUid(String uid) {
            mUser.mUid = uid;
            return this;
        }

        @Override
        public IUserBuilder addTeamUid(String teamUid) {
            mUser.updateTeamUid(teamUid);
            return this;
        }

        @Override
        public IUser build() {
            return mUser;
        }
    }

}
