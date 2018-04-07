package com.example.phili.foodpaldemo;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.phili.foodpaldemo.Fragment.GroupListFragment;
import com.example.phili.foodpaldemo.Fragment.MyGroupsFragment;
import com.example.phili.foodpaldemo.Fragment.RestaurantsFragment;
import com.example.phili.foodpaldemo.Fragment.SettingsFragment;
import com.example.phili.foodpaldemo.models.UserGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import java.util.HashSet;

public class MainHomeActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
                    BottomNavigationView.OnNavigationItemReselectedListener{

    private Fragment[] fragmentsArray;
    private FragmentManager fragmentManager;
    private long num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        fragmentManager = getSupportFragmentManager();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference uRef = FirebaseDatabase.getInstance().getReference().child("users")
                .child(user.getUid()).child("joinedGroups");


        uRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        DatabaseReference gRef = FirebaseDatabase.getInstance().getReference().child("groups");

        gRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                UserGroup g = dataSnapshot.getValue(UserGroup.class);
                if (g.getCurrentMembers().containsKey(user.getUid())) {

                    Intent intent = new Intent(getApplicationContext(), MainHomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                            getApplicationContext(), "0")
                            .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                            .setContentTitle("Some one joined the group")
                            .setContentText("You have a new message!")
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText("Much longer text that cannot fit one line..."))
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setContentIntent(pendingIntent)
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .setAutoCancel(true);

                    

                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

// notificationId is a unique int for each notification that you must define
                    notificationManager.notify(0, mBuilder.build());
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Fragment fragmentAllGroup = new GroupListFragment();
        Fragment fragmentMyGroup = new MyGroupsFragment();
        Fragment fragmentRes = new RestaurantsFragment();
        Fragment fragmentSett = new SettingsFragment();
        fragmentsArray = new Fragment[] {fragmentAllGroup, fragmentMyGroup, fragmentRes, fragmentSett};


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        // default load this fragment:
        switchFragments("group", "my", "restr", "setting", 0);

    }

    @Override
    protected void onResume() {
        Log.i("test", "resume on activity");
        super.onResume();
    }

    private void startWhichFragment(BottomNavigationView navigation) {

       // Intent intent = getIntent();
        Boolean load_mygroup = CreateGroupActivity.LOAD_MY_GROUP;
        Boolean settings = SettingsFragment.SETTING;
        Intent intent = getIntent();
        final String judge_notification = intent.getStringExtra("judge_notification");
        if(judge_notification == "yes")
            load_mygroup = true;

        if(load_mygroup){
            navigation.setSelectedItemId(R.id.navigation_my_groups);
            CreateGroupActivity.LOAD_MY_GROUP = false;
           // switchFragments("my", "group", "restr", "setting", 1);

        } else if (settings){
            navigation.setSelectedItemId(R.id.navigation_settings);
            SettingsFragment.SETTING = false;

        } else {
            // default load this fragment:
            switchFragments("group", "my", "restr", "setting", 0);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        // get previous fragment

       Fragment fragment = null;

       String str1, str2, str3, str4;
       int i;

       str1="group"; str2="my"; str3="restr"; str4="setting";
        i = 0;

        // switch between fragments
        switch (item.getItemId()) {
            case R.id.navigation_group_list:
                // [8]
                str1="group"; str2="my"; str3="restr"; str4="setting";
                i = 0;


                break;

            case R.id.navigation_my_groups:


                str1="my"; str2="group"; str3="restr"; str4="setting";
                i = 1;

                break;
            case R.id.navigation_restaurants:


                str1="restr"; str2="group"; str3="my"; str4="setting";
                i = 2;

                break;
            case R.id.navigation_settings:

                str1="setting"; str2="group"; str3="my"; str4="restr";
                i = 3;

                break;

        }
            // load the current selected fragment
        return  switchFragments(str1, str2, str3, str4, i);



    }

    private boolean switchFragments(String str1, String str2, String str3, String str4, int i) {

        // code from stackOverflow
        // [8]
        if(fragmentManager.findFragmentByTag(str1) != null) {
            fragmentManager.beginTransaction()
                    .show(fragmentManager.findFragmentByTag(str1))
                    .commit();

        } else {
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragmentsArray[i], str1)
                    .commit();

        }
        if(fragmentManager.findFragmentByTag(str2) != null){
            fragmentManager.beginTransaction()
                    .hide(fragmentManager.findFragmentByTag(str2))
                    .commit();
        }
        if(fragmentManager.findFragmentByTag(str3) != null){
            fragmentManager.beginTransaction()
                    .hide(fragmentManager.findFragmentByTag(str3))
                    .commit();
        }
        if(fragmentManager.findFragmentByTag(str4) != null){
            fragmentManager.beginTransaction()
                    .hide(fragmentManager.findFragmentByTag(str4))
                    .commit();
        }

        return true;
    }


    @Override
    public void onNavigationItemReselected(@NonNull MenuItem item) {
        return;
    }


}
