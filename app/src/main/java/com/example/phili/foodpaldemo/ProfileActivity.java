package com.example.phili.foodpaldemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.phili.foodpaldemo.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yiren on 2018-02-10.
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

    private String uId;

    // firebase
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabaseGroup;
    private DatabaseReference mDatabaseUsers;

    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        if (firebaseAuth.getCurrentUser() == null) {
            //Finish the activity
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        FirebaseUser user = firebaseAuth.getCurrentUser();
        uId = user.getUid();
        mDatabaseUsers = firebaseDatabase.getReference("users").child(uId);




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

        gender.setFocusable(false);
        gender.setFocusableInTouchMode(false);

        birthday.setFocusable(false);
        birthday.setFocusableInTouchMode(false);

        major.setFocusable(false);
        major.setFocusableInTouchMode(false);

        about.setFocusable(false);
        about.setFocusableInTouchMode(false);
        //Get current value
        String uName = username.getText().toString().trim();
        String uEmail = email.getText().toString().trim();
        String uGender = gender.getText().toString().trim();
        String uBirthday = birthday.getText().toString().trim();
        String uMajor = major.getText().toString().trim();
        String uAbout = about.getText().toString().trim();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String uId = firebaseUser.getUid();

        //Map<String, Boolean> currentMembers = new HashMap<>();

        User user = new User(uId, uName, uEmail, uMajor, uGender, uBirthday,uAbout);
        mDatabaseUsers.setValue(user);

        Toast.makeText(this, "Changes submitted", Toast.LENGTH_LONG).show();

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

        mDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);

                username.setText(currentUser.getUserName());
                email.setText(currentUser.getUserEmailAddress());
                gender.setText(currentUser.getUserGender());
                birthday.setText(currentUser.getUserBirthday());
                major.setText(currentUser.getUserMajor());
                about.setText(currentUser.getSelfDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


}
