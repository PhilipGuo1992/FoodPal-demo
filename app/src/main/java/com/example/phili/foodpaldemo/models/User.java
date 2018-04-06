package com.example.phili.foodpaldemo.models;

import java.util.HashMap;
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
    private String selfDescription;
    private String userBirthday;
    private String photoUrl;
    private String device_token;

    // default constructor   **DO NOT REMOVE THE constructor**
    public User() {
    }

    // groups: user belongs to
    private Map<String, Boolean> joinedGroups = new HashMap<>();

    @Override
    public String toString() {
        return "userID " + userID + ". userName: " + userName + ". user joined groups: " + joinedGroups;
    }

    public String getUserBirthday() {
        return userBirthday;
    }

    public void setUserBirthday(String userBirthday) {
        this.userBirthday = userBirthday;
    }

    public User(String userID,String device_token, String userName, String userEmailAddress, String userMajor,
                String userGender, String userBirthday, String selfDescription, String photoUrl) {

        this.userID = userID;
        this.userName = userName;
        this.userEmailAddress = userEmailAddress;
        this.userMajor = userMajor;
        this.userGender = userGender;
        this.selfDescription = selfDescription;
        this.userBirthday = userBirthday;
        this.photoUrl = photoUrl;
        this.device_token = device_token;   //this.photoUrl = photoUrl;
    }

    //this constructor is used to save uri of profile image
    public User(String photoUrl) {
        this.photoUrl = photoUrl;
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

    public String getSelfDescription() {
        return selfDescription;
    }

    public void setSelfDescription(String selfDescription) {
        this.selfDescription = selfDescription;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Set<String> keySet() { return joinedGroups.keySet(); }

    public String getDevice_token() {return  device_token; }

    public void setDevice_token(){this.device_token = device_token;}
}
