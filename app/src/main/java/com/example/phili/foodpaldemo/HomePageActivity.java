package com.example.phili.foodpaldemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

/**
 * Created by phili on 2018-02-21.
 */

public class HomePageActivity extends AppCompatActivity {

    private ListView groupListView;

    // firebase
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;

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


        // get widges
        groupListView = findViewById(R.id.group_list);
        // set click event
        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // get current clicked group
                UserGroup currentGroup = groupList.get(i);
                //  start a new activity and pass data too.
                // only pass groupID

            }
        });




    }


    @Override
    protected void onStart() {
        super.onStart();
        // check if current user is sign in or not
        FirebaseUser currentUser = mAuth.getCurrentUser();


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




