package com.example.phili.foodpaldemo;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phili.foodpaldemo.models.User;
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

    // tag for dialog
    private static final String LEAVE_GROUP = "DialogLeave";
    // group id
    String groupID;
    private DatabaseReference mDatabaseGroup;
    private DatabaseReference mDatabaseUsers;
    //
    private TextView groupName, mealTime, restaurantName, description, memberNames;
    private Button joinGroupBtn, leaveGroupBtn;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Boolean if_contain_user;

    private String userID;
    // group-currentMembers
    Map<String, Boolean> currentMembers;
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


        // assign the firebaseAuth first, before using it
        firebaseAuth = FirebaseAuth.getInstance();

        // check if user login or not
        if (firebaseAuth.getCurrentUser() == null) {
            //finish the activity
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        // get group id from intent
        Intent intent = getIntent();
        groupID = intent.getStringExtra(HomePageActivity.GROUP_ID);
        if_contain_user = intent.getBooleanExtra(HomePageActivity.GROUP_CONTAIN_USER, false);
        // disable the related button
        if(if_contain_user){
            joinGroupBtn.setEnabled(false);
        } else {
            leaveGroupBtn.setEnabled(false);
        }

        // query firebase using group id
        // get firebase
        mDatabaseGroup = FirebaseDatabase.getInstance().getReference("groups").child(groupID);

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference("users");


        joinGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // user want to join the group.

                // first: update the group member info
                mDatabaseGroup.child("currentMembers").child(userID).setValue(true);
                // update UI or not?
                // update the current-member-UI ?

                // second: update the user's group info
                mDatabaseUsers.child(userID).child("joinedGroups").child(groupID).setValue(true);

                // disable join group button
                Log.i("test","click join group");


                Toast.makeText(DisplayGroupInfoActivity.this, "add group success", Toast.LENGTH_SHORT).show();

            }
        });
        leaveGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start a dialog first
               // android.app.FragmentManager manager = getFragmentManager();
                FragmentManager manager = getSupportFragmentManager();
                LeaveGroupConfirmFragment dialog = new LeaveGroupConfirmFragment();
                dialog.show(manager, LEAVE_GROUP);


                // user want to leave the group.
                // first: update the group member info
                //  mDatabaseGroup.child("currentMembers").child(userID).removeValue();
                // update UI or not?1

                // second: update the user's group info
                //  mDatabaseUsers.child(userID).child("joinedGroups").child(groupID).removeValue();

                // disable join group button
                Log.i("test","click leave group");

                Toast.makeText(DisplayGroupInfoActivity.this, "leave group success", Toast.LENGTH_SHORT).show();


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        // read data from database

        // get current user
        firebaseUser = firebaseAuth.getCurrentUser();

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


        if(firebaseUser != null && currentMembers != null){

            userID = firebaseUser.getUid();


        }
    }

    private void updateUI(UserGroup currentGroup){
//        private TextView groupName, mealTime, restaurantName, description, currentMembers;
        groupName.setText(currentGroup.getGroupName());
        mealTime.setText(currentGroup.getMealTime());
        restaurantName.setText(currentGroup.getRestaurantName());
        description.setText(currentGroup.getDescription());
//        currentMembers.setText(currentGroup.getCurrentMembers());
        // currentMembers is a Map.
        currentMembers = currentGroup.getCurrentMembers();

        if(currentMembers != null){
            // get all currentMembers ID
            Set<String> membersID = currentMembers.keySet();
            // query firebase based on currentMembers id
            // for each user id , get the related username
            final List<String> userNamesList = new ArrayList<>();


            for (String userID : membersID) {

                // this does not require active listen
                // use listen once
                mDatabaseUsers.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // get user
                        User currentUser = dataSnapshot.getValue(User.class);

                        Log.i("test", currentUser.toString());

                        String currentUserName = currentUser.getUserName();
                        Log.i("test", currentUserName);
                        // add username to list
                        userNamesList.add(currentUserName);
                        Log.i("test", "this is working, " + userNamesList.toString());


                        // is there another way to do this?

                        // this is the callback function.. that is why.
                        memberNames.setText(userNamesList.toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            // show all the group currentMembers.
            Log.i("test", "mmm, " + userNamesList.toString());



        }


    }


}
