package com.mtr.codetrip.codetrip.Object;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mtr.codetrip.codetrip.CourseActivity;
import com.mtr.codetrip.codetrip.MainActivity;
import com.mtr.codetrip.codetrip.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Catrina on 2/3/2018 at 11:41 PM.
 * Within Package: ${PACKAGE_NAME}
 */


public class Course {

    public enum CourseType{LECTURE, PROJECT, QUIZ}
    public enum CourseStatus{AVAILABLE, UNAVAILABLE}

    public int courseID;
    public CourseType courseType;
    public CourseStatus courseStatus;
    public Button boundBtn;
    public RelativeLayout.LayoutParams buttonLayoutParams;
    public RelativeLayout.LayoutParams titleLayoutParams;
    private Drawable background;
    private int defaultMarginTop;
    public TextView courseTitle;
    public List<ImageView> stars;
    public List<RelativeLayout.LayoutParams> starsLayoutParams;
    public float course_score;
    private Context mContext;



    @SuppressLint("InflateParams")
    public Course(Context context, Cursor cursor, int margin_top, SharedPreferences prefs){

        mContext = context;

        defaultMarginTop = margin_top;

        courseID = cursor.getInt(cursor.getColumnIndex("courseid"));

        background = context.getDrawable(context.getResources().getIdentifier("course"+Integer.toString(courseID),"mipmap",context.getPackageName()));

        courseTitle = (TextView) LayoutInflater.from(context).inflate(R.layout.course_title,null);
        courseTitle.setText(cursor.getString(cursor.getColumnIndex("title")));
//        courseTitle.setClickable(false);

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

        boolean available = prefs.getBoolean(Integer.toString(courseID) + "Available", false);
        if (available){
            courseStatus = CourseStatus.AVAILABLE;
        }else{
            courseStatus = CourseStatus.UNAVAILABLE;
        }

        course_score = prefs.getFloat(Integer.toString(courseID) + "Score", 0);

        tmp = cursor.getString(cursor.getColumnIndex("position"));
        boundBtn = generateCourseButton(context);
        stars = generateStars(context);

        setStarsVisibility();
        buttonLayoutParams = setUpLayout(context,tmp);
    }

    public int getStars(SharedPreferences prefs){
        return prefs.getInt(Integer.toString(courseID) + "Star", 0);
    }

    public int calculateStars(){
        if (course_score>=100){
            return 3;
        }else if (course_score>=70){
            return 2;
        }else if (course_score>=50){
            return 1;
        }else {
            return 0;
        }
    }

    private void setStarsVisibility(){
        if (course_score>=100){
            stars.get(2).setVisibility(View.VISIBLE);
        }

        if (course_score>=70){
            stars.get(1).setVisibility(View.VISIBLE);
        }

        if (course_score>=50){
            stars.get(0).setVisibility(View.VISIBLE);
        }
    }

    public void updateScore(float newScore, SharedPreferences prefs){
        if (newScore>course_score){
            course_score = newScore;
            setStarsVisibility();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putFloat(Integer.toString(courseID) + "Score", newScore);
            editor.putInt(Integer.toString(courseID) + "Star", calculateStars());
//            editor.putBoolean(Integer.toString(courseID + 1) + "Available", true);
            editor.apply();

        }
    }

    private List<ImageView> generateStars(Context context){
        List<ImageView> starList = new ArrayList<>();
        for (int starIndex = 0; starIndex < 3; ++starIndex){
            ImageView star = new ImageView(context);
            star.setImageResource(R.mipmap.course_star);
            star.setVisibility(View.INVISIBLE);
            starList.add(star);
        }
        return starList;
    }

    public void updateBtn(){
        Drawable backgroundImg = boundBtn.getBackground();

        if (courseStatus==CourseStatus.AVAILABLE){
            setGrayscale(background, false);
            boundBtn.setBackground(backgroundImg);
        }else{
            setGrayscale(background, true);
            boundBtn.setBackground(background);
        }
    }

    private Button generateCourseButton(Context context){
        Button courseBtn = new Button(context);
        courseBtn.setStateListAnimator(null);

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

        int unit_width = (int)(((courseType != CourseType.QUIZ)? context.getResources().getInteger(R.integer.non_quiz_width) : context.getResources().getInteger(R.integer.quiz_width))* MainActivity.ScreenHeightRatio +0.5f);
        int unit_height = (int)(((courseType != CourseType.QUIZ)? context.getResources().getInteger(R.integer.non_quiz_height) : context.getResources().getInteger(R.integer.quiz_height))*MainActivity.ScreenHeightRatio +0.5f);
        int flag_y = (int)(((courseType != CourseType.QUIZ)? context.getResources().getInteger(R.integer.non_quiz_flag_y) : context.getResources().getInteger(R.integer.quiz_flag_y))*MainActivity.ScreenHeightRatio +0.5f);
        int flag_x = (int) (int) (unit_width * 0.05 + 0.5f);
        int flag_width = (int) (unit_width*0.9 + 0.5f);
        int flag_height = (int)(((courseType != CourseType.QUIZ)? context.getResources().getInteger(R.integer.non_quiz_flag_height) : context.getResources().getInteger(R.integer.quiz_flag_height))*MainActivity.ScreenHeightRatio +0.5f);


        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(unit_width,unit_height);
        rlp.setMargins(xyPosition.first,xyPosition.second + defaultMarginTop,0,0);
        titleLayoutParams = new RelativeLayout.LayoutParams(flag_width,flag_height);
        titleLayoutParams.setMargins(xyPosition.first + flag_x,xyPosition.second + defaultMarginTop + flag_y,0,0);

        starsLayoutParams = new ArrayList<>();
        for (int starLayoutParamsIndex = 0; starLayoutParamsIndex < 3; starLayoutParamsIndex++){
            RelativeLayout.LayoutParams starLayoutParams = new RelativeLayout.LayoutParams((int)(unit_width*0.3+0.5f),(int)(unit_width*0.3+0.5f));
            int x_offset = (starLayoutParamsIndex - 1)* (int)(unit_width*0.35+0.5f);
            int y_offset = (starLayoutParamsIndex==1)? 0: (-1)*(int)(unit_width*0.12+0.5f);
            starLayoutParams.setMargins(xyPosition.first + (int)(unit_width*0.35+0.5f) + x_offset, xyPosition.second + defaultMarginTop + (int)(unit_height*0.8+0.5f) + y_offset, 0,0);
            starsLayoutParams.add(starLayoutParams);
        }
        return rlp;
    }


    private Pair<Integer,Integer> nomalizPositioneString(Context context,String position){
        String[] rowColNum = position.split("\\D");
        String direction = position.replaceAll("[0-9]","");

        double x, y;
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



//    protected void updateCourseBtn(Context context){
//        if(courseStatus==CourseStatus.AVAILABLE){
//            setGrayscale(background,false);
//            boundBtn.setBackground(background);
//        }
//    }

    private static void setGrayscale(Drawable drawable, boolean bool) {
        if (bool){
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);

            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

            drawable.setColorFilter(filter);

        }
        else{
            drawable.setColorFilter(null);
        }
    }
}
