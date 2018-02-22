package com.example.phili.foodpaldemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by phili on 2018-02-10.
 */

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton imageButton;
    private Button edit;
    private Button settings;
    private EditText username;
    private EditText major;
    private EditText email;
    private EditText gender;
    private EditText age;

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
    public void onClick(View view) {

    }


    @Override
    protected void onStart() {
        super.onStart();

    }
}
