package com.mtr.codetrip.codetrip.Utility;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mtr.codetrip.codetrip.CostumWidgets.RunButton;
import com.mtr.codetrip.codetrip.CostumWidgets.TextViewDropBlank;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Catrina on 25/02/2018 at 12:16 AM.
 * Within Package: ${PACKAGE_NAME}
 */

public class DropReceiveBlank {

    private RunButton doitButton;
    private List<List<Button>> blankSpaceList;

    public void print(){

        for (int lineNumebr = 0; lineNumebr < blankSpaceList.size(); lineNumebr++){
            List<Button> buttonList = blankSpaceList.get(lineNumebr);
            for (int linePosition = 0; linePosition < buttonList.size(); linePosition++){
                Button b = buttonList.get(linePosition);
                Log.d(String.format("position: %d, %d",lineNumebr,linePosition), b.getText().toString());
            }
        }
    }

    public DropReceiveBlank(RunButton button){
        this.doitButton = button;
//        doitButton.updateDoItButtonState(RunButton.RunButtonState.INVALID);
        blankSpaceList = new ArrayList<>();
    }

    public List<Button> getBlankSpaceListSingleLine(int listIndex){
        return blankSpaceList.get(listIndex);
    }

    public void  restore(TextViewDropBlank currentTextView){
        int lineNumber = currentTextView.lineNumber;
        int linePosition = currentTextView.linePosition;
        Button currButton = blankSpaceList.get(lineNumber).get(linePosition);
        if (currButton!=null) currButton.setVisibility(View.VISIBLE);
        blankSpaceList.get(lineNumber).set(linePosition,null);
        //set textview to default
        currentTextView.updateDropState(TextViewDropBlank.DropState.DEFAULT);

        //update doit button
        for (List<Button> buttonList : blankSpaceList){
            if (buttonList.contains(null)) {
                doitButton.updateDoItButtonState(RunButton.RunButtonState.INVALID);
                return;
            }
        }

    }

    public void addEntry(List<Button> dropBlankSingleLine){

        blankSpaceList.add(dropBlankSingleLine);
    }

    public void updateFillIn(TextViewDropBlank textViewDropBlank, Button newButton){
        int lineNumber = textViewDropBlank.lineNumber;
        int linePosition = textViewDropBlank.linePosition;
        Button oldButton = blankSpaceList.get(lineNumber).get(linePosition);
        if (oldButton!=null) oldButton.setVisibility(View.VISIBLE);
        blankSpaceList.get(lineNumber).set(linePosition,newButton);


        for (List<Button> buttonList: blankSpaceList){
            if (buttonList.contains(null)){
                return;
//            Log.d("++++++CAN","RUN");
            }
        }
        doitButton.updateDoItButtonState(RunButton.RunButtonState.RUN);
    }

    public void checkContains(Button currButton){
        for (List<Button> buttonList : blankSpaceList){
            if (buttonList.contains(currButton)) return;
        }
        currButton.setVisibility(View.VISIBLE);
    }

}
