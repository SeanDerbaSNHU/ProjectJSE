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
import android.widget.TextView;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
    private TextView confirmBut;
    private TextView takePicture;
    private String fileName;
    private ImageView picture;
    Uri imageUri;
    private FirebaseAuth mAuth;
    private static final String USER_KEY = "user";
    private static final String PIC_KEY = "pic";
    private EditText textPost;
    private static final String TEXT_KEY = "text";
    private static final String DATE_KEY = "date";
    private String username;
    private String numID;
    private String currentID;
    private String userID;
    String postID = "nothing";
    public boolean isGot;
    private Date now;
    Random rand = new Random();

    private String userPath;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        isGot = false;

        if(getIntent().hasExtra("ID")) {
            Toast.makeText(this, "I am here", Toast.LENGTH_SHORT);
            postID = getIntent().getExtras().getString("ID");
        }
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = currentUser.getUid();
        currentID = currentUser.getUid();// Get user info this returns the string that is save in user part of database not the actual user string needs to be fixed
        db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        textPost = findViewById(R.id.textPostText);
        takePicture = findViewById(R.id.takePhotoBtn);
        picture = findViewById(R.id.imageView);
        confirmBut = (TextView) findViewById(R.id.confirmButton);
        getUsername();


        confirmBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPost();
            }
        });
        takePicture.setOnClickListener(new View.OnClickListener() { //takes picture
            @Override
            public void onClick(View v) {
                isGot = true;
                selectImage();
            }
        });

    }

    private void createPost(){
        text = textPost.getText().toString();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        now = new Date();
        fileName = formatter.format(now);
        storageReference = FirebaseStorage.getInstance().getReference("pictures/"+fileName);
        Map<String, Object> newPost = new HashMap<>();
        newPost.put(TEXT_KEY, text);
        if(isGot == true) {
            newPost.put(PIC_KEY, fileName);
            storageReference.putFile(imageUri);
        }
        else{
            newPost.put(PIC_KEY, null);
        }
        newPost.put(DATE_KEY, now);
        if(username == null){
            username = currentID;
        }
        newPost.put(USER_KEY, username);
        newPost.put("userPath", userPath);
        addPost(newPost);
    }

    protected void addPost(Map<String, Object> newPost) {
        //code for comments when i can add it
        //db.collection("posts").document(hold).collection("replies").add(newPost);
        if(postID == "nothing") {
            db.collection("posts").add(newPost) .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    db.collection("users").document(userID).update("posts", FieldValue.arrayUnion(documentReference));
                    picture.setImageURI(null);
                    Toast.makeText(PostActivity.this, "posted", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PostActivity.this, MainFeedActivity.class));
                }
            });

        }
        else {
            DocumentReference originalPost = db.document(getIntent().getExtras().getString("path"));
            originalPost.collection("replies").add(newPost).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    db.collection("users").document(userID).update("replies", FieldValue.arrayUnion(documentReference));
                    Toast.makeText(PostActivity.this, "Commented", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PostActivity.this, MainFeedActivity.class));
                }
            });
        }

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
                        userPath = document.getReference().getPath();
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