package com.cse110team24.walkwalkrevolution.models.invitation;

import com.cse110team24.walkwalkrevolution.models.Builder;
import com.cse110team24.walkwalkrevolution.models.user.IUser;

public interface InvitationBuilder extends Builder<Invitation> {
    InvitationBuilder addFromUser(IUser user);
    InvitationBuilder addToUser(IUser user);
    InvitationBuilder addUid(String uid);
    InvitationBuilder addStatus(InvitationStatus status);
}
