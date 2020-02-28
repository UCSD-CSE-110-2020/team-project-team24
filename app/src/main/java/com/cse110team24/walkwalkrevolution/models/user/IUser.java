package com.cse110team24.walkwalkrevolution.models.user;

public interface IUser {
    String getDisplayName();
    String getEmail();
    void updateDisplayName(String name);
    void signOut();
}
