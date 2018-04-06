package com.example.phili.foodpaldemo.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.phili.foodpaldemo.LoginActivity;
import com.example.phili.foodpaldemo.R;
import com.example.phili.foodpaldemo.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends android.support.v4.app.Fragment implements View.OnClickListener{

    public static boolean SETTING = false;
    //declare all view components
    private ImageView imageViewedit, imageViewsubmit;
    private CircleImageView circleImageViewPhoto;
    private EditText username, major, email,
            gender, birthday, about;
    private Button buttonSignOut;

    private TimePickerView timePickerView;

    //store the current user ID
    private String uId;

    //used to store and upload image
    private static final int IMAGE_REQUEST = 100;
    private Uri imageUri;

    //firebase settings
    private FirebaseAuth firebaseAuth;
    private DatabaseReference groupReference;
    private DatabaseReference userReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private StorageReference imageReference;
    private FirebaseFirestore imageStore;


    //empty constructor for fragment
    public SettingsFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initFirebase();
        initImageStore();
        initTimePickView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View settingView = inflater.inflate(R.layout.fragment_settings, container, false);

        //controller -- view
        imageViewedit = settingView.findViewById(R.id.editprofile);
        circleImageViewPhoto = (CircleImageView) settingView.findViewById(R.id.circleImage);
        imageViewsubmit = settingView.findViewById(R.id.editsubmit);
        username = settingView.findViewById(R.id.profileName);
        email = settingView.findViewById(R.id.email);
        gender = settingView.findViewById(R.id.gender);
        birthday = settingView.findViewById(R.id.birthday);
        major = settingView.findViewById(R.id.major);
        about = settingView.findViewById(R.id.selfdes);
        buttonSignOut = settingView.findViewById(R.id.btn_signout);


        //bind onClick event to those image views
        imageViewedit.setOnClickListener(this);
        imageViewsubmit.setOnClickListener(this);
        circleImageViewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setAspectRatio(1,1)
                        .start(getContext(), SettingsFragment.this);
            }
        });

        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerView.setDate(Calendar.getInstance());
                timePickerView.show(view);
               // timePickerView.setDate();
                setVisibility();
            }
        });

        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                getActivity().finish();
                startActivity(new Intent(getContext(), LoginActivity.class));
                Toast.makeText(getContext(),"Signed out successfully!",Toast.LENGTH_LONG).show();
            }
        });



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

                RequestOptions requestOptions = new RequestOptions();
                requestOptions.placeholder(R.drawable.photo2);

                //[4] “bumptech/glide,” GitHub. [Online]. Available: https://github.com/bumptech/glide.
                //if (currentUser.getPhotoUrl())
                Glide.with(container.getContext())
                        .setDefaultRequestOptions(requestOptions)
                        .load(currentUser.getPhotoUrl())
                        .into(circleImageViewPhoto);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return settingView;
    }

    private void updateFragmentView() {

    }

    @Override
    public void onClick(View view) {

        if (view == imageViewedit) {
            setEditable();
        }

        if (view == imageViewsubmit) {
            saveChange();
        }



    }

    //save current changes to user information
    private void saveChange() {

        Toast toast;
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

        Map<String, Object> users = new HashMap<>();
        users.put("userName", uName);
        users.put("userGender", uGender);
        users.put("userBirthday", uBirthday);
        users.put("userMajor", uMajor);
        users.put("selfDescription", uAbout);

        userReference.updateChildren(users);

        toast = Toast.makeText(getContext(), "Changes submitted", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }

    //set edit view to focusable
    private void setEditable() {

        //Set Visibility of 2 image buttons
        setVisibility();

        //cannot set focus so that it can be edited.
        username.setFocusable(true);
        username.setFocusableInTouchMode(true);
        username.requestFocus();

        gender.setFocusable(true);
        gender.setFocusableInTouchMode(true);

//        birthday.setFocusable(true);
//        birthday.setFocusableInTouchMode(true);

        major.setFocusable(true);
        major.setFocusableInTouchMode(true);

        about.setFocusable(true);
        about.setFocusableInTouchMode(true);
    }

    //CropImage.activity on Activity Result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //retrieved from https://github.com/ArthurHub/Android-Image-Cropper
        //Image-Cropper [3]“ArthurHub/Android-Image-Cropper,” GitHub. [Online].
        // Available: https://github.com/ArthurHub/Android-Image-Cropper.
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE ) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                StorageReference userProfile = imageReference.child(uId + ".jpg");
                //put uri into users
                userProfile.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            String downloadUrl = task.getResult().getDownloadUrl().toString();
                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("photoUrl", downloadUrl);
                            userReference.updateChildren(userMap);
                            SETTING = true;

                            imageStore.collection("users").document(uId).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getActivity(), "Success!", Toast.LENGTH_LONG).show();
                                   // goToFragment();
                                }
                            });
                        } else {
                            Toast.makeText(getActivity(),"Error!",Toast.LENGTH_LONG).show();
                        }
                    }
                });

                circleImageViewPhoto.setImageURI(imageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


    }

    // use getActivity() method from fragment for Intents.
    public void goToFragment() {

        Intent intent = new Intent(getActivity(), SettingsFragment.class);
        startActivity(intent);
        getActivity().finishActivity(CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    public void initFirebase() {

        //deploy firebase connection and user
        firebaseAuth = FirebaseAuth.getInstance();
        //get current user
        firebaseUser = firebaseAuth.getCurrentUser();
        //get current userID
        uId = firebaseUser.getUid();
        //firebase instance
        firebaseDatabase = FirebaseDatabase.getInstance();
        //user DatabaseReference
        userReference = firebaseDatabase.getReference("users").child(uId);
    }

    public void initImageStore() {

        //set up image reference
        imageReference = FirebaseStorage.getInstance().getReference().child("images");
        //use firebase firestore
        imageStore = FirebaseFirestore.getInstance();
        //init image uri
        imageUri = null;
    }

    public void initTimePickView() {

        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        Calendar currentDate = Calendar.getInstance();

        //[7] Bigkoo, “Bigkoo/Android-PickerView,” GitHub. [Online].Available: https://github.com/Bigkoo/Android-PickerView.
        timePickerView = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                EditText editText = (EditText) v;
                editText.setText(getTime(date));
            }
        })
                //year/month/day/hour/minute/second
                .setType(new boolean[]{true,true,true,false,false,false})
                .setLabel("","","","","","")
                .isCenterLabel(false)
                //IOS-liked color
                .setDividerColor(Color.DKGRAY)
                .setContentSize(18)
                .setDate(selectedDate)
                .setDecorView(null)
                .build();
    }

    private String getTime(Date date) {

        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        return sdFormat.format(date);
    }

    private void setVisibility() {
        //Set Visibility of 2 image buttons
        imageViewedit.setVisibility(View.INVISIBLE);
        imageViewsubmit.setVisibility(View.VISIBLE);

    }

}
