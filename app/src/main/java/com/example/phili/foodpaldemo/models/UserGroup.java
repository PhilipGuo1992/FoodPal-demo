package com.example.phili.foodpaldemo.models;

import java.util.Map;


/**
 * Created by phili on 2018-02-21.
 */

public class UserGroup {

    // the group creater: user ID.
    private String groupCreater;

    private String groupID;
    private String groupName;
    private String mealTime;
    private String restaurantName;

    private String description;


    // current members in the group
    private Map<String, Boolean> currentMembers;


    // default constructor
    public UserGroup(){

    }

    public UserGroup(String groupID, String groupName, String mealTime, String restaurantName) {
        this.groupID = groupID;
        this.groupName = groupName;
        this.mealTime = mealTime;
        this.restaurantName = restaurantName;
        //this.description = description;
    }


    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public Map<String, Boolean> getCurrentMembers() {
        return currentMembers;
    }

    public void setCurrentMembers(Map<String, Boolean> currentMembers) {
        this.currentMembers = currentMembers;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMealTime() {
        return mealTime;
    }

    public void setMealTime(String mealTime) {
        this.mealTime = mealTime;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    }

