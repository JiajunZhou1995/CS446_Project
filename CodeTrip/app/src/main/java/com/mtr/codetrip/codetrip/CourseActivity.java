package com.mtr.codetrip.codetrip;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Catrina on 2/3/2018.
 */

public class CourseActivity extends MainActivity implements View.OnClickListener {

    ArrayList<Course>courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_course);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        generate_courses();

        courseList.get(0).courseStatus = CourseStatus.AVAILABLE;

        update_course_node(0);
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
//            intent.setClass(CourseActivity.this,CourseActivity.class);
//            startActivity(intent);
//            finish();
        } else if (id == R.id.sidebar_favorite) {
            intent.setClass(this,MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.sidebar_achievement) {

        } else if (id == R.id.sidebar_setting) {

        } else if (id == R.id.sidebar_about_us) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_course);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void generate_courses(){
        courseList = new ArrayList<>();
        for (int i = 0; i < 10; ++i){
            Course newCourse = new Course(i);

            if (i==7) { newCourse.courseType = CourseType.QUIZ;}
            else if(i==6){newCourse.courseType = CourseType.PROJECT;}
            else{newCourse.courseType=CourseType.LECTURE;}

            newCourse.courseStatus = CourseStatus.UNAVAILABLE;

            String stringID = "course_" + i;
            int id = getResources().getIdentifier(stringID,"id",getPackageName());
            newCourse.setBoundBtn((Button) findViewById(id));

            newCourse.boundBtn.setOnClickListener(this);

            courseList.add(newCourse);
        }
    }

    private void update_course_node(int index){
        Course currentCourse = courseList.get(index);
        Button btn = currentCourse.boundBtn;
        if (currentCourse.courseStatus==CourseStatus.AVAILABLE){
            btn.setBackgroundColor(Color.YELLOW);
        }
    }


    @Override
    public void onClick(View view) {
        String courseIdString = getResources().getResourceEntryName(view.getId());
        int courseId = Integer.parseInt(courseIdString.substring(7));
        Course currentCourse = courseList.get(courseId);

        CourseType courseType  =  currentCourse.courseType;
        CourseStatus courseStatus = currentCourse.courseStatus;

        if (courseStatus == CourseStatus.AVAILABLE){

            Intent intent = new Intent();
            intent.setClass(this,QuestionActivity.class);
            intent.putExtra("question_file_name","file_name");
            startActivity(intent);
        }else{
            if (courseType == CourseType.LECTURE){

            }else if (courseType == CourseType.PROJECT){

            }else{

            }
        }
    }
}
