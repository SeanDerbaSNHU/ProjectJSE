package com.example.projectjse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    // XML Variables
    private TextView registerUser;
    private EditText editEmail, editPassword, editPasswordConfirm;
    private String userID;

    // Firebase Variables
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;


    // @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Connect to Firebase database
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        registerUser = (TextView) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        editEmail = (EditText) findViewById(R.id.emailBox);
        editPassword = (EditText) findViewById(R.id.password);
        editPasswordConfirm = (EditText) findViewById(R.id.editPasswordConfirm);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) { // Add a case to go back to MainActivity.class
            case R.id.registerUser:
                registerUser();
                break;
        }
    }

    // FUNC: Adds account to Firebase database
    private void registerUser() {

        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String passwordCnfm = editPasswordConfirm.getText().toString().trim();

        // Case Checking -->>


        if (email.isEmpty()) {
            editEmail.setError("Email is required");
            editEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editPassword.setError("Password is required");
            editPassword.requestFocus();
            return;
        }

        // Is the email a valid email address?
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Email must be valid");
            editEmail.requestFocus();
            return;
        }

        // Password must be 6 char for Firebase
        if(password.length() < 6) {
            editPassword.setError("Must be greater than 6 characters");
            editPassword.requestFocus();
            return;
        }

        if(!password.equals(passwordCnfm)) {
            // passwords do not match
            editPassword.setError("Passwords do not match");
            editPassword.requestFocus();
            return;
        }

        // <-- End Case Checking

        mAuth.createUserWithEmailAndPassword(email, password) // Create a new user in the Firebase database with Email and Password
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) { // Has user been registered successfully?
                        Toast.makeText(RegisterActivity.this, "Account successfully registered!", Toast.LENGTH_LONG).show();
                        userID = mAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = fStore.collection("users").document(userID);
                        Map<String, Object> user = new HashMap<>();
                        user.put("email", editEmail.getText().toString().trim());


                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() { // Adds the Document users on Firebase with user information
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("test", "onSuccess: User folder created");
                            }
                        });

                        //UserInformation users = new UserInformation(email);

                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class)); // Return to main class


                    } else {
                        Toast.makeText(RegisterActivity.this, "Error! Something went wrong!", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
