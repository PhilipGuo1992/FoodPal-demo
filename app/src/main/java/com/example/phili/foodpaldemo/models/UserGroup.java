package com.example.phili.foodpaldemo.models;

import java.util.Set;

/**
 * Created by phili on 2018-02-21.
 */

public class UserGroup {

    public String groupID;
    public String groupName;
    public String mealTime;
    public String restaurantName;

   // private String description;



    // one group can have many users
   // public Set<User> currentUsers;

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



}
