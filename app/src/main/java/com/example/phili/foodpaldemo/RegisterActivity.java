package com.example.phili.foodpaldemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phili.foodpaldemo.Fragment.SettingsFragment;
import com.example.phili.foodpaldemo.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by yiren on 2018-02-10.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewLogin;
    private ProgressDialog progressDialog;
    //private String TAG="feature";
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        //databaseReference = firebaseDatabase.getReference().child()

        btnRegister = (Button) findViewById(R.id.btn_register);
        editTextEmail = (EditText) findViewById(R.id.input_email);
        editTextPassword = (EditText) findViewById(R.id.input_password);
        textViewLogin = (TextView) findViewById(R.id.textViewLogin);
        progressDialog = new ProgressDialog(this);

        btnRegister.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);


    }

    public void UserRegister() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        //Log.i(TAG, "UserRegister: here ");
        if (TextUtils.isEmpty(email)) {
            //If email is empty
            Toast.makeText(this, "Please enter your Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            //If password is empty
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Redirecting...");
        progressDialog.show();


        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {

                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            String uId = firebaseUser.getUid();
                            String uEmail = firebaseUser.getEmail();
                            String deviceToken = FirebaseInstanceId.getInstance().getToken();
                            databaseReference = firebaseDatabase.getReference("users").child(uId);
                            User user = new User(uId,deviceToken,"",uEmail,"","","","","");
                            databaseReference.setValue(user);



                            Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), SettingsFragment.class));
                        } else {
                            Toast.makeText(RegisterActivity.this, "Could not register", Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }

    @Override
    public void onClick(View view) {
        if (view == btnRegister) {
            UserRegister();
        }
        if (view == textViewLogin) {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
