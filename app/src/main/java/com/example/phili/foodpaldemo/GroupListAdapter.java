package com.example.phili.foodpaldemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.phili.foodpaldemo.Fragment.SettingsFragment;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by phili on 2018-02-21.
 */

public class GroupListAdapter extends ArrayAdapter<UserGroup> {

    private Activity context;
    // list to store the groups
    private List<UserGroup> userGroups;
    private CardView cardView;
    public static final String USER_ID = "userID";

    private CircleImageView userImage;

    private DatabaseReference mDatabaseUser;
    private Set<User> currentUsers;

    public GroupListAdapter(Activity context, List<UserGroup> userGroups){
        super(context, R.layout.groups_list_layout, userGroups);

        this.context = context;
        this.userGroups = userGroups;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        // inflate the view
        View groupViewList = inflater.inflate(R.layout.groups_list_layout, null, true);

        // get widges from layout
        final ImageView resImage = groupViewList.findViewById(R.id.res_image);
        TextView groupName = groupViewList.findViewById(R.id.getGroupName);


        TextView groupTotalMember = groupViewList.findViewById(R.id.total_members);
        userImage = groupViewList.findViewById(R.id.user_image);
        cardView = groupViewList.findViewById(R.id.card_view);
        final TextView createrName = groupViewList.findViewById(R.id.getCreaterName);



        // only show description when user click the group

        // get current group
        UserGroup userGroup = userGroups.get(position);


        // update the UI
        groupName.setText(userGroup.getGroupName());


        // only show the total group numbers, after user click the group: show currentMembers' name.

        // need to fix dabase first: some group has no members.

        int members = userGroup.getCurrentMembers().size();
        Log.i("test", members+"");
        // need to convert int to string first
        groupTotalMember.setText(members + "");

        // get the picture
        String resID = userGroup.getRestaurantID();
        setRestaurantPhoto(resID, resImage);


//        // show the creater name

        final String groupCreaterID =  userGroup.getGroupCreatorID();
        Log.i("test", userGroup.getGroupID()+" , the creater ID");
            // it does not have creater yet.

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SettingsFragment.class);
                getContext().startActivity(intent);

                intent.putExtra(USER_ID, groupCreaterID);

            }
        });

//        // read firebaase to get the creater's name
        if (groupCreaterID != null) {
            mDatabaseUser = FirebaseDatabase.getInstance().getReference("users");
            Log.i("test", " set test here");



            try {
                mDatabaseUser.child(groupCreaterID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User currentUser = dataSnapshot.getValue(User.class);

                        // get user name, and set user name
                        String username = currentUser.getUserName();
                        createrName.setText(username);

                        // get picture
                        Glide.with(context)
                                // .setDefaultRequestOptions(requestOptions)
                                .load(currentUser.getPhotoUrl())
                                .into(userImage);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } catch (Exception e){
                Log.i("test", e + "");

            }

        }
        // load picture








        return groupViewList;
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

                        int newWidth = resImage.getWidth();
                        int newHeight = (int)(newWidth * ratio);

                        Bitmap resized = Bitmap.createScaledBitmap(bitmap,
                                newWidth, newHeight, true );

                        resImage.setImageBitmap(resized);

                    }
                });
            }
        });



    }
}
