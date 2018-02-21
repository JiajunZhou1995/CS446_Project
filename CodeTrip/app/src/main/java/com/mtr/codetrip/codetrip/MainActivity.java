package com.mtr.codetrip.codetrip;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SQLiteDatabase myDB;
    private MyDatabaseUtil myDatabaseUtil;
    private Resources mResources;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        System.out.println("before creating.....");
        initDB();
    }


    public void initDB(){
        String course = "course.db";
        myDatabaseUtil = new MyDatabaseUtil(this, course,null,1);
        myDB = this.openOrCreateDatabase(course,Context.MODE_PRIVATE,null);

        if(!myDatabaseUtil.tableIsExist("course")){
            myDB.execSQL("CREATE TABLE course " +
                    "(courseid integer primary key autoincrement," +    //1
                    "title text not null," +                            //print
                    "type text not null," +                             //lecture
                    "position text not null," +                         //L1 -> left 1
                    "complete integer not null," +                      //0 -> 0 question completed
                    "total interger not null)");                        //7 -> 7 question in this course
        }else{
            Log.i("+++++","already exist");
        }

        //String question = "question.db";

        if(!myDatabaseUtil.tableIsExist("question")){
            myDB.execSQL("CREATE TABLE question " +
                    "(questionid integer primary key autoincrement," +
                    "knowledge text," +
                    "instruction text," +
                    "code text," +
                    "console integer," +
                    "total text," +
                    "choice text," +
                    "hint text," +
                    "FOREIGN KEY(courseid) REFERENCES course (courseid))");
        }else{
            Log.i("+++++", "already exist");
        }

        try {
            readDataToDb(myDB);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*更新数据库*/
//        ContentValues values =new ContentValues();
//        values.put("name", "wxl");
//        myDB.update("person", values, "_id=1", null);
//        myDB.update("person", values, "_id=?", new String[]{"5"});
//
//        /*查询数据*/
//        Cursor c = myDB.query("person", null, null, null, null, null, null);
//        c.moveToFirst();
//        while(!c.isAfterLast()){
//            int index = c.getColumnIndex("name");
//            Log.d("SQLite", c.getString(index));
//            c.moveToNext();
//        }
//
//        c = myDB.rawQuery("select * from person", null);
//        c.moveToFirst();
//        while(!c.isAfterLast()){
//            int index = c.getColumnIndex("name");
//            Log.d("SQLite", c.getString(index));
//            c.moveToNext();
//        }
    }

    private void readDataToDb(SQLiteDatabase db) throws IOException, JSONException {

        try {
            String jsonDataString = readJsonDataFromFile();
            JSONArray courseArray = new JSONArray(jsonDataString);

            int courseid = 0;
            String title;
            String type;
            String position;
            int complete;
            int total;

            int questionid = 0;
            String knowledge;
            String instruction;
            String code;
            int console;
            String codeblock;
            String choice;
            String hint;


            for (int i = 0; i < courseArray.length(); ++i) {

                JSONObject courseObject = courseArray.getJSONObject(i);

                title = courseObject.getString("Title");
                type = courseObject.getString("Type");
                position = courseObject.getString("Position");
                complete = 0;
                total = courseObject.getInt("Total");

                ContentValues courseValues = new ContentValues();

                courseValues.put("courseid", courseid);
                courseValues.put("title", title);
                courseValues.put("type", type);
                courseValues.put("position", position);
                courseValues.put("complete", complete);
                courseValues.put("total", total);

                db.insert("course", null, courseValues);

                courseid++;
                Log.d("+++++", "Inserted Successfully " + courseValues );

                JSONArray questionArray = new JSONArray(courseObject.getString("Question"));

                for (int j = 0; j < questionArray.length(); ++j) {

                    JSONObject question = questionArray.getJSONObject(j);

                    type = question.getString("Type");
                    knowledge = question.getString("Knowledge");
                    instruction = question.getString("Instruction");
                    code = question.getString("Code");
                    console = question.getInt("Console");
                    codeblock = question.getString("CodeBlock");
                    choice = question.getString("Choice");
                    hint = question.getString("Hint");

                    ContentValues questionValues = new ContentValues();

                    questionValues.put("questionid", questionid);
                    questionValues.put("courseid", courseid);
                    questionValues.put("type", type);
                    questionValues.put("knowledge", knowledge);
                    questionValues.put("instruction", instruction);
                    questionValues.put("code", code);
                    questionValues.put("console", console);
                    questionValues.put("codeblock", codeblock);
                    questionValues.put("choice", choice);
                    questionValues.put("hint", hint);

                    db.insert("question", null, questionValues);

                    questionid++;

                    Log.d("+++++", "Inserted Successfully " + questionValues );

                }
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
            String jsonDataString = null;
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent = new Intent();

        if (id == R.id.sidebar_home) {
            intent.setClass(this,HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.sidebar_course) {
            intent.setClass(this,CourseActivity.class);
            startActivity(intent);
        } else if (id == R.id.sidebar_favorite) {
            intent.setClass(this,FavoriteActivity.class);
            startActivity(intent);
        } else if (id == R.id.sidebar_achievement) {

        } else if (id == R.id.sidebar_setting) {

        } else if (id == R.id.sidebar_about_us) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        drawer.closeDrawer(GravityCompat.START);
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

            }
            return result;
        }
    }
}
