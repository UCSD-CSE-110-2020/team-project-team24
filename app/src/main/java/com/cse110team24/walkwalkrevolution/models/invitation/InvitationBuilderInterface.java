package com.cse110team24.walkwalkrevolution.models.invitation;

import com.cse110team24.walkwalkrevolution.models.Builder;
import com.cse110team24.walkwalkrevolution.models.user.IUser;

public interface InvitationBuilderInterface extends Builder<Invitation> {
    InvitationBuilderInterface addFromUser(IUser user);
    InvitationBuilderInterface addToUser(IUser user);
    InvitationBuilderInterface addUid(String uid);
}
