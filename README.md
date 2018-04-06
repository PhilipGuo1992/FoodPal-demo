# FoodPal
We intend to create a mobile application called FoodPal that is an easy-to-use social dining application for students at Dalhousie. Since the target users for this application are students at Dalhousie, they need to have a Dalhousie email address to register for an account. Users can use this application to do several things: create a dine out group that other users can join, join an existing group that was created by other users, check a list of restaurants near Dalhousie University and in Halifax, and modify their profiles.

## Libraries

**FirebaseUI for Android:** FirebaseUI database is used to bind data from the Firebase realtime database. It allows us to use a recyclerView much easier. Source [here](https://github.com/firebase/FirebaseUI-Android/blob/master/database/README.md)

**OneSignal:** OneSignal is a multi-platform push notification service. It can help developers to store tags of users who are currently logging in. And push notification to the specific user by filter the tag when some action is triggered. Source [here](https://github.com/OneSignal/OneSignal-Android-SDK)

**okHttp:** okhttp can silently recover from some common connection problems. Source [here](https://github.com/square/okhttp)

**Volley:** Volley is an HTTP library for transmitting network data on Android platform. In this project, it is used to send HTTP request to the REST API provided by Zomato (an Indian restaurant search and discovery service), and to retrieve information about various restaurants. Source [here](https://github.com/google/volley)

**Glide:** Glide is a fast and efficient open source media management and image loading framework for Android that wraps media decoding, memory and disk caching, and resource pooling into a simple and easy to use interface. Source [here](https://github.com/bumptech/glide)

**Android Image Cropper:** Powerful (Zoom, Rotation, Multi-Source), customizable (Shape, Limits, Style), optimized (Async, Sampling, Matrix) and simple image cropping library for Android. Source [here](https://github.com/ArthurHub/Android-Image-Cropper)

**Firebase-Database:** Firebase gives functionality like analytics, databases, messaging and crash reporting, etc. Source [here](https://firebase.google.com/docs/reference/android/com/google/firebase/database/package-summary)

**Firebase-Storage:** Store and retrieve large files like images, audio, and video without writing server-side code. Source [here](https://firebase.google.com/docs/reference/android/com/google/firebase/storage/package-summary)

**Android-PickerView:** This is an iOS-like PickerView control with time selectors and option selectors. Source [here](https://github.com/Bigkoo/Android-PickerView)

**Firebase-Auth:** Sign in and manage users with ease, accepting emails, Google Sign-In, Facebook and other login providers. Source [here](https://firebase.google.com/docs/reference/android/com/google/firebase/auth/package-summary)

**Firebase-Messaging:** Deliver and receive messages and notifications reliably across cloud and device. Source [here](https://firebase.google.com/docs/reference/android/com/google/firebase/messaging/package-summary)

**Firebase-Firestore:** Store and sync your app data at global scale. Source [here](https://firebase.google.com/docs/reference/android/com/google/firebase/firestore/package-summary)

**Picasso:** A powerful image downloading and caching library for Android. Picasso allows for hassle-free image loading in applications. Source [here](http://square.github.io/picasso/)

**Cardview:** A FrameLayout with a rounded corner background and shadow. Source [here](https://developer.android.com/training/material/lists-cards.html?hl=zh-cn)

## Installation Notes
The API level should be 23 or above and

## Code Examples

**Problem 1: In the beginning, the model design is a challenge.**

The many-to-many relationship need to be managed well in Firebase. For our project, we have users and groups. Each user can join many groups, and each group can have many users, which is a many-to-many relationship. We used Map to manage the relationship.  In Firebase, we stored the groups’ id in the user file, also we stored the user’s id to the group file.
```
//  the user model
public class User {
   private String userID;
   private String userName;
   private String userEmailAddress;
   // user major : like CS, math, ..
   private String userMajor;
   // groups: user belongs to
   private Map<String, Boolean> joinedGroups = new HashMap<>();

//  the usergroup model
public class UserGroup {
   // the group creator: user ID.
   private String groupCreatorID;
   // current members in the group
   private Map<String, Boolean> currentMembers;
   private String groupID;
   private String groupName;
   private String mealTime;
   private String description;
   private String restaurantID;
// default constructor **DO NOT REMOVE THE constructor**
   public UserGroup() {
}
// when user clicks join the group button:
// first: update the group member info
mDatabaseGroup.child("currentMembers").child(currentUserID).setValue(true);
// second: update the user's group info
mDatabaseUsers.child(currentUserID).child("joinedGroups").child(groupID).setValue(true);
```

**Problem 2: Chat functionality.**

We also have tried to implement chat functionality in our group where the users who are added to any specific groups can chat with other users in that group to decide the common place to eat or to meet. So for that we have map the unique groupName ,groupId and userEmail from firebase with this chat functions that i have created with the help of a adapter
```
// add the message to the database
send_messsage.setOnClickListener(new View.OnClickListener() {
   @Override
   public void onClick(View view) {
              message = chatbox.getText().toString();
       chatbox.setText("");

       chatDatabaseReference = FirebaseDatabase.getInstance().getReference().child("groups").child(groupId).child("Chat").push();
       final FirebaseUser mUser = firebaseAuth.getCurrentUser();
       chatDatabaseReference.child("Message").setValue(userEmail + " :\n" + message);
       chatDatabaseReference.child("uid").setValue(userEmail);
   }
});
```

**Problem 3: Cropping and saving images.**

We chose circle image view as the user’s profile photo which needs a circle image cropping tool. We introduced Image-Cropper to set up attributes and save the uri of the image to Firebase for retrieving.
```
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
   }
```

## Feature Section
The login and registration page is the first page that users see when they open the application. After they registered, they are on the page of Profile, where they can edit their personal information.  

On the list of groups page, users can view all the existing groups created by other users. The user can click each group, then it will show the detail of the group, and gives the option to join or leave the group. Also, on the list of the group page, there is a button on the right bottom corner to create a new group. When a user clicks this button, it requires the user to enter the group name, group description, choose a meal time and choose a restaurant, which uses the Google PlacePicker API.  

Users can also view all the groups he/she has joined by viewing my group page. Also, users can see the restaurants’ information on the restaurant page. Including location, type, price, and comments.  

On the setting page, users can see their profile and change them if they like. On the top of group list page and my group page, there is a search bar. If there is a huge amount of group on these two pages and the user can not find what they want to in an efficient way. They can use this searching function to help them. Also in these two pages, the card of every single group shows the basic information of the group, including the number of people in this group, the name of who created this group, the basic information of the restaurant. 
 
When users are in a group, there is a button on the group information page called to chat. By this, users can have a chat with the members of this group. 


## Final Project Status
We did achieve our goals, even through we have one expected functionality is partially completed. We finished all the minimum functionality, and three bonus functionality.

#### Minimum Functionality
- Feature 1 Camera (Completed)
- Feature 2 User Registration(Using Firebase) (Completed)
- Feature 3 Access to local storage  (Completed)
- Feature 4 User Profile (Completed)
- Feature 5 User can join or leave the group(Completed)
- Feature 6 Register with Dalhousie Mail(Completed)

#### Expected Functionality
- Feature 1 Notification (Completed)
- Feature 2 List of Restaurants (Completed)

#### Bonus Functionality
- Feature 1 Chat Functionality(Completed)
- Feature 2 Group Search (Completed)
- Feature 3 Restaurant rating (not implemented)

## Sources
[1] "Java (programming language)", En.wikipedia.org, 2018. [Online]. Available: https://en.wikipedia.org/wiki/Java_(programming_language).  
[2]  Akshayejh, “TVAC Studio,” YouTube. [Online]. Available: https://www.youtube.com/user/akshayejh/videos. [Accessed: 06-Apr-2018].  
[3] “ArthurHub/Android-Image-Cropper,” GitHub. [Online]. Available: https://github.com/ArthurHub/Android-Image-Cropper. [Accessed: 06-Apr-2018].  
[4] “bumptech/glide,” GitHub. [Online]. Available: https://github.com/bumptech/glide. [Accessed: 06-Apr-2018].  
[5] “hdodenhof/CircleImageView,” GitHub. [Online]. Available: https://github.com/hdodenhof/CircleImageView. [Accessed: 06-Apr-2018].  
[6] “Picasso,” Square. [Online]. Available: http://square.github.io/picasso/. [Accessed: 06-Apr-2018].  
[7] Bigkoo, “Bigkoo/Android-PickerView,” GitHub. [Online]. Available: https://github.com/Bigkoo/Android-PickerView. [Accessed: 06-Apr-2018].  
[8] How can I switch between two fragments, w. (2018). How can I switch between two fragments, without recreating the fragments each time?. [online] Stackoverflow.com. Available at: https://stackoverflow.com/questions/22713128/how-can-i-switch-between-two-fragments-without-recreating-the-fragments-each-ti/22714222 [Accessed 6 Apr. 2018].  
[9] “Place Picker  |  Google Places API for Android  |  Google Developers,” Google. [Online]. Available: https://developers.google.com/places/android-api/placepicker.  
[10] The Google Android Place Picker Part 2 - The Java Coding. [Online]. Available: http://www.northborder-software.com/placepicker2.html.  
[11] E. Desjardins, CSCI 5708, Lab 6 Notes, Topic: “Currency Application”, Faculty of Computer Science, Dalhousie University, Halifax, Nova Scotia, Mar. 9, 2018.  
[12] georgiecasey, How to set custom header in Volley Request, Stack Overflow. Accessed on Mar. 18, 2018. [Online]. Available: https://stackoverflow.com/questions/17049473/how-to-set-custom-header-in-volley-request  
[13] “Modern Profile UI Design in Android Studio,” YouTube, 05-Oct-2017. [Online]. Available: https://www.youtube.com/watch?v=2pirZvqXza0&t=1153s.  