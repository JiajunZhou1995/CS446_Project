package com.mtr.codetrip.codetrip;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.floor;

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



        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View action_menu = layoutInflater.inflate(R.layout.stars_indicator,null);
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
        View child = layoutInflater.inflate(R.layout.content_course,null);
        container.addView(child);

        generateCourses();





//        courseList.get(0).courseStatus = Course.CourseStatus.AVAILABLE;
//
//        updateCourseNode(0);
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


    private Pair<Integer,Integer> nomalizPositioneString(String position){
        String[] rowColNum = position.split("\\D");
        String direction = position.replaceAll("[0-9]","");

        int x = (Integer.parseInt(rowColNum[1])-1) *
                (getResources().getInteger(R.integer.non_quiz_horizontal_space) +
                        getResources().getInteger(R.integer.non_quiz_size));
        if (direction.equals("L")){
            x += getResources().getInteger(R.integer.non_quiz_left_marginLeft);
        }else{
            x += getResources().getInteger(R.integer.non_quiz_right_marginLeft);
        }

        int y = getResources().getInteger(R.integer.non_quiz_size) * (Integer.parseInt(rowColNum[0])-1);

        Pair<Integer,Integer> xyPosition = new Pair<>(DensityUtil.dip2px(this,x),DensityUtil.dip2px(this,y));

        return  xyPosition;
    }


    private void generateCourses(){

        Cursor c = myDB.query("course", null, null, null, null, null, null);
        c.moveToFirst();

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.course_content_page);
//        TextView textView = new TextView(this);
//        textView.setText("okokokokoko");
//        textView.setBackgroundColor(getColor(R.color.colorAccent));
//        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(DensityUtil.dip2px(this,200),DensityUtil.dip2px(this,200));
//        rlp.setMargins(DensityUtil.dip2px(this,90),DensityUtil.dip2px(this,90),0,0);
//        relativeLayout.addView(textView,rlp);

//        LayoutInflater layoutInflater = LayoutInflater.from(this);

        int margin_top = DensityUtil.dip2px(this,getResources().getInteger(R.integer.unit_marginTop));
        int size = DensityUtil.dip2px(this,getResources().getInteger(R.integer.non_quiz_size));

        while(!c.isAfterLast()){
//            int index = c.getColumnIndex("title");
//            Log.d("SQLite", c.getString(index));
            String courseTag = "course_"+c.getString(c.getColumnIndex("courseid"));
            String title = c.getString(c.getColumnIndex("title"));
            String type = c.getString(c.getColumnIndex("type"));
            Pair<Integer,Integer> xyPosition = nomalizPositioneString(c.getString(c.getColumnIndex("position")));
            Button course = new Button(this);
            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(size,size);
            rlp.setMargins(xyPosition.first,xyPosition.second+margin_top,0,0);
            course.setBackgroundColor(getColor(R.color.colorAquaMarine));
            course.setTag(courseTag);
            course.setText(title);
            course.setTextSize(8);
            relativeLayout.addView(course,rlp);
            c.moveToNext();
        }

//        courseList = new ArrayList<>();
//        for (int i = 0; i < 10; ++i){
//            Course newCourse = new Course(i);
//
//            if (i==7) { newCourse.courseType = Course.CourseType.QUIZ;}
//            else if(i==6){newCourse.courseType = Course.CourseType.PROJECT;}
//            else{newCourse.courseType= Course.CourseType.LECTURE;}
//
//            newCourse.courseStatus = Course.CourseStatus.UNAVAILABLE;
//
//            String stringID = "course_" + i;
//            int id = getResources().getIdentifier(stringID,"id",getPackageName());
//
//            newCourse.setBoundBtn((Button) findViewById(id));
//
//            newCourse.boundBtn.setOnClickListener(this);
//
//            courseList.add(newCourse);
//        }
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
