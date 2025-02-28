package com.example.smart1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
private EditText emailEditText ,passwordEditText ;
private Button loginButton ;
private TextView signupTextview ;

FirebaseDatabase firebaseDatabase;
DatabaseReference firebaseReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //find views by id
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
       signupTextview = findViewById(R.id.signup);
       loginButton = findViewById(R.id.login);
       firebaseDatabase=firebaseDatabase.getInstance();
       firebaseReference= firebaseDatabase.getReference();


        //handle log in button

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email= emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                //login logic
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEditText.setError("Enter a valid email");
                    emailEditText.requestFocus();
                    return;
                }
                if (email.isEmpty()) {
                    emailEditText.setError("Email is required");
                    emailEditText.requestFocus();

                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    passwordEditText.setError("password is required");
                    passwordEditText.requestFocus();
                    return;
                }
                if (!isValidPassword(password)) {
                    passwordEditText.setError("Password must be at least 8 characters long and include letters, numbers, and a symbol");
                    passwordEditText.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    passwordEditText.setError("Password is required");
                    return;
                }



                if (email.isEmpty()||password.isEmpty()){
                    Toast.makeText(MainActivity.this,"please fill all fields",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this,"log in successful",Toast.LENGTH_SHORT).show();
                    //Proceed with log-in logic here
                    Intent intent = new Intent(MainActivity.this, HomePage.class);
                    startActivity(intent);
                    finish();


                }

            }
        });
        signupTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MainActivity2.class);
                startActivity(intent);
            }
        });


    }
    private boolean isValidPassword(String password) {
        // Criteria for a strong password: At least 8 characters, contains uppercase and lowercase letters, digits, and special characters
        Pattern pattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");
        return pattern.matcher(password).matches();
    }
}