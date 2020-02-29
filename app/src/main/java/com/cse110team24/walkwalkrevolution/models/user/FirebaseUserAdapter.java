package com.cse110team24.walkwalkrevolution.models.user;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.HashMap;
import java.util.Map;

public class FirebaseUserAdapter implements IUser {
    private static final String TAG = "FirebaseUserAdapter";

    public static final String NAME_KEY = "displayName";
    public static final String EMAIL_KEY = "email";
    public static final String UID_KEY = "uid";
    public static final String TEAM_UID_KEY = "teamUid";
    public static final String INVITATIONS_UID_KEY "invitationsUid";

    private FirebaseUser mFirebaseUser;
    private String mTeamUid;
    private String mInvitationsUid;
    private String mDisplayName;

    public FirebaseUserAdapter(FirebaseUser firebaseUser) {
        mFirebaseUser = firebaseUser;
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
        return getDisplayName();
    }

    @Override
    public String teamUid() {
        return mTeamUid;
    }

    @Override
    public String invitationsUid() {
        return mInvitationsUid;
    }

    @Override
    public void setTeamUid(String teamUid) {
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
        userData.put(INVITATIONS_UID_KEY, invitationsUid());
        return userData;
    }

}
