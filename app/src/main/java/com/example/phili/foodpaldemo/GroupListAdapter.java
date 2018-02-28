package com.example.phili.foodpaldemo;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.phili.foodpaldemo.models.User;
import com.example.phili.foodpaldemo.models.UserGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Set;

/**
 * Created by phili on 2018-02-21.
 */

public class GroupListAdapter extends ArrayAdapter<UserGroup> {

    private Activity context;
    // list to store the groups
    private List<UserGroup> userGroups;


    private DatabaseReference mDatabaseUser;


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
        TextView groupTotalMember = groupViewList.findViewById(R.id.total_members);
        final TextView createrName = groupViewList.findViewById(R.id.getCreaterName);




        // only show description when user click the group

        // get current group
        UserGroup userGroup = userGroups.get(position);
        // update the UI
        groupName.setText(userGroup.getGroupName());
        grouRest.setText(userGroup.getRestaurantName());
        groupMealTime.setText(userGroup.getMealTime());

        // only show the total group numbers, after user click the group: show currentMembers' name.

        // need to fix dabase first: some group has no members.

        int members = userGroup.getCurrentMembers().size();
        Log.i("test", members+"");
        // need to convert int to string first
        groupTotalMember.setText(members + "");
//        // show the creater name
        String groupCreaterID =  userGroup.getGroupCreaterID();
//        // read firebaase to get the creater's name
        try{
            mDatabaseUser = FirebaseDatabase.getInstance().getReference("users").child(groupCreaterID);
        } catch (Exception e){
            Log.i("test", e+"");
        }
       // mDatabaseUser = FirebaseDatabase.getInstance().getReference("users").child(groupCreaterID);

//        mDatabaseUser.child("userName").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // get user name, and set user name
//                String username = dataSnapshot.getValue(String.class);
//                createrName.setText(username);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


        return groupViewList;
    }
}
