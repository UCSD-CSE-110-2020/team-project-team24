package com.cse110team24.walkwalkrevolution.models.user;

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
    private List<Invitation> mInvitations;

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
        return mFirebaseUser.getEmail();
    }

    @Override
    public String getUid() {
        return mFirebaseUser.getUid();
    }

    @Override
    public String documentKey() {
        return getUid();
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

}
