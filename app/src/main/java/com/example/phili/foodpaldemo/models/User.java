package com.example.phili.foodpaldemo.models;

/**
 * Created by phili on 2018-02-21.
 */

public class User {

    private String userName;
    private String userEmailAddress;

    // user major : like CS, math, ..
    private String userMajor;
    private String userGender;
    private String userAddress;
    private String selfDescription;


    // default constructor
    public User(){

    }
    //


    public User(String userName, String userEmailAddress, String userMajor, String userSex, String userAddress, String selfDescription) {
        this.userName = userName;
        this.userEmailAddress = userEmailAddress;
        this.userMajor = userMajor;
        this.userGender = userSex;
        this.userAddress = userAddress;
        this.selfDescription = selfDescription;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmailAddress() {
        return userEmailAddress;
    }

    public void setUserEmailAddress(String userEmailAddress) {
        this.userEmailAddress = userEmailAddress;
    }

    public String getUserMajor() {
        return userMajor;
    }

    public void setUserMajor(String userMajor) {
        this.userMajor = userMajor;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getSelfDescription() {
        return selfDescription;
    }

    public void setSelfDescription(String selfDescription) {
        this.selfDescription = selfDescription;
    }
}
