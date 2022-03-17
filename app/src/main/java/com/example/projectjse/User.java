package com.example.projectjse;

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

    private QueryDocumentSnapshot document;

    public User(QueryDocumentSnapshot doc){
        document = doc;
        userID = doc.get("userID").toString();
        username = doc.get("username").toString();
        path = doc.getReference().getPath();
        db = FirebaseFirestore.getInstance();
        getFollows();
    }

    private void getFollows(){
        following = (List<String>) document.get("following");
        followers = (List<String>) document.get("followers");
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
