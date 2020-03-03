package com.cse110team24.walkwalkrevolution;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cse110team24.walkwalkrevolution.models.user.IUser;

import java.util.List;

public class ListviewAdapter extends BaseAdapter {
    Context context;
    List<IUser> users;
    LayoutInflater inflater;

    public ListviewAdapter(Context context, List<IUser> users) {
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
        view = inflater.inflate(R.layout.item_teammate, null);
        TextView nameView = (TextView)view.findViewById(R.id.nameView);
        TextView initialView = (TextView)view.findViewById(R.id.initialView);
        nameView.setText(users.get(i).getDisplayName());
        initialView.setText(getInitialFromName(users.get(i).getDisplayName()));
        return null;
    }

    private String getInitialFromName(String name) {
        String[] nameArraySeperatedBySpace = name.split(" ");
        String initialsToReturn = "";
        for(int i = 0; i < nameArraySeperatedBySpace.length; i++) {
            initialsToReturn += nameArraySeperatedBySpace[i].toUpperCase().charAt(0);
        }
        return initialsToReturn;
    }


}
