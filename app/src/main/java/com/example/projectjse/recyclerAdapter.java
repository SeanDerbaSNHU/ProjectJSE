package com.example.projectjse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {

    private ArrayList<Post> postList;
    private Context context;

    public recyclerAdapter(ArrayList<Post> posts, Context context){
        this.postList = posts;
        this.context = context;
    }

    //list_items implementation
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView usernameText;
        private TextView postText;
        private ImageView postImage;

        public MyViewHolder(final View view){
            super(view);
            usernameText = view.findViewById(R.id.textViewUsername);
            postText = view.findViewById(R.id.textViewPostText);
            postImage = view.findViewById(R.id.imageViewPost);
        }

        public void removeImage(){

        }


    }




    //Create RecyclerView
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        return new MyViewHolder(itemView);
    }
    //Populate Data
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.usernameText.setText(post.postUsername);
        holder.postText.setText(post.postText);
        if(post.postImageName != null){
            StorageReference storageRef = post.GetStorageReference();
            GlideApp.with(context)
                    .load(storageRef)
                    .into(holder.postImage);
        }
        else{
            holder.removeImage();
        }
    }
    //Return # of objects
    @Override
    public int getItemCount() {
        return postList.size();
    }


}
