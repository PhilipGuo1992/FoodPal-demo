package com.example.phili.foodpaldemo.models;

import java.util.Map;
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
    private String userGender;
    private String userAddress;
    private String selfDescription;

    // groups: user belongs to
    private Map<String, Boolean> joinedGroups;

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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Map<String, Boolean> getJoinedGroups() {
        return joinedGroups;
    }

    public void setJoinedGroups(Map<String, Boolean> joinedGroups) {
        this.joinedGroups = joinedGroups;
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
