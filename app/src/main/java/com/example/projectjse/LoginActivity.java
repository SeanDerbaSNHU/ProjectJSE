package com.example.projectjse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    // Temp variable, delete later:
    //FirebaseConnection newConnection;

    // Firebase Variables
    private FirebaseAuth mAuth;
    // Firebase Variables End

    private Button register;
    private EditText editUsername;      // email field
    private EditText editPassword;      // password field
    private Button signIn;          // Sign-in button
    private Button createAccount;   // Create-Account button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Firebase Start
        register = (Button) findViewById(R.id.CreateAccountButton);
        register.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

        // Firebase End

        // Init variables
        editUsername = findViewById(R.id.UsernameText);
        editPassword = findViewById(R.id.PasswordText);
        signIn = findViewById(R.id.SignInButton);
        signIn.setOnClickListener(this);

        // temp init
        //newConnection = new FirebaseConnection();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // Create Account button event
            case R.id.CreateAccountButton:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            // Sign in button event
            case R.id.SignInButton:
                userLogin();
                break;
        }
    }

    private void userLogin() {

        // Two inputs, email and password
        String email = editUsername.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        // Case Checking -->


        if(email.isEmpty()) {
            editUsername.setError("Email is required");
            editUsername.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editUsername.setError("Email is required!");
            editUsername.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            editPassword.setError("Password is required!");
            editPassword.requestFocus();
            return;
        }

        if(password.length() < 6) {
            editPassword.setError("Password must be 6 characters long!");
            editPassword.requestFocus();
            return;
        }

        // <-- End Case Checking

        // Login function that connects to Firebase database
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) { // Did we find a matching account?
                    // Successful login

                    startActivity(new Intent(LoginActivity.this, MainFeedActivity.class)); // Run main feed

                }
                else{ // Unsuccessful login
                    Toast.makeText(LoginActivity.this, "Failed to login", Toast.LENGTH_LONG).show(); // Error, invalid login.
                }
            }
        });
    }
}