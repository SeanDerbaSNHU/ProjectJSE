package com.example.projectjse;

import static com.example.projectjse.MainFeedActivity.redirectActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;

public class Boards extends AppCompatActivity {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boards);

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
        recreate();
    }

    public void ClickSavedPosts(View view) {
        MainFeedActivity.redirectActivity(this, SavedPosts.class);
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
