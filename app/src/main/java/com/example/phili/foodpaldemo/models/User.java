package com.example.phili.foodpaldemo.models;

import java.util.Set;

/**
 * Created by phili on 2018-02-21.
 */

public class User {

    private String userID;
    private String userName;
    private String userEmailAddress;

    // user major : like CS, math, ..
    private String userMajor;
    private String userSex;
    private String userAddress;
    private String selfDescription;

    // one user can join many groups.
    private Set<UserGroup> joinedGroups;

    // default constructor
    public User(){

    }
    // constructor to generate new user.
    public User(String userName, String userEmailAddress, String userMajor, String userSex, String userAddress, String selfDescription) {
        this.userName = userName;
        this.userEmailAddress = userEmailAddress;
        this.userMajor = userMajor;
        this.userSex = userSex;
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

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
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
