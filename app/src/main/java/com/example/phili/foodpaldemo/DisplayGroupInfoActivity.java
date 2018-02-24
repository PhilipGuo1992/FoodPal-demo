package com.example.phili.foodpaldemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.phili.foodpaldemo.models.User;
import com.example.phili.foodpaldemo.models.UserGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DisplayGroupInfoActivity extends AppCompatActivity {

    // group id
    String groupID;
    private DatabaseReference mDatabaseGroups;
    private DatabaseReference mDatabaseUsers;
    //
    private TextView groupName, mealTime, restaurantName, description, memberNames;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_group_info);

        // widges
        groupName = findViewById(R.id.display_group_name);
        mealTime = findViewById(R.id.display_mealTime);
        restaurantName = findViewById(R.id.display_rest_name);
        description = findViewById(R.id.display_group_descrip);
        memberNames = findViewById(R.id.display_group_members);


        // get group id from intent
        Intent intent = getIntent();
        groupID = intent.getStringExtra(HomePageActivity.GROUP_ID);
        // query firebase using group id
        // get firebase
        mDatabaseGroups = FirebaseDatabase.getInstance().getReference("groups").child(groupID);
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference("users");

    }

    @Override
    protected void onStart() {
        super.onStart();

        // read data from database
        // this require active listen
        mDatabaseGroups.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // convert to java object
                UserGroup currentGroup = dataSnapshot.getValue(UserGroup.class);
                // updateUI
                updateUI(currentGroup);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateUI(UserGroup currentGroup){
//        private TextView groupName, mealTime, restaurantName, description, members;
        groupName.setText(currentGroup.getGroupName());
        mealTime.setText(currentGroup.getMealTime());
        restaurantName.setText(currentGroup.getRestaurantName());
        description.setText(currentGroup.getDescription());
//        members.setText(currentGroup.getCurrentMembers());
        // members is a Map.
        Map<String, Boolean> members = currentGroup.getCurrentMembers();
        if(members != null){
            Set<String> membersID = members.keySet();
            // query firebase based on members id
            // for each user id , get the related username
            final List<String> userNames = new ArrayList<>();

            for (String userID : membersID) {
                // this does not require active listen
                // use listen once
                mDatabaseUsers.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // get user
                        User currentUser = dataSnapshot.getValue(User.class);
                        String currentUserName = currentUser.getUserName();

                        // add username to list
                        userNames.add(currentUserName);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
            // finish adding username
            // update all members name




        }


    }
}
