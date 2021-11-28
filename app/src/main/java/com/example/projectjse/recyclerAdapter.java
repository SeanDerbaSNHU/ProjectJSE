package com.example.projectjse;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {

    private ArrayList<Post> postList;

    public recyclerAdapter(ArrayList<Post> posts){
        this.postList = posts;
    }

    //list_items implementation
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView usernameText;
        private TextView postText;

        public MyViewHolder(final View view){
            super(view);
            usernameText.findViewById(R.id.textViewUsername);
            postText.findViewById(R.id.textPostText);

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
    }
    //Return # of objects
    @Override
    public int getItemCount() {
        return postList.size();
    }


}
