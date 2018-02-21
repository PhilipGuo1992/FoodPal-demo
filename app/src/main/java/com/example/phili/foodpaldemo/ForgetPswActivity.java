package com.example.phili.foodpaldemo;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgetPswActivity extends AppCompatActivity {


    private EditText input_email;
    private Button reset_pwd;
    private TextView back_login;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_psw);

        reset_pwd = findViewById(R.id.reset_psw_btn);
        back_login = findViewById(R.id.back_to_login);
        input_email = findViewById(R.id.reset_psw_email);
        firebaseAuth = FirebaseAuth.getInstance();


        reset_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if user entered email or not
                final String email_address = input_email.getText().toString().trim();

                if(TextUtils.isEmpty(email_address)){
                    Toast.makeText(getApplicationContext(), "Please enter your email!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // reset password by sending email.
               firebaseAuth.sendPasswordResetEmail(email_address)
                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               if(task.isSuccessful()){
                                   Toast.makeText(ForgetPswActivity.this, "Check your email to reset password.",
                                           Toast.LENGTH_SHORT).show();
                               } else {
                                   Toast.makeText(ForgetPswActivity.this, "Fail to send the " +
                                                   "email. Pleaes make sure the email address is correct",
                                           Toast.LENGTH_SHORT).show();
                               }
                           }
                       });

            }
        });

        back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go back the login page.
                startActivity(new Intent(ForgetPswActivity.this, LoginActivity.class));

            }
        });

    }



}
