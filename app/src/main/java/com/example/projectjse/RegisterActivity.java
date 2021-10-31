package com.example.projectjse;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    // XML Variables
    private Button registerUser;
    private EditText editEmail, editPassword, editPasswordConfirm, editUsername;
    //private String userID;

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

        registerUser = findViewById(R.id.registerUser);
        //registerUser.setOnClickListener(this);

        editEmail = (EditText) findViewById(R.id.emailBox);
        editPassword = (EditText) findViewById(R.id.password);
        editPasswordConfirm = (EditText) findViewById(R.id.editPasswordConfirm);
        editUsername = (EditText) findViewById(R.id.usernameBox);

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

        String username = editUsername.getText().toString().trim();
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

        if(username.isEmpty()){
            editUsername.setError("Username is required");
            editUsername.requestFocus();
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

                        login(email, password);

                        mAuth.signOut();
                                //UserInformation users = new UserInformation(email);




                    } else {
                        Toast.makeText(RegisterActivity.this, "Error! Something went wrong!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void login (String email, String password){

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) { // Did we find a matching account?
                // Successful login
                uploadToFirestore();
            }
            else{ // Unsuccessful login

            }
        }
    });}

    private void uploadToFirestore(){
        FirebaseUser user1 = mAuth.getCurrentUser();
        String userID = user1.getUid();
        //userID = mAuth.getCurrentUser().getUid();
        String username = editUsername.getText().toString().trim();
        String email = editEmail.getText().toString().trim();

        //DocumentReference documentReference = fStore.collection("users").document(userID);
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("username", username);
        user.put("userID", userID);

        fStore.collection("users")
                .document(userID)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() { // Adds the Document users on Firebase with user information
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("test", "onSuccess: User folder created");
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class)); // Return to main class
                        //Toast.makeText(RegisterActivity.this, "Wrote to database", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "ERROR WRITING DOCUMENT", e);
                //Toast.makeText(RegisterActivity.this, "Error! Cant upload to firestore", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean verifyUniqueUsername(String username){
        CollectionReference userList = fStore.collection("users");
        boolean isUnique = true;



        if(isUnique == true){
            return true;
        }
        return false;
    }

    private boolean verifyUniqueEmail(String email){
        CollectionReference userList = fStore.collection("users");
        boolean isUnique = true;



        if(isUnique == true){
            return true;
        }
        return false;
    }


}
