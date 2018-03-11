package com.example.phili.foodpaldemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

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

/**
 * Created by wzb on 2018/3/11.
 */

public class MyGroupActivity extends AppCompatActivity {

    public static final String GROUP_ID = "groupID";
    public static final String GROUP_CONTAIN_USER= "IF_CONTAIN_USER";

    private ListView groupListView;


    // fields for the four sections
    private ImageView imageViewJoinGroup;
    private ImageView imageViewGroupList;
    private ImageView imageViewRestaurant;
    private ImageView imageViewProfile;

    // firebase
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser currentUser;

    List<UserGroup> groupList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_group);


        // get firebase auth
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("groups");

        //
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        // get widges
        groupListView = findViewById(R.id.group_list);

        imageViewJoinGroup = findViewById(R.id.join_group_main);
        imageViewGroupList = findViewById(R.id.group_list_main);
        imageViewRestaurant = findViewById(R.id.restaurant_list_main);
        imageViewProfile = findViewById(R.id.profile_main);



        // set click event
        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // get current clicked group
                UserGroup currentGroup = groupList.get(i);
                //  start a new activity and pass data: the group id .
                // only pass groupID
                final String  currentGroupID = currentGroup.getGroupID();

                // before user click: to check if current user already in the group
                // if not, show the button of join the group
                final String userId = currentUser.getUid();
                myRef.child(currentGroupID).child("currentMembers").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // if contains the value
                            // contain the user
                            Log.i("test", "group contains the user");

                            // start intent
                            Intent intent = new Intent(getApplicationContext(), DisplayGroupInfoActivity.class);
                            // put id to intent
                            intent.putExtra(GROUP_ID, currentGroupID);
                            intent.putExtra(GROUP_CONTAIN_USER, true);
                            startActivity(intent);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




            }
        });


        //Bottom images onClick event
        imageViewJoinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //return
            }
        });
        imageViewGroupList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to myGroup
            }
        });
        imageViewRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //look up restaurant

            }
        });
        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to profile
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });




    }


    @Override
    protected void onStart() {
        super.onStart();
        // check if current user is sign in or not
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        final String userId = currentUser.getUid();

        if (currentUser != null){
            updateUI(currentUser);

            // get the current user information if user logined
            // the current group user has joined?
            loadGroups();


        } else {
            // finish the current activity.
            finish();
            // go to login page
            startActivity( new Intent(this, LoginActivity.class));
        }

    }


    // update the UI when user login in
    private void updateUI(FirebaseUser currentUser){

    }

    // load the existing groups from firebase;
    private void loadGroups(){
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        final String userId = currentUser.getUid();
        //
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // read from firebase
                groupList.clear();

                for(DataSnapshot userGroupSnap : dataSnapshot.getChildren()){

                    UserGroup userGroup = userGroupSnap.getValue(UserGroup.class);
                    if (dataSnapshot.child(userId).exists())
                    // add to list
                    groupList.add(userGroup);
                }

                // check the group list if it is correct
                Log.i("test", groupList.toString());

                // add listview adapter
                GroupListAdapter groupListAdapter = new GroupListAdapter(MyGroupActivity.this, groupList);
                // set adapter to listview
                groupListView.setAdapter(groupListAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
