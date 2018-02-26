package com.mtr.codetrip.codetrip.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.mtr.codetrip.codetrip.DensityUtil;
import com.mtr.codetrip.codetrip.R;

/**
 * Created by Catrina on 25/02/2018.
 */

public class DropTextView extends android.support.v7.widget.AppCompatTextView {




    public enum DropState{DEFAULT, ENABLED, DROPPED};

    private DropState dropState;
    private Context context;


    private int STROKE_WIDTH;
    private int borderCol;
    private Paint borderPaint;


    public DropTextView(Context context){
        super(context);
//        this.setOnClickListener();
        this.context = context;
        STROKE_WIDTH = DensityUtil.dip2px(context,2);
        this.setBackgroundColor(context.getColor(R.color.colorWhite));
        this.setPadding(DensityUtil.dip2px(context,20),DensityUtil.dip2px(context,5),DensityUtil.dip2px(context,20),DensityUtil.dip2px(context,5));
//        this.setBackground(context.getDrawable(R.drawable.drop_block_round_default));
        this.setBackgroundColor(context.getColor(R.color.colorWhite));
        this.setTextSize(DensityUtil.dip2px(context,14));
        this.setTextAppearance(context,R.style.DropButtonStyle);


//        dropState = DropState.DEFAULT;
//
//        borderCol = context.getColor(R.color.colorLightGrey);
//        borderPaint = new Paint();
//        borderPaint.setStyle(Paint.Style.STROKE);
//        borderPaint.setStrokeWidth(STROKE_WIDTH);
//        borderPaint.setAntiAlias(true);
    }



//
//    @Override
//    protected void onDraw(Canvas canvas) {
//
//        if (0 == this.getText().toString().length())
//            return;
//
//        borderPaint.setColor(borderCol);
//
//        int width = this.getMeasuredWidth();
//        int height = this.getMeasuredHeight();
//
//        RectF r = new RectF(STROKE_WIDTH, STROKE_WIDTH, width - STROKE_WIDTH, height - STROKE_WIDTH);
//        canvas.drawRoundRect(r, DensityUtil.dip2px(context,2), DensityUtil.dip2px(context,2), borderPaint);
//        super.onDraw(canvas);
//    }


//    private void setBorderColor(int newColor) {
//        borderCol = newColor;
//        invalidate();
//        requestLayout();
//    }

    public void setDropState(DropState dropState){
        this.dropState = dropState;
        switch (dropState){
            case DEFAULT:
                this.setText("?");
                this.setBackground(context.getDrawable(R.drawable.drop_block_round_default));
//                setBorderColor(context.getColor(R.color.colorLightGrey));
                break;
            case ENABLED:
                this.setBackground(context.getDrawable(R.drawable.drop_block_round_enabled));

//                setBorderColor(context.getColor(R.color.colorDarkGrey));
                break;
            case DROPPED:
                this.setBackground(context.getDrawable(R.drawable.drop_block_round_dropped));

//                setBorderColor(context.getColor(R.color.colorOrange));
                break;
        }
    }
}
