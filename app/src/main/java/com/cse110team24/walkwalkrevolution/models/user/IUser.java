package com.cse110team24.walkwalkrevolution.models.user;

public interface IUser {
    String getDisplayName();
    String getEmail();
    String getUid();
    void updateDisplayName(String name);
    void signOut();
}
