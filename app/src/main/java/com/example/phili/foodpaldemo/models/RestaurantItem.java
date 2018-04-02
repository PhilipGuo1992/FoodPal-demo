package com.example.phili.foodpaldemo.models;

/**
 * Created by zongming on 2018-03-19.
 */

public class RestaurantItem {
    private String name;
    private String rating;
    private String address;

    public RestaurantItem(String name, String rating, String address) {
        this.name = name;
        this.rating = rating;
        this.address = address;
    }


    public String getName() {
        return this.name;
    }

    public String getRating() {
        return this.rating;
    }

    public String getAddress() {return this.address;}
}
