package com.mtr.codetrip.codetrip;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.RelativeLayout;

import com.mtr.codetrip.codetrip.Object.Course;
import com.mtr.codetrip.codetrip.Utility.MultipleClickUtility;

import java.util.ArrayList;

/**
 * Created by Catrina on 2/3/2018 at 12:37 AM.
 * Within Package: ${PACKAGE_NAME}
 */

public class CourseActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    static ArrayList<Course>courseList;
    public static int marginTop;
    public static int currentCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentCourse = 0;


        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);


//        LinearLayout toolbar_right_corner = (LinearLayout) findViewById(R.id.toobar_right_corner);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        @SuppressLint("InflateParams") View action_menu = layoutInflater.inflate(R.layout.stars_indicator,null);
//        toolbar_right_corner.addView(action_menu);
        toolbar.addView(action_menu);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(1).setChecked(true);

        CoordinatorLayout container = findViewById(R.id.app_bar_main);
        @SuppressLint("InflateParams") View child = layoutInflater.inflate(R.layout.content_course,null);
        container.addView(child);

        generateCourses();
        //should be commented
//        makeAvailable(3);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent = new Intent();

        if (id == R.id.sidebar_home) {
            intent.setClass(this,MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.sidebar_course) {

        } else if (id == R.id.sidebar_favorite) {
            intent.setClass(this,KeynoteActivity.class);
            startActivity(intent);
        } else if (id == R.id.sidebar_achievement) {
            intent.setClass(this,AchievementActivity.class);
            startActivity(intent);
        } else if (id == R.id.sidebar_setting) {

        } else if (id == R.id.sidebar_about_us) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
        drawer.closeDrawer(GravityCompat.START);
        if (id != R.id.sidebar_course  && id != R.id.sidebar_setting && id != R.id.sidebar_about_us)finish();

        return true;
    }



    private void generateCourses(){
        courseList = new ArrayList<>();

        @SuppressLint("Recycle") Cursor c = MainActivity.myDB.query("course", null, null, null, null, null, null);
        c.moveToFirst();

        RelativeLayout relativeLayout = findViewById(R.id.course_content_page);
        marginTop = (int) (getResources().getInteger(R.integer.unit_marginTop)* MainActivity.ScreenHeightRatio+0.5f);
        while(!c.isAfterLast()){


            // there should be a filter to filter the unit
            Course newCourse = new Course(this, c,marginTop);
            newCourse.boundBtn.setOnClickListener(this);
            courseList.add(newCourse);

//            newCourse.printCourse();

            relativeLayout.addView(newCourse.boundBtn,newCourse.buttonLayoutParams);
            relativeLayout.addView(newCourse.courseTitle,newCourse.titleLayoutParams);
            newCourse.boundBtn.setTranslationZ(0);
            newCourse.courseTitle.setTranslationZ(10);

            c.moveToNext();
        }
    }



    private static void makeAvailable(int index){
        Course course = courseList.get(index);
        //should modify database
        course.courseStatus = Course.CourseStatus.AVAILABLE;
        updateCourseNode(index);
    }

    private static void updateCourseNode(int index){
        courseList.get(index).updateBtn();
    }

    public static void refreshCourseMAp(){
        int courseIndex = 0;
        while(courseIndex <= currentCourse){
            makeAvailable(courseIndex);
            courseIndex++;
        }
    }


    @Override
    public void onClick(View view) {
        if (MultipleClickUtility.isFastDoubleClick()) {
            return;
        }
        String courseIdString = (String)view.getTag();
        Course currentCourse = courseList.get(Integer.parseInt(courseIdString));

//        Course.CourseType courseType  =  currentCourse.courseType;
        Course.CourseStatus courseStatus = currentCourse.courseStatus;

        if (courseStatus == Course.CourseStatus.AVAILABLE){

            Intent intent = new Intent();
            intent.setClass(this,QuestionActivity.class);
            intent.putExtra("courseID",currentCourse.courseID);
            startActivity(intent);
        }

//        else if (courseType == Course.CourseType.LECTURE){
//
//        }else if (courseType == Course.CourseType.PROJECT){
//
//        }else{
//
//        }
    }


}
