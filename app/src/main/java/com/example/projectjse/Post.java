package com.example.projectjse;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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
    public String postID;

    private QueryDocumentSnapshot document;
    private StorageReference postImage;

    private String likes;
    private String datePosted;


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

    public Post(QueryDocumentSnapshot document){
        this.document = document;
        viewType = 0;
        postUsername = document.get("user").toString();
        postText = document.get("text").toString();
        if(document.get("pic") != null){
            viewType = 1;
            postImageName = document.get("pic").toString();
            SearchDatabaseForStorageReference();
        }
        postID = document.getId();
        //likes = document.get("likes").toString();
    }


    protected void finalize()
    {

    }
    private void SearchDatabaseForStorageReference(){
        StorageReference storageRef = storage.getReference().child("pictures").child(postImageName);
        postImage = storageRef;
    }

    public StorageReference GetStorageReference(){
        return postImage;
    }

    public void SetDocumentReference(QueryDocumentSnapshot doc){document = doc;}

    public QueryDocumentSnapshot GetDocumentReference(){return document;}

    public String getLikes(){

        return document.get("likes").toString();
    }

    public void setLikes(int x){

    }


    public int getViewType(){return viewType;}

    public void updatePost(){   //Will update firebase whenever post is changed

    }


}
