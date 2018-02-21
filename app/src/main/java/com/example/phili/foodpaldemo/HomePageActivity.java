package com.example.phili.foodpaldemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by phili on 2018-02-21.
 */

public class HomePageActivity extends AppCompatActivity {

    private ListView groupList;

    // firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        // get widges
        groupList = findViewById(R.id.group_list);



        // get firebase auth
        mAuth = FirebaseAuth.getInstance();

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
            //


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
}




