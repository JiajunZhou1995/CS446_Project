package com.mtr.codetrip.codetrip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Catrina on 27/02/2018.
 */

public class AchievementActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    GridView androidGridView;

    Integer[] imageIDs = {
            R.mipmap.beginner,
            R.mipmap.stars10,
            R.mipmap.stars50,
            R.mipmap.stars100,
            R.mipmap.traveler,
            R.mipmap.oneweek,
            R.mipmap.learningstreak,
            R.mipmap.bravetraveler,
            R.mipmap.traveler,
            R.mipmap.tourismexpert,
            R.mipmap.pythondiploma,
            R.mipmap.pythonking
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);



        LayoutInflater layoutInflater = LayoutInflater.from(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(1).setChecked(true);

        CoordinatorLayout container = (CoordinatorLayout) findViewById(R.id.app_bar_main);
        View child = layoutInflater.inflate(R.layout.content_achievement,null);
        container.addView(child);


        androidGridView = (GridView) findViewById(R.id.gridview_android_example);
        androidGridView.setAdapter(new AchievementActivity.ImageAdapterGridView(this));



        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {
                ArrayList<String> reward = new ArrayList<String>();
                reward.add("Beginner on the road");
                reward.add("Collected 10 stars");
                reward.add("Collected 50 stars");
                reward.add("Collected 100 stars");
                reward.add("Traveler");
                reward.add("One week goal");
                reward.add("Learning streak");
                reward.add("Brave Traveler");
                reward.add("Traveler");
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
            intent.setClass(this,MainActivity.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.sidebar_course) {
            intent.setClass(this,CourseActivity.class);
            startActivity(intent);
        } else if (id == R.id.sidebar_favorite) {
            intent.setClass(this,KeynoteActivity.class);
            startActivity(intent);
        } else if (id == R.id.sidebar_achievement) {

        } else if (id == R.id.sidebar_setting) {

        } else if (id == R.id.sidebar_about_us) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        drawer.closeDrawer(GravityCompat.START);
        if (id != R.id.sidebar_achievement  && id != R.id.sidebar_setting && id != R.id.sidebar_about_us)finish();

        return true;
    }


    public class ImageAdapterGridView extends BaseAdapter {
        private Context mContext;

        public ImageAdapterGridView(Context c) {
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
                mImageView.setPadding(70,70,70,70);
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                lp.setMargins(10,0,10,0);
//                mImageView.setLayoutParams(lp);
            } else {
                mImageView = (ImageView) convertView;
            }
            mImageView.setImageResource(imageIDs[position]);
            return mImageView;
        }
    }
}
