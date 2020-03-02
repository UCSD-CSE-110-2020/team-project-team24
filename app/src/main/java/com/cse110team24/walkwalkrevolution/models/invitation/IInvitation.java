package com.cse110team24.walkwalkrevolution.models.invitation;

import com.cse110team24.walkwalkrevolution.models.user.IUser;

import java.util.Map;

public interface IInvitation {
    public IUser fromUser();
    public String fromEmail();
    public String toEmail();
    public String fromName();
    public String toName();
    public String toDocumentKey();
    public InvitationStatus status();
    public String uid();
    public void setUid(String uid);
    public Map<String, Object> invitationData();
}
