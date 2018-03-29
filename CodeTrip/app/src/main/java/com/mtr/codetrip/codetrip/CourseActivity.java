package com.mtr.codetrip.codetrip;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    public static int currentCourseID;
    private static TextView totalStarCount;
    private static SharedPreferences prefs;
    public Course currentOnClickCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentCourseID = 0;


        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        Runnable mRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    generateCourses();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

//        CoordinatorLayout container = findViewById(R.id.app_bar_main);
        LinearLayout container = findViewById(R.id.activity_content);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        @SuppressLint("InflateParams") View child = layoutInflater.inflate(R.layout.content_course,null);

        container.addView(child);

        Thread thread = new Thread(mRunnable);
        thread.run();

        @SuppressLint("InflateParams") View action_menu = layoutInflater.inflate(R.layout.stars_indicator,null);
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

        totalStarCount = findViewById(R.id.total_star_count);
        updateStarCount();

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
//            startActivity(intent);
        } else if (id == R.id.sidebar_course) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
            drawer.closeDrawer(GravityCompat.START,true);
            return true;
        } else if (id == R.id.sidebar_favorite) {
            intent.setClass(this,KeynoteActivity.class);
//            startActivity(intent);
        } else if (id == R.id.sidebar_achievement) {
            intent.setClass(this,AchievementActivity.class);
//            startActivity(intent);
        } else if (id == R.id.sidebar_setting) {

        } else if (id == R.id.sidebar_about_us) {
            intent.setClass(this,AboutUsActivity.class);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
        drawer.closeDrawer(GravityCompat.START,true);
        startActivity(intent);

        if (id != R.id.sidebar_setting )finish();

        return true;
    }



    private void generateCourses(){
        courseList = new ArrayList<>();
        String course = "codetrip.db";
        SQLiteDatabase appDB = this.openOrCreateDatabase(course, Context.MODE_PRIVATE,null);
        prefs = this.getSharedPreferences(
                getString(R.string.course_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Integer.toString(0) + "Available", true);
        editor.putBoolean(Integer.toString(7) + "Available", true);
        editor.commit();

        @SuppressLint("Recycle") Cursor c = appDB.query("course", null, null, null, null, null, null);
        c.moveToFirst();

        RelativeLayout relativeLayout = findViewById(R.id.course_content_page);
        marginTop = (int) (getResources().getInteger(R.integer.unit_marginTop)* MainActivity.ScreenHeightRatio+0.5f);
        while(!c.isAfterLast()){


            // there should be a filter to filter the unit
            Course newCourse = new Course(this, c,marginTop, prefs);
            newCourse.boundBtn.setOnClickListener(this);
//            newCourse.printCourse();

            relativeLayout.addView(newCourse.boundBtn,newCourse.buttonLayoutParams);
            relativeLayout.addView(newCourse.courseTitle,newCourse.titleLayoutParams);
            for (int starIndex = 0; starIndex < 3; starIndex++){
                relativeLayout.addView(newCourse.stars.get(starIndex),newCourse.starsLayoutParams.get(starIndex));
                newCourse.stars.get(starIndex).setTranslationZ(10);
            }
            newCourse.boundBtn.setTranslationZ(0);
            newCourse.courseTitle.setTranslationZ(10);

            courseList.add(newCourse);
            c.moveToNext();
        }
        c.close();
        appDB.close();
    }




    private static void makeAvailable(int index){
        Course course = courseList.get(index);
        //should modify database
        course.courseStatus = Course.CourseStatus.AVAILABLE;
        updateCourseNode(index);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Integer.toString(index) + "Available", true);
        editor.apply();

    }

    private static void updateCourseNode(int index){
        courseList.get(index).updateBtn();
    }

    public static void refreshCourseMap(){
        int courseIndex = 0;
        while(courseIndex <= currentCourseID){
            makeAvailable(courseIndex);
            courseIndex++;
        }
        updateStarCount();
    }


    public static void updateStarCount(){
        int totalStars = 0;
        for (Course course : courseList){
            totalStars+=course.getStars(prefs);
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("Total_Stars", totalStars);
        editor.apply();
        totalStarCount.setText(Integer.toString(totalStars));
    }

    public static void updateScore(int courseid, float newScore){
        courseList.get(courseid).updateScore(newScore, prefs);
    }


    @Override
    public void onClick(View view) {
        if (MultipleClickUtility.isFastDoubleClick()) {
            return;
        }
        String courseIdString = (String)view.getTag();
        currentOnClickCourse = courseList.get(Integer.parseInt(courseIdString));

        Course.CourseType courseType  =  currentOnClickCourse.courseType;
        Course.CourseStatus courseStatus = currentOnClickCourse.courseStatus;

        if (courseStatus == Course.CourseStatus.AVAILABLE){

            if (courseType == Course.CourseType.LECTURE){
//                Intent intent = new Intent();
//                intent.setClass(this,QuestionActivity.class);
//                intent.putExtra("courseID",currentCourse.courseID);
//                startActivity(intent);
                startQuestionActivity();

            }else if(courseType == Course.CourseType.PROJECT){

            }else{
                // if Quiz
                AlertDialog dialog = new AlertDialog.Builder(this).setTitle(currentOnClickCourse.courseTitle.getText())
                        .setNegativeButton("Cancel", null).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //处理确认按钮的点击事件
                                startQuestionActivity();

                            }
                        })
                        .setMessage("Test out your Knowledge by taking this Quiz").create();
                dialog.show();
            }

        }else{
            if (courseType == Course.CourseType.LECTURE){

            }else if(courseType == Course.CourseType.PROJECT){

            }else{
                // if Quiz
                AlertDialog dialog = new AlertDialog.Builder(this).setTitle(currentOnClickCourse.courseTitle.getText())
                        .setNegativeButton("Cancel", null).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //处理确认按钮的点击事件
                            }
                        })
                        .setMessage("Test out your Knowledge by taking this Quiz").create();
                dialog.show();
            }
        }
    }

    private void startQuestionActivity(){
        Intent intent = new Intent();
        intent.setClass(this,QuestionActivity.class);
        intent.putExtra("courseID",currentOnClickCourse.courseID);
        startActivity(intent);
    }


}
