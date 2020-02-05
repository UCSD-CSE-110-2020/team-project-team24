package com.cse110team24.walkwalkrevolution;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cse110team24.walkwalkrevolution.models.Route;

import org.w3c.dom.Text;

import java.util.List;


public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView routeName;
        public TextView startingLocation;

        public ViewHolder(View itemView) {
            super(itemView);
            routeName = (TextView) itemView.findViewById(R.id.route_name);
            startingLocation = (TextView) itemView.findViewById(R.id.route_location);
        }
    }

    private List<Route> mRoutes;

    public RouteAdapter(List<Route> myRoutes) {
        mRoutes = myRoutes;
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

        TextView textView = viewHolder.routeName;
        textView.setText(route.getTitle());
        TextView textView1 = viewHolder.startingLocation;
        textView1.setText(route.getStartingLocation());
    }

    @Override
    public int getItemCount() {
        return mRoutes.size();
    }

}


