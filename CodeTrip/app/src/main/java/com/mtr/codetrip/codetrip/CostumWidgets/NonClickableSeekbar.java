package com.mtr.codetrip.codetrip.CostumWidgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Catrina on 3/13/2018 at 11:21 PM.
 * Within Package com.mtr.codetrip.codetrip.CostumWidgets
 */

public class NonClickableSeekbar extends android.support.v7.widget.AppCompatSeekBar {


    public NonClickableSeekbar(Context context) {
        super(context);
    }

    public NonClickableSeekbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NonClickableSeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean performClick() {
        // do what you want
        return true;
    }


}
