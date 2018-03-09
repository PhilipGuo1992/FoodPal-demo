package com.example.phili.foodpaldemo;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.phili.foodpaldemo.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends android.support.v4.app.Fragment implements View.OnClickListener{

    //declare all view components
    private ImageView imageViewedit, imageViewphoto, imageViewsubmit;
    private EditText username, major, email,
            gender, birthday, about;

    //store the current user ID
    private String uId;

    //used to store and upload image
    private static final int IMAGE_REQUEST = 100;
    private Uri filePath;

    //firebase settings
    private FirebaseAuth firebaseAuth;
    private DatabaseReference groupReference;
    private DatabaseReference userReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;

    //empty constructor for fragment
    public SettingsFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //deploy firebase connection and user
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View settingView = inflater.inflate(R.layout.fragment_settings, container, false);

        //controller -- view
        imageViewedit = settingView.findViewById(R.id.editprofile);
        imageViewphoto = settingView.findViewById(R.id.editphoto);
        imageViewsubmit = settingView.findViewById(R.id.editsubmit);
        username = settingView.findViewById(R.id.profileName);
        email = settingView.findViewById(R.id.email);
        gender = settingView.findViewById(R.id.gender);
        birthday = settingView.findViewById(R.id.birthday);
        major = settingView.findViewById(R.id.major);
        about = settingView.findViewById(R.id.selfdes);

        //bind onClick event to those image views
        imageViewedit.setOnClickListener(this);
        imageViewsubmit.setOnClickListener(this);
        imageViewphoto.setOnClickListener(this);

        updateFragmentView();

        return settingView;
    }

    private void updateFragmentView() {

        //get current userID
        uId = firebaseUser.getUid();
        //firebase instance
        firebaseDatabase = FirebaseDatabase.getInstance();
        //user DatabaseReference
        userReference = firebaseDatabase.getReference("users").child(uId);

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //get user class
                User currentUser = dataSnapshot.getValue(User.class);
                //read data from database
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

    @Override
    public void onClick(View view) {

        if (view == imageViewedit) {
            setEditable();
        }

        if (view == imageViewsubmit) {
            saveChange();
        }

        if (view == imageViewphoto) {
            imageChooser();
        }

    }

    //open image chooser
    private void imageChooser() {
    }

    //save current changes to user information
    private void saveChange() {
    }

    //set edit view to focusable
    private void setEditable() {
    }
}
