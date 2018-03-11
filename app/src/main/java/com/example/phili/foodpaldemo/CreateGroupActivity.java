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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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


    public static final String message_month = "MONTH";
    public static final String message_date = "DATE";
    public static final String message_hour = "HOUR";
    public static final String message_minute = "MINUTE";

    private Spinner spinnerMinute, spinnerDate, spinnerHour, spinnerMonth;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_creat_group);

        spinnerDate = findViewById(R.id.spinnerDate);
        spinnerMonth = findViewById(R.id.spinnerMonth);
        spinnerHour = findViewById(R.id.spinnerHour);
        spinnerMinute = findViewById(R.id.spinnerMinute);

        ArrayAdapter<CharSequence> MonthAdapter = ArrayAdapter.createFromResource(this,
                R.array.month_array, R.layout.spinner_item);
        MonthAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        spinnerMonth.setAdapter(MonthAdapter);

        ArrayAdapter<CharSequence> DateAdapter = ArrayAdapter.createFromResource(this,
                R.array.date_array, R.layout.spinner_item);
        DateAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        spinnerDate.setAdapter(DateAdapter);

        ArrayAdapter<CharSequence> minuteAdapter = ArrayAdapter.createFromResource(this,
                R.array.minute_array, R.layout.spinner_item);
        minuteAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        spinnerMinute.setAdapter(minuteAdapter);

        ArrayAdapter<CharSequence> HourAdapter = ArrayAdapter.createFromResource(this,
                R.array.hours_array, R.layout.spinner_item);
        HourAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        spinnerHour.setAdapter(HourAdapter);

        Intent intent = new Intent();

        intent.putExtra(message_date,spinnerDate.getSelectedItem().toString());
        intent.putExtra(message_minute,spinnerMinute.getSelectedItem().toString());
        intent.putExtra(message_month,spinnerMonth.getSelectedItem().toString());
        intent.putExtra(message_hour,spinnerHour.getSelectedItem().toString());


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
        String mealTime = message_month+"."+message_date+"."+message_hour+"."+message_minute+".";
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


