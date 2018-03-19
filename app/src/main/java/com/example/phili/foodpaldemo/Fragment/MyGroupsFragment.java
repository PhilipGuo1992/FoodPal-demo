package com.example.phili.foodpaldemo.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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
public class MyGroupsFragment extends android.support.v4.app.Fragment implements ListView.OnItemClickListener{

    public static final String GROUP_ID = "groupID";
    private List<UserGroup> usergroups = new ArrayList<>();

    private ListView groupList;

    // firebase
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser currentUser;

    public MyGroupsFragment() {
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
        View groupListView = inflater.inflate(R.layout.fragment_my_groups, container, false);
        groupList = groupListView.findViewById(R.id.group_list);


        updateFragmentView();

        groupList.setOnItemClickListener((AdapterView.OnItemClickListener) this);


        return groupListView;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    private void updateFragmentView(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("groups");
        currentUser = mAuth.getCurrentUser();
        final String userID = currentUser.getUid();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usergroups.clear();
                for(DataSnapshot userGroupSnap : dataSnapshot.getChildren()){

                    UserGroup userGroup = userGroupSnap.getValue(UserGroup.class);

                    // add to list with user id
                    if (dataSnapshot.child(userID).exists())
                    usergroups.add(userGroup);
                }
                Log.i("test", "get user's groups");

                GroupListAdapter groupListAdapter = new GroupListAdapter(getActivity(), usergroups);
                // set adapter to listview
                groupList.setAdapter(groupListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        UserGroup currentGroup = usergroups.get(i);
        Log.i("test", "first");
        final String currentGroupID = currentGroup.getGroupID();

        mAuth = FirebaseAuth.getInstance();
        myRef.child(currentGroupID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // if contains the value
                Log.i("test", "second ");


                // start intent
                Intent intent = new Intent(getActivity(), DisplayGroupInfoActivity.class);
                // put id to intent
                intent.putExtra(GROUP_ID, currentGroupID);
                startActivity(intent);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
