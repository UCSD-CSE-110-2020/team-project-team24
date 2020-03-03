package com.cse110team24.walkwalkrevolution;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cse110team24.walkwalkrevolution.models.invitation.Invitation;
import com.cse110team24.walkwalkrevolution.models.user.IUser;

import java.util.List;
import java.util.Random;

public class InvitationsListViewAdapter extends BaseAdapter {
    Context context;
    List<Invitation> invitations;
    LayoutInflater inflater;

    public InvitationsListViewAdapter(Context context, List<Invitation> invitations) {
        this.context = context;
        this.invitations = invitations;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return invitations.size();
    }

    @Override
    public Object getItem(int i) {
        return invitations.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_inviter, null);
        TextView nameView = view.findViewById(R.id.inviterNameView);
        nameView.setText(invitations.get(i).fromName());
        return view;
    }

}
