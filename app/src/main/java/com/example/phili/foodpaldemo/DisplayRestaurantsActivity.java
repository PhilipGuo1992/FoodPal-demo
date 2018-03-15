package com.example.phili.foodpaldemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayRestaurantsActivity extends AppCompatActivity {

    private TextView txt_display_restaurant_heading;
    private String city, cuisine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_restaurants);

        txt_display_restaurant_heading = findViewById(R.id.txt_display_restaurant_heading);

        Intent intent = getIntent();
        city = intent.getStringExtra(DiscoverRestaurantActivity.MESSAGE_CITY);
        cuisine = intent.getStringExtra(DiscoverRestaurantActivity.MESSAGE_CUISINE);

        txt_display_restaurant_heading.setText(cuisine.concat(" in ".concat(city)));
    }
}
