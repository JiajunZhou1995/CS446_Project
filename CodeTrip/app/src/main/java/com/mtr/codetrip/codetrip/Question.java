package com.mtr.codetrip.codetrip;

import android.database.Cursor;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by j66zhu on 2018-02-23.
 */

public class Question {

    private View rootView;
    private String knowledge;
    private String instruction;
    private String hint;
    private List<String> codeInstructions;
    private List<String> codeBlocks;
    private List<String> choices;

    private enum RUN_BUTTON_STATUS{FILL_IN_THE_BLANK, RUN, CONTINUE, BACK_TO_CURRENT};


    Question(View view){
        rootView = view;
    }

    public void populateFromDB(Cursor c){
        try{
            knowledge = c.getString(c.getColumnIndex("knowledge"));
            instruction = c.getString(c.getColumnIndex("instruction"));
            hint = c.getString(c.getColumnIndex("hint"));


            codeInstructions =  getArrayFromDB(c, "code");
            codeBlocks = getArrayFromDB(c, "codeblocks");
            choices = getArrayFromDB(c, "choice");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

    private List<String> getArrayFromDB(Cursor c, String columnName) throws JSONException{
        List<String> strings = new ArrayList<String>();
        JSONArray jsArr = new JSONArray(c.getString(c.getColumnIndex(columnName)));
        for(int i = 0; i < jsArr.length(); i++){
            strings.add(jsArr.getString(i));
        }
        return strings;
    }



}
