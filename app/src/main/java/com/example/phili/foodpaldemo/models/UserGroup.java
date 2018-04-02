package com.example.phili.foodpaldemo.models;

import com.google.android.gms.location.places.Place;

import java.util.Map;


/**
 * Created by phili on 2018-02-21.
 */

public class UserGroup {

    // the group creator: user ID.
    private String groupCreatorID;

    private String groupID;
    private String groupName;
    private String mealTime;

    private String description;
    private String restaurantID;


    // default constructor **DO NOT REMOVE THE constructor**
    public UserGroup() {
    }

    // current members in the group
    private Map<String, Boolean> currentMembers;

    public UserGroup(String groupID, String groupCreatorID, String groupName,
                     String mealTime, String restaurantID, Map<String, Boolean> currentMembers, String description) {
        this.groupID = groupID;
        this.groupCreatorID = groupCreatorID;
        this.groupName = groupName;
        this.mealTime = mealTime;
        this.restaurantID = restaurantID;
        //this.restaurantName = restaurantName;
        this.description = description;

        this.currentMembers = currentMembers;

    }

    public String getGroupCreatorID() {
        return groupCreatorID;
    }

    public void setGroupCreatorID(String groupCreatorID) {
        this.groupCreatorID = groupCreatorID;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }
}

