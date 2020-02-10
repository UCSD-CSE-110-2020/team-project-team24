package com.cse110team24.walkwalkrevolution;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cse110team24.walkwalkrevolution.models.Route;
import com.cse110team24.walkwalkrevolution.models.WalkStats;
//import com.cse110team24.walkwalkrevolution.models.WalkStats;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {

    private List<Route> mRoutes;
    private Context context;

    public RouteAdapter(List<Route> myRoutes, Context context) {
        this.context = context;
        mRoutes = myRoutes;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView routeNameTv;
        private TextView stepsTv;
        private TextView distanceTv;
        private TextView dateTv;
        private Button favoriteBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            routeNameTv = itemView.findViewById(R.id.route_name);
            stepsTv = itemView.findViewById(R.id.steps);
            distanceTv = itemView.findViewById(R.id.distance);
            dateTv = itemView.findViewById(R.id.date_completed);
            favoriteBtn = itemView.findViewById(R.id.favorite);
        }
    }

    @Override
    public RouteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.content_routes2, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder (RouteAdapter.ViewHolder viewHolder, int position) {
        Route route = mRoutes.get(position);

        viewHolder.routeNameTv.setText(route.getTitle());

        if(route.getStats() == null) {
            viewHolder.stepsTv.setVisibility(View.INVISIBLE);
            viewHolder.distanceTv.setVisibility(View.INVISIBLE);
            viewHolder.dateTv.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.stepsTv.setVisibility(View.VISIBLE);
            viewHolder.distanceTv.setVisibility(View.VISIBLE);
            viewHolder.dateTv.setVisibility(View.VISIBLE);

            WalkStats stats = route.getStats();

            Resources res = context.getResources();
            viewHolder.stepsTv.setText(res.getQuantityString(R.plurals.show_steps, 1, stats.getSteps()));
            viewHolder.distanceTv.setText(res.getQuantityString(R.plurals.show_miles, 1, stats.getDistance()));
            Date date = route.getStats().getDateCompleted().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd", Locale.US);
            viewHolder.dateTv.setText(sdf.format(date));

        }
    }

    @Override
    public int getItemCount() {
        return mRoutes.size();
    }

}


