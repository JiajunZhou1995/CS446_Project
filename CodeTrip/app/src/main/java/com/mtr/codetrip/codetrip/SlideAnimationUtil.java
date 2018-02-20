package com.mtr.codetrip.codetrip;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;

/**
 * Created by Catrina on 2/18/2018.
 */

public class SlideAnimationUtil {

    public static void slideInFromLeft(Context context, View view){
        runAnimation(context,view,R.anim.slide_from_left);
    }

    public static void slideInFromRight(Context context, View view){
        runAnimation(context,view,R.anim.slide_from_right);
    }

    public static void slideOutToLeft(Context context, View view){
        runAnimation(context,view,R.anim.slide_to_left);
    }

    public static void slideOutToRight(Context context, View view){
        runAnimation(context,view,R.anim.slide_to_right);
    }

    private static void runAnimation(Context context, View view, int animationId){
        view.startAnimation(AnimationUtils.loadAnimation(context,animationId));
    }
}
