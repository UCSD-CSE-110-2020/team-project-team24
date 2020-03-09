package com.cse110team24.walkwalkrevolution.models.invitation;

import com.cse110team24.walkwalkrevolution.models.user.IUser;

import java.util.Map;

public interface IInvitation {
    IUser fromUser();
    String fromEmail();
    String fromName();
    String fromDocumentKey();
    String toEmail();
    String toName();
    String toDocumentKey();
    String getTeamUid();
    InvitationStatus status();
    void setStatus(InvitationStatus status);
    String uid();
    void setUid(String uid);
    Map<String, Object> invitationData();
}
