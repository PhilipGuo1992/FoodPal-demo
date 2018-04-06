package com.example.phili.foodpaldemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phili.foodpaldemo.Fragment.GroupListFragment;
import com.example.phili.foodpaldemo.models.ChatScreenActivity;
import com.example.phili.foodpaldemo.models.Restaurant;
import com.example.phili.foodpaldemo.models.User;
import com.example.phili.foodpaldemo.models.UserGroup;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.lang.String;
import java.lang.Boolean;

public class DisplayGroupInfoActivity extends AppCompatActivity
                        implements LeaveGroupConfirmFragment.LeaveDialogListener{

    // tag for dialog
    private static final String LEAVE_GROUP = "DialogLeave";

    // group id
    String groupID;
    private DatabaseReference mDatabaseGroup, mDatabaseRestr;
    private DatabaseReference mDatabaseUsers;
    private DatabaseReference mDatabaseNotification;
    //
    private TextView groupName, mealTime, restaurantName, description, memberNames;
    private Button joinGroupBtn, leaveGroupBtn;
private Button chatButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Boolean if_contain_user;
    String group_Name;
    String user_Name;
    String userEmail;
  public  Boolean flag =false;

    private String currentUserID;
    // group-currentMembers
    Map<String, Boolean> currentMembers;

    //
    private TextView restrName, restrPhone, restrWeb, restrAddress;
    private ImageView groupImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_group_info);

        // widges
        groupName = findViewById(R.id.display_group_name);
        mealTime = findViewById(R.id.display_mealTime);
       // restaurantName = findViewById(R.id.display_rest_name);
        description = findViewById(R.id.display_group_descrip);
        memberNames = findViewById(R.id.display_group_members);

        restrName = findViewById(R.id.display_resName);
        restrAddress = findViewById(R.id.display_resLocation);
        restrPhone = findViewById(R.id.display_phone);
        restrWeb = findViewById(R.id.display_web);
        groupImage = findViewById(R.id.show_group_image);

        // get buttons
        joinGroupBtn = findViewById(R.id.click_join_group);
        leaveGroupBtn = findViewById(R.id.click_leave_group);
        chatButton = findViewById(R.id.chatbutton);


        // assign the firebaseAuth first, before using it
        firebaseAuth = FirebaseAuth.getInstance();

        // check if user login or not
        if (firebaseAuth.getCurrentUser() == null) {
            //finish the activity
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        // get group id from intent
        Intent intent = getIntent();
        groupID = intent.getStringExtra(GroupHolder.GROUP_ID);
        group_Name = intent.getStringExtra("GROUPNAME");

        if_contain_user = intent.getBooleanExtra(GroupHolder.GROUP_CONTAIN_USER, false);

        // disable the related button
        if(if_contain_user){
            joinGroupBtn.setEnabled(false);
            chatButton.setVisibility(View.VISIBLE);
            // hide this button
            joinGroupBtn.setVisibility(View.GONE);

        } else {
            leaveGroupBtn.setEnabled(false);
            leaveGroupBtn.setVisibility(View.GONE);

        }

        // query firebase using group id
        mDatabaseGroup = FirebaseDatabase.getInstance().getReference("groups").child(groupID);

        mDatabaseRestr = FirebaseDatabase.getInstance().getReference("restaurants");

        mDatabaseUsers = FirebaseDatabase.getInstance().getReference("users");
        mDatabaseNotification = FirebaseDatabase.getInstance().getReference().child("notifications");

        firebaseUser = firebaseAuth.getCurrentUser();
        currentUserID = firebaseUser.getUid();

        joinGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // user want to join the group.
                // first: update the group member info
                try {
                    mDatabaseGroup.child("currentMembers").child(currentUserID).setValue(true);

                } catch (Exception e){
                    Log.i("test","click join group, " + e);

                }
                // second: update the user's group info
               mDatabaseUsers.child(currentUserID).child("joinedGroups").child(groupID).setValue(true);

                mDatabaseGroup.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // convert to java object
                        UserGroup currentGroup = dataSnapshot.getValue(UserGroup.class);
                        currentMembers = currentGroup.getCurrentMembers();
                        Set<String> membersID = currentMembers.keySet();
                        HashMap<String,String> notificationData = new HashMap<>();
                        notificationData.put("from", currentUserID);
                        notificationData.put("type", "join");

                // go to my group acvitity
                Intent intent = new Intent(DisplayGroupInfoActivity.this, MainHomeActivity.class);
                // put id to intent
                CreateGroupActivity.LOAD_MY_GROUP = true;
                //intent.putExtra("loadMyGroup", true);
                startActivity(intent);

                        for (final String userID : membersID){
                            //mDatabaseNotification.child(userID).push().setValue(notificationData);

                            //https://documentation.onesignal.com/reference#create-notification
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    int SDK_INT = Build.VERSION.SDK_INT;
                                    if (SDK_INT > 5) {
                                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                                .permitAll().build();
                                        StrictMode.setThreadPolicy(policy);
                                        try {
                                            String jsonResponse;

                                            URL url = new URL("https://onesignal.com/api/v1/notifications");
                                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                            con.setUseCaches(false);
                                            con.setDoOutput(true);
                                            con.setDoInput(true);

                                            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                                            con.setRequestProperty("Authorization", "Basic Yzc2OTZjYTUtMjlmNC00MjQ4LWIyMzAtMjIxNGI0M2ZiMjFk");
                                            con.setRequestMethod("POST");

                                            String strJsonBody = "{"
                                                    + "\"app_id\": \"9afeae9e-6904-4589-bc89-47f314578487\","
                                                    + "\"filters\": [{\"field\": \"tag\", \"key\": \"level\", \"relation\": \">\", \"value\": \"" + userID + "\"},{\"operator\": \"OR\"},{\"field\": \"amount_spent\", \"relation\": \">\",\"value\": \"0\"}],"
                                                    + "\"data\": {\"foo\": \"bar\"},"
                                                    + "\"contents\": {\"en\": \"English Message\"}"
                                                    + "}";


                                            System.out.println("strJsonBody:\n" + strJsonBody);

                                            byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                                            con.setFixedLengthStreamingMode(sendBytes.length);

                                            OutputStream outputStream = con.getOutputStream();
                                            outputStream.write(sendBytes);

                                            int httpResponse = con.getResponseCode();
                                            System.out.println("httpResponse: " + httpResponse);

                                            if (httpResponse >= HttpURLConnection.HTTP_OK
                                                    && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                                                Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                                                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                                                scanner.close();
                                            } else {
                                                Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                                                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                                                scanner.close();
                                            }
                                            System.out.println("jsonResponse:\n" + jsonResponse);

                                        } catch (Throwable t) {
                                            t.printStackTrace();
                                        }
                                    }
                                }
                            });


                        }

                            // user want to join the group.
                        // first: update the group member info
                        mDatabaseGroup.child("currentMembers").child(currentUserID).setValue(true);
                        // second: update the user's group info
                        mDatabaseUsers.child(currentUserID).child("joinedGroups").child(groupID).setValue(true);

                        Toast.makeText(DisplayGroupInfoActivity.this, "join the group success", Toast.LENGTH_SHORT).show();

                        // go to my group activity
                        intent = new Intent(DisplayGroupInfoActivity.this, MainHomeActivity.class);
                        // put id to intent
                        intent.putExtra("loadMyGroup", true);
                        startActivity(intent);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        });
        leaveGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // start a dialog first
               // android.app.FragmentManager manager = getFragmentManager();
                FragmentManager manager = getSupportFragmentManager();
                LeaveGroupConfirmFragment dialog = new LeaveGroupConfirmFragment();
                dialog.show(manager, LEAVE_GROUP);

            }
        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //
                //  String userName = FirebaseDatabase.getInstance().getReference().child()
               // group_Name =
              /*  mDatabaseGroup.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // convert to java object
                        UserGroup currentGroup = dataSnapshot.getValue(UserGroup.class);
                        group_Name= currentGroup.getGroupName();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
*/
             userEmail = firebaseAuth.getCurrentUser().getEmail();
                Intent chatScreen_intent = new Intent(DisplayGroupInfoActivity.this,ChatScreenActivity.class);
                chatScreen_intent.putExtra("GROUPNAME",group_Name);
                chatScreen_intent.putExtra("GROUPID",groupID);
                chatScreen_intent.putExtra("EMAIL",userEmail);
              startActivity(chatScreen_intent);
            }
        });

    }
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // first: update the group member info
         mDatabaseGroup.child("currentMembers").child(currentUserID).removeValue();
        // update UI or not?1

        // second: update the user's group info
        mDatabaseUsers.child(currentUserID).child("joinedGroups").child(groupID).removeValue();

        // disable join group button
        Log.i("test","click leave group");

        Toast.makeText(DisplayGroupInfoActivity.this, "leave the group success", Toast.LENGTH_SHORT).show();

        // go to my group acvitity
        Intent intent = new Intent(DisplayGroupInfoActivity.this, MainHomeActivity.class);
        // put id to intent
        intent.putExtra("loadMyGroup", true);
        startActivity(intent);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        Toast.makeText(DisplayGroupInfoActivity.this, "Stay in the group", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onStart() {
        super.onStart();

        // get current group info from firebase
        // this require active listen
        mDatabaseGroup.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // convert to java object
                UserGroup currentGroup = dataSnapshot.getValue(UserGroup.class);
                // updateUI
                updateUI(currentGroup);

                group_Name =currentGroup.getGroupName();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void updateUI(final UserGroup currentGroup){
        // SET descrip
        if(currentGroup.getDescription() != null){
            description.setText(currentGroup.getDescription());
        }
        // get restaurant
        String restaurantID = currentGroup.getRestaurantID();
        mDatabaseRestr.child(restaurantID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Restaurant currentRestr = dataSnapshot.getValue(Restaurant.class);
                // set restanrant attribute
                restrName.setText(currentRestr.getResName());
                restrAddress.setText(currentRestr.getResAddress());
                restrPhone.setText(currentRestr.getResPhoneNum());
                restrWeb.setText(currentRestr.getResWebsite());

                // load image
                final GeoDataClient mGeoDataClient ;
                mGeoDataClient = Places.getGeoDataClient(getApplicationContext(), null);

                String placeId = currentRestr.getResID();

                final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);
                photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                        // Get the list of photos.
                        PlacePhotoMetadataResponse photos = task.getResult();
                        PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                        PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(0);
                        CharSequence attribution = photoMetadata.getAttributions();
                        Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
                        photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                            @Override
                            public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                                PlacePhotoResponse photo = task.getResult();
                                Bitmap bitmap = photo.getBitmap();

                                // set up map
                                int oldWidth = bitmap.getWidth();
                                int oldHeight = bitmap.getHeight();
                                double ratio = oldHeight*1.0/(oldWidth*1.0);

                                int newWidth = 800;
                                int newHeight = (int)(newWidth * ratio);

                                Bitmap resized = Bitmap.createScaledBitmap(bitmap,
                                        newWidth, newHeight, true );

                                groupImage.setImageBitmap(resized);

                            }
                        });
                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        private TextView groupName, mealTime, restaurantName, description, currentMembers;
        groupName.setText(currentGroup.getGroupName());
        mealTime.setText(currentGroup.getMealTime());
      //  restaurantName.setText(currentGroup.getRestaurantName());
        description.setText(currentGroup.getDescription());
        // currentMembers is a Map.
        currentMembers = currentGroup.getCurrentMembers();

        if(currentMembers != null){
            // get all currentMembers ID
            Set<String> membersID = currentMembers.keySet();

            final List<String> userNamesList = new ArrayList<>();
            // for each user id , get the related username
            for (String userID : membersID) {

                // this does not require active listen
                mDatabaseUsers.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        User currentUser = dataSnapshot.getValue(User.class);
                        String currentUserName = currentUser.getUserName();

                        // add username to list
                        userNamesList.add(currentUserName);
                        user_Name = currentUserName;
                        // userEmail = currentUser.getUserEmailAddress();

                        // update ui
                        int listLength = userNamesList.toString().length();
                        memberNames.setText(userNamesList.toString().substring(1, listLength-1));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

        }


    }

}
