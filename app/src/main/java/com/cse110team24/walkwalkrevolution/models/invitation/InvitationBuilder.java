package com.cse110team24.walkwalkrevolution.models.invitation;

import com.cse110team24.walkwalkrevolution.utils.Builder;
import com.cse110team24.walkwalkrevolution.models.user.IUser;

public interface InvitationBuilder extends Builder<Invitation> {
    InvitationBuilder addFromUser(IUser user);
    InvitationBuilder addToEmail(String email);
    InvitationBuilder addToDisplayName(String displayName);
    InvitationBuilder addUid(String uid);
    InvitationBuilder addTeamUid(String teamUid);
    InvitationBuilder addStatus(InvitationStatus status);
}
