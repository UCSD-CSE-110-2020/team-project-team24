package com.cse110team24.walkwalkrevolution.activities.userroutes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cse110team24.walkwalkrevolution.R;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.route.WalkStats;
import com.cse110team24.walkwalkrevolution.models.user.IUser;
import com.cse110team24.walkwalkrevolution.utils.RoutesManager;
import com.cse110team24.walkwalkrevolution.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RouteRecyclerViewAdapter extends RecyclerView.Adapter<RouteRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "WWR_RouteRecyclerViewAdapter";
    private List<Route> mRoutes;
    private Context mContext;
    private SharedPreferences mPreferences;
    public RouteRecyclerViewAdapter(Context context, List<Route> myRoutes, SharedPreferences preferences) {
        mPreferences = preferences;
        mContext = context;
        mRoutes = myRoutes;
    }

    @Override
    public RouteRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_route, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RouteRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        Route currRoute = mRoutes.get(position);
        viewHolder.bind(currRoute);
    }

    @Override
    public int getItemCount() {
        return mRoutes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView routeNameTv;
        private TextView stepsTv;
        private TextView distanceTv;
        private TextView dateTv;
        private Button favoriteBtn;
        private TextView initialsTv;
        private TextView previouslyWalkedTv;

        private Map<String, Integer> initialsColors = new HashMap<>();
        RelativeLayout container;

        public ViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.routes_container);
            routeNameTv = itemView.findViewById(R.id.tv_route_name);
            stepsTv = itemView.findViewById(R.id.tv_routes_steps);
            distanceTv = itemView.findViewById(R.id.tv_routes_distance);
            dateTv = itemView.findViewById(R.id.tv_routes_date_completed);
            favoriteBtn = itemView.findViewById(R.id.btn_routes_favorite);
            favoriteBtn.setOnClickListener(this);
            initialsTv = itemView.findViewById(R.id.tv_team_routes_initials);
            previouslyWalkedTv = itemView.findViewById(R.id.tv_previously_walked_checkmark);
        }

        @Override
        public void onClick(View v) {
            Route currRoute = mRoutes.get(getAdapterPosition());
            boolean isFavorite = !currRoute.isFavorite();
            currRoute.setFavorite(isFavorite);
            notifyDataSetChanged();
        }

        private void setStatsVisibility(int visibility) {
            stepsTv.setVisibility(visibility);
            distanceTv.setVisibility(visibility);
            dateTv.setVisibility(visibility);
            previouslyWalkedTv.setVisibility(visibility);
        }

        private void checkFavorite(boolean isFavorite) {
            if (isFavorite) {
                favoriteBtn.setBackgroundResource(R.drawable.ic_star_yellow_24dp);
            } else {
                favoriteBtn.setBackgroundResource(R.drawable.ic_star_border_black_24dp);
            }
        }

        public void bind(Route route) {
            launchRouteDetailsActivityOnClick(route);
            setInitialsColor(route);
            checkFavorite(route.isFavorite());
            routeNameTv.setText(route.getTitle());
            WalkStats stats = route.getStats();
            checkWalkStats(stats, route);
        }

        private void checkWalkStats(WalkStats stats, Route route) {
            if(stats == null) {
                setStatsVisibility(View.INVISIBLE);
            }  else if (routeBelongsToUser(route)) {
                setStatsDisplayedValues(stats);
                setColorStats(mContext.getColor(R.color.color_my_routes));
            } else if (userHasWalkTeammateRoute(route)) {
                getUserStatsForTeamRoute(stats, route);
                setStatsDisplayedValues(stats);
                setColorStats(mContext.getColor(R.color.color_curr_user_completed_team_route));
            } else {
                setStatsDisplayedValues(stats);
                setColorStats(Color.GRAY);
                previouslyWalkedTv.setVisibility(View.INVISIBLE);
            }
        }
        private void setColorStats(int color) {
            stepsTv.setTextColor(color);
            distanceTv.setTextColor(color);
            dateTv.setTextColor(color);
        }

        private void getUserStatsForTeamRoute(WalkStats stats, Route route) {
            try {
                Route teammateRouteSavedStats = RoutesManager.readSingle(route.getRouteUid(), mContext);
                stats.setDistance(teammateRouteSavedStats.getStats().getDistance());
                stats.setSteps(teammateRouteSavedStats.getStats().getSteps());
                stats.setDateCompleted(teammateRouteSavedStats.getStats().getDateCompleted());
            } catch (IOException e) {
                Log.e(TAG, "getUserStatsForTeamRoute: error getting saved team route", e);
            }
        }

        private void setStatsDisplayedValues(WalkStats stats) {
            setStatsVisibility(View.VISIBLE);
            stepsTv.setText(String.format("%s%s", String.valueOf(stats.getSteps()), " steps"));
            distanceTv.setText(stats.formattedDistance());
            dateTv.setText(stats.formattedDate());
        }

        private boolean routeBelongsToUser(Route route) {
            return mPreferences.getString(IUser.USER_NAME_KEY, "").equals(route.getCreatorName());
        }

        private boolean userHasWalkTeammateRoute(Route route) {
            return new File(mContext.getFilesDir(), route.getRouteUid()).exists();
        }

        private void launchRouteDetailsActivityOnClick(Route route) {
            container.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, RouteDetailsActivity.class)
                        .putExtra(RouteDetailsActivity.ROUTE_KEY, route)
                        .putExtra(RouteDetailsActivity.ROUTE_IDX_KEY, getAdapterPosition());
                if (mContext instanceof Activity) {
                    ((Activity) mContext).startActivityForResult(intent, RouteDetailsActivity.REQUEST_CODE);
                }
            });
        }

        private void setInitialsColor(Route route) {
            int color = mPreferences.getInt(route.getCreatorName(), -1);
            if (color == -1) {
                color = initialsColors.getOrDefault(route.getCreatorName(), Utils.generateRandomARGBColor(1));
                initialsColors.put(route.getCreatorName(), color);
            }
            initialsTv.setText(Utils.getInitials(route.getCreatorName(), 2));
            initialsTv.setTextColor(color);
        }
    }

}


