package com.example.phili.foodpaldemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.phili.foodpaldemo.models.User;
import com.example.phili.foodpaldemo.models.UserGroup;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by yunfei on 2018-03-16.
 */

public class GroupHolder extends RecyclerView.ViewHolder {

    public static final String GROUP_ID = "groupID";
    public static final String GROUP_CONTAIN_USER= "IF_CONTAIN_USER";

    // define widgets
    private TextView createrName;
    private CircleImageView userImage;
    private TextView groupName;
    private ImageView restauImage;;
    private TextView totalMembers;
    private DatabaseReference mDatabaseUser;
    private Activity context;
    private FirebaseAuth mAuth;
    private String groupID;

    public GroupHolder(View itemView, Activity context) {
        super(itemView);
        this.context = context;

        createrName = itemView.findViewById(R.id.getCreaterName);
        userImage = itemView.findViewById(R.id.user_image);
        groupName = itemView.findViewById(R.id.getGroupName);
        restauImage = itemView.findViewById(R.id.res_image);
        totalMembers = itemView.findViewById(R.id.total_members);

        // set click listener
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(groupID != null){
                    checkIfGroupContainUser(groupID);
                }
            }
        });

    }

    private void checkIfGroupContainUser(final String currentGroupID) {
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        final String userId = currentUser.getUid();

        FirebaseDatabase.getInstance().getReference("groups")
                .child(currentGroupID).child("currentMembers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // if contains the value
                Log.i("test", "second ");

                if (dataSnapshot.child(userId).exists()) {
                    // contain the user
                    Log.i("test", "group contains the user");

                    // start intent
                    Intent intent = new Intent(context, DisplayGroupInfoActivity.class);
                    // put id to intent
                    intent.putExtra(GROUP_ID, currentGroupID);
                    intent.putExtra(GROUP_CONTAIN_USER, true);
                    context.startActivity(intent);

                } else {
                    // start intent
                    Intent intent = new Intent(context, DisplayGroupInfoActivity.class);
                    // put id to intent
                    intent.putExtra(GROUP_ID, currentGroupID);
                    intent.putExtra(GROUP_CONTAIN_USER, false);
                    context.startActivity(intent);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }


    public void bind(UserGroup userGroup){
        // group name
        groupName.setText(userGroup.getGroupName());
        createrName.setText("whatakjk");
        //
        String groupCreaterID =  userGroup.getGroupCreatorID();
        if(groupCreaterID != null){
            setCreaterPhotoAndName(groupCreaterID);

        }
        // total member
        int members = userGroup.getCurrentMembers().size();
        totalMembers.setText(members + "");

        // get the picture
        String resID = userGroup.getRestaurantID();
        setRestaurantPhoto(resID, restauImage);

    }




    private void setCreaterPhotoAndName(String groupCreaterID) {
        mDatabaseUser = FirebaseDatabase.getInstance().getReference("users");

            mDatabaseUser.child(groupCreaterID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User currentUser = dataSnapshot.getValue(User.class);

                    // get user name, and set user name
                    String username = currentUser.getUserName();
                    createrName.setText(username);

                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.placeholder(R.drawable.photo2);

                    // get picture
                    Glide.with(context)
                            .setDefaultRequestOptions(requestOptions)
                            .load(currentUser.getPhotoUrl())
                            .into(userImage);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


    }

    private void setRestaurantPhoto(String resID, final ImageView resImage) {

        final GeoDataClient mGeoDataClient ;
        mGeoDataClient = Places.getGeoDataClient(context, null);

        String placeId = resID;



        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(placeId);

        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {

            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                // Get the list of photos.
                PlacePhotoMetadataResponse photos = task.getResult();
                // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
                // Get the first photo in the list.
                PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(0);
                // Get the attribution text.
                CharSequence attribution = photoMetadata.getAttributions();
                // Get a full-size bitmap for the photo.
                Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
                photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                        PlacePhotoResponse photo = task.getResult();
                        Bitmap bitmap = photo.getBitmap();

                        // set up map
                        // resImage.getMaxWidth()

                        int oldWidth = bitmap.getWidth();
                        int oldHeight = bitmap.getHeight();
                        double ratio = oldHeight*1.0/(oldWidth*1.0);

                        int newWidth = 800;
//                                = resImage.getWidth();
//                        if (newWidth <= 0) {
//                            newWidth = 400;
//                        }
                        int newHeight = (int)(newWidth * ratio);


                        Bitmap resized = Bitmap.createScaledBitmap(bitmap,
                                newWidth, newHeight, true );

                        resImage.setImageBitmap(resized);

                    }
                });
            }
        });

        photoMetadataResponse.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resImage.setImageResource(R.drawable.photo2);
            }
        });



    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }
}
