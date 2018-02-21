package com.example.phili.foodpaldemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.phili.foodpaldemo.models.UserGroup;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



/**
 * Created by yiren on 2018-02-21.
 */

public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener{

    //EditText on this page
    private EditText editTextgName;
    private EditText editTextTime;
    private EditText editTextRestaurant;

    private TextView textViewEmail;

    private Button btnCreate;

    //Dialog for redirecting
    private ProgressDialog progressDialog;

    //Firebase
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_creat_group);

        //Initialize firebase authentication
        firebaseAuth = FirebaseAuth.getInstance();

        //If user is not logged on
        if (firebaseAuth.getCurrentUser() == null){
            //Finish the activity
            finish();
            startActivity(new Intent(this, LoginActivity.class));

        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        editTextgName = findViewById(R.id.create_name);
        editTextTime = findViewById(R.id.create_time);
        editTextRestaurant = findViewById(R.id.create_res);
        textViewEmail = findViewById(R.id.textViewEmail);

        btnCreate = findViewById(R.id.btn_create);

        textViewEmail.setText("Welcome" + user.getEmail());

        btnCreate.setOnClickListener(this);
    }

    private void createGroup(){
        String groupName = editTextgName.getText().toString().trim();
        String mealTime = editTextTime.getText().toString().trim();
        String restaurantName = editTextRestaurant.getText().toString().trim();

        UserGroup userGroup = new UserGroup(groupName,mealTime,restaurantName);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference.child(user.getUid()).setValue(userGroup);

        Toast.makeText(this, "Group created.", Toast.LENGTH_LONG).show();


    }



    @Override
    public void onClick(View view) {
        if (view == btnCreate) {
            createGroup();
        }
    }
}
