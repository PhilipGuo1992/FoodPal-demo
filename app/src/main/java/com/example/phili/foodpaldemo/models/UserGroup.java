package com.example.phili.foodpaldemo.models;

import java.util.Set;

/**
 * Created by phili on 2018-02-21.
 */

public class UserGroup {

    private String groupID;
    private String groupName;

    private String description;

    // one group can have many users
    private Set<User> currentUsers;



}
