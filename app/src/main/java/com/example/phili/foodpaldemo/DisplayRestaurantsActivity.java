package com.example.phili.foodpaldemo;

import com.example.phili.foodpaldemo.Fragment.RestaurantsFragment;
import com.example.phili.foodpaldemo.models.RestaurantItem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DisplayRestaurantsActivity extends AppCompatActivity {

    // RecyclerView
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TextView txt_display_restaurant_heading;
    private String city, cuisine;
    private ArrayList<RestaurantItem> restaurantList;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_restaurants);
        mRecyclerView = (RecyclerView) findViewById(R.id.rest_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);

        restaurantList = new ArrayList<>();

        mAdapter = new RestaurantAdapter(restaurantList);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        mRecyclerView.setAdapter(mAdapter);

        txt_display_restaurant_heading = findViewById(R.id.txt_display_restaurant_heading);


        Intent intent = getIntent();
        city = intent.getStringExtra(RestaurantsFragment.MESSAGE_CITY);
        cuisine = intent.getStringExtra(RestaurantsFragment.MESSAGE_CUISINE);

        cuisine = cuisine.substring(0, 1).toUpperCase().concat(cuisine.substring(1));

        txt_display_restaurant_heading.setText(cuisine.concat(" cuisines in ".concat(city)));


        runnable = new Runnable() {
            @Override
            public void run() {
                getRestaurantId();
            }
        };

        Thread thread = new Thread(null, runnable, "background");
        thread.start();

    }


    public void getRestaurantId() {
        final String urlCuisines = "https://developers.zomato.com/api/v2.1/cuisines?city_id=";
        final String entity_id = city.equals("Halifax") ? "3099" : "3095";
        String urlCuisinesWithCity = urlCuisines.concat(entity_id);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, urlCuisinesWithCity, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "First request: Success!",
                                Toast.LENGTH_SHORT).show();
                        try {
                            JSONArray jsonArray = response.getJSONArray("cuisines");

                            String cuisine_id = "";
                            for (int i = 0; i < jsonArray.length(); i++) {
                                if (jsonArray.getJSONObject(i).getJSONObject("cuisine")
                                        .getString("cuisine_name")
                                        .equals(cuisine)) {

                                    cuisine_id = String.valueOf(jsonArray.getJSONObject(i)
                                            .getJSONObject("cuisine")
                                            .getString("cuisine_id"));
                                    break;
                                }
                            }

                            if (!cuisine_id.equals("")) {
                                getRestaurants(entity_id, cuisine_id);
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "No such kind of cuisine existed",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException error) {
                            error.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

                Toast.makeText(getApplicationContext(),
                        "First request: Error retrieving data",
                        Toast.LENGTH_SHORT).show();
            }
        }) {

            /**
             * Override the getHeaders method to pass the API key for GET method for Zamato
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("user-key", "4aecd32564a00af0b37e755ae360d4bc");
                return headers;
            }
        };

        RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    public void getRestaurants(String entity_id, String cuisine_id) {
        final String url = "https://developers.zomato.com/api/v2.1/search?entity_id=";
        String urlCuisines = url.concat(entity_id + "&entity_type=city&cuisines=" + cuisine_id);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, urlCuisines, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "Second request: Success!",
                                Toast.LENGTH_SHORT).show();
                        try {
                            JSONArray jsonArray = response.getJSONArray("restaurants");
                            restaurantList.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i)
                                        .getJSONObject("restaurant");
                                String id = jsonObject.getJSONObject("R").getString("res_id");
                                String name = jsonObject.getString("name");
                                String rating = jsonObject.getJSONObject("user_rating")
                                        .getString("aggregate_rating");
                                String address = jsonObject.getJSONObject("location")
                                        .getString("address");
                                restaurantList.add(new RestaurantItem(name, rating, address, id));
                            }

                            mAdapter.notifyDataSetChanged();

                        } catch (JSONException error) {
                            error.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

                Toast.makeText(getApplicationContext(),
                        "Second request: Error retrieving data",
                        Toast.LENGTH_SHORT).show();
            }
        }) {

            /**
             * Override the getHeaders method to pass the API key for GET method for Zamato
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("user-key", "4aecd32564a00af0b37e755ae360d4bc");
                return headers;
            }
        };

        RequestQueueSingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}
