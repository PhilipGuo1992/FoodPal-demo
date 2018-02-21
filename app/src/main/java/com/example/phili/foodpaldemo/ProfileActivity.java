package com.example.phili.foodpaldemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by phili on 2018-02-10.
 */

public class ProfileActivity extends AppCompatActivity {


    // firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // get firebase auth
        mAuth = FirebaseAuth.getInstance();


    }




    @Override
    protected void onStart() {
        super.onStart();

    }



}
