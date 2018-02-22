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

    int courseID;
//    Pair<Integer,Integer> position;
    CourseType courseType;
    CourseStatus courseStatus;
    Button boundBtn;
    RelativeLayout.LayoutParams layoutParams;

    public Course(Context context, Cursor cursor){

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
        layoutParams = setUpLayout(context,tmp,courseType);

//        tmp = cursor.getString()
    }

    public void printCourse(){
        if (courseType==CourseType.LECTURE) Log.d("type","lecture");
        if (courseType==CourseType.QUIZ) Log.d("type","quiz");
        if (courseType==CourseType.PROJECT) Log.d("type","project");
    }

    private Button generateCourseButton(Context context){
        Button courseBtn = new Button(context);
        courseBtn.setBackgroundColor(context.getColor(R.color.colorAquaMarine));
        courseBtn.setTextSize(8);
        courseBtn.setTag(Integer.toString(courseID));
        return courseBtn;
    }

    private RelativeLayout.LayoutParams setUpLayout(Context context,String position, CourseType courseType){
        int margin_top = DensityUtil.dip2px(context,context.getResources().getInteger(R.integer.unit_marginTop));
        int size = DensityUtil.dip2px(context,context.getResources().getInteger(R.integer.non_quiz_size));
        Pair<Integer,Integer> xyPosition = nomalizPositioneString(context,position);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(size,size);
        rlp.setMargins(xyPosition.first,xyPosition.second+margin_top,0,0);
        return rlp;
    }


    private Pair<Integer,Integer> nomalizPositioneString(Context context,String position){
        String[] rowColNum = position.split("\\D");
        String direction = position.replaceAll("[0-9]","");

        int x = (Integer.parseInt(rowColNum[1])-1) *
                (context.getResources().getInteger(R.integer.non_quiz_horizontal_space) +
                        context.getResources().getInteger(R.integer.non_quiz_size));
        if (direction.equals("L")){
            x += context.getResources().getInteger(R.integer.non_quiz_left_marginLeft);
        }else{
            x += context.getResources().getInteger(R.integer.non_quiz_right_marginLeft);
        }

        int y = context.getResources().getInteger(R.integer.non_quiz_size) * (Integer.parseInt(rowColNum[0])-1);
        Pair<Integer,Integer> xyPosition = new Pair<>(DensityUtil.dip2px(context,x),DensityUtil.dip2px(context,y));
        return  xyPosition;
    }

}
