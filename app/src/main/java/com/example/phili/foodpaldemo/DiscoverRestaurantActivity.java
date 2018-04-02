package com.example.phili.foodpaldemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class DiscoverRestaurantActivity extends AppCompatActivity {

    final static String MESSAGE_CITY = "CITY";
    final static String MESSAGE_CUISINE = "CUISINE";

    private Button btn_discover_restaurant;
    private Spinner spinner_city;
    private EditText txt_search_for_restaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_restaurant);

        btn_discover_restaurant = findViewById(R.id.btn_discover_restaurant);
        spinner_city = findViewById(R.id.spinner_city);
        txt_search_for_restaurants = findViewById(R.id.txt_search_for_restaurants);

        ArrayAdapter<CharSequence> cityArrays = ArrayAdapter.createFromResource(this,
                R.array.cities_array, R.layout.spinner_item);

        cityArrays.setDropDownViewResource(R.layout.spinner_dropdown_item);

        spinner_city.setAdapter(cityArrays);

        btn_discover_restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        DisplayRestaurantsActivity.class);
                intent.putExtra(MESSAGE_CITY, spinner_city.getSelectedItem().toString());
                intent.putExtra(MESSAGE_CUISINE, txt_search_for_restaurants.getText().toString());
                startActivity(intent);
            }
        });
    }
}
