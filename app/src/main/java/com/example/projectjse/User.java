package com.example.projectjse;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class User {

    public String username;
    public String userID;
    public List<String> following;      //Change from string to document reference, arrays in FS can use reference strings
    public List<String> followers;
    public List<String> posts;
    public String path;
    private FirebaseFirestore db;

    private DocumentSnapshot docSnap;

    public User(DocumentSnapshot doc){
        db = FirebaseFirestore.getInstance();
        docSnap = doc;
        userID = doc.get("userID").toString();
        username = doc.get("username").toString();
        path = (String) doc.getReference().getPath();
        //getFollows();
    }

    public User(String p){
        db = FirebaseFirestore.getInstance();
        /*db.document(p).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                document = task.getResult();
                userID = document.get("userID").toString();
                username = document.get("username").toString();
                path = (String) document.getReference().getPath();
                getFollows();
            }
        });*/
        username = "default";
        path = p;
        p = p.replace("users/","");
        db.collection("users").document(p).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                username = "In Loop";
                DocumentSnapshot document = documentSnapshot;
                docSnap = document;
                userID = document.get("userID").toString();
                username = document.get("username").toString();
                //path = p;
                //getFollows();
            }
        });

    }

    private void getFollows(){
        //following = (List<String>) document.get("following");
        //followers = (List<String>) document.get("followers");
    }

    public boolean checkIfFollows(String ID){   //If user is following the current user (when visiting
        if(followers.contains(ID)){
            return true;
        }
        return false;
    }

    public boolean checkIfFollowing(String ID){
        if(following.contains(ID)){
            return true;
        }
        return false;
    }

    private void addFollower(User user){ //Must not be following already
        db.document(path).update("following", FieldValue.arrayUnion(user.path));
    }

    private void removeFollower(User user){
        db.document(path).update("following", FieldValue.arrayRemove(user.path));
    }

    public void changeFollower(User user){
        if(checkIfFollowing(user.path)){
            removeFollower(user);
        }
        else{
            addFollower(user);
        }
    }

}
