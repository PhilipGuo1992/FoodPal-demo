package com.example.phili.foodpaldemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.bigkoo.pickerview.TimePickerView;
import com.example.phili.foodpaldemo.models.ChatScreenActivity;
import com.example.phili.foodpaldemo.models.MyLatLng;
import com.example.phili.foodpaldemo.models.Restaurant;
import com.example.phili.foodpaldemo.models.UserGroup;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by yiren on 2018-02-21.
 */

public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener{

    public static  boolean LOAD_MY_GROUP = false;

    //EditText on this page
    private EditText editTextGroupName;
    private EditText editTextTime;
    private EditText editTextRestaurant;
    private TextView textViewEmail;
    private EditText editTextDescription;
    private Button btnCreate;

    private TimePickerView timePickerView;

    // google place
    private ImageButton choosePlace;
    private final static int PLACE_PICKER_REQUEST = 1;
    private final static LatLngBounds bounds = new LatLngBounds(new LatLng( 44.623740,-63.645071), new LatLng(44.684002, -63.557137));
    private TextView placeName;
    private Place place;

    //Dialog for redirecting
    private ProgressDialog progressDialog;

    //Firebase
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference, userDataReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_creat_group);

        initFirebase();
        initTimePicker();

        editTextGroupName = findViewById(R.id.et_groupname);
        editTextTime = findViewById(R.id.et_mealtime);
        choosePlace = findViewById(R.id.create_res);
        placeName = findViewById(R.id.place_name);
        editTextDescription = findViewById(R.id.gp_descrip);
        //textViewEmail = findViewById(R.id.textViewEmail);
        btnCreate = findViewById(R.id.btn_create);

        //textViewEmail.setText("Welcome" + firebaseUser.getEmail());
        btnCreate.setOnClickListener(this);
        editTextTime.setOnClickListener(this);
    }

    private void createGroup(){
        String groupName = editTextGroupName.getText().toString().trim();
        String mealTime = editTextTime.getText().toString().trim();

       // String restaurantName = editTextRestaurant.getText().toString().trim();

        String description = editTextDescription.getText().toString().trim();

        if (!TextUtils.isEmpty(groupName) && place != null && mealTime!=null && description!=null) {
            // android.os.TransactionTooLargeException: data parcel size 1163212 bytes
            // get the restaurant
            MyLatLng myLatLng = new MyLatLng(place.getLatLng().latitude, place.getLatLng().longitude);

            Restaurant restaurant = new Restaurant(place.getId(), place.getName().toString(), place.getAddress().toString(),
                        place.getPhoneNumber().toString(), place.getWebsiteUri().toString(),myLatLng);

            String gId = databaseReference.child("groups").push().getKey();
            //Construct a map to manage users and groups
            Map<String, Boolean> members = new HashMap<>();

            String uId = firebaseUser.getUid();

            // add the group id to the user
            //  update the user's group info
            userDataReference.child(uId).child("joinedGroups").child(gId).setValue(true);

            //Put user to the current group
            members.put(uId,true);

            UserGroup userGroup = new UserGroup(gId, uId, groupName, mealTime, place.getId(), members, description);
            try {
                // only store restaurant id in group.
                databaseReference.child("groups").child(gId).setValue(userGroup);

                databaseReference.child("restaurants").child(place.getId()).setValue(restaurant);

            } catch (Exception e){
                e.printStackTrace();
            }

            Toast.makeText(this, "create group success", Toast.LENGTH_SHORT).show();
            // go to my groups
            Intent intent = new Intent(this, MainHomeActivity.class);

            Intent chat_intent = new Intent(this, DisplayGroupInfoActivity.class);
            chat_intent.putExtra("GROUPNAME",groupName);
            //LOAD_MY_GROUP = true;
            // put id to intent
           // intent.putExtra(LOAD_MY_GROUP, true);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(this, "Please enter required information", Toast.LENGTH_LONG).show();
        }
    }

    private void chooseRestaurant(){

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        builder.setLatLngBounds(bounds);

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {


                place = PlacePicker.getPlace(CreateGroupActivity.this, data);
                // address=6299 South St, Halifax, NS B3H 4R2, Canada, phoneNumber=+1 902-494-2211,
                // latlng=lat/lng: (44.636581199999995,-63.591655499999995), viewport=LatLngBounds{southwest=lat/lng: (44.63575445,-63.60206124999999),

                if(place != null){
                    placeName.setText(place.getName());

                }else {
                    Toast.makeText(this, "Please choose a restaurant", Toast.LENGTH_LONG).show();
                    return;
                }

            }
        }
    }



    @Override
    public void onClick(View view) {

        if (view == btnCreate) {
            createGroup();
        }

        if (view == choosePlace) {
            // start the place picker
            chooseRestaurant();
        }

        if (view == editTextTime) {
            pickTime(view);
        }
    }

    private void pickTime(View view) {

        timePickerView.setDate(Calendar.getInstance());
        timePickerView.show(view);
    }

    public void initFirebase() {
        //Initialize firebase authentication
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        //If user is not logged on
        if (firebaseAuth.getCurrentUser() == null){
            //Finish the activity
            finish();
            startActivity(new Intent(this, LoginActivity.class));

        }
        databaseReference = firebaseDatabase.getReference();
        userDataReference = firebaseDatabase.getReference("users");
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    public void initTimePicker() {

        Calendar selectedDate = Calendar.getInstance();

        timePickerView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                EditText editText = (EditText) v;
                editText.setText(getTime(date));
            }
        })
                //year/month/day/hour/minute/second
                .setType(new boolean[]{true,true,true,true,true,false})
                .setLabel("","","","","","")
                .isCenterLabel(false)
                //IOS-liked color
                .setDividerColor(Color.DKGRAY)
                .setContentSize(18)
                .setDate(selectedDate)
                .setDecorView(null)
                .build();

    }

    public String getTime(Date date) {

        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdFormat.format(date);

    }
}


