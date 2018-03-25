package com.mtr.codetrip.codetrip;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Catrina on 3/25/2018 at 4:41 PM.
 * Within Package: com.mtr.codetrip.codetrip
 */

public class AboutUsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);







        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

//        @SuppressLint("InflateParams") View action_menu = layoutInflater.inflate(R.layout.stars_indicator,null);
//        toolbar.addView(action_menu);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(5).setChecked(true);

        LinearLayout container = findViewById(R.id.activity_content);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        @SuppressLint("InflateParams") View child = layoutInflater.inflate(R.layout.content_aboutus,null);
        container.addView(child);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        Intent intent = new Intent();

        if (id == R.id.sidebar_home) {
            intent.setClass(this,MainActivity.class);
//            startActivity(intent);
        } else if (id == R.id.sidebar_course) {
            intent.setClass(this,CourseActivity.class);
        } else if (id == R.id.sidebar_favorite) {
            intent.setClass(this,KeynoteActivity.class);
//            startActivity(intent);
        } else if (id == R.id.sidebar_achievement) {
            intent.setClass(this,AchievementActivity.class);
//            startActivity(intent);
        } else if (id == R.id.sidebar_setting) {

        } else if (id == R.id.sidebar_about_us) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
            drawer.closeDrawer(GravityCompat.START,true);
            return true;        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
        drawer.closeDrawer(GravityCompat.START,true);
        startActivity(intent);

        if (id != R.id.sidebar_setting )finish();

        return true;
    }
}
