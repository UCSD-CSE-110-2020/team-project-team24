package com.cse110team24.walkwalkrevolution;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cse110team24.walkwalkrevolution.models.user.IUser;

import java.util.List;
import java.util.Random;

public class InvitationsListViewAdapter extends BaseAdapter {
    Context context;
    List<IUser> users;
    LayoutInflater inflater;

    public InvitationsListViewAdapter(Context context, List<IUser> users) {
        this.context = context;
        this.users = users;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_inviter, null);
        TextView nameView = view.findViewById(R.id.inviterNameView);
        nameView.setText(users.get(i).getDisplayName());
        return view;
    }

}
