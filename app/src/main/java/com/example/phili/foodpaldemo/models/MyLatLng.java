package com.example.phili.foodpaldemo.models;

/**
 * Created by yunfei on 2018-03-19.
 */

public class MyLatLng {

    public double latitude;
    public double longitude;

    public MyLatLng(){

    }

    public MyLatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
