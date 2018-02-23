package com.mtr.codetrip.codetrip;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import me.grantland.widget.AutofitTextView;


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
    RelativeLayout.LayoutParams buttonLayoutParams;
    RelativeLayout.LayoutParams titleLayoutParams;
    Drawable background;
    private int defaultMarginTop;
    TextView courseTitle;



    public Course(Context context, Cursor cursor, int margin_top){

        defaultMarginTop = margin_top;

        courseID = cursor.getInt(cursor.getColumnIndex("courseid"));

        background = context.getDrawable(context.getResources().getIdentifier("course"+Integer.toString(courseID),"mipmap",context.getPackageName()));

        courseTitle = (TextView) LayoutInflater.from(context).inflate(R.layout.course_title,null);
        courseTitle.setText(cursor.getString(cursor.getColumnIndex("title")));


        String tmp = cursor.getString(cursor.getColumnIndex("type"));
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

        tmp = cursor.getString(cursor.getColumnIndex("available"));
        if (tmp.equals("true")){
            courseStatus = CourseStatus.AVAILABLE;
        }else{
            courseStatus = CourseStatus.UNAVAILABLE;
        }

        tmp = cursor.getString(cursor.getColumnIndex("position"));
        boundBtn = generateCourseButton(context);
        buttonLayoutParams = setUpLayout(context,tmp);
    }

    public void printCourse(){
        if (courseType==CourseType.LECTURE) Log.d("type","lecture");
        if (courseType==CourseType.QUIZ) Log.d("type","quiz");
        if (courseType==CourseType.PROJECT) Log.d("type","project");
        Log.d("nothing","happened");
    }

    private void updateBtn(Context context, Button boundBtn){
        Drawable backgroundImg = boundBtn.getBackground();

        if (courseStatus==CourseStatus.AVAILABLE){
            boundBtn.setBackground(backgroundImg);
        }else{
            setGrayscale(background, true);
            boundBtn.setBackground(background);
        }
    }

    private Button generateCourseButton(Context context){
        Button courseBtn = new Button(context);
        if (courseStatus==CourseStatus.AVAILABLE){
            courseBtn.setBackground(background);
        }else{
            setGrayscale(background, true);
            courseBtn.setBackground(background);
        }
        courseBtn.setTextSize(8);
        courseBtn.setTag(Integer.toString(courseID));
        return courseBtn;
    }

    private RelativeLayout.LayoutParams setUpLayout(Context context,String position){
        Pair<Integer,Integer> xyPosition = nomalizPositioneString(context,position);

        int unit_width = (int)(((courseType != CourseType.QUIZ)? context.getResources().getInteger(R.integer.non_quiz_width) : context.getResources().getInteger(R.integer.quiz_width))*MainActivity.ScreenHeightRatio +0.5f);
        int unit_height = (int)(((courseType != CourseType.QUIZ)? context.getResources().getInteger(R.integer.non_quiz_height) : context.getResources().getInteger(R.integer.quiz_height))*MainActivity.ScreenHeightRatio +0.5f);
        int flag_y = (int)(((courseType != CourseType.QUIZ)? context.getResources().getInteger(R.integer.non_quiz_flag_y) : context.getResources().getInteger(R.integer.quiz_flag_y))*MainActivity.ScreenHeightRatio +0.5f);
        int flag_x = (int) (int) (unit_width * 0.05 + 0.5f);
        int flag_width = (int) (unit_width*0.9 + 0.5f);
        int flag_height = (int)(((courseType != CourseType.QUIZ)? context.getResources().getInteger(R.integer.non_quiz_flag_height) : context.getResources().getInteger(R.integer.quiz_flag_height))*MainActivity.ScreenHeightRatio +0.5f);


        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(unit_width,unit_height);
        rlp.setMargins(xyPosition.first,xyPosition.second + defaultMarginTop,0,0);
        titleLayoutParams = new RelativeLayout.LayoutParams(flag_width,flag_height);
        titleLayoutParams.setMargins(xyPosition.first + flag_x,xyPosition.second + defaultMarginTop + flag_y,0,0);
        return rlp;
    }


    private Pair<Integer,Integer> nomalizPositioneString(Context context,String position){
        String[] rowColNum = position.split("\\D");
        String direction = position.replaceAll("[0-9]","");

        double x = 0, y;
        if (courseType != CourseType.QUIZ){
            x = Integer.parseInt(rowColNum[1]) * (context.getResources().getInteger(R.integer.non_quiz_horizontal_space) +
                    context.getResources().getInteger(R.integer.non_quiz_width));
            if (direction.equals("L")){
                x += context.getResources().getInteger(R.integer.non_quiz_left_marginLeft);
            }else{
                x += context.getResources().getInteger(R.integer.non_quiz_right_marginLeft);
            }
        }else if (direction.equals("L") && rowColNum[1].equals("0")){
            x = context.getResources().getInteger(R.integer.quiz_left_first_marginLeft);
        }else if (direction.equals("L") && rowColNum[1].equals("1")){
            x = 0;
        }else if (direction.equals("R") && rowColNum[1].equals("0")){
            x = context.getResources().getInteger(R.integer.quiz_right_first_marginLeft);
        }else{
            x = context.getResources().getInteger(R.integer.quiz_right_second_marginLeft);
        }

        x = x * MainActivity.ScreenWidthRatio + 0.5f;

        y = context.getResources().getInteger(R.integer.non_quiz_height)* MainActivity.ScreenHeightRatio * Integer.parseInt(rowColNum[0]) +0.5f;
        if (courseType == CourseType.QUIZ){
            CourseActivity.marginTop = (int)y + context.getResources().getInteger(R.integer.quiz_height);
        }
        return  new Pair<>((int)x,(int)y);
    }



    protected void updateCourseBtn(Context context){
        if(courseStatus==CourseStatus.AVAILABLE){
            boundBtn.setBackground(background);
        }
    }

    public static Drawable setGrayscale(Drawable drawable, boolean bool) {
        if (bool){
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);

            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

            drawable.setColorFilter(filter);

            return drawable;
        }
        else{
            drawable.setColorFilter(null);
            return drawable;
        }
    }
}
