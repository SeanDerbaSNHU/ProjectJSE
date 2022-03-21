package com.example.projectjse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ViewUserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_profile);

    }
    public void ClickAdd(View view){
        startActivity(new Intent(ViewUserProfileActivity.this, PostActivity.class));

    }

    private boolean CheckIfFollowing(){
        return true;
    }


}