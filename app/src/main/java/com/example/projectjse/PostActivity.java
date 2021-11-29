package com.example.projectjse;
import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.HashMap;
import java.util.Map;


public class PostActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference;
    private String text;
    private Button confirmBut;
    private Button takePicture;
    private Button uploadPicture;
    private ImageView picture;
    Uri imageUri;
    private String filename;
    private FirebaseAuth mAuth;
    private String currentPhotoPath;
    private static final String USER_KEY = "user";
    private static final String PIC_KEY = "pic";
    private EditText textPost;
    private static final String TEXT_KEY = "text";
    private String userResult;
    private String username;
    Random rand = new Random();


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentID = currentUser.getUid(); // Get user info this returns the string that is save in user part of database not the actual user string needs to be fixed

        db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        textPost = findViewById(R.id.textPostText);
        takePicture = findViewById(R.id.takePhotoBtn);

        picture = findViewById(R.id.imageView);
        uploadPicture = findViewById(R.id.PhotoPost);
        confirmBut = (Button) findViewById(R.id.confirmButton);

        getUsername();

        confirmBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int int_random = rand.nextInt(10000000);
                String hold = Integer.toString(int_random);

                text = textPost.getText().toString();
                Map<String, Object> newPost = new HashMap<>();
                newPost.put(TEXT_KEY, text);
                newPost.put(USER_KEY, username);
                db.collection("posts").document(hold).set(newPost)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
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
        takePicture.setOnClickListener(new View.OnClickListener() { //takes picture
            @Override
            public void onClick(View v) {
                selectImage();

            }
        });
        uploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                uploadPicture();

            }
        });
    }

    protected void uploadPicture() {


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now = new Date();
        String fileName = formatter.format(now);
        storageReference = FirebaseStorage.getInstance().getReference("pictures/"+fileName);
        Map<String, Object> newPost = new HashMap<>();
        newPost.put(PIC_KEY, fileName);
        db.collection("pic").document(fileName).set(newPost);

        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        picture.setImageURI(null);
                        Toast.makeText(PostActivity.this,"Successfully Uploaded",Toast.LENGTH_SHORT).show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {



                Toast.makeText(PostActivity.this,"Failed to Upload",Toast.LENGTH_SHORT).show();


            }
        });

    }

    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null) {

            imageUri = data.getData();
            picture.setImageURI(imageUri);
        }
    }

    private void getUsername(){
        String ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference docRef = db.collection("users").document(ID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        username = document.get("username").toString();
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


}