package com.example.projectjse;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Post {
    // Integers assigned to each layout
    // these are declared static so that they can
    // be accessed from the class name itself
    // And final so that they are not modified later
    public static final int LayoutTxt = 0;
    public static final int LayoutImg = 1;

    // This variable ViewType specifies
    // which out of the two layouts
    // is expected in the given item
    private int viewType;

    public String postUsername;
    public String postText;
    public String postImageName;
    private StorageReference postImage;

    private FirebaseStorage storage = FirebaseStorage.getInstance();

    public Post(String postUsername, String postText, int viewType) {
        this.postUsername = postUsername;
        this.postText = postText;
        this.viewType = viewType;
    }

    public Post(String postUsername, String postText, String postImage, int viewType) {
        this.postUsername = postUsername;
        this.postText = postText;
        this.postImageName = postImage;
        this.viewType = viewType;
        SearchDatabaseForStorageReference();
    }


    private void SearchDatabaseForStorageReference(){
        StorageReference storageRef = storage.getReference().child("pictures").child(postImageName);
        postImage = storageRef;
    }

    public StorageReference GetStorageReference(){
        return postImage;
    }

    public int getViewType(){return viewType;}



}
