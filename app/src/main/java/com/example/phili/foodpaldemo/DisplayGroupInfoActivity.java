package com.example.phili.foodpaldemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.phili.foodpaldemo.models.UserGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private DatabaseReference mDatabaseGroup;
    private DatabaseReference mDatabaseUsers;
    //
    private TextView groupName, mealTime, restaurantName, description, memberNames;
    private Button joinGroupBtn, leaveGroupBtn;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private String userID;
    // group-members
    Map<String, Boolean> members;
    // user-groups
    Map<String, Boolean> userGroups;

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
        // get buttons
        joinGroupBtn = findViewById(R.id.click_join_group);
        leaveGroupBtn = findViewById(R.id.click_leave_group);



        // get group id from intent
        Intent intent = getIntent();
        groupID = intent.getStringExtra(HomePageActivity.GROUP_ID);
        // query firebase using group id
        // get firebase
        mDatabaseGroup = FirebaseDatabase.getInstance().getReference("groups").child(groupID);
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference("users");


    }

    @Override
    protected void onStart() {
        super.onStart();

        // read data from database
        // this require active listen
        mDatabaseGroup.addValueEventListener(new ValueEventListener() {
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

        //
        // get current user
        currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null && members != null){

            userID = currentUser.getUid();

            // check if current user already joined this group: if joined, then disable the join button.
            //join the group
            // check if member contains uid
            if( members.containsKey(userID)) {
                joinGroupBtn.setEnabled(false);
            }
            // check if current user not in this group : if not in, then disable the leave button.
            if(!members.containsKey(userID)){
                leaveGroupBtn.setEnabled(false);
            }


            joinGroupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // user want to join the group.

                    // first: update the group member info
                    mDatabaseGroup.child("currentMembers").child(userID).setValue(true);
                    // update UI or not?

                    // second: update the user's group info
                    mDatabaseUsers.child(userID).child("joinedGroups").child(groupID).setValue(true);

                }
            });
            leaveGroupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // user want to leave the group.
                    // first: update the group member info
                    mDatabaseGroup.child("currentMembers").child(userID).removeValue();
                    // update UI or not?

                    // second: update the user's group info
                    mDatabaseUsers.child(userID).child("joinedGroups").child(groupID).removeValue();


                }
            });

        }


    }

    private void updateUI(UserGroup currentGroup){
//        private TextView groupName, mealTime, restaurantName, description, members;
        groupName.setText(currentGroup.getGroupName());
        mealTime.setText(currentGroup.getMealTime());
        restaurantName.setText(currentGroup.getRestaurantName());
        description.setText(currentGroup.getDescription());
//        members.setText(currentGroup.getCurrentMembers());
        // members is a Map.
        members = currentGroup.getCurrentMembers();

        if(members != null){
            // get all members ID
            Set<String> membersID = members.keySet();
            // query firebase based on members id
            // for each user id , get the related username
            final List<String> userNames = new ArrayList<>();

            Log.i("test", membersID.toString());

//            for (String userID : membersID) {
//                // this does not require active listen
//                // use listen once
//                mDatabaseUsers.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        // get user
//                        User currentUser = dataSnapshot.getValue(User.class);
//                        String currentUserName = currentUser.getUserName();
//
//                        // add username to list
//                        userNames.add(currentUserName);
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//            }

        }


    }


}
