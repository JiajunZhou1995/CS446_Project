package com.mtr.codetrip.codetrip;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mtr.codetrip.codetrip.Object.QuestionPageFragment;
import com.mtr.codetrip.codetrip.Utility.ControlScrollViewPager;

import java.util.zip.Inflater;

/**
 * Created by Catrina on 2/4/2018.
 */

public class QuestionActivity extends FragmentActivity implements View.OnClickListener {



    public static int NUM_PAGES;
    public static int currentProgress;
    private static Context currentContext;
    public static QuestionActivity currentQuestionActivity;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    public static ControlScrollViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private ScreenSlidePagerAdapter mPagerAdapter;

    private static LinearLayout mLinearLayout;
    Button mButton;
    Button returnButton;


    public  static SeekBar progressBar;

    private PopupWindow mPopupWindow;

    private int courseID;
    private PopupWindow mPopupdim;

//    private List<Question> listofQuestion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentProgress = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        currentContext = this;
        currentQuestionActivity = this;

        Intent intent = getIntent();
        courseID = intent.getIntExtra("courseID",0);
        Log.d("courseID", Integer.toString(courseID));

        String sql = "SELECT * FROM course WHERE courseid =" + Integer.toString(courseID);
        Cursor c = MainActivity.myDB.rawQuery(sql,null);
        c.moveToFirst();


//        NUM_PAGES=1;


        NUM_PAGES = c.getInt(c.getColumnIndex("total"));
        progressBar = findViewById(R.id.question_progressbar);

        progressBar.setMax(NUM_PAGES*1000000);
//        progressBar.setMax(1000000);


        progressBar.setProgress(0);
        progressBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ControlScrollViewPager) findViewById(R.id.questionPager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(NUM_PAGES);

        mLinearLayout = (LinearLayout) findViewById(R.id.question_page_toolbar);
        returnButton = (Button) findViewById(R.id.retrun_button);
        returnButton.setOnClickListener(this);
        mButton = (Button) findViewById(R.id.hint_button);
        mButton.setOnClickListener(this);
    }

    
    public static void inflateCompletionPage(){
        LayoutInflater inflater = LayoutInflater.from(currentContext);
        ScrollView completionPage = (ScrollView) inflater.inflate(R.layout.activity_demo,null);

        PopupWindow hi = new PopupWindow(
                completionPage,
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );

        hi.showAtLocation(mLinearLayout, Gravity.CENTER,0,0);
    }

    public static boolean isLastQuestion(){
        return currentProgress == NUM_PAGES - 1;
    }

    
    public static void updateProgressBar(int progress){
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(QuestionActivity.progressBar,"progress",(progress-1)*1000000,progress*1000000);
        progressAnimator.setStartDelay(200);
        progressAnimator.setDuration(1000);
        progressAnimator.start();
        
        if (progress == NUM_PAGES){
            progressAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}
                @Override
                public void onAnimationEnd(Animator animation) { inflateCompletionPage(); }
                @Override
                public void onAnimationCancel(Animator animation) {}
                @Override
                public void onAnimationRepeat(Animator animation) {}
            });
        }

    }


    public static void onQuestionContinue(){
        currentProgress++;
        mPager.setCurrentItem(currentProgress);
        updateProgressBar(currentProgress);
//
//        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(QuestionActivity.progressBar,"progress",(QuestionActivity.currentProgress-1)*1000000,QuestionActivity.currentProgress*1000000);
//        progressAnimator.setStartDelay(200);
//        progressAnimator.setDuration(1000);
//        progressAnimator.start();
    }



    public static void backtocurrent(){
        mPager.setCurrentItem(currentProgress);
    }
    /**
     * A simple pager adapter that represents 5 {@link QuestionPageFragment} objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int currentQuestion) {
            return QuestionPageFragment.create(courseID,currentQuestion);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, final int position, Object object) {
            super.setPrimaryItem(container,position,object);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


//    private class ProgressBarAnimation extends Animation {
//        private ProgressBar progressBar;
//        private  float from;
//        private  float to;
//        private ProgressBarAnimation(SeekBar progressBar, float from, float to){
//            super();
//            this.progressBar = progressBar;
//            this.from = from;
//            this.to = to;
//        }
//        @Override
//        protected void applyTransformation(float interpolatedTime, Transformation transformation){
//            super.applyTransformation(interpolatedTime,transformation);
//            float progress = from + (to - from) * interpolatedTime;
//            progressBar.setProgress((int) (progress));
//        }
//    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.retrun_button){
//            if (id == R.id.retrun_button || id == R.id.question_complete_return_button){
            // save state
            this.finish();
        }else if (id == R.id.question_complete_return_button){
            Intent intent = new Intent();
            intent.setClass(this,DemoActivity.class);
            startActivity(intent);
        }else if(id == R.id.hint_button){
            // Initialize a new instance of LayoutInflater service
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

            // Inflate the custom layout/view
            View customView = inflater.inflate(R.layout.question_hint_popup,null);

            int index = mPager.getCurrentItem();

            String hintSql = "SELECT hint FROM question WHERE courseid =" + Integer.toString(courseID) + " AND questionid =" + Integer.toString(index);
            Cursor c = MainActivity.myDB.rawQuery(hintSql,null);
            c.moveToFirst();
            TextView hintTV = customView.findViewById(R.id.tv);
            hintTV.setText(c.getString(0));

            // Inflate the custom layout/view
            View customDimView = inflater.inflate(R.layout.question_hint_overlay,null);

            // Initialize a new instance of question_hint_overlay window
            mPopupdim = new PopupWindow(
                    customDimView,
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
            );

            // Initialize a new instance of popup window
            mPopupWindow = new PopupWindow(
                    customView,
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );

            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    // TODO Auto-generated method stub
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

}
