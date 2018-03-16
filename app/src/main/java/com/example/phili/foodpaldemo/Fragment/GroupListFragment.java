package com.example.phili.foodpaldemo.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.phili.foodpaldemo.CreateGroupActivity;
import com.example.phili.foodpaldemo.DisplayGroupInfoActivity;
import com.example.phili.foodpaldemo.GroupListAdapter;
import com.example.phili.foodpaldemo.R;
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
 * A simple {@link Fragment} subclass.
 */
public class GroupListFragment extends android.support.v4.app.Fragment implements ListView.OnItemClickListener {

    public static final String GROUP_ID = "groupID";
    public static final String GROUP_CONTAIN_USER= "IF_CONTAIN_USER";

    private List<UserGroup> allGroups = new ArrayList<>();
    // widegs
    private ListView groupList;
    private FloatingActionButton createGroup;


    // firebase
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser currentUser;

    public GroupListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get current user
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View groupListView = inflater.inflate(R.layout.fragment_group_list, container, false);

        // get view elements
        groupList = groupListView.findViewById(R.id.group_list);
        createGroup = groupListView.findViewById(R.id.create_group);
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CreateGroupActivity.class));
            }
        });

        updateFragmentView();

        groupList.setOnItemClickListener(this);


        return groupListView;
    }


    private void updateFragmentView(){
        //
        // get firebase auth
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("groups");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // read from firebase
                allGroups.clear();

                for(DataSnapshot userGroupSnap : dataSnapshot.getChildren()){

                    UserGroup userGroup = userGroupSnap.getValue(UserGroup.class);

                    // add to list
                    allGroups.add(userGroup);
                }

                // check the group list if it is correct
                Log.i("test", "get all groups");

                // add listview adapter

                if (allGroups.size() != 0){
                    GroupListAdapter groupListAdapter = new GroupListAdapter(getActivity(), allGroups);
                    // set adapter to listview
                    groupList.setAdapter(groupListAdapter);
                } else {
                    Toast.makeText(getActivity(),"No group yet! Try to create one", Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // get current clicked group
        UserGroup currentGroup = allGroups.get(i);
        Log.i("test", "first ");

        //  start a new activity and pass data: the group id .
        // only pass groupID
        final String  currentGroupID = currentGroup.getGroupID();

        // before user click: to check if current user already in the group
        // if not, show the button of join the group
        // get current user
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        final String userId = currentUser.getUid();
        myRef.child(currentGroupID).child("currentMembers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // if contains the value
                Log.i("test", "second ");

                if (dataSnapshot.child(userId).exists()) {
                    // contain the user
                    Log.i("test", "group contains the user");

                    // start intent
                    Intent intent = new Intent(getActivity(), DisplayGroupInfoActivity.class);
                    // put id to intent
                    intent.putExtra(GROUP_ID, currentGroupID);
                    intent.putExtra(GROUP_CONTAIN_USER, true);
                    startActivity(intent);

                } else {
                    // start intent
                    Intent intent = new Intent(getActivity(), DisplayGroupInfoActivity.class);
                    // put id to intent
                    intent.putExtra(GROUP_ID, currentGroupID);
                    intent.putExtra(GROUP_CONTAIN_USER, false);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
