package com.mtr.codetrip.codetrip.CostumWidgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.mtr.codetrip.codetrip.R;

import java.io.InputStream;
import java.lang.reflect.Field;

/**
 * Created by Catrina on 3/25/2018 at 7:33 PM.
 * Within Package: com.mtr.codetrip.codetrip.CostumWidgets
 */

public class GifImageView extends android.support.v7.widget.AppCompatImageView {

    private Movie mMovie;
    private long mMovieStart = 0;

    @SuppressLint("ResourceType")
    public GifImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GifImageView);

        String gif_name_string = typedArray.getString(R.styleable.GifImageView_gif_pic_name);

        int drawableResourceId = this.getResources().getIdentifier(gif_name_string, "drawable", context.getPackageName());

        mMovie = Movie.decodeStream(getResources().openRawResource(drawableResourceId));
        typedArray.recycle();

    }


    @Override
    public void onDraw(Canvas canvas) {

        long now = android.os.SystemClock.uptimeMillis();

        if (mMovieStart == 0) {
            mMovieStart = now;
        }

        if (mMovie != null) {
            int dur = mMovie.duration();

            if (dur == 0) {
                dur = 1000;
            }

            int relTime = (int) ((now - mMovieStart) % dur);
            mMovie.setTime(relTime);
            mMovie.draw(canvas, 0, 0);

            invalidate();
        }
    }
}
