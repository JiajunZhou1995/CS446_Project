package com.mtr.codetrip.codetrip;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;


/**
 * Created by Catrina on 2/3/2018.
 */


public class Course extends Object  {

    enum CourseType{LECTURE, PROJECT, QUIZ}
    enum CourseStatus{AVAILABLE, UNAVAILABLE}

    private int courseID;
    CourseType courseType;
    CourseStatus courseStatus;
    Button boundBtn;
    RelativeLayout.LayoutParams layoutParams;
    private int defaultMarginTop;

    public Course(Context context, Cursor cursor, int margin_top){

        defaultMarginTop = margin_top;

        courseID = cursor.getInt(cursor.getColumnIndex("courseid"));

        String tmp = cursor.getString(cursor.getColumnIndex("title"));
        switch (tmp){
            case "Lecture":
                courseType = CourseType.LECTURE;
                break;
            case "Quiz":
                courseType = CourseType.QUIZ;
                break;
            case "Project":
                courseType = CourseType.PROJECT;
                break;
            default:
                courseType = null;
                break;
        }

        tmp = cursor.getString(cursor.getColumnIndex("position"));
        boundBtn = generateCourseButton(context);
        layoutParams = setUpLayout(context,tmp);

//        tmp = cursor.getString()
    }

    protected void printCourse(){
        if (courseType==CourseType.LECTURE) Log.d("type","lecture");
        if (courseType==CourseType.QUIZ) Log.d("type","quiz");
        if (courseType==CourseType.PROJECT) Log.d("type","project");
    }

    private Button generateCourseButton(Context context){
        Button courseBtn = new Button(context);
        if (courseStatus==CourseStatus.AVAILABLE){
            courseBtn.setBackground(context.getDrawable(R.mipmap.hex_yellow));
        }else{
            courseBtn.setBackground(context.getDrawable(R.mipmap.hex_green));
        }
        courseBtn.setTextSize(8);
        courseBtn.setTag(Integer.toString(courseID));
        return courseBtn;
    }

    private RelativeLayout.LayoutParams setUpLayout(Context context,String position){
        double unit_width, unit_height;

        if (courseType != CourseType.QUIZ){

            unit_width = context.getResources().getInteger(R.integer.non_quiz_width);
            unit_height = context.getResources().getInteger(R.integer.non_quiz_height);
        }else {
            unit_width = context.getResources().getInteger(R.integer.quiz_width);
            unit_height = context.getResources().getInteger(R.integer.quiz_height);
        }
        unit_width = unit_width * MainActivity.ScreenWidthRatio +0.5f;
        unit_height = unit_height *  MainActivity.ScreenHeightRatio +0.5f;

        Pair<Integer,Integer> xyPosition = nomalizPositioneString(context,position);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams((int)unit_width,(int)unit_height);
        rlp.setMargins(xyPosition.first,xyPosition.second + defaultMarginTop,0,0);
        return rlp;
    }


    private Pair<Integer,Integer> nomalizPositioneString(Context context,String position){
        String[] rowColNum = position.split("\\D");
        String direction = position.replaceAll("[0-9]","");

        double x = 0, y;
        if (courseType != CourseType.QUIZ){
            x = (Integer.parseInt(rowColNum[1])-1) * (context.getResources().getInteger(R.integer.non_quiz_horizontal_space) +
                    context.getResources().getInteger(R.integer.non_quiz_width));
            if (direction.equals("L")){
                x += context.getResources().getInteger(R.integer.non_quiz_left_marginLeft);
            }else{
                x += context.getResources().getInteger(R.integer.non_quiz_right_marginLeft);
            }
        }else if (direction.equals("L") && rowColNum[1].equals("1")){
            x = context.getResources().getInteger(R.integer.quiz_left_first_marginLeft);
        }else if (direction.equals("L") && rowColNum[1].equals("2")){
            x = 0;
        }else if (direction.equals("R") && rowColNum[1].equals("1")){
            x = context.getResources().getInteger(R.integer.quiz_right_first_marginLeft);
        }else{
            //r1
            x = context.getResources().getInteger(R.integer.quiz_right_second_marginLeft);
        }

        x = x * MainActivity.ScreenWidthRatio + 0.5f;

        y = context.getResources().getInteger(R.integer.non_quiz_height)* MainActivity.ScreenHeightRatio * (Integer.parseInt(rowColNum[0])-1) +0.5f;
        if (courseType == CourseType.QUIZ){
            CourseActivity.marginTop = (int)y + context.getResources().getInteger(R.integer.quiz_height);
        }
        return  new Pair<>((int)x,(int)y);
    }



    protected void updateCourseBtn(Context context){
        if (courseStatus== Course.CourseStatus.AVAILABLE){
            boundBtn.setBackground(context.getDrawable(R.mipmap.hex_yellow));
        }
    }

}
