package com.mtr.codetrip.codetrip;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by Catrina on 2/3/2018.
 */

public class CourseActivity extends MainActivity implements View.OnClickListener {

    ArrayList<Course>courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        LayoutInflater layoutInflater1 = LayoutInflater.from(this);
        View action_menu = layoutInflater1.inflate(R.layout.stars_indicator,null);
        toolbar.addView(action_menu);

        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(1).setChecked(true);

        CoordinatorLayout container = (CoordinatorLayout) findViewById(R.id.app_bar_main);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View child = layoutInflater.inflate(R.layout.content_course,null);
        container.addView(child);


        generateCourses();

        courseList.get(0).courseStatus = Course.CourseStatus.AVAILABLE;

        updateCourseNode(0);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent = new Intent();

        if (id == R.id.sidebar_home) {
            intent.setClass(this,HomeActivity.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.sidebar_course) {

        } else if (id == R.id.sidebar_favorite) {
            intent.setClass(this,FavoriteActivity.class);
            startActivity(intent);
        } else if (id == R.id.sidebar_achievement) {

        } else if (id == R.id.sidebar_setting) {

        } else if (id == R.id.sidebar_about_us) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void generateCourses(){
        courseList = new ArrayList<>();
        for (int i = 0; i < 10; ++i){
            Course newCourse = new Course(i);

            if (i==7) { newCourse.courseType = Course.CourseType.QUIZ;}
            else if(i==6){newCourse.courseType = Course.CourseType.PROJECT;}
            else{newCourse.courseType= Course.CourseType.LECTURE;}

            newCourse.courseStatus = Course.CourseStatus.UNAVAILABLE;

            String stringID = "course_" + i;
            int id = getResources().getIdentifier(stringID,"id",getPackageName());
            newCourse.setBoundBtn((Button) findViewById(id));

            newCourse.boundBtn.setOnClickListener(this);

            courseList.add(newCourse);
        }
    }

    private void updateCourseNode(int index){
        Course currentCourse = courseList.get(index);
        Button btn = currentCourse.boundBtn;
        if (currentCourse.courseStatus== Course.CourseStatus.AVAILABLE){
            btn.setBackgroundColor(Color.YELLOW);
        }
    }


    @Override
    public void onClick(View view) {
        String courseIdString = getResources().getResourceEntryName(view.getId());
        int courseId = Integer.parseInt(courseIdString.substring(7));
        Course currentCourse = courseList.get(courseId);

        Course.CourseType courseType  =  currentCourse.courseType;
        Course.CourseStatus courseStatus = currentCourse.courseStatus;

        if (courseStatus == Course.CourseStatus.AVAILABLE){

            Intent intent = new Intent();
            intent.setClass(this,QuestionActivity.class);
            intent.putExtra("question_file_name","file_name");
            startActivity(intent);
        }else{
            if (courseType == Course.CourseType.LECTURE){

            }else if (courseType == Course.CourseType.PROJECT){

            }else{

            }
        }
    }
}
