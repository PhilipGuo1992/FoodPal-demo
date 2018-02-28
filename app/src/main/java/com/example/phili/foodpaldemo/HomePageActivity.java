package com.example.phili.foodpaldemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
 * Created by phili on 2018-02-21.
 */

public class HomePageActivity extends AppCompatActivity {

    public static final String GROUP_ID = "groupID";

    private ListView groupListView;

    private FloatingActionButton createGroup;

    // firebase
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser currentUser;

    List<UserGroup> groupList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);


        // get firebase auth
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("groups");

        //
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        // get widges
        groupListView = findViewById(R.id.group_list);
        createGroup = findViewById(R.id.create_group);
        // set click event
        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // get current clicked group
                UserGroup currentGroup = groupList.get(i);
                //  start a new activity and pass data: the group id .
                // only pass groupID
               String currentGroupID = currentGroup.getGroupID();

               // before user click: to check if current user already in the group
                // if not, show the button of join the group
                final String userId = currentUser.getUid();
                myRef.child(currentGroupID).child("currentMembers").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // if contains the value
                        if (dataSnapshot.child(userId).exists()) {
                            // contain the user
                            Log.i("test", "group contains the user");
                        } else {

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


               // start intent
                Intent intent = new Intent(getApplicationContext(), DisplayGroupInfoActivity.class);
                // put id to intent
                intent.putExtra(GROUP_ID, currentGroupID);
                startActivity(intent);

            }
        });

        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CreateGroupActivity.class));
            }
        });




    }


    @Override
    protected void onStart() {
        super.onStart();
        // check if current user is sign in or not
        currentUser = mAuth.getCurrentUser();


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
        //
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // read from firebase
                groupList.clear();

                for(DataSnapshot userGroupSnap : dataSnapshot.getChildren()){

                    UserGroup userGroup = userGroupSnap.getValue(UserGroup.class);

                    // add to list
                    groupList.add(userGroup);
                }

                // check the group list if it is correct
               Log.i("test", groupList.toString());

                // add listview adapter
                GroupListAdapter groupListAdapter = new GroupListAdapter(HomePageActivity.this, groupList);
                // set adapter to listview
                groupListView.setAdapter(groupListAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}




