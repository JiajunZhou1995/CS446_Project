package com.mtr.codetrip.codetrip.CostumWidgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;

import com.mtr.codetrip.codetrip.R;
import com.mtr.codetrip.codetrip.Utility.DensityUtil;

/**
 * Created by Catrina on 2/26/2018 at 11:40 PM.
 * Within Package: ${PACKAGE_NAME}
 */

@SuppressLint("ViewConstructor")
public class TextViewNormalCode extends AppCompatTextView {

    public TextViewNormalCode(Context context, String normalCode){
        super(context);

        this.setPadding(DensityUtil.dip2px(context,3),
                DensityUtil.dip2px(context,5),
                DensityUtil.dip2px(context,3),
                DensityUtil.dip2px(context,5));
        this.setTextAppearance(R.style.FontStyle_NormalCode);
        this.setTypeface(this.getTypeface(), Typeface.BOLD);
        this.setText(normalCode);
        this.setMaxLines(1);
    }
}
