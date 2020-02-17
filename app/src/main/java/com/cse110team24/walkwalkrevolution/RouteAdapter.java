package com.cse110team24.walkwalkrevolution;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cse110team24.walkwalkrevolution.models.Route;
import com.cse110team24.walkwalkrevolution.models.WalkStats;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {
    private static final String TAG = "RouteAdapter";
    private List<Route> mRoutes;
    private Context context;

    public RouteAdapter(List<Route> myRoutes, Context context) {
        this.context = context;
        mRoutes = myRoutes;
    }

    @Override
    public RouteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_route, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RouteAdapter.ViewHolder viewHolder, int position) {
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
        }

        private void checkFavorite(boolean isFavorite) {
            Log.i(TAG, "checkFavorite: toggling route is favorite");
            if (isFavorite) {
                favoriteBtn.setBackgroundResource(R.drawable.ic_star_yellow_24dp);
            } else {
                favoriteBtn.setBackgroundResource(R.drawable.ic_star_border_black_24dp);
            }
        }

        public void bind(Route route) {
            container.setOnClickListener(view -> {
                Intent intent = new Intent(context, RouteDetailsActivity.class)
                        .putExtra(RouteDetailsActivity.ROUTE_KEY, route)
                        .putExtra(RouteDetailsActivity.ROUTE_IDX_KEY, getAdapterPosition());
                if (context instanceof Activity) {
                    ((Activity) context).startActivityForResult(intent, RouteDetailsActivity.REQUEST_CODE);
                }
            });

            checkFavorite(route.isFavorite());
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
    }

}


