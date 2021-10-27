package com.example.projectjse;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Random;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;



import java.util.HashMap;
import java.util.Map;


public class PostActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private String text;
    private Button confirmBut;
    private FirebaseAuth mAuth;


    private static final String USER_KEY = "user";
    private EditText textPost;
    private static final String TEXT_KEY = "text";
    private String userResult;
    Random rand = new Random();


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentID = currentUser.getUid(); // Get user info this returns the string that is save in user part of database not the actual user string needs to be fixed

        db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        textPost = findViewById(R.id.textPostText);
        confirmBut = (Button) findViewById(R.id.confirmButton);
        confirmBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int int_random = rand.nextInt(10000000);
                String hold = Integer.toString(int_random);

                text = textPost.getText().toString();
                Map< String, Object > newContact = new HashMap< >();
                newContact.put(TEXT_KEY , text);
                newContact.put(USER_KEY, currentID);
                db.collection("posts").document(hold).set(newContact)
                        .addOnSuccessListener(new OnSuccessListener< Void >() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(PostActivity.this, "posted",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PostActivity.this, "ERROR" + e.toString(),
                                        Toast.LENGTH_SHORT).show();
                                Log.d("TAG", e.toString());
                            }

                        });
            }
        });
    }

}