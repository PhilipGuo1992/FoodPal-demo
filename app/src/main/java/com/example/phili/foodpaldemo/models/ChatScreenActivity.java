package com.example.phili.foodpaldemo.models;

import android.app.ListActivity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phili.foodpaldemo.DisplayGroupInfoActivity;
import com.example.phili.foodpaldemo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatScreenActivity extends ListActivity {


    String groupName;
    String groupId;
    String message;
    String userEmail;
    private TextView user;

    private DatabaseReference chatDatabaseReference;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    ArrayList<String> messageList = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    EditText chatbox;
    Button send_messsage;

    int chatMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_chat_screen);

        Intent i = getIntent();
        groupName = i.getStringExtra("GROUPNAME");
        groupId = i.getStringExtra("GROUPID");
        userEmail = i.getStringExtra("EMAIL");



        chatbox = (EditText)findViewById(R.id.chatbox);
        send_messsage = (Button)findViewById(R.id.send_messsage);


        //https://stackoverflow.com/questions/4540754/dynamically-add-elements-to-a-listview-android
       adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
               messageList);
        setListAdapter(adapter);

      //retrieve older messages
        chatDatabaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference gRef = chatDatabaseReference.child("groups").child(groupId).child("Chat");
        gRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                // if new message is added, then add it to the ListView (chat screen)

                Log.i("child",dataSnapshot.toString());
                HashMap map =(HashMap) dataSnapshot.getValue();
                if(map!=null)
                    messageList.add(map.get("Message").toString());

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        send_messsage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add the message to the database
                message = chatbox.getText().toString();
                chatbox.setText("");

                chatDatabaseReference = FirebaseDatabase.getInstance().getReference().child("groups").child(groupId).child("Chat").push();
                final FirebaseUser mUser = firebaseAuth.getCurrentUser();
                chatDatabaseReference.child("Message").setValue(userEmail + " :\n" + message);
                chatDatabaseReference.child("uid").setValue(userEmail);
            }
        });

    }
}
