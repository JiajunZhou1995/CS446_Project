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
import android.util.DisplayMetrics;
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

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

/**
 * Created by Catrina on 2/4/2018.
 */

public class QuestionActivity extends FragmentActivity implements View.OnClickListener {



    public static int NUM_PAGES;
    public static int currentProgress;
    private static Context currentContext;
    public static QuestionActivity currentQuestionActivity;

    private static ColorArcProgressBar bar2;

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

    private static int screenWidth;

//    private List<Question> listofQuestion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentProgress = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        currentContext = this;
        currentQuestionActivity = this;

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenWidth = metric.widthPixels;

        Intent intent = getIntent();
        courseID = intent.getIntExtra("courseID",0);
        Log.d("courseID", Integer.toString(courseID));

        String sql = "SELECT * FROM course WHERE courseid =" + Integer.toString(courseID);
        Cursor c = MainActivity.myDB.rawQuery(sql,null);
        c.moveToFirst();


        NUM_PAGES=1;
//        NUM_PAGES = c.getInt(c.getColumnIndex("total"));
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

        // save to database

        LayoutInflater inflater = LayoutInflater.from(currentContext);
        LinearLayout completionPage = (LinearLayout) inflater.inflate(R.layout.question_complete_screen,null);

        PopupWindow hi = new PopupWindow(
                completionPage,
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );

        hi.showAtLocation(mLinearLayout, Gravity.CENTER,0,0);

//        Button start_button = (Button) completionPage.findViewById(R.id.start_anim);
        bar2 = (ColorArcProgressBar) completionPage.findViewById(R.id.bar2);

        bar2.setCurrentValues(88.52f);



        final KonfettiView konfettiView = (KonfettiView) completionPage.findViewById(R.id.kongfetti);
        konfettiView.build()
                .addColors(currentContext.getColor(R.color.colorLightBlue),
                        currentContext.getColor(R.color.colorLightGreen),
                        currentContext.getColor(R.color.colorLime100))
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(1000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(8, 5f))
                .setPosition(-50f, screenWidth+50f, -50f, -50f)
                .stream(300, 100000L);


        Button return_button = (Button) completionPage.findViewById(R.id.question_complete_return_button);
        return_button.setOnClickListener(currentQuestionActivity);

    }

    public static boolean isLastQuestion(){
        return currentProgress == NUM_PAGES - 1;
    }

    
    public static void updateProgressBar(int progress){
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(QuestionActivity.progressBar,"progress",(progress-1)*1000000,progress*1000000);
        progressAnimator.setStartDelay(200);
        progressAnimator.setDuration(1000);
        progressAnimator.start();
        
//        if (progress == NUM_PAGES){
//            progressAnimator.addListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animation) {}
//                @Override
//                public void onAnimationEnd(Animator animation) { inflateCompletionPage(); }
//                @Override
//                public void onAnimationCancel(Animator animation) {}
//                @Override
//                public void onAnimationRepeat(Animator animation) {}
//            });
//        }

    }


    public static void onQuestionContinue(){
        if (currentProgress++ == NUM_PAGES - 1){
            inflateCompletionPage();
            updateProgressBar(currentProgress);
            return;
        }
        mPager.setCurrentItem(currentProgress);
        updateProgressBar(currentProgress);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.retrun_button){
//            if (id == R.id.retrun_button || id == R.id.question_complete_return_button){
            // save state
            this.finish();
        }else if (id == R.id.question_complete_return_button){
            this.finish();
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
