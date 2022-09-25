package com.example.budgetingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class registrationActivity extends AppCompatActivity {
    private EditText email, password;
    private TextView loginTextview;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginTextview = findViewById(R.id.loginTextview);
        registerButton = findViewById(R.id.registerButton);

        loginTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(registrationActivity.this,loginActivity.class);
                startActivity(intent);
            }
        });
    }
}