package com.example.projectjse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainFeedActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private Button post;

    private String hold;
    private Button loadButton;
    private Button photoButton;

    private RecyclerView recyclerView;

    private ImageView picView;
    private ArrayList<String> picturesList = new ArrayList<String>();
    Random rand = new Random();
    DrawerLayout drawerLayout;
    private ArrayList<String> documents = new ArrayList<String>();
    private String TAG = "MainActivity";

    private ArrayList<Post> PostList = new ArrayList<Post>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        setPostList();
        getPosts();
        getPhotos();



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewPosts);
        setAdapter();

        picView = (ImageView) findViewById(R.id.showImage);
        loadButton = (Button) findViewById(R.id.refreshButton);



        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int int_random = rand.nextInt(documents.size());
                String txt = " ";
                int irandom = rand.nextInt(picturesList.size());
                String ID = picturesList.get(irandom);
                StorageReference storageRef = storage.getReference().child("pictures").child(ID);
                GlideApp.with(MainFeedActivity.this)
                        .load(storageRef)
                        .into(picView);
                DocumentReference docRef = db.collection("posts").document(documents.get(int_random));
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "Refreshed");
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }

                    }

                });
                setAdapter();
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        }

        public void ClickMenu(View view){
            //open drawer
            openDrawer(drawerLayout);
        }

     static void openDrawer(DrawerLayout drawerLayout) {
        //Open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view){
        //Close drawer
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        //Close drawer layout
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickHome(View view){
        recreate();
    }

    public void ClickMyAccount(View view){
        redirectActivity(this, ProfileActivity.class);
    }

    public void ClickBoards(View view){
        redirectActivity(this, Boards.class);
    }

    public void ClickSavedPosts(View view){
        redirectActivity(this, SavedPosts.class);
    }

    public void ClickSettings(View view){
        redirectActivity(this, Settings.class);
    }
    public void ClickAdd(View view){
        redirectActivity(this, PostActivity.class);
    }

    public void ClickLogout(View view){
        logout(this);
    }

    public void ClickSearch(View view){
        MainFeedActivity.redirectActivity(this, SearchActivity.class);
    }

    public static void logout(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle("Logout");

        builder.setMessage("Are you sure you want to logout?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Finish activity
                activity.finishAffinity();
                //Exit app
                System.exit(0);
            }

        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();

    }

    public static void redirectActivity(Activity activity,Class aClass) {
        Intent intent = new Intent(activity, aClass);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Close drawer
        closeDrawer(drawerLayout);
    }
    private void getPosts(){
        db = FirebaseFirestore.getInstance();
        db.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                documents.add(document.getId());
                                i++;

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    private void getPhotos() {
        db = FirebaseFirestore.getInstance();
        db.collection("pic")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                picturesList.add(document.getId());
                                i++;

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void setPostList(){
        db = FirebaseFirestore.getInstance();
        db.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String username = document.get("user").toString();
                                String text = document.get("text").toString();
                                PostList.add(new Post(username, text));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            PostList.add(new Post("fail","fail"));
                        }
                    }
                });
        //getUsersByID();
    }

    private void getUsersByID() {
        db = FirebaseFirestore.getInstance();
        for(int i = 0; i < PostList.size(); i++){
            DocumentReference docRef = db.collection("users").document((PostList.get(i).postUsername));
            int finalI = i;
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            PostList.get(finalI).postUsername = document.get("username").toString();
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


    private void setAdapter(){
        recyclerAdapter adapter = new recyclerAdapter(PostList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }


}