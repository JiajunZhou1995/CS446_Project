package com.mtr.codetrip.codetrip.Object;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mtr.codetrip.codetrip.QuestionActivity;
import com.mtr.codetrip.codetrip.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by j66zhu on 2018-02-23 at 10:16 PM at 10:16 PM at 10:16 PM.
 * Within Package ${PACKAGE_NAME}
 */

public abstract class Question {
    protected Context context;
    ViewGroup rootView;
    private String knowledge;
    private String instruction;
    private String hint;
    private String topic;
    private String difficulty;
    private QuestionActivity currentQuestionActivity;
    protected String answer;



    public Question(ViewGroup view){
        rootView = view;
        context = rootView.getContext();
    }

    void setCurrentQuestionActivity(QuestionActivity questionActivity){
        this.currentQuestionActivity = questionActivity;
    }

    protected void populateFromDB(Cursor c){
        //null check

        knowledge = c.getString(c.getColumnIndex("knowledge"));
        instruction = c.getString(c.getColumnIndex("instruction"));
        hint = c.getString(c.getColumnIndex("hint"));
        topic = c.getString(c.getColumnIndex("topic"));
        difficulty = c.getString(c.getColumnIndex("difficulty"));
        answer = c.getString(c.getColumnIndex("answer"));
    }

    List<String> getArrayFromDB(Cursor c, String columnName){
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

        TextView topicTV = rootView.findViewById(R.id.quesiton_topic);
        TextView difficultyTV = rootView.findViewById(R.id.question_difficulty);
        topicTV.setText(String.format("Topic: %s",topic));
        difficultyTV.setText(String.format("Level: %s",difficulty));
        if (difficulty.equals("Easy")){
            difficultyTV.setTextColor(context.getColor(R.color.colorOrange));
        }else if (difficulty.equals("Medium")){
            difficultyTV.setTextColor(context.getColor(R.color.colorBluePurple));
        }else{
            difficultyTV.setTextColor(context.getColor(R.color.colorFireBrick));
        }

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



    String prependArrow(String output){
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

        return builder.toString();
    }

    void increaseGrade(){
        currentQuestionActivity.changeGrade();
    }

    protected void checkAnswer(String output){

    }

    public void runAction(){

    }

}
