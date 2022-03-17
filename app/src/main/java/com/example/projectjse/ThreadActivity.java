package com.example.projectjse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class ThreadActivity extends AppCompatActivity {
    String postID = "nothing";
    private String TAG = "ThreadActivity";
    private FirebaseFirestore db;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private RecyclerView threadView;
    private ArrayList<Post> PostList = new ArrayList<Post>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().hasExtra("ID")) {
            Toast.makeText(this, "I am here", Toast.LENGTH_SHORT);
            postID = getIntent().getExtras().getString("ID");
        }

        try {
            setPostList();
            Thread.sleep(1150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        threadView = (RecyclerView) findViewById(R.id.threadView);
        setAdapter();

    }

    private void setPostList() {
        db = FirebaseFirestore.getInstance();
        DocumentReference originalPost = db.document(getIntent().getExtras().getString("path"));
        originalPost.collection("replies")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                               /* String username = document.get("user").toString();
                                String text = "";
                                if(document.get("text") != null){
                                    text = document.get("text").toString();
                                }
                                if(document.get("pic") != null){
                                    String imageName = document.get("pic").toString();
                                    PostList.add(new Post(username, text, imageName, Post.LayoutImg));
                                }
                                else {
                                    PostList.add(new Post(username, text, Post.LayoutTxt));
                                }*/
                                PostList.add(new Post(document));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            //PostList.add(new Post("fail","fail"));
                        }

                    }
                });

    }

    private void setAdapter() {
        recyclerAdapter adapter = new recyclerAdapter(PostList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        threadView.setLayoutManager(layoutManager);
        threadView.setItemAnimator(new DefaultItemAnimator());
        threadView.setAdapter(adapter);

    }
}