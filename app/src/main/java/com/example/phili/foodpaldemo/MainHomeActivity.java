package com.example.phili.foodpaldemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

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

        // default load this fragment:
        loadFragment(new GroupListFragment(), R.id.fragment_container);
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
                break;
            case R.id.navigation_my_groups:
                fragment = new MyGroupsFragment();
                break;
            case R.id.navigation_restaurants:
                fragment = new RestaurantsFragment();
                break;
            case R.id.navigation_settings:
                fragment = new SettingsFragment();
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
