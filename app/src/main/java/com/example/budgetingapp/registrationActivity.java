package com.example.budgetingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class registrationActivity extends AppCompatActivity {
    private EditText email, password;
    private TextView loginTextview;
    private Button registerButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();

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
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailString = email.getText().toString().trim();
                String passwordString = password.getText().toString().trim();

                if(emailString.isEmpty()){
                    email.setError("Please input email");
                    email.requestFocus();

                }
                if(passwordString.isEmpty()){
                    password.setError("please input password");
                    password.requestFocus();

                }else{
                    mAuth.createUserWithEmailAndPassword(emailString,passwordString).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Toast.makeText(registrationActivity.this,"success registration",Toast.LENGTH_LONG);
                            Intent intent = new Intent(registrationActivity.this,loginActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(registrationActivity.this, task.getException().toString(),Toast.LENGTH_LONG).show();

                        }
                    });
                }


            }
        });
    }
}