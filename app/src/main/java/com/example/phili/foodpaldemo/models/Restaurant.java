package com.example.phili.foodpaldemo.models;

/**
 * Created by yunfei on 2018-03-12.
 */

public class Restaurant {
    private String resID;
    private String resName;
    private String resAddress;
    private String resPhoneNum;
    private String resWebsite;
   // private LatLng resLatLng;
    private MyLatLng resLatLng;

    public Restaurant(){

    }

    public Restaurant(String resID, String resName, String resAddress, String resPhoneNum, String resWebsite,  MyLatLng myLatLng) {
        this.resID = resID;
        this.resName = resName;
        this.resAddress = resAddress;
        this.resPhoneNum = resPhoneNum;
        this.resWebsite = resWebsite;
        this.resLatLng = myLatLng;
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


    public MyLatLng getResLatLng() {
        return resLatLng;
    }

    public void setResLatLng(MyLatLng resLatLng) {
        this.resLatLng = resLatLng;
    }
}
