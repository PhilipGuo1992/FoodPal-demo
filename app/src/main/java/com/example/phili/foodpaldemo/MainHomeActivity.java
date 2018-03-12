package com.example.phili.foodpaldemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.example.phili.foodpaldemo.Fragment.GroupListFragment;
import com.example.phili.foodpaldemo.Fragment.MyGroupsFragment;
import com.example.phili.foodpaldemo.Fragment.RestaurantsFragment;
import com.example.phili.foodpaldemo.Fragment.SettingsFragment;

public class MainHomeActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener{





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        Boolean load_mygroup = intent.getBooleanExtra(CreateGroupActivity.LOAD_MY_GROUP, false);

        if(load_mygroup){
            navigation.setSelectedItemId(R.id.navigation_my_groups);
            loadFragment(new MyGroupsFragment(),  R.id.fragment_container);
        } else {
            // default load this fragment:
            loadFragment(new GroupListFragment(), R.id.fragment_container);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // get previous fragment
        Fragment previousFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

       Fragment fragment = null;
        // switch between fragments
        switch (item.getItemId()) {
            case R.id.navigation_group_list:
                fragment = new GroupListFragment();
                if(previousFragment instanceof GroupListFragment){
                    fragment = null;
                }
                break;
            case R.id.navigation_my_groups:
                fragment = new MyGroupsFragment();
                if(previousFragment instanceof MyGroupsFragment){
                    fragment = null;
                }
                break;
            case R.id.navigation_restaurants:
                fragment = new RestaurantsFragment();
                if(previousFragment instanceof RestaurantsFragment){
                    fragment = null;
                }
                break;
            case R.id.navigation_settings:
                fragment = new SettingsFragment();
                if(previousFragment instanceof SettingsFragment){
                    fragment = null;
                }
                break;
        }
        // load the current selected fragment
        return loadFragment(fragment, R.id.fragment_container);

    }

    private boolean loadFragment(Fragment fragment, int viewPosition){
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    // load view to the container
                    .replace(viewPosition, fragment)
                    .commit();

            return true;
        } else {
            return false;
        }
    }















}
