package com.example.phili.foodpaldemo;

import android.content.Intent;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.text.TextWatcher;
import android.text.Editable;

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


public class MyGroupActivity extends AppCompatActivity{

    public static final String GROUP_ID = "groupID";
    private ListView MyGroupList;
    private GroupListAdapter adapter;
    private ImageView HomePage;
    private ImageView Restaurant;
    private ImageView profile;
    private EditText mEdtSearch;

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser currentUser;

    List<UserGroup> groupList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_group);

        MyGroupList = findViewById(R.id.group_list);

        HomePage = findViewById(R.id.group_list_main);
        Restaurant = findViewById(R.id.restaurant_list_main);
        profile = findViewById(R.id.profile_main);
        mEdtSearch = findViewById(R.id.text_search);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("groups");

        mAuth = FirebaseAuth.getInstance();

        MyGroupList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l ){
                UserGroup currentGroup = groupList.get(i);
                final String  currentGroupID = currentGroup.getGroupID();
                myRef.child(currentGroupID).addListenerForSingleValueEvent(new ValueEventListener(){
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Intent intent = new Intent(getApplicationContext(), DisplayGroupInfoActivity.class);
                        intent.putExtra(GROUP_ID, currentGroupID);
                        startActivity(intent);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        /**
        mEdtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user change the text
                MyGroupList.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                //
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                //
            }
        });         */
        HomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainHomeActivity.class));//go to myGroup
            }
        });
        Restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //look up restaurant

            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go to profile
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });


    }

    @Override
    protected void onStart(){
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        final String userID = currentUser.getUid();

        if(currentUser!=null)
        {
            updateUI(currentUser);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    groupList.clear();

                    for(DataSnapshot userGroupSnap : dataSnapshot.getChildren()){

                        UserGroup userGroup = userGroupSnap.getValue(UserGroup.class);
                        if (dataSnapshot.child(userID).exists())
                            groupList.add(userGroup);
                    }


                    Log.i("test", groupList.toString());
                    GroupListAdapter groupListAdapter = new GroupListAdapter(MyGroupActivity.this, groupList);
                    MyGroupList.setAdapter(groupListAdapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
    private void updateUI(FirebaseUser currentUser){

    }


}