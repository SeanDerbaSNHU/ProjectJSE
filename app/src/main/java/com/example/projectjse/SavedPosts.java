package com.example.projectjse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

public class SavedPosts extends AppCompatActivity {
    private RecyclerView recyclerView;

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_posts);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewSavedPosts);

        drawerLayout = findViewById(R.id.drawer_layout);
    }

    public void ClickMenu(View view) {
        MainFeedActivity.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view) {
        MainFeedActivity.closeDrawer(drawerLayout);
    }

    public void ClickHome(View view) {
        MainFeedActivity.redirectActivity(this, MainFeedActivity.class);
    }

    public void ClickMyAccount(View view) {
        MainFeedActivity.redirectActivity(this, ProfileActivity.class);
    }

    public void ClickBoards(View view) {
        MainFeedActivity.redirectActivity(this, Boards.class);
    }

    public void ClickSavedPosts(View view) {
        recreate();
    }

    public void ClickSettings(View view) {
        MainFeedActivity.redirectActivity(this, Settings.class);
    }

    public void ClickLogout(View view) {
        MainFeedActivity.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainFeedActivity.closeDrawer(drawerLayout);
    }
}