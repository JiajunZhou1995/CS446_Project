package com.mtr.codetrip.codetrip.CostumWidgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.mtr.codetrip.codetrip.Object.Question;
import com.mtr.codetrip.codetrip.QuestionActivity;
import com.mtr.codetrip.codetrip.R;

/**
 * Created by Catrina on 26/02/2018.
 */

public class RunButton extends android.support.v7.widget.AppCompatButton implements View.OnClickListener {



    public enum RunButtonState{INVALID,RUN,CONTINUE,BACKTOCURRENT}

    private RunButtonState buttonState;
    private Context context;
    private Question currentQuestion;

    public RunButton(Context context) {
        super(context);
        this.context = context;
        this.setOnClickListener(this);
    }


    public RunButton(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        this.setOnClickListener(this);
    }

    public RunButton(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
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
                QuestionActivity.onQuestionFragmentSwipe(1);
                updateDoItButtonState(RunButtonState.BACKTOCURRENT);
                QuestionActivity.currentProgress += 1;
                break;
            case BACKTOCURRENT:
                QuestionActivity.backtocurrent();
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
