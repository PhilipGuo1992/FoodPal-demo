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
    private Button edit;
    private Button settings;
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
        username = findViewById(R.id.profileName);
        email = findViewById(R.id.email);
        gender = findViewById(R.id.gender);
        birthday = findViewById(R.id.birthday);
        major = findViewById(R.id.major);
        about = findViewById(R.id.selfdes);






    }

    @Override
    public void onClick(View view) {

    }


    @Override
    protected void onStart() {
        super.onStart();
    }
}
