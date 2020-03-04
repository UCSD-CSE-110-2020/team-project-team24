package com.cse110team24.walkwalkrevolution;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cse110team24.walkwalkrevolution.models.user.IUser;

import java.util.List;
import java.util.Random;

import static android.icu.lang.UCharacter.toUpperCase;

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
        View newView;
        if (view == null) {
            newView = inflater.inflate(R.layout.item_teammate, viewGroup, false);
            TextView nameView = (TextView) newView.findViewById(R.id.nameView);
            TextView initialView = (TextView) newView.findViewById(R.id.initialView);
            nameView.setText(users.get(i).getDisplayName());
            initialView.setText(getInitialFromName(users.get(i).getDisplayName()));
            setTextColor(initialView);
        } else {
            newView = view;
        }
        return newView;
    }

    private String getInitialFromName(String name) {
        String[] nameArraySeperatedBySpace = name.split(" ");
        String initialsToReturn = "";
        for(int i = 0; i < nameArraySeperatedBySpace.length; i++) {
            initialsToReturn += toUpperCase(nameArraySeperatedBySpace[i]).charAt(0);
        }
        return initialsToReturn;
    }

    private void setTextColor(TextView view) {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(192), rnd.nextInt(192), rnd.nextInt(192));
        view.setTextColor(color);
    }

}
