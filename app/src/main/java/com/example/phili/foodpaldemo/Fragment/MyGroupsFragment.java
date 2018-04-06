package com.example.phili.foodpaldemo.Fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.phili.foodpaldemo.GroupHolder;
import com.example.phili.foodpaldemo.R;
import com.example.phili.foodpaldemo.models.UserGroup;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyGroupsFragment extends android.support.v4.app.Fragment implements RecyclerView.OnClickListener {

    public static final String GROUP_ID = "groupID";
    public static final String GROUP_CONTAIN_USER= "IF_CONTAIN_USER";

    private List<UserGroup> allGroups = new ArrayList<>();
    // widegs
    private ListView groupList;
    FirebaseRecyclerAdapter<UserGroup, GroupHolder> recyclerAdapter;
    private RecyclerView recyclerView;
    private SearchView searchViewMyGroup;
    // firebase
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseUser currentUser;
    private String currentUserID;
    private DatabaseReference mDatabaseUsers;
    Map<String, Boolean> usergroup;
    private Query query;

    public MyGroupsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get current user
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        myRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("groups");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View groupListView = inflater.inflate(R.layout.fragment_my_groups, container, false);

        // get view elements
        recyclerView = groupListView.findViewById(R.id.recycler_view);
        //searchViewMyGroup = groupListView.findViewById(R.id.sv_mygroup);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        currentUserID = currentUser.getUid();

        query = myRef.orderByChild("currentMembers/"+currentUserID).equalTo(true);

        initRecyclerView(query);

//        searchViewMyGroup.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//                onStop();
//                query = myRef.orderByChild("currentMembers/"+currentUserID)
//                        .equalTo(true)
//                        .orderByChild("groupName")
//                        .startAt(newText)
//                        .endAt(newText + "\uf8ff");
//                initRecyclerView(query);
//                onStart();
//                return false;
//            }
//        });
        // groupList = groupListView.findViewById(R.id.group_list);
        recyclerView.setOnClickListener(this);

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


    public void initRecyclerView(Query query) {
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

    }


    @Override
    public void onClick(View v) {



    }
}
