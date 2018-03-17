package com.example.phili.foodpaldemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.example.phili.foodpaldemo.Fragment.GroupListFragment;
import com.example.phili.foodpaldemo.Fragment.MyGroupsFragment;
import com.example.phili.foodpaldemo.Fragment.RestaurantsFragment;
import com.example.phili.foodpaldemo.Fragment.SettingsFragment;

public class MainHomeActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener{


    private Fragment[] fragmentsArray;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        fragmentManager = getSupportFragmentManager();

        Fragment fragmentAllGroup = new GroupListFragment();
        Fragment fragmentMyGroup = new MyGroupsFragment();
        Fragment fragmentRes = new RestaurantsFragment();
        Fragment fragmentSett = new SettingsFragment();
        fragmentsArray = new Fragment[] {fragmentAllGroup, fragmentMyGroup, fragmentRes, fragmentSett};


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        // default load this fragment:
        //loadFragment(null, new GroupListFragment(), R.id.fragment_container);
        switchFragments("group", "my", "restr", "setting", 0);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        startWhichFragment(navigation);
    }

    private void startWhichFragment(BottomNavigationView navigation) {

        Intent intent = getIntent();
        Boolean load_mygroup = intent.getBooleanExtra("loadMyGroup", false);

        if(load_mygroup){
            navigation.setSelectedItemId(R.id.navigation_my_groups);
            loadFragment(null, new MyGroupsFragment(),  R.id.fragment_container);
        } else {
            // default load this fragment:
            loadFragment(null, new GroupListFragment(), R.id.fragment_container);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // get previous fragment
        Fragment previousFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

       Fragment fragment = null;

       String str1, str2, str3, str4;
       int i;

       str1="group"; str2="my"; str3="restr"; str4="setting";
        i = 0;

        // switch between fragments
        switch (item.getItemId()) {
            case R.id.navigation_group_list:
                //https://stackoverflow.com/questions/22713128/how-can-i-switch-between-two-fragments-without-recreating-the-fragments-each-ti/22714222

                str1="group"; str2="my"; str3="restr"; str4="setting";
                i = 0;

                if(previousFragment instanceof GroupListFragment){
                    fragment = null;
                }

                break;

            case R.id.navigation_my_groups:


                str1="my"; str2="group"; str3="restr"; str4="setting";
                i = 1;


                if(previousFragment instanceof MyGroupsFragment){
                    fragment = null;
                }
                break;
            case R.id.navigation_restaurants:


                str1="restr"; str2="group"; str3="my"; str4="setting";
                i = 2;
                fragment = fragmentsArray[2];
                if(previousFragment instanceof RestaurantsFragment){
                    fragment = null;
                }
                break;
            case R.id.navigation_settings:

                str1="setting"; str2="group"; str3="my"; str4="restr";
                i = 3;
                fragment = fragmentsArray[3];
                if(previousFragment instanceof SettingsFragment){
                    fragment = null;
                }
                break;
        }
        // load the current selected fragment
        return  switchFragments(str1, str2, str3, str4, i);


    }

    private boolean switchFragments(String str1, String str2, String str3, String str4, int i) {

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

    private boolean loadFragment(Fragment preFragment ,Fragment fragment, int viewPosition){


        if (fragment != null && preFragment!=null) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(preFragment)
                    // load view to the container
                    .show(fragment)
                    .commit();

            return true;
        } else if(fragment!=null && preFragment==null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(viewPosition, fragment, "group")
                    .commit();

            return true;
        }

        else {
            return false;
        }
    }















}
