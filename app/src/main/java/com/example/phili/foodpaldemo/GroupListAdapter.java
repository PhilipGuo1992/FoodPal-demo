package com.example.phili.foodpaldemo;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.phili.foodpaldemo.models.User;
import com.example.phili.foodpaldemo.models.UserGroup;

import java.util.List;
import java.util.Set;

/**
 * Created by phili on 2018-02-21.
 */

public class GroupListAdapter extends ArrayAdapter<UserGroup> {

    private Activity context;
    // list to store the groups
    private List<UserGroup> userGroups;

//    private String groupName;
//    private String mealTime;
//    private String restaurantName;
//
//    private String description;


    // one group can have many users
    private Set<User> currentUsers;


    public GroupListAdapter(Activity context, List<UserGroup> userGroups){
        super(context, R.layout.groups_list_layout, userGroups);

        this.context = context;
        this.userGroups = userGroups;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        // inflate the view
        View groupViewList = inflater.inflate(R.layout.groups_list_layout, null, true);
        // get widges from layout
        TextView groupName = groupViewList.findViewById(R.id.getGroupName);
        TextView grouRest = groupViewList.findViewById(R.id.getResName);
        TextView groupMealTime = groupViewList.findViewById(R.id.getMealTime);

        // only show the total group numbers, after user click the group: show currentMembers' name.
        // only show description when user click the group
        //TextView groupDescp = groupViewList.findViewById(R.id.getGroupDescrip);


        // get current group
        UserGroup userGroup = userGroups.get(position);
        // update the UI
        groupName.setText(userGroup.getGroupName());
        grouRest.setText(userGroup.getRestaurantName());
        groupMealTime.setText(userGroup.getMealTime());

        //groupDescp.setText(userGroup.getDescription());


        return groupViewList;
    }
}
