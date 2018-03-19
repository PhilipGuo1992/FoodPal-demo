package com.example.phili.foodpaldemo;

import com.example.phili.foodpaldemo.models.RestaurantItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by zongming on 2018-03-19.
 */

public class RestaurantAdapter extends ArrayAdapter<RestaurantItem> {
    private ArrayList<RestaurantItem> restaurantList;

    public RestaurantAdapter(Context context, int textViewResourceId, ArrayList<RestaurantItem>
            restaurantList) {
        super(context, textViewResourceId, restaurantList);
        this.restaurantList = restaurantList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.restaurant_list_item, null);
        }

        RestaurantItem i = restaurantList.get(position);

        if (i != null) {
            TextView restaurant_name = v.findViewById(R.id.restaurant_name);
            TextView restaurant_rating = v.findViewById(R.id.restaurant_rating);

            restaurant_name.setText(i.getName());
            restaurant_rating.setText(String.valueOf(i.getRating()));
        }

        return v;
    }
}
