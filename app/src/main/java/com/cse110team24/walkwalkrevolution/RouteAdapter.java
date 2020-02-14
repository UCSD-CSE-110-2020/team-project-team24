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

        public void bind(Route route) {
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

    @Override
    public RouteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_route, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // TODO: 2020-02-14 most of the UI stuff should be handled by the ViewHolder
        // again, walk stats should handle parsing its values into strings
    @Override
    public void onBindViewHolder(RouteAdapter.ViewHolder viewHolder, int position) {
        Route currRoute = mRoutes.get(position);

        viewHolder.routeNameTv.setText(currRoute.getTitle());

        WalkStats stats = currRoute.getStats();
        if(stats == null) {
            setStatsVisibility(viewHolder, View.INVISIBLE);
        } else {
            setStatsVisibility(viewHolder, View.VISIBLE);

            Resources res = context.getResources();
            viewHolder.stepsTv.setText(String.format("%s%s", String.valueOf(stats.getSteps()), " steps"));
            NumberFormat numberFormat = new DecimalFormat("#0.00");
            viewHolder.distanceTv.setText(String.format("%s%s", numberFormat.format(stats.getDistance()), " mile(s)"));
            Date date = currRoute.getStats().getDateCompleted().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd", Locale.US);
            viewHolder.dateTv.setText(sdf.format(date));
        }
        checkFavorite(viewHolder, currRoute.isFavorite());
        viewHolder.bind(currRoute);
    }

    private void setStatsVisibility(ViewHolder viewHolder, int visibility) {
        viewHolder.stepsTv.setVisibility(visibility);
        viewHolder.distanceTv.setVisibility(visibility);
        viewHolder.dateTv.setVisibility(visibility);
    }

    private void checkFavorite(ViewHolder viewHolder, boolean isFavorite) {
        Log.i(TAG, "checkFavorite: toggling route is favorite");
        if (isFavorite) {
            viewHolder.favoriteBtn.setBackgroundResource(R.drawable.ic_star_yellow_24dp);
        } else {
            viewHolder.favoriteBtn.setBackgroundResource(R.drawable.ic_star_border_black_24dp);
        }
    }

    @Override
    public int getItemCount() {
        return mRoutes.size();
    }

}


