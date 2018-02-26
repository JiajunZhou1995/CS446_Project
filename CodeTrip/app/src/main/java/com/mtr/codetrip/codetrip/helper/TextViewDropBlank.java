package com.mtr.codetrip.codetrip.helper;

import android.content.Context;
import android.graphics.Paint;
import android.view.DragEvent;
import android.view.View;

import com.mtr.codetrip.codetrip.DensityUtil;
import com.mtr.codetrip.codetrip.Question;
import com.mtr.codetrip.codetrip.QuestionDragAndDrop;
import com.mtr.codetrip.codetrip.R;

/**
 * Created by Catrina on 25/02/2018.
 */

public class TextViewDropBlank extends android.support.v7.widget.AppCompatTextView implements View.OnClickListener, View.OnDragListener {

    public enum DropState{DEFAULT, ENABLED, DROPPED};
    private Question question;
    private Context context;

    public TextViewDropBlank(Context context, int tag, Question question){
        super(context);
        this.question = question;
        this.context = context;
        this.setTag(tag);
        this.setBackgroundColor(context.getColor(R.color.colorWhite));
        this.setPadding(DensityUtil.dip2px(context,20),DensityUtil.dip2px(context,5),DensityUtil.dip2px(context,20),DensityUtil.dip2px(context,5));
        this.setBackgroundColor(context.getColor(R.color.colorWhite));
        this.setTextSize(DensityUtil.dip2px(context,14));
        this.setTextAppearance(R.style.FontStyle_DropBlank);

        this.setOnClickListener(this);
        this.setOnDragListener(this);
    }


    public void updateDropState(DropState dropState){
        switch (dropState){
            case DEFAULT:
                this.setText("?");
                this.setBackground(context.getDrawable(R.drawable.drop_block_round_default));
                break;
            case ENABLED:
                this.setBackground(context.getDrawable(R.drawable.drop_block_round_enabled));
                break;
            case DROPPED:
                this.setBackground(context.getDrawable(R.drawable.drop_block_round_dropped));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        ((QuestionDragAndDrop)question).dropReceiveBlank.restore((TextViewDropBlank) v);
    }


    @Override
    public boolean onDrag(View v, DragEvent event) {
        TextViewDropBlank textView = (TextViewDropBlank) v;
        final int action = event.getAction();
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                textView.updateDropState(TextViewDropBlank.DropState.DEFAULT);
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                textView.updateDropState(TextViewDropBlank.DropState.ENABLED);
                break;
            case DragEvent.ACTION_DRAG_LOCATION:
                break;
            case DragEvent.ACTION_DROP:
                textView.updateDropState(TextViewDropBlank.DropState.DROPPED);
                textView.setText(((QuestionDragAndDrop)question).currentOnDragButtonText);
                ((QuestionDragAndDrop)question).dropReceiveBlank.updateFillIn((int)v.getTag(),((QuestionDragAndDrop)question).currenOnDragButton);
                break;
        }
        return true;
    }


}
