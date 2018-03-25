package com.mtr.codetrip.codetrip;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.json.JSONObject;
import com.mtr.codetrip.codetrip.Utility.AsyncResponse;
import com.mtr.codetrip.codetrip.Utility.HttpPostAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AsyncResponse, View.OnClickListener {


    public static double ScreenWidthRatio;
    public static double ScreenHeightRatio;


    ObjectAnimator animator_course;
    ObjectAnimator animator_keynotes;
    ObjectAnimator animator_achievement;

    MainActivity me;

//    public static SQLiteDatabase appDB;
//    private Resources mResources;
//    String APIrespones = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Runnable mRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    initDB();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(mRunnable);
        thread.run();


        me = this;


        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        int height = metric.heightPixels;   // 屏幕高度（像素）
        double ratioWidthHeight = width/(height+0.0);


        double uiScreenHeight = getResources().getInteger(R.integer.ui_design_screen_width) / ratioWidthHeight;
        ScreenWidthRatio = width/(getResources().getInteger(R.integer.ui_design_screen_width)+0.0);

        ScreenHeightRatio = height/uiScreenHeight;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);


        CoordinatorLayout container = findViewById(R.id.app_bar_main);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        @SuppressLint("InflateParams") View child = layoutInflater.inflate(R.layout.content_home,null);
        container.addView(child);


//        initDB();

        // Make Http request
//        HttpPostAsyncTask request = new HttpPostAsyncTask("print(\"Hello World\")");
//        request.delegate = this;
//        request.execute();

        initHomeScreenButtonListener();
    }

    private void initHomeScreenButtonListener(){


        Button course_button = findViewById(R.id.home_navigation_course);
        course_button.setOnClickListener(this);

        Button keynotes_button = findViewById(R.id.home_navigation_keynotes);
        keynotes_button.setOnClickListener(this);
        Button achievement_button = findViewById(R.id.home_navigation_achievement);
        achievement_button.setOnClickListener(this);


        animator_course = ObjectAnimator.ofFloat(course_button, "translationX", 1700);
        animator_course.setDuration(1000);
        animator_keynotes = ObjectAnimator.ofFloat(keynotes_button, "translationX", 1700);
        animator_keynotes.setDuration(1000);
        animator_achievement = ObjectAnimator.ofFloat(achievement_button, "translationX", 1700);
        animator_achievement.setDuration(1000);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
//        Intent intent = new Intent();

        switch (id){
            case R.id.home_navigation_course:
                animator_achievement.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        animator_keynotes.start();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Intent intent = new Intent();
                        intent.setClass(me,CourseActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) { }

                    @Override
                    public void onAnimationRepeat(Animator animation) { }
                });
                animator_keynotes.setStartDelay(250);
                animator_achievement.start();
                break;
            case R.id.home_navigation_keynotes:
                animator_course.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        animator_achievement.start();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Intent intent = new Intent();
                        intent.setClass(me,KeynoteActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) { }

                    @Override
                    public void onAnimationRepeat(Animator animation) { }
                });
                animator_achievement.setStartDelay(250);
                animator_course.start();
                break;
            case R.id.home_navigation_achievement:
                animator_course.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        animator_keynotes.start();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Intent intent = new Intent();
                        intent.setClass(me,AchievementActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) { }

                    @Override
                    public void onAnimationRepeat(Animator animation) { }
                });
                animator_keynotes.setStartDelay(250);
                animator_course.start();
                break;
        }
//        finish();
    }




    // After Http request
    @Override
    public void processFinish(String output){
        Log.d("Http Request result", output);
    }

    public void initDB(){
        String course = "codetrip.db";
        MyDatabaseUtil myDatabaseUtil = new MyDatabaseUtil(this, course, null, 1);
        SQLiteDatabase appDB = this.openOrCreateDatabase(course,Context.MODE_PRIVATE,null);

        appDB.execSQL("DROP TABLE IF EXISTS course");

        appDB.execSQL("CREATE TABLE IF NOT EXISTS course " +
                    "(courseid integer primary key," +                  //1
                    "available text not null," +                        //true
                    "unit text not null," +                             //1
                    "type text not null," +                             //lecture
                    "title text not null," +                            //print
                    "position text not null," +                         //L1 -> left 1
                    "complete integer not null," +                      //0 -> 0 question completed
                    "total interger not null," +                        //1
                    "topics text not null)");                           //7 -> 7 question in this course

        appDB.execSQL("DROP TABLE IF EXISTS question");

        appDB.execSQL("CREATE TABLE IF NOT EXISTS question " +
                    "(questionid integer," +
                    "courseid interger," +
                    "difficulty text," +
                    "topic text," +
                    "answer text," +
                    "type text," +
                    "knowledge text," +
                    "instruction text," +
                    "code text," +
                    "console integer," +
                    "codeblock text," +
                    "choice text," +
                    "hint text," +
                    "FOREIGN KEY (courseid) REFERENCES course (courseid)," +
                    "PRIMARY KEY (questionid, courseid))");

        try {
            //if (!myDatabaseUtil.tableIsExist("course") || !myDatabaseUtil.tableIsExist("question")){
                readDataToDb(appDB);
            //}

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

//        @SuppressLint("Recycle") Cursor c = appDB.rawQuery("select * from course", null);

//        c.moveToFirst();
//        while(!c.isAfterLast()){
//            int index = c.getColumnIndex("courseid");
//            c.moveToNext();
//        }
        appDB.close();
    }

    private void readDataToDb(SQLiteDatabase db) throws IOException, JSONException {

        try {
            String jsonDataString = readJsonDataFromFile();
            JSONArray courseArray = new JSONArray(jsonDataString);

            int courseid = 0;
            String topics;
            String title;
            String coursetype;
            String position;
            int complete;
            int total;
            String available;

            String unit;

            for (int i = 0; i < courseArray.length(); ++i) {

                JSONObject courseObject = courseArray.getJSONObject(i);

                available = courseObject.getString("Available");
                unit = courseObject.getString("Unit");
                coursetype = courseObject.getString("Type");
                title = courseObject.getString("Title");
                total = courseObject.getInt("Total");
                position = courseObject.getString("Position");
                topics = courseObject.getJSONArray("Topics").toString();
                complete = 0;

                ContentValues courseValues = new ContentValues();

                courseValues.put("courseid", courseid);
                courseValues.put("available", available);
                courseValues.put("unit", unit);
                courseValues.put("type", coursetype);
                courseValues.put("title", title);
                courseValues.put("total", total);
                courseValues.put("position", position);
                courseValues.put("topics", topics);
                courseValues.put("complete", complete);

                db.insert("course", null, courseValues);

                JSONArray questionArray = new JSONArray(courseObject.getString("Question"));

                int questionid = 0;
                String difficulty;
                String topic;
                String answer;
                String questiontype;
                String knowledge;
                String instruction;
                String code;
                int console;
                String codeblock;
                String choice;
                String hint;

                for (int j = 0; j < questionArray.length(); ++j) {

                    JSONObject question = questionArray.getJSONObject(j);

                    difficulty = question.getString("Difficulty");
                    topic = question.getString("Topic");
                    answer = question.getString("Answer");
                    questiontype = question.getString("Type");
                    knowledge = question.getString("Knowledge");
                    instruction = question.getString("Instruction");
                    code = question.getJSONArray("Code").toString();
                    console = question.getInt("Console");
                    codeblock = question.getJSONArray("CodeBlock").toString();
                    choice = question.getJSONArray("Choice").toString();
                    hint = question.getString("Hint");

                    ContentValues questionValues = new ContentValues();

                    questionValues.put("questionid", questionid);
                    questionValues.put("courseid", courseid);
                    questionValues.put("difficulty", difficulty);
                    questionValues.put("topic", topic);
                    questionValues.put("answer", answer);
                    questionValues.put("type", questiontype);
                    questionValues.put("knowledge", knowledge);
                    questionValues.put("instruction", instruction);
                    questionValues.put("code", code);
                    questionValues.put("console", console);
                    questionValues.put("codeblock", codeblock);
                    questionValues.put("choice", choice);
                    questionValues.put("hint", hint);

                    db.insert("question", null, questionValues);

                    questionid = questionid + 1;
//                    Log.d("+++++", "Inserted Successfully " + questionValues );

                }

                courseid = courseid + 1;
            }


        } catch (JSONException e) {
            Log.e("ERROR", e.getMessage(),e);
            e.printStackTrace();
        }
    }

    private String readJsonDataFromFile() throws IOException {

        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();

        try {
            String jsonDataString;
            inputStream = getResources().openRawResource(
                    getResources().getIdentifier("data",
                            "raw", getPackageName()));
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, "UTF-8"));
            while ((jsonDataString = bufferedReader.readLine()) != null) {
                builder.append(jsonDataString);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return new String(builder);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent = new Intent();

        if (id == R.id.sidebar_home) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
            drawer.closeDrawer(GravityCompat.START,true);
            return true;
        } else if (id == R.id.sidebar_course) {
            intent.setClass(this,CourseActivity.class);
//            startActivity(intent);
        } else if (id == R.id.sidebar_favorite) {
            intent.setClass(this,KeynoteActivity.class);
//            startActivity(intent);
        } else if (id == R.id.sidebar_achievement) {
            intent.setClass(this,AchievementActivity.class);
//            startActivity(intent);
        } else if (id == R.id.sidebar_setting) {

        } else if (id == R.id.sidebar_about_us) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
        drawer.closeDrawer(GravityCompat.START,true);
        startActivity(intent);

        if (id != R.id.sidebar_home && id != R.id.sidebar_setting && id != R.id.sidebar_about_us)finish();
        return true;
    }



    public class MyDatabaseUtil extends SQLiteOpenHelper{
        public MyDatabaseUtil(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
            super(context,name,factory,version);
        }

        @Override
        public void onCreate(SQLiteDatabase arg0){

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }

        public boolean tableIsExist(String tableName){
            boolean result = false;
            if(tableName==null) return result;
            SQLiteDatabase db = null;
            Cursor cursor = null;
            try{
                db = this.getReadableDatabase();
                String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"+tableName.trim()+"' ";
                cursor = db.rawQuery(sql,null);
                if(cursor.moveToNext()){
                    int count = cursor.getInt(0);
                    if (count > 0) result = true;
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if (cursor!=null){
                    cursor.close();
                }
            }
            db.close();
            return result;
        }
    }
}
