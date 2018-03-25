package com.mtr.codetrip.codetrip;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mtr.codetrip.codetrip.CostumWidgets.ColorArcProgressBar;
import com.mtr.codetrip.codetrip.CostumWidgets.NonClickableSeekbar;
import com.mtr.codetrip.codetrip.Object.Question;
import com.mtr.codetrip.codetrip.Object.QuestionPageFragment;
import com.mtr.codetrip.codetrip.Utility.ControlScrollViewPager;
import com.mtr.codetrip.codetrip.Utility.QuestionPicker;

import java.util.ArrayList;
import java.util.List;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

import static com.mtr.codetrip.codetrip.R.id.question_complete_return_button;
import static com.mtr.codetrip.codetrip.Utility.DataBaseUtility.getStrArrayFromDB;

/**
 * Created by Catrina on 2/4/2018 at 11:27 PM.
 * Within Package ${PACKAGE_NAME}
 */

public class QuestionActivity extends FragmentActivity implements View.OnClickListener {



    public int NUM_PAGES;
    public int currentProgress;
    private  QuestionActivity currentQuestionActivity;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ControlScrollViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private ScreenSlidePagerAdapter mPagerAdapter;

    private LinearLayout mLinearLayout;
    private NonClickableSeekbar progressBar;

    private int courseID;
    private PopupWindow mPopupdim;

    private float grade;
    private boolean isReview;
    private List<Integer> incorrectQuestionList;

    public QuestionPicker questionPicker;

    public List<String> topicList;
    private int MAX_PAGES;
    final int maxNumberOfQuetionPerTopic = 4;

//    private PopupWindow completePopView;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentProgress = 0;
        grade = 0;
        isReview = false;
        incorrectQuestionList = new ArrayList<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        currentQuestionActivity = this;

        Intent intent = getIntent();
        courseID = intent.getIntExtra("courseID",0);
        Log.d("courseID", Integer.toString(courseID));

        questionPicker = new QuestionPicker(this,courseID,maxNumberOfQuetionPerTopic);

        String course = "codetrip.db";
        SQLiteDatabase appDB = this.openOrCreateDatabase(course, Context.MODE_PRIVATE,null);


        String sql = String.format("SELECT * FROM course WHERE courseid=%d",courseID);
        Cursor cursor = appDB.rawQuery(sql, null);
        cursor.moveToFirst();

        topicList = getStrArrayFromDB(cursor, "topics");
        cursor.close();
        appDB.close();

        MAX_PAGES = topicList.size() * 4;


//        String sql = "SELECT * FROM course WHERE courseid =" + Integer.toString(courseID);
//        @SuppressLint("Recycle") Cursor c = MainActivity.appDB.rawQuery(sql,null);
//        c.moveToFirst();

        NUM_PAGES=1;
//        NUM_PAGES = c.getInt(c.getColumnIndex("total"));
        progressBar = findViewById(R.id.question_progressbar);

        progressBar.setMax(MAX_PAGES*1000000);

        progressBar.setProgress(0);
        progressBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });



        // Instantiate a ViewPager and a PagerAdapter.
        mPager = findViewById(R.id.questionPager);
        mPager.setBoundedQuestionActivity(currentQuestionActivity);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(MAX_PAGES);

        mLinearLayout = findViewById(R.id.question_page_toolbar);
        Button returnButton = findViewById(R.id.retrun_button);
        returnButton.setOnClickListener(this);
        Button hintButton = findViewById(R.id.hint_button);
        hintButton.setOnClickListener(this);
    }

    
    private void inflateCompletionPage(){

        // save to database

        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        @SuppressLint("InflateParams") RelativeLayout completionPage = (RelativeLayout) inflater.inflate(R.layout.question_complete_screen,null);


        RelativeLayout container = findViewById(R.id.question_root_container);
        container.addView(completionPage);

//        completePopView = new PopupWindow(
//                completionPage,
//                RelativeLayout.LayoutParams.MATCH_PARENT,
//                RelativeLayout.LayoutParams.MATCH_PARENT
//        );
//
//        completePopView.showAtLocation(mLinearLayout, Gravity.CENTER,0,0);

        ColorArcProgressBar bar2 = completionPage.findViewById(R.id.bar2);
        grade = (grade * 100) / NUM_PAGES;
        Log.i("grade",Float.toString(grade));
        Log.i("totoal",Integer.toString(NUM_PAGES));

        bar2.setCurrentValues(grade);

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int screenWidth = metric.widthPixels;

        KonfettiView konfettiView = completionPage.findViewById(R.id.kongfetti);
        konfettiView.build()
                .addColors(getBaseContext().getColor(R.color.colorLightBlue),
                        getBaseContext().getColor(R.color.colorLightGreen),
                        getBaseContext().getColor(R.color.colorLime100))
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(1000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(8, 5f))
                .setPosition(-50f, screenWidth+50f, -50f, -50f)
                .stream(300, 100000L);


        Button return_button = completionPage.findViewById(question_complete_return_button);
        return_button.setOnClickListener(currentQuestionActivity);

    }

//    public static boolean isLastQuestion(){
//        return currentProgress == NUM_PAGES - 1;
//    }

    public void changeGrade(){
        grade += 1;
    }

    private void updateProgressBar(){
        int currentProgress = questionPicker.currentProgress;
        int lastProgress = questionPicker.lastProgress;
//        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(progressBar,"progress",(progress-1)*1000000,progress*1000000);
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(progressBar,"progress",lastProgress*1000000,currentProgress*1000000);
        progressAnimator.setStartDelay(200);
        progressAnimator.setDuration(1000);
        progressAnimator.start();
    }

    public void notifyChange(){
        mPagerAdapter.notifyDataSetChanged();
    }

    public void onQuestionContinue(){
//        NUM_PAGES++;
//        currentProgress++;
//        questionPicker.ganerateNextQuestion(true);
//        Question currentSelectedQuestion = questionPicker.getCurrentQuestion();
//
//        if (currentSelectedQuestion==null){
//            inflateCompletionPage();
//            updateProgressBar(currentProgress);
//            return;
//        }
//
//        mPagerAdapter.notifyDataSetChanged();
//        qustionProgress++;

        if (currentProgress++ == NUM_PAGES - 1){
            inflateCompletionPage();
//            updateProgressBar();
            return;
        }
//        NUM_PAGES++;

        mPager.setCurrentItem(currentProgress);
        updateProgressBar();
    }



    public void backtocurrent(){
        mPager.setCurrentItem(currentProgress);
    }
    /**
     * A simple pager adapter that represents 5 {@link QuestionPageFragment} objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private QuestionPageFragment currentFragment;

        QuestionPageFragment getCurrentFragment(){
            return currentFragment;
        }

        Question getcurrentQuestion(){
            if (currentFragment != null){
                return currentFragment.getCurrentQuestion();
            }else{
                return null;
            }
        }

        ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int currentQuestion) {

//            if (currentQuestion < NUM_PAGES && NUM_PAGES!=1){
//                return this.setPrimaryItem();
//            }

            QuestionPageFragment questionPageFragment;
            Question currentSelectedQuestion = questionPicker.getCurrentQuestion();

            questionPageFragment = QuestionPageFragment.create();
            questionPageFragment.setCurrentQuestion(currentSelectedQuestion);
//            if(isReview) questionPageFragment = QuestionPageFragment.create(courseID,incorrectQuestionList.get(currentQuestion));
//            else questionPageFragment = QuestionPageFragment.create(courseID,currentQuestion);
            questionPageFragment.setCurrentQuestionActivity(currentQuestionActivity);
            return questionPageFragment;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, final int position, Object object) {
            if (getCurrentFragment() != object) {
                currentFragment = ((QuestionPageFragment) object);
            }
            super.setPrimaryItem(container,position,object);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.retrun_button){
            this.finish();
        }else if (id == question_complete_return_button){
            CourseActivity.currentCourse=courseID+1;
            CourseActivity.refreshCourseMAp();
            this.finish();
//            completePopView.dismiss();
        }else if(id == R.id.hint_button) inflateHintPopView();
    }

    private void inflateHintPopView(){
        String hint = mPagerAdapter.getcurrentQuestion().getHint();

        LayoutInflater inflater = LayoutInflater.from(this);
        // Inflate the custom layout/view
        @SuppressLint("InflateParams") View hintPopUp = inflater.inflate(R.layout.question_hint_popup,null);
        TextView hintTV = hintPopUp.findViewById(R.id.tv);
        hintTV.setText(hint);

        // Inflate the custom layout/view
        @SuppressLint("InflateParams") View hintBackgroundOverlay = inflater.inflate(R.layout.question_hint_overlay,null);

        // Initialize a new instance of question_hint_overlay window
        mPopupdim = new PopupWindow(
                hintBackgroundOverlay,
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );

        // Initialize a new instance of popup window
        PopupWindow mPopupWindow = new PopupWindow(
                hintPopUp,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                mPopupdim.dismiss();

            }
        });

        // Closes the popup window when touch outside.
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);

        // Removes default background.
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.BLACK));

        mPopupWindow.setElevation(6.0f);

        // Finally, show the popup window at the center location of root relative layout
        mPopupdim.showAtLocation(mLinearLayout, Gravity.CENTER,0,0);

        // Finally, show the popup window at the center location of root relative layout
        mPopupWindow.showAtLocation(mLinearLayout, Gravity.CENTER,0,0);
    }

}
