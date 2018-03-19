package com.example.phili.foodpaldemo.models;

/**
 * Created by zongming on 2018-03-19.
 */

public class RestaurantItem {
    private String name;
    private String rating;

    public RestaurantItem(String name, String rating) {
        this.name = name;
        this.rating = rating;
    }


    public String getName() {
        return this.name;
    }

    public String getRating() {
        return this.rating;
    }
}
