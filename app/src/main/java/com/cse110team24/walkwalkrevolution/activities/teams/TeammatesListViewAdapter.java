package com.cse110team24.walkwalkrevolution.activities.teams;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cse110team24.walkwalkrevolution.R;
import com.cse110team24.walkwalkrevolution.models.team.walk.TeammateStatus;
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
    boolean showStatusIcons;
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

    public void setShowStatusIcons(boolean showIcons) {
        showStatusIcons = showIcons;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View newView;
        if (view == null) {
            newView = inflater.inflate(R.layout.item_teammate, viewGroup, false);

            ImageView statusView = newView.findViewById(R.id.statusView);
            TextView nameView = newView.findViewById(R.id.nameView);
            TextView initialView = newView.findViewById(R.id.initialView);
            setStatusImage(statusView, users.get(i).getLatestWalkStatus());
            if (showStatusIcons) statusView.setVisibility(View.VISIBLE);
            nameView.setText(users.get(i).getDisplayName());
            String name = users.get(i).getDisplayName();
            initialView.setText(Utils.getInitials(name, -1));

            setInitialsColor(initialView, name, i);
        } else {
            newView = view;
        }
        return newView;
    }

    private void setStatusImage(ImageView statusView, TeammateStatus status) {
        if (status == null) return;

        if (status == TeammateStatus.ACCEPTED)
            statusView.setBackgroundResource(R.drawable.ic_check_green_24dp);
        else if (status == TeammateStatus.DECLINED_NOT_INTERESTED)
            statusView.setBackgroundResource(R.drawable.ic_close_black_24dp);
        else if (status == TeammateStatus.DECLINED_SCHEDULING_CONFLICT)
            statusView.setBackgroundResource(R.drawable.ic_event_busy_black_24dp);
        else if (status == TeammateStatus.PENDING)
            statusView.setBackgroundResource(R.drawable.ic_schedule_black_24dp);
    }

                                /**
     * makes the color for a teammate permanent by saving it to SharedPreferences
     * @param initialsView TextView that holds the teammate's initials
     * @param name the teammate's name
     * @param idx the idx of the teammate in this adapter's data set
     */
    private void setInitialsColor(TextView initialsView, String name, int idx) {
        int savedColor = mPreferences.getInt(name, -1);
        Log.d(TAG, "setInitialsColor: getting " + name + "'s color " + savedColor);
        if (savedColor == -1) {
            savedColor = Utils.generateRandomARGBColor(name.hashCode() + 420);
            Log.d(TAG, "setInitialsColor: generated new color " + savedColor);
            mPreferences.edit().putInt(name, savedColor).apply();
        }
        initialsView.setTextColor(savedColor);
    }

}
