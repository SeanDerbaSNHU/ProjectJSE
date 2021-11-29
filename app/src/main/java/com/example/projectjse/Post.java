package com.example.projectjse;

public class Post {
    public String postUsername;
    public String postText;
    public String postImage;

    public Post(String postUsername, String postText) {
        this.postUsername = postUsername;
        this.postText = postText;
    }

    public Post(String postUsername, String postText, String postImage) {
        this.postUsername = postUsername;
        this.postText = postText;
        this.postImage = postImage;
    }


}
