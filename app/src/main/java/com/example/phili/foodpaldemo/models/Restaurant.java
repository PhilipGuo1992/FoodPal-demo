package com.example.phili.foodpaldemo.models;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by yunfei on 2018-03-12.
 */

public class Restaurant {
    private String resID;
    private String resName;
    private String resAddress;
    private String resPhoneNum;
    private String resWebsite;
    private LatLng resLatLng;

    public Restaurant(){

    }

    public Restaurant(String resID, String resName, String resAddress, String resPhoneNum, String resWebsite, LatLng resLatLng) {
        this.resID = resID;
        this.resName = resName;
        this.resAddress = resAddress;
        this.resPhoneNum = resPhoneNum;
        this.resWebsite = resWebsite;
        this.resLatLng = resLatLng;
    }

    public String getResID() {
        return resID;
    }

    public void setResID(String resID) {
        this.resID = resID;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getResAddress() {
        return resAddress;
    }

    public void setResAddress(String resAddress) {
        this.resAddress = resAddress;
    }

    public String getResPhoneNum() {
        return resPhoneNum;
    }

    public void setResPhoneNum(String resPhoneNum) {
        this.resPhoneNum = resPhoneNum;
    }

    public String getResWebsite() {
        return resWebsite;
    }

    public void setResWebsite(String resWebsite) {
        this.resWebsite = resWebsite;
    }

    public LatLng getResLatLng() {
        return resLatLng;
    }

    public void setResLatLng(LatLng resLatLng) {
        this.resLatLng = resLatLng;
    }
}
