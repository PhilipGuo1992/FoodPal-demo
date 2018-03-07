package com.example.phili.foodpaldemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
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

import java.util.HashMap;
import java.util.Map;


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
    private DatabaseReference databaseReference, userDataReference;

    private FirebaseDatabase firebaseDatabase;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_creat_group);

        //Initialize firebase authentication
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        //If user is not logged on
        if (firebaseAuth.getCurrentUser() == null){
            //Finish the activity
            finish();
            startActivity(new Intent(this, LoginActivity.class));

        }

        databaseReference = firebaseDatabase.getReference("groups");
        userDataReference = firebaseDatabase.getReference("users");

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
        //String description =

        if (!TextUtils.isEmpty(groupName)) {
            String gId = databaseReference.push().getKey();
            //Construct a map to manage users and groups
            Map<String, Boolean> members = new HashMap<>();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            String uId = user.getUid();

            // add the group id to the user
            //  update the user's group info
            userDataReference.child(uId).child("joinedGroups").child(gId).setValue(true);

            //Put user to the current group
            members.put(uId,true);
            UserGroup userGroup = new UserGroup(gId,uId,groupName,mealTime,restaurantName,members);
            databaseReference.child(gId).setValue(userGroup);
            Log.i("test", "add group success");

            Toast.makeText(this, "create group success", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }



    @Override
    public void onClick(View view) {
        if (view == btnCreate) {
            createGroup();
        }
    }
}
