package com.example.phili.foodpaldemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ForgetPswActivity extends AppCompatActivity {

    private Button reset_pwd;
    private TextView back_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_psw);

        reset_pwd = findViewById(R.id.reset_psw_btn);
        back_login = findViewById(R.id.back_to_login);

        reset_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // reset password by sending email.

            }
        });

        back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go back the login page.

            }
        });

    }



}
