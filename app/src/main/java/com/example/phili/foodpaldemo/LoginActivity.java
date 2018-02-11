package com.example.phili.foodpaldemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

//import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{


    private Button btnLogin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView createAccount;
    private TextView forgetPassword;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        createAccount.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
    }

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

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
                    finish();
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

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

        if (view == btnLogin) {
            userLogin();
        }



    }



}
