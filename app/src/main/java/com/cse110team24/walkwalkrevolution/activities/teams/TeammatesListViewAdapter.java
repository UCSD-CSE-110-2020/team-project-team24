package com.cse110team24.walkwalkrevolution.activities.teams;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
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
    private static final String TAG = "WWR_TeammatesListViewAdapter";
    Context context;
    List<IUser> users;
    LayoutInflater inflater;
    private SharedPreferences mPreferences;

    public TeammatesListViewAdapter(Context context, List<IUser> users, SharedPreferences preferences) {
        this.context = context;
        this.users = users;
        mPreferences = preferences;
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

            setInitialsColor(initialView, name, i);
        } else {
            newView = view;
        }
        return newView;
    }

    private void setInitialsColor(TextView initialsView, String name, int idx) {
        int savedColor = mPreferences.getInt(name, -1);
        Log.d(TAG, "setInitialsColor: getting " + name + "'s color " + savedColor);
        if (savedColor == -1) {
            savedColor = Utils.generateRandomARGBColor(idx);
            Log.d(TAG, "setInitialsColor: generated new color " + savedColor);
            mPreferences.edit().putInt(name, savedColor).apply();
        }
        initialsView.setTextColor(savedColor);
    }

}
