package com.mtr.codetrip.codetrip.helper;

import android.content.Context;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mtr.codetrip.codetrip.DensityUtil;
import com.mtr.codetrip.codetrip.R;

/**
 * Created by Catrina on 2/26/2018.
 */

public class TextViewLineNumber extends android.support.v7.widget.AppCompatTextView {

    public TextViewLineNumber(Context context, String lineNumber){
        super(context);

        this.setPadding(DensityUtil.dip2px(context,0),
                        DensityUtil.dip2px(context,5),
                        DensityUtil.dip2px(context,3),
                        DensityUtil.dip2px(context,5));
        this.setTextAppearance(R.style.FontStyle_LineNumber);
        this.setTypeface(this.getTypeface(), Typeface.BOLD);
        this.setText(lineNumber);
    }

}
