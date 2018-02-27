package com.mtr.codetrip.codetrip.CostumWidgets;

import android.content.Context;
import android.graphics.Typeface;

import com.mtr.codetrip.codetrip.R;
import com.mtr.codetrip.codetrip.Utility.DensityUtil;

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
