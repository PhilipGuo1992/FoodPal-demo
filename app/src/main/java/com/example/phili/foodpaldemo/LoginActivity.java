package com.example.phili.foodpaldemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    // user register new account
    private TextView createAccount;
    private Button btnLogin;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        createAccount = (TextView) findViewById(R.id.app_create);
        btnLogin = (Button) findViewById(R.id.btn_login);

        createAccount.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == createAccount) {
            startActivity(new Intent(this, RegisterActivity.class));
        }
    }
    //FirebaseDatabase database = FirebaseDatabase.getInstance();

}
