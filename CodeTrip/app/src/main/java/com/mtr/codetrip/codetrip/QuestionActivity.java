package com.mtr.codetrip.codetrip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Catrina on 2/4/2018.
 */

public class QuestionActivity extends FragmentActivity implements View.OnClickListener {



    private int NUM_PAGES = 0;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    private Context mContext;
    private Activity mActivity;

    private LinearLayout mLinearLayout;
    private Button mButton;
    private Button returnButton;

    private PopupWindow mPopupWindow;

    private int courseID;
    private PopupWindow mPopupdim;

//    private List<Question> listofQuestion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);


        Intent intent = getIntent();
        courseID = intent.getIntExtra("courseID",0);
        Log.d("courseID", Integer.toString(courseID));

        String sql = "SELECT * FROM course WHERE courseid =" + Integer.toString(courseID);
        Cursor c = MainActivity.myDB.rawQuery(sql,null);
        c.moveToFirst();
        NUM_PAGES = c.getInt(c.getColumnIndex("total"));

        Log.d("number of questions", Integer.toString(NUM_PAGES));

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.questionPager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
                invalidateOptionsMenu();
            }
        });


        // Get the activity
        mActivity = QuestionActivity.this;

        // Get the widgets reference from XML layout
        mLinearLayout = (LinearLayout) findViewById(R.id.rl);

        returnButton = (Button) findViewById(R.id.retrun_button);
        returnButton.setOnClickListener(this);
        mButton = (Button) findViewById(R.id.hint_button);

        // Set a click listener for the text view
        mButton.setOnClickListener(this);
    }

    public void onQuestionFragmentSwipe(int swipeGesture){
        mPager.setCurrentItem(mPager.getCurrentItem() + swipeGesture);
    }
    /**
     * A simple pager adapter that represents 5 {@link ScreenSlidePageFragment} objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int currentQuestion) {
            return ScreenSlidePageFragment.create(courseID,currentQuestion);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                // Navigate "up" the demo structure to the launchpad activity.
//                // See http://developer.android.com/design/patterns/navigation.html for more.
//                NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
//                return true;
//
//            case R.id.action_previous:
//                // Go to the previous step in the wizard. If there is no previous step,
//                // setCurrentItem will do nothing.
//                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
//                return true;
//
//            case R.id.action_next:
//                // Advance to the next step in the wizard. If there is no next step, setCurrentItem
//                // will do nothing.
//                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.retrun_button){
            // save state
            this.finish();
        }else if(id == R.id.hint_button){
            // Initialize a new instance of LayoutInflater service
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

            // Inflate the custom layout/view
            View customView = inflater.inflate(R.layout.hint,null);

            // Inflate the custom layout/view
            View customdimView = inflater.inflate(R.layout.dim,null);


            // Initialize a new instance of dim window
            mPopupdim = new PopupWindow(
                    customdimView,
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
            );


            // Initialize a new instance of popup window
            mPopupWindow = new PopupWindow(
                    customView,
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );

            // Set an elevation value for popup window
            if(Build.VERSION.SDK_INT>=21){
                mPopupdim.setElevation(5.0f);
            }

            // Set an elevation value for popup window
            if(Build.VERSION.SDK_INT>=21){
                mPopupWindow.setElevation(6.0f);
            }

            // Get a reference for the custom view close button
            ImageButton closeButton = (ImageButton) customView.findViewById(R.id.ib_close);

            // Set a click listener for the popup window close button
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Dismiss the popup window
                    mPopupWindow.dismiss();
                    // Dismiss the popup dim
                    mPopupdim.dismiss();
                }
            });

            // Finally, show the popup window at the center location of root relative layout
            mPopupdim.showAtLocation(mLinearLayout, Gravity.CENTER,0,0);

            // Finally, show the popup window at the center location of root relative layout
            mPopupWindow.showAtLocation(mLinearLayout, Gravity.CENTER,0,0);

        }
    }


}
