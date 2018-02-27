package com.mtr.codetrip.codetrip.Object;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mtr.codetrip.codetrip.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by j66zhu on 2018-02-23.
 */

public abstract class Question {
    protected Context context;
    protected ViewGroup rootView;
    protected String knowledge;
    protected String instruction;
    protected String hint;
    protected RUN_BUTTON_STATUS status;


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
        List<String> strings = new ArrayList<>();
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
        TextView knowledgeTV = rootView.findViewById(R.id.question_knowledge);
        TextView instructionTV = rootView.findViewById(R.id.question_instruction);

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

    public String getHint(){
        return hint;
    }



    protected String prependArrow(String output){
        String[] lines = output.split("\n");
        String[] consoleOutput = new String[lines.length];
        int i = 0;
        for (String l : lines){
            consoleOutput[i++] =  "> " + l;
        }
        StringBuilder builder = new StringBuilder();

        for(int index = 0; index < consoleOutput.length; index++){
            String s = consoleOutput[index];
            builder.append(s);
            if (index < consoleOutput.length -1)builder.append("\n");
        }
        String str = builder.toString();

        return str;
    }


    public void runAction(){

    }

//    protected void setUpView(View view,int width,int height, int marginLeft, int marginTop, int marginRight, int marginBottom){
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
//                (width==0)?ViewGroup.LayoutParams.MATCH_PARENT:ViewGroup.LayoutParams.WRAP_CONTENT,
//                (height==0)?ViewGroup.LayoutParams.MATCH_PARENT:ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.setMargins(DensityUtil.dip2px(context,marginLeft),
//                DensityUtil.dip2px(context,marginTop),
//                DensityUtil.dip2px(context,marginRight),
//                DensityUtil.dip2px(context,marginBottom));
//        view.setLayoutParams(layoutParams);
//    }


}