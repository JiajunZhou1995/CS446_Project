package com.mtr.codetrip.codetrip;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by j66zhu on 2018-02-23.
 */

public class Question {
    protected Context context;
    protected ViewGroup rootView;
    protected String knowledge;
    protected String instruction;
    protected String hint;
    protected String answer;


    protected enum RUN_BUTTON_STATUS{FILL_IN_THE_BLANK, RUN, CONTINUE, BACK_TO_CURRENT};


    public Question(ViewGroup view){
        rootView = view;
        context = rootView.getContext();
    }

    protected void populateFromDB(Cursor c){
        //null check
        knowledge = c.getString(c.getColumnIndex("knowledge"));
        instruction = c.getString(c.getColumnIndex("instruction"));
        hint = c.getString(c.getColumnIndex("hint"));
    }

    protected List<String> getArrayFromDB(Cursor c, String columnName){
        List<String> strings = new ArrayList<String>();
        try{
            JSONArray jsArr = new JSONArray(c.getString(c.getColumnIndex(columnName)));
            for(int i = 0; i < jsArr.length(); i++){
                strings.add(jsArr.getString(i));
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return strings;
    }

    protected void inflateContent(ViewGroup rootView){
        TextView knowledgeTV = (TextView)rootView.findViewById(R.id.question_knowledge);
        TextView instructionTV = (TextView)rootView.findViewById(R.id.question_instruction);

        if (!knowledge.equals("null")){
            knowledgeTV.setText(knowledge);
        }
        else {
            knowledgeTV.setVisibility(View.GONE);
        }

        if (!instruction.equals("null")){
            instructionTV.setText(instruction);
        }
        else {
            instructionTV.setVisibility(View.GONE);
        }
    }


}
