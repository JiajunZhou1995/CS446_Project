package com.mtr.codetrip.codetrip;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.util.Pair;
import android.view.View;
import android.widget.Button;


/**
 * Created by Catrina on 2/3/2018.
 */


enum CourseType{LECTURE, PROJECT, QUIZ}
enum CourseStatus{AVAILABLE, UNAVAILABLE}

public class Course extends Object  {

    int courseID;
//    Pair<Integer,Integer> position;
    CourseType courseType;
    CourseStatus courseStatus;
    Button boundBtn;

    public Course(int id){
        this.courseID = id;
    }

    public void setBoundBtn(Button button) {

        this.boundBtn = button;
//        boundBtn.setOnClickListener(this);

    }

}
