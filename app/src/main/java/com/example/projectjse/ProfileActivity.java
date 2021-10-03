package com.example.projectjse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProfileActivity extends AppCompatActivity {
    private Button Back;
    private Button logOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Back = (Button) findViewById(R.id.BackButtonPro);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity();
            }
        });
        logOut = (Button) findViewById(R.id.logOutButton);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });
    }
    public void openActivity(){
        Intent intent = new Intent(this, MainFeedActivity.class);
        startActivity(intent);
        //
    }public void openActivity2(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}