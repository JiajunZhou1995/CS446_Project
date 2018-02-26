package com.mtr.codetrip.codetrip.helper;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mtr.codetrip.codetrip.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Catrina on 25/02/2018.
 */

public class FillInTheBlank {

    enum DoItButtonState{INVALID,RUN,CONTINUE,BACKTOCURRENT}
    private Context context;
    private Button doitButton;
    private DoItButtonState doitButtonState;
    private List<Button> blankSpaceList;

    public FillInTheBlank(Context context, Button button){
        this.context = context;
        this.doitButton = button;
        updateDoItButtonState(DoItButtonState.INVALID);
        blankSpaceList = new ArrayList<>();
    }

    public void  restore(DropTextView currentTextView){
        int index = (int) currentTextView.getTag();
        Button currButton = blankSpaceList.get(index);
        if (currButton!=null) currButton.setVisibility(View.VISIBLE);
        blankSpaceList.set(index,null);
        //set textview to default
        currentTextView.setDropState(DropTextView.DropState.DEFAULT);

        //update doit button
        for (Button b : blankSpaceList){
            if (b!=null) return;
        }
        updateDoItButtonState(DoItButtonState.INVALID);
    }

    public void addEntry(){
        blankSpaceList.add(null);
    }

    public void updateFillIn(int position, Button newButton){
        Button oldButton = blankSpaceList.get(position);
        if (oldButton!=null) oldButton.setVisibility(View.VISIBLE);
        blankSpaceList.set(position,newButton);

        // update doit button
        if (!blankSpaceList.contains(null)){
            Log.d("++++++CAN","RUN");
            updateDoItButtonState(DoItButtonState.RUN);
        }
    }

    private void updateDoItButtonState(DoItButtonState newState){
        doitButtonState = newState;
        switch(newState){
            case INVALID:
                doitButton.setClickable(false);
                doitButton.setBackground(context.getDrawable(R.drawable.doit_button_invalid));
                doitButton.setText(context.getString(R.string.question_action_doit));
                break;
            case RUN:
                doitButton.setClickable(true);
                doitButton.setBackground(context.getDrawable(R.drawable.doit_button_run));
                doitButton.setText(context.getString(R.string.question_action_run));
                break;
            case CONTINUE:
                doitButton.setClickable(true);
                doitButton.setBackground(context.getDrawable(R.drawable.doit_button_continue));
                doitButton.setText(context.getString(R.string.question_action_continue));
                break;
            case BACKTOCURRENT:
                doitButton.setClickable(true);
                doitButton.setBackground(context.getDrawable(R.drawable.doit_button_backtocurrent));
                doitButton.setText(context.getString(R.string.question_action_backtocurrent));
                break;
        }
    }

    public void checkContains(Button currButton){
        if (!blankSpaceList.contains(currButton))
            currButton.setVisibility(View.VISIBLE);
    }

}
