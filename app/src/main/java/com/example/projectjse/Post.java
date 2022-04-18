package com.example.projectjse;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public String postUsername;
    public String postText;
    public String postImageName;
    public String postID;

    private DocumentSnapshot document;
    private StorageReference postImage;

    private User user;

    private String userPath;

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

    public Post(DocumentSnapshot document){
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
        if(document.get("userPath") == null){
            final String[] x = new String[1];
            db.collection("users").whereEqualTo("username", postUsername).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        x[0] = document.getReference().getPath();
                    }
                }
            });
            document.getReference().update("userPath", x[0]);
        }
        else {
            userPath = document.get("userPath").toString();
            setUser();
        }
        db.collection("users").whereEqualTo("username", postUsername).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                user = new User((task.getResult().getDocuments()).get(0));
            }
        });

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

    public DocumentSnapshot GetDocumentReference(){return document;}

    public String getLikes(){

        return document.get("likes").toString();
    }

    public void setLikes(int x){

    }


    public void setUser(){
        /*db.collection("users").whereEqualTo("username", postUsername).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    user = new User(document);
                }
            }
        });*/

        user = new User(userPath);
    }

    public User getUser(){return user;}


    public int getViewType(){return viewType;}

    public void updatePost(){   //Will update firebase whenever post is changed

    }


}
