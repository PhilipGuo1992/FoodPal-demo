package com.example.phili.foodpaldemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phili.foodpaldemo.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * Created by yiren on 2018-02-10.
 */

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageViewedit;
    private ImageView imageViewphoto;
    private ImageView imageViewsubmit;
    private Button btnedit;
    private Button settings;
    private EditText username;
    private EditText major;
    private EditText email;
    private EditText gender;
    //private EditText age;
    private EditText birthday;
    private EditText about;
    private TextView signOut;

    // fields for the four sections
    private ImageView imageViewJoinGroup;
    private ImageView imageViewGroupList;
    private ImageView imageViewRestaurant;
    private ImageView imageViewProfile;


    private String uId;

    private static final int IMAGE_REQUEST = 100;
    private Uri filePath;

    // firebase
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabaseGroup;
    private DatabaseReference mDatabaseUsers;

    private FirebaseDatabase firebaseDatabase;
    //StorageReference for images
    private StorageReference imageStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        imageStorage = FirebaseStorage.getInstance().getReference();

        if (firebaseAuth.getCurrentUser() == null) {
            //Finish the activity
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
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
        signOut = findViewById(R.id.signOut);

        imageViewJoinGroup = findViewById(R.id.join_group);
        imageViewGroupList = findViewById(R.id.group_list);
        imageViewRestaurant = findViewById(R.id.restaurant_list);
        imageViewProfile = findViewById(R.id.profile);

        imageViewedit.setOnClickListener(this);
        imageViewsubmit.setOnClickListener(this);
        imageViewphoto.setOnClickListener(this);

        imageViewJoinGroup.setOnClickListener(this);
        imageViewGroupList.setOnClickListener(this);
        imageViewRestaurant.setOnClickListener(this);
        imageViewProfile.setOnClickListener(this);
        signOut.setOnClickListener(this);
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

        // signout disapperaring

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

        User user = new User(uId, uName, uEmail, uMajor, uGender, uBirthday, uAbout,"");
        mDatabaseUsers.setValue(user);
        Toast.makeText(this, "Changes saved", Toast.LENGTH_LONG).show();
    }

    //onClick event for image choosing and uploading
    private void imageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an image"), IMAGE_REQUEST);
    }

    private void uploadImage() {

        if (filePath != null){
            StorageReference riversRef = imageStorage.child("images/profile.jpg");
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                           progressDialog.dismiss();
                           Toast.makeText(getApplicationContext(), "Photo uploaded", Toast.LENGTH_LONG).show();

                           String imgUrl = taskSnapshot.getDownloadUrl().toString();


                           mDatabaseUsers.child("photoUrl").setValue(imgUrl);


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage(progress + "% uploaded");
                }
            });
        } else {
            //Error toast
            Toast.makeText(getApplicationContext(), "File path is empty", Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public void onClick(View view) {
        if (view == imageViewedit) {
            setEdit();
        }

        if (view == imageViewsubmit) {
            save();
        }

        if (view == imageViewphoto){
            //open image chooser
            imageChooser();
            uploadImage();
        }

        if (view == imageViewJoinGroup) {

            startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
        }

        if (view == imageViewGroupList) {
            //startActivity(new Intent(getApplicationContext(), .class));

        }

        if (view == imageViewRestaurant) {
            //look up restaurant list
        }

        if (view == imageViewProfile) {
            //return
        }

        if(view== signOut)
        {
            firebaseAuth.signOut();
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        public void onComplete(@NonNull Task<Void> task) {
//                            // user is now signed out
//                            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
//                            finish();
//                        }
//                    });
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && requestCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageViewphoto.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
