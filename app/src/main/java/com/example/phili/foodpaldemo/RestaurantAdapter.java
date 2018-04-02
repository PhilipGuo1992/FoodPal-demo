package com.example.phili.foodpaldemo;

import com.example.phili.foodpaldemo.models.RestaurantItem;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by zongming on 2018-03-19.
 */

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
    private ArrayList<RestaurantItem> restaurantList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView restaurant_name, restaurant_rating, restaurant_address;
        public ViewHolder(View v) {
            super(v);
            restaurant_name =  (TextView) v.findViewById(R.id.restaurant_name);
            restaurant_rating = (TextView) v.findViewById(R.id.restaurant_rating);
            restaurant_address = (TextView) v.findViewById(R.id.restaurant_address);
        }
    }

    public RestaurantAdapter(ArrayList<RestaurantItem>
            restaurantList) {
        this.restaurantList = restaurantList;
    }

    @Override
    public RestaurantAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.restaurant_name.setText(restaurantList.get(position).getName());
        holder.restaurant_rating.setText(restaurantList.get(position).getRating());
        holder.restaurant_address.setText(restaurantList.get(position).getAddress());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

}
