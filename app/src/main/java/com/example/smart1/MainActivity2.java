package com.example.smart1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.text.TextUtils;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class MainActivity2 extends AppCompatActivity
{
    private EditText emailEditText ,passwordEditText, confirmpasswordEditText ;
    private Button signinButton ;
    private TextView signinTextView;









        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main2);

            emailEditText = findViewById(R.id.email);
            passwordEditText = findViewById(R.id.password);
            confirmpasswordEditText= findViewById(R.id.confirmpassword);
            signinButton= findViewById(R.id.signin);

            signinButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validateInput();
                }
            });
        }

        private void validateInput() {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmpasswordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!isValidPassword(password)) {
                Toast.makeText(this, "Password must be at least 8 characters long and include letters, numbers, and a symbol", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(this, "Please confirm your password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Sign in successful", Toast.LENGTH_SHORT).show();
            // Proceed with sign-in logic here
            Intent intent = new Intent(MainActivity2.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    private boolean isValidPassword(String password) {
        // Criteria for a strong password: At least 8 characters, contains uppercase and lowercase letters, digits, and special characters
        Pattern pattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");
        return pattern.matcher(password).matches();
    }
}







