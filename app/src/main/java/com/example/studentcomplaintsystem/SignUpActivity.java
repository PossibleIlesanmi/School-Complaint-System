package com.example.studentcomplaintsystem;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignUp;
    private TextView GotoText;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        GotoText = findViewById(R.id.Goto);

        GotoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add code to navigate to the login screen using an Intent.
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class); // Replace LoginActivity with your actual login activity.
                startActivity(intent);
            }
        });

        auth = FirebaseAuth.getInstance();

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    showSnackbar("Please fill in all fields");
                    return;
                }

                // Check if the password meets the complexity requirements
                if (!isPasswordValid(password)) {
                    showSnackbar("Password must be at least 6 characters long and contain at least one capital letter and one symbol.");
                    return;
                }

                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Registration successful, navigate to the home screen.
                                    Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish(); // Close the signup activity.
                                } else {
                                    // Signup failed, show the error message
                                    TextView errorMessage = findViewById(R.id.errorMessage);
                                    errorMessage.setVisibility(View.VISIBLE);
                                    errorMessage.setText("Incorrect email or password");
                                    errorMessage.setTextColor(Color.RED);
                                }
                            }
                        });

            }
        });
    }

    // Custom password validation function
    private boolean isPasswordValid(String password) {
        // Check if the password is at least 6 characters long
        return password.length() >= 6;
    }

    // Helper method to show a Snackbar with a message
    private void showSnackbar(String message) {
        View parentLayout = findViewById(android.R.id.content); // Get the parent layout
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG).show();
    }
}
