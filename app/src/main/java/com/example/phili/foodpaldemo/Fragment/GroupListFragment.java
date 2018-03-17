package com.example.phili.foodpaldemo.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.phili.foodpaldemo.CreateGroupActivity;
import com.example.phili.foodpaldemo.DisplayGroupInfoActivity;
import com.example.phili.foodpaldemo.GroupHolder;
import com.example.phili.foodpaldemo.GroupListAdapter;
import com.example.phili.foodpaldemo.R;
import com.example.phili.foodpaldemo.models.UserGroup;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupListFragment extends android.support.v4.app.Fragment implements RecyclerView.OnClickListener {

    public static final String GROUP_ID = "groupID";
    public static final String GROUP_CONTAIN_USER= "IF_CONTAIN_USER";

    private List<UserGroup> allGroups = new ArrayList<>();
    // widegs
    private ListView groupList;
    private FloatingActionButton createGroup;
    FirebaseRecyclerAdapter<UserGroup, GroupHolder> recyclerAdapter;
    private RecyclerView recyclerView;
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
        recyclerView = groupListView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        // https://github.com/firebase/FirebaseUI-Android/blob/master/database/README.md
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("groups");

        FirebaseRecyclerOptions<UserGroup> options =
                new FirebaseRecyclerOptions.Builder<UserGroup>()
                        .setQuery(query, UserGroup.class)
                        .build();
        // create adapter object
        recyclerAdapter = new FirebaseRecyclerAdapter<UserGroup, GroupHolder>(options) {

            @NonNull
            @Override
            public GroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                // create a instance of viewHolder
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.groups_list_layout, parent, false);

                return new GroupHolder(view, getActivity());
            }

            @Override
            protected void onBindViewHolder(@NonNull GroupHolder holder, int position, @NonNull UserGroup model) {
                // bind data to widget
                holder.bind(model);
                String clickedKey =  recyclerAdapter.getRef(position).getKey();
                holder.setGroupID(clickedKey);


            }
        };



        // attach the adapter to recyclerView
        recyclerView.setAdapter(recyclerAdapter);
       // groupList = groupListView.findViewById(R.id.group_list);
        recyclerView.setOnClickListener(this);

        createGroup = groupListView.findViewById(R.id.create_group);
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CreateGroupActivity.class));
            }
        });

       // updateFragmentView(recyclerView);

//        groupList.setOnItemClickListener(this);


        return groupListView;
    }







    @Override
    public void onStart() {
        super.onStart();
        recyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        recyclerAdapter.stopListening();
    }



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

    @Override
    public void onClick(View v) {

        

    }
}
