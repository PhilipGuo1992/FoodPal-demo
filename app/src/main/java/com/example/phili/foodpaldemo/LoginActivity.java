package com.example.phili.foodpaldemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.onesignal.OneSignal;

//import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by yiren on 2018-02-10.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private Button btnLogin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView createAccount;
    private TextView forgetPassword;
    private ImageView imageViewUsernameClear;
    private ImageView imageViewPasswordClear;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    FirebaseUser user;
    static String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_login);

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        firebaseAuth = FirebaseAuth.getInstance();

//        if (firebaseAuth.getCurrentUser() != null) {
//            finish();
//            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
//        }

        createAccount = (TextView) findViewById(R.id.app_create);
        btnLogin = (Button) findViewById(R.id.btn_login);
        editTextEmail = (EditText) findViewById(R.id.et_username);
        editTextPassword = (EditText) findViewById(R.id.et_password);
        forgetPassword = (TextView) findViewById(R.id.app_forget);
        imageViewUsernameClear = (ImageView) findViewById(R.id.icon_username_clear);
        imageViewPasswordClear = (ImageView) findViewById(R.id.icon_password_clear);

        createAccount.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
        imageViewUsernameClear.setOnClickListener(this);
        imageViewPasswordClear.setOnClickListener(this);

        editTextEmail.getOnFocusChangeListener();
        editTextPassword.getOnFocusChangeListener();

        progressDialog = new ProgressDialog(this);
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

//        if (editTextEmail.getOnFocusChangeListener().onFocusChange();) {
//            imageViewUsernameClear.setVisibility(View.VISIBLE);}
//
//        if (!password.isEmpty()) {
//            imageViewPasswordClear.setVisibility(View.VISIBLE);
//
//        }

        //Log.i(TAG, "UserRegister: here ");
        if(TextUtils.isEmpty(email)) {
            //If email is empty
            Toast.makeText(this, "Please enter your Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)) {
            //If password is empty
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Redirecting...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {

                    String deviceToken = FirebaseInstanceId.getInstance().getToken();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
                    String current_user = firebaseAuth.getCurrentUser().getUid();
                    databaseReference.child(current_user).child("device_token").setValue(deviceToken);

                    //startActivity(new Intent(getApplicationContext(), CreateGroupActivity.class));
                    //Keep this line
                    //startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    //Test for creating a group
                    userid = current_user;
                    OneSignal.sendTag("user_id", userid);


                  startActivity(new Intent(getApplicationContext(), MainHomeActivity.class));
                    finish();
                    // go to home page
                    //@
                    // test create group

                } else {
                    Toast.makeText(LoginActivity.this, "Your username or password is incorrect!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    public void onClick(View view) {
        if (view == createAccount) {
            startActivity(new Intent(this, RegisterActivity.class));
        }

        if (view == forgetPassword){
            startActivity(new Intent(this, ForgetPswActivity.class));
        }

        if (view == btnLogin) {
            userLogin();
        }
        if (view == imageViewUsernameClear) {
            editTextEmail.getText().clear();
        }

        if (view == imageViewPasswordClear) {
            editTextPassword.getText().clear();
        }


    }



}
