package com.example.projectjse;

import static java.lang.Thread.sleep;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SavedPosts extends AppCompatActivity {
    String postID = "nothing";
    String userID;
    private String TAG = "SavedPosts";
    private FirebaseFirestore db;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    ArrayList<String> LPosts = new ArrayList<String>();
    private RecyclerView saveView;
    private ArrayList<Post> PostList = new ArrayList<Post>();
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        userID = currentUser.getUid();
        setPostList();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_posts);

        saveView = (RecyclerView) findViewById(R.id.savedView);



        drawerLayout = findViewById(R.id.drawer_layout);



    }
    private void setPostList(){
        db.collection("users").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                LPosts = (ArrayList<String>) documentSnapshot.get("SavedPosts");

                for (int i = 0; i < LPosts.size(); i++) {
                    db.collection("posts").document(LPosts.get(i).replace("posts/","")).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Post x = new Post(documentSnapshot);
                            PostList.add(x);
                            setAdapter();
                        }

                    });


                }
                //getUsersByID();
            }

        });

    }
    private void setAdapter() {
        recyclerAdapter adapter = new recyclerAdapter(PostList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        saveView.setLayoutManager(layoutManager);
        saveView.setItemAnimator(new DefaultItemAnimator());
        saveView.setAdapter(adapter);

    }
    public void ClickMenu(View view) {
        MainFeedActivity.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view) {
        MainFeedActivity.closeDrawer(drawerLayout);
    }

    public void ClickHome(View view) {
        MainFeedActivity.redirectActivity(this, MainFeedActivity.class);
    }

    public void ClickMyAccount(View view) {
        MainFeedActivity.redirectActivity(this, ProfileActivity.class);
    }

    public void ClickBoards(View view) {
        MainFeedActivity.redirectActivity(this, SavedPosts.class);
    }

    public void ClickSavedPosts(View view) {
        recreate();
    }

    public void ClickSettings(View view) {
        MainFeedActivity.redirectActivity(this, Settings.class);
    }

    public void ClickLogout(View view) {
        MainFeedActivity.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainFeedActivity.closeDrawer(drawerLayout);
    }
}