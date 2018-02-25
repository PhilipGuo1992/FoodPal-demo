package com.example.phili.foodpaldemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by phili on 2018-02-10.
 */

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageViewedit;
    private ImageView imageViewphoto;
    private ImageView imageViewsubmit;
    //private Button btnedit;
    //private Button settings;
    private EditText username;
    private EditText major;
    private EditText email;
    private EditText gender;
    //private EditText age;
    private EditText birthday;
    private EditText about;

    // firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        mAuth = FirebaseAuth.getInstance();

        imageViewedit = findViewById(R.id.editprofile);
        imageViewphoto = findViewById(R.id.editphoto);
        imageViewsubmit = findViewById(R.id.editsubmit);
        username = findViewById(R.id.profileName);
        email = findViewById(R.id.email);
        gender = findViewById(R.id.gender);
        birthday = findViewById(R.id.birthday);
        major = findViewById(R.id.major);
        about = findViewById(R.id.selfdes);

        imageViewedit.setOnClickListener(this);
        imageViewsubmit.setOnClickListener(this);


    }

    private void setEdit() {
        //Set Visibility of 2 image buttons
        imageViewedit.setVisibility(View.INVISIBLE);
        imageViewsubmit.setVisibility(View.VISIBLE);
        //cannot set focus so that it can be edited.
        username.setFocusable(true);
        username.setFocusableInTouchMode(true);
        username.requestFocus();

        email.setFocusable(true);
        email.setFocusableInTouchMode(true);

        gender.setFocusable(true);
        gender.setFocusableInTouchMode(true);

        birthday.setFocusable(true);
        birthday.setFocusableInTouchMode(true);

        major.setFocusable(true);
        major.setFocusableInTouchMode(true);

        about.setFocusable(true);
        about.setFocusableInTouchMode(true);
    }

    private void save() {

        //Set Visibility of 2 image buttons
        imageViewedit.setVisibility(View.VISIBLE);
        imageViewsubmit.setVisibility(View.INVISIBLE);
        //cannot set focus so that it can be edited.
        username.setFocusable(false);
        username.setFocusableInTouchMode(false);

        email.setFocusable(false);
        email.setFocusableInTouchMode(false);

        gender.setFocusable(false);
        gender.setFocusableInTouchMode(false);

        birthday.setFocusable(false);
        birthday.setFocusableInTouchMode(false);

        major.setFocusable(false);
        major.setFocusableInTouchMode(false);

        about.setFocusable(false);
        about.setFocusableInTouchMode(false);

    }
    @Override
    public void onClick(View view) {
        if (view == imageViewedit) {
            setEdit();
        }

        if (view == imageViewsubmit) {
            save();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }
}
