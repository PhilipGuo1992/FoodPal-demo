package com.example.phili.foodpaldemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnRegister;
    private EditText editTextEmail;
    private EditText editTextPasswword;
    private TextView textViewLogin;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = (Button) findViewById(R.id.btn_register);
        editTextEmail = (EditText) findViewById(R.id.input_email);
        editTextPasswword = (EditText) findViewById(R.id.input_password);
        textViewLogin = (TextView) findViewById(R.id.textViewLogin);

        


    }

    @Override
    public void onClick(View view) {

    }
}
