package com.example.projectjse;

import static com.example.projectjse.Post.LayoutImg;
import static com.example.projectjse.Post.LayoutTxt;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class recyclerAdapter extends RecyclerView.Adapter {

    private ArrayList<Post> postList;
    private Context context;

    public recyclerAdapter(ArrayList<Post> posts, Context context){
        this.postList = posts;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position)
    {
        switch (postList.get(position).getViewType()) {
            case 0:
                return LayoutTxt;
            case 1:
                return LayoutImg;
            default:
                return -1;
        }
    }

    //Class for posts with no image
    class LayoutOneViewHolder extends RecyclerView.ViewHolder
             {

        private TextView usernameText;
        private TextView postText;
        private ImageView replyButton;
        private ImageView likeButton;

        public LayoutOneViewHolder(@NonNull View itemView)
        {
            super(itemView);
            // Find the Views
            usernameText = itemView.findViewById(R.id.textViewUsername);
            postText = itemView.findViewById(R.id.textViewPostText);
            replyButton = itemView.findViewById(R.id.commentButton);
            likeButton = itemView.findViewById(R.id.likeButton);
            usernameText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(itemView.getContext(), ViewUserProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("UserName", usernameText.toString());
                    itemView.getContext().startActivity(i);
                }
            });
            replyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                }
            });
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        private void setView(String username, String text)
        {
            usernameText.setText(username);
            postText.setText(text);
        }
    }

    //Class for post with image
    class LayoutTwoViewHolder
            extends RecyclerView.ViewHolder {

        private TextView usernameText;
        private TextView postText;
        private ImageView postImage;
        private ImageView likeButton;

        public LayoutTwoViewHolder(@NonNull View itemView)
        {
            super(itemView);

            // Find the Views
            usernameText = itemView.findViewById(R.id.textViewUsername);
            postText = itemView.findViewById(R.id.textViewPostText);
            postImage = itemView.findViewById(R.id.imageViewPost);
            likeButton = itemView.findViewById(R.id.likeButton);

            usernameText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(itemView.getContext(), ViewUserProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("UserName", usernameText.toString());
                    itemView.getContext().startActivity(i);
                }
            });

            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        private void setView(String username, String text,StorageReference postImg,@NonNull RecyclerView.ViewHolder holder )
        {
            usernameText.setText(username);
            postText.setText(text);
            GlideApp.with(context)
                    .load(postImg)
                    .into((ImageView) holder.itemView.findViewById(R.id.imageViewPost));
        }
    }





    // In the onCreateViewHolder, inflate the
    // xml layout as per the viewType.
    // This method returns either of the
    // ViewHolder classes defined above,
    // depending upon the layout passed as a parameter.

    @NonNull
    @Override
    public RecyclerView.ViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType)
    {
        switch (viewType) {
            case LayoutTxt:
                View layoutOne
                        = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_items, parent, false);
                return new LayoutOneViewHolder(layoutOne);
            case LayoutImg:
                View layoutTwo
                        = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_items_image, parent, false);
                return new LayoutTwoViewHolder(layoutTwo);
            default:
                return null;
        }
    }

    // In onBindViewHolder, set the Views for each element
    // of the layout using the methods defined in the
    // respective ViewHolder classes.

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        String username, text;
        switch (postList.get(position).getViewType()) {
            case LayoutTxt:

                username = postList.get(position).postUsername;
                text = postList.get(position).postText;
                ((LayoutOneViewHolder)holder).setView(username, text);
                break;

            case LayoutImg:
                username = postList.get(position).postUsername;
                text = postList.get(position).postText;
                StorageReference img = postList.get(position).GetStorageReference();
                ((LayoutTwoViewHolder)holder).setView(username, text, img, holder);
                break;
            default:
                return;
        }
    }

    // This method returns the count of items present in the
    // RecyclerView at any given time.

    @Override
    public int getItemCount()
    {
        return postList.size();
    }
}








    /*//Create RecyclerView
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
*/