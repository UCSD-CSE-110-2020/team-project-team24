package com.cse110team24.walkwalkrevolution.activities.teams;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cse110team24.walkwalkrevolution.R;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.utils.Utils;

import java.util.List;
import java.util.Random;

import static com.google.common.base.Ascii.toUpperCase;

public class TeammatesListViewAdapter extends BaseAdapter {
    Context context;
    List<IUser> users;
    LayoutInflater inflater;

    public TeammatesListViewAdapter(Context context, List<IUser> users) {
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

            TextView nameView = newView.findViewById(R.id.nameView);
            TextView initialView = newView.findViewById(R.id.initialView);
            nameView.setText(users.get(i).getDisplayName());
            String name = users.get(i).getDisplayName();
            initialView.setText(Utils.getInitials(name, -1));

            initialView.setTextColor(Utils.generateRandomARGBColor(i));
        } else {
            newView = view;
        }
        return newView;
    }
    
}
