package com.cse110team24.walkwalkrevolution.models.invitation;

import com.cse110team24.walkwalkrevolution.models.user.IUser;

import java.util.Map;

public interface IInvitation {
    public IUser fromUser();
    public IUser toUser();
    public String uid();
    public void setUid(String uid);
    public Map<String, Object> invitationData();
}
