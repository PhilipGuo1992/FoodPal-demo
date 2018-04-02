package com.example.phili.foodpaldemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RestaurantInfoActivity extends AppCompatActivity {
    private TextView restaurant_info_name;
    private TextView restaurant_info_address;
    private TextView restaurant_info_cuisines;
    private TextView restaurant_info_avg_cost;
    private TextView restaurant_info_url;
    private TextView restaurant_info_rating;
    private TextView restaurant_info_aggregate_rating;
    private TextView restaurant_info_votes;
    private String id;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_info);

        restaurant_info_name = findViewById(R.id.restaurant_info_name);
        restaurant_info_address = findViewById(R.id.restaurant_info_address);
        restaurant_info_cuisines = findViewById(R.id.restaurant_info_cuisines);
        restaurant_info_avg_cost = findViewById(R.id.restaurant_info_avg_cost);
        restaurant_info_url = findViewById(R.id.restaurant_info_url);
        restaurant_info_rating = findViewById(R.id.restaurant_info_rating);
        restaurant_info_aggregate_rating = findViewById(R.id.restaurant_info_aggregate_rating);
        restaurant_info_votes = findViewById(R.id.restaurant_info_votes);

        Intent intent = getIntent();
        id = intent.getStringExtra(RestaurantAdapter.RESTAURANT_ID);

        runnable = new Runnable() {
            @Override
            public void run() {
                getRestaurantInfo();
            }
        };
        Thread thread = new Thread(null, runnable, "background");
        thread.start();
    }

    public void getRestaurantInfo() {
        final String url = "https://developers.zomato.com/api/v2.1/restaurant?res_id=".concat(id);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "First request: Success!",
                                Toast.LENGTH_SHORT).show();
                        try {
                            String name = response.getString("name");

                            String currency = response.getString("currency");
                            String url = "<font color=#000000>".concat(response.getString("url").concat("</font>"));
                            String address = "<font color=#000000>".concat(response.getJSONObject("location").getString("address").concat("</font>"));
                            String cuisines = "<font color=#000000>".concat(response.getString("cuisines").concat("</font>"));
                            String avg_cost = "<font color=#000000>".concat(currency.concat(response.getString("average_cost_for_two")).concat("</font>"));
                            String rating_color = response.getJSONObject("user_rating").getString("rating_color");
                            String user_rating = "<font color=#".concat(rating_color.concat(">")).concat(response.getJSONObject("user_rating").getString("rating_text").concat("</font>"));
                            String aggregate_rating = "<font color=#".concat(rating_color.concat(">")).concat(response.getJSONObject("user_rating").getString("aggregate_rating").concat("</font>"));
                            String votes = "<font color=#".concat(rating_color.concat(">")).concat(response.getJSONObject("user_rating").getString("votes").concat("</font>"));
                            restaurant_info_name.setText(name);

                            //ref: https://stackoverflow.com/questions/6094315/single-textview-with-multiple-colored-text/14936995
                            String address_pre = "<font color=#666666>".concat(restaurant_info_address.getText().toString().concat("</font>"));
                            String cuisines_pre = "<font color=#666666>".concat(restaurant_info_cuisines.getText().toString().concat("</font>"));
                            String avg_cost_pre = "<font color=#666666>".concat(restaurant_info_avg_cost.getText().toString().concat("</font>"));
                            String rating_pre = "<font color=#666666>".concat(restaurant_info_rating.getText().toString().concat("</font>"));
                            String url_pre = "<font color=#666666>".concat(restaurant_info_url.getText().toString().concat("</font>"));
                            String agg_pre = "<font color=#666666>".concat(restaurant_info_aggregate_rating.getText().toString().concat("</font>"));
                            String votes_pre = "<font color=#666666>".concat(restaurant_info_votes.getText().toString().concat("</font>"));

                            restaurant_info_address.setText(Html.fromHtml(address_pre.concat(address)));
                            restaurant_info_cuisines.setText(Html.fromHtml(cuisines_pre.concat(cuisines)));
                            restaurant_info_avg_cost.setText(Html.fromHtml(avg_cost_pre.concat(avg_cost)));
                            restaurant_info_rating.setText(Html.fromHtml(rating_pre.concat(user_rating)));
                            restaurant_info_url.setText(Html.fromHtml(url_pre.concat(url)));
                            restaurant_info_aggregate_rating.setText(Html.fromHtml(agg_pre.concat(aggregate_rating)));
                            restaurant_info_votes.setText(Html.fromHtml(votes_pre.concat(votes)));

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
}
