package com.mtr.codetrip.codetrip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Catrina on 27/02/2018 at 12:34 AM.
 * Within Package: ${PACKAGE_NAME}
 */

public class AchievementActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    GridView androidGridView;
    private static SharedPreferences prefs;

    Integer[] imageIDs = {
            R.mipmap.achievement_beginner,
            R.mipmap.achievement_stars3,
            R.mipmap.achievement_stars10,
            R.mipmap.achievement_stars100,
            R.mipmap.achievement_traveler,
            R.mipmap.achievement_oneweek,
            R.mipmap.achievement_questions100,
            R.mipmap.achievement_learningstreak,
            R.mipmap.achievement_bravetraveler,
            R.mipmap.achievement_tourismexpert,
            R.mipmap.achievement_pythondiploma,
            R.mipmap.achievement_pythonking
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        LayoutInflater layoutInflater = LayoutInflater.from(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(3).setChecked(true);

        CoordinatorLayout container = findViewById(R.id.app_bar_main);
        @SuppressLint("InflateParams") View child = layoutInflater.inflate(R.layout.content_achievement, null);
        container.addView(child);

        prefs = this.getSharedPreferences(
                getString(R.string.course_file_key), Context.MODE_PRIVATE);

        androidGridView = findViewById(R.id.gridview_android_example);
        androidGridView.setAdapter(new AchievementActivity.ImageAdapterGridView(this));

        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {
                ArrayList<String> reward = new ArrayList<>();
                reward.add("Beginner on the road");
                reward.add("Collected 3 stars");
                reward.add("Collected 10 stars");
                reward.add("Collected 100 stars");
                reward.add("Traveller");
                reward.add("One week goal");
                reward.add("Answer 100 question");
                reward.add("Learning streak");
                reward.add("Brave Traveller");
                reward.add("Tourism Expert");
                reward.add("Python Diploma");
                reward.add("Python King");
                Toast.makeText(getBaseContext(), reward.get(position), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent = new Intent();

        if (id == R.id.sidebar_home) {
            intent.setClass(this, MainActivity.class);
//            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.sidebar_course) {
            intent.setClass(this, CourseActivity.class);
//            startActivity(intent);
        } else if (id == R.id.sidebar_favorite) {
            intent.setClass(this, KeynoteActivity.class);
//            startActivity(intent);
        }else if (id == R.id.sidebar_achievement) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
            drawer.closeDrawer(GravityCompat.START,true);
            return true;
//        }else if (id == R.id.sidebar_setting) {

        }else if (id == R.id.sidebar_about_us) {
            intent.setClass(this,AboutUsActivity.class);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
        drawer.closeDrawer(GravityCompat.START,true);
        startActivity(intent);

//        if (id != R.id.sidebar_setting )finish();

        return true;
    }


    public class ImageAdapterGridView extends BaseAdapter {
        private Context mContext;

        ImageAdapterGridView(Context c) {
            mContext = c;
        }

        public int getCount() {
            return imageIDs.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView mImageView;

            if (convertView == null) {
                mImageView = new ImageView(mContext);
                mImageView.setLayoutParams(new GridView.LayoutParams(350, 350));
                mImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                mImageView.setPadding(90, 70, 90, 70);
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                lp.setMargins(10,0,10,0);
//                mImageView.setLayoutParams(lp);
            } else {
                mImageView = (ImageView) convertView;
            }
            mImageView.setImageResource(imageIDs[position]);
            switch(position){
                case 0:
                    if (prefs.getBoolean("1Available", false)){
                        setGrayscale(mImageView.getDrawable(), false);
                    }
                    else{
                        setGrayscale(mImageView.getDrawable(), true);
                    }
                    break;
                case 1:
                    if(prefs.getInt("Total_Stars", 0) >= 3){
                        setGrayscale(mImageView.getDrawable(), false);
                    }
                    else{
                        setGrayscale(mImageView.getDrawable(), true);
                    }
                    break;
                case 2:
                    if(prefs.getInt("Total_Stars", 0) >= 10){
                        setGrayscale(mImageView.getDrawable(), false);
                    }
                    else{
                        setGrayscale(mImageView.getDrawable(), true);
                    }
                    break;
                default:
                    setGrayscale(mImageView.getDrawable(), true);

            }
            return mImageView;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_main);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            Intent intent = new Intent();
            intent.setClass(this,MainActivity.class);
            startActivity(intent);
            finish();

        }
    }

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

//    protected void lightUpAchievements(){
//        if (prefs.getInt("Total_Stars", 0) >= 5){
//            Object o = androidGridView.getItemAtPosition(0);
//            if (o instanceof ImageView){
//                Log.d("object", "true");
//            }
//        }
//    }
}
