package com.example.projectjse;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Post {
    public String postUsername;
    public String postText;
    public String postImageName;
    private StorageReference postImage;

    private FirebaseStorage storage = FirebaseStorage.getInstance();

    public Post(String postUsername, String postText) {
        this.postUsername = postUsername;
        this.postText = postText;
    }

    public Post(String postUsername, String postText, String postImage) {
        this.postUsername = postUsername;
        this.postText = postText;
        this.postImageName = postImage;
        SearchDatabaseForStorageReference();
    }


    private void SearchDatabaseForStorageReference(){
        StorageReference storageRef = storage.getReference().child("pictures").child(postImageName);
        postImage = storageRef;
    }

    public StorageReference GetStorageReference(){
        return postImage;
    }



}
