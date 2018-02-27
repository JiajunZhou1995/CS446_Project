package com.mtr.codetrip.codetrip.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mtr.codetrip.codetrip.Question;
import com.mtr.codetrip.codetrip.QuestionShortAnswer;
import com.mtr.codetrip.codetrip.R;

/**
 * Created by Catrina on 26/02/2018.
 */

public class RunButton extends android.support.v7.widget.AppCompatButton implements View.OnClickListener {



    public enum RunButtonState{INVALID,RUN,CONTINUE,BACKTOCURRENT}

    private RunButtonState buttonState;
    private Context context;
    public Question currentQuestion;
    public RunButton(Context context) {
        super(context);
//        updateDoItButtonState(RunButtonState.INVALID);
        this.context = context;
        this.setOnClickListener(this);

//        this.setId(R.id.question_run_button);
//
//        this.setAllCaps(false);
//        this.setTextColor(context.getColor(R.color.colorWhite));
//        this.setBackground(context.getDrawable(R.drawable.doit_button_invalid));
//
//
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DensityUtil.dip2px(context,360), LinearLayout.LayoutParams.WRAP_CONTENT);
//        lp.gravity = Gravity.CENTER_HORIZONTAL;
//        lp.setMargins(0,DensityUtil.dip2px(context,20),0,DensityUtil.dip2px(context,20));
//        this.setLayoutParams(lp);
    }


    public RunButton(Context context, AttributeSet attrs){
        super(context, attrs);
//        updateDoItButtonState(RunButtonState.INVALID);
        this.context = context;
        this.setOnClickListener(this);
    }

    public RunButton(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
//        updateDoItButtonState(RunButtonState.INVALID);
        this.context = context;
        this.setOnClickListener(this);
    }


    public void setQuestion(Question currentQuestion){
        this.currentQuestion = currentQuestion;
    }


    @Override
    public void onClick(View v) {
        switch (buttonState){
            case INVALID:
                break;
            case RUN:
                currentQuestion.runAction();
                updateDoItButtonState(RunButtonState.CONTINUE);
                break;
            case CONTINUE:
                break;
            case BACKTOCURRENT:
                break;
        }
    }

    public void updateDoItButtonState(RunButton.RunButtonState newState){
        buttonState = newState;
        switch(newState){
            case INVALID:
                setClickable(false);
                setBackground(context.getDrawable(R.drawable.doit_button_invalid));
                setText(context.getString(R.string.question_action_doit));
                break;
            case RUN:
                setClickable(true);
                setBackground(context.getDrawable(R.drawable.doit_button_run));
                setText(context.getString(R.string.question_action_run));
                break;
            case CONTINUE:
                setClickable(true);
                setBackground(context.getDrawable(R.drawable.doit_button_continue));
                setText(context.getString(R.string.question_action_continue));
                break;
            case BACKTOCURRENT:
                setClickable(true);
                setBackground(context.getDrawable(R.drawable.doit_button_backtocurrent));
                setText(context.getString(R.string.question_action_backtocurrent));
                break;
        }
    }
}
