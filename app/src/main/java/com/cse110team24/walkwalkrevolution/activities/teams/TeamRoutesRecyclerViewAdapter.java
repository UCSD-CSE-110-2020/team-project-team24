package com.cse110team24.walkwalkrevolution.activities.teams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cse110team24.walkwalkrevolution.R;
import com.cse110team24.walkwalkrevolution.activities.userroutes.RouteDetailsActivity;
import com.cse110team24.walkwalkrevolution.models.route.Route;
import com.cse110team24.walkwalkrevolution.models.route.WalkStats;

import java.util.List;


public class TeamRoutesRecyclerViewAdapter extends RecyclerView.Adapter<TeamRoutesRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "WWR_TeamRoutesAdapter";
    private List<Route> mRoutes;
    private Context context;

    public TeamRoutesRecyclerViewAdapter(List<Route> myRoutes, Context context) {
        this.context = context;
        mRoutes = myRoutes;
    }

    @Override
    public TeamRoutesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_route, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TeamRoutesRecyclerViewAdapter.ViewHolder viewHolder, int position) {
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
        RelativeLayout container;

        public ViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.routes_container);
            routeNameTv = itemView.findViewById(R.id.tv_route_name);
            stepsTv = itemView.findViewById(R.id.tv_routes_steps);
            distanceTv = itemView.findViewById(R.id.tv_routes_distance);
            dateTv = itemView.findViewById(R.id.tv_routes_date_completed);
        }

        @Override
        public void onClick(View v) {
        }

        private void setStatsVisibility(int visibility) {
            stepsTv.setVisibility(visibility);
            distanceTv.setVisibility(visibility);
            dateTv.setVisibility(visibility);
        }

        public void bind(Route route) {
            launchRouteDetailsActivityOnClick(route);

            routeNameTv.setText(route.getTitle());
            WalkStats stats = route.getStats();
            if(stats == null) {
                setStatsVisibility(View.INVISIBLE);
            } else {
                setStatsVisibility(View.VISIBLE);

                stepsTv.setText(String.format("%s%s", String.valueOf(stats.getSteps()), " steps"));
                distanceTv.setText(stats.formattedDistance());
                dateTv.setText(stats.formattedDate());
            }
        }

        private void launchRouteDetailsActivityOnClick(Route route) {
            container.setOnClickListener(view -> {
                Intent intent = new Intent(context, RouteDetailsActivity.class)
                        .putExtra(RouteDetailsActivity.ROUTE_KEY, route)
                        .putExtra(RouteDetailsActivity.ROUTE_IDX_KEY, getAdapterPosition());
                if (context instanceof Activity) {
                    ((Activity) context).startActivityForResult(intent, RouteDetailsActivity.REQUEST_CODE);
                }
            });
        }
    }

}


