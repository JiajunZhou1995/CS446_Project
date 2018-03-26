package com.mtr.codetrip.codetrip.Object;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mtr.codetrip.codetrip.CostumWidgets.RunButton;
import com.mtr.codetrip.codetrip.CourseActivity;
import com.mtr.codetrip.codetrip.QuestionActivity;
import com.mtr.codetrip.codetrip.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


/**
 * Created by j66zhu on 2018-02-23 at 10:16 PM at 10:16 PM at 10:16 PM.
 * Within Package ${PACKAGE_NAME}
 */

public abstract class Question extends Observable{
    protected Context context;
    ViewGroup rootView;
    public int questionID;
    private String knowledge;
    private String instruction;
    private String hint;
    private String topic;
    private String difficulty;
    protected QuestionActivity currentQuestionActivity;
    protected String answer;
    final String nullAnswer = "No Answer";
    final String errorString = "Error";
    public RunButton runButton;

//    protected RunButton doIt;
//
//    public void setUpRunButton(){
//        RunButton runButton = rootView.findViewById(R.id.doit);
//        runButton.setCurrentQuestion(this);
//        runButton.setCurrentQuestionActivity(currentQuestionActivity);
//        doIt = runButton;
//    }


    public Question(ViewGroup view){
        this.rootView = view;
        context = rootView.getContext();
//        runButton = rootView.findViewById(R.id.doit);
    }

    public Question(){

    }

    public void setRootView(ViewGroup rootView){
        this.rootView = rootView;
        context = rootView.getContext();
        runButton = rootView.findViewById(R.id.doit);
    }

    void setCurrentQuestionActivity(QuestionActivity questionActivity){
        this.currentQuestionActivity = questionActivity;
    }

    public void populateFromDB(Cursor c){
        //null check
        questionID = c.getInt(c.getColumnIndex("questionid"));
        knowledge = c.getString(c.getColumnIndex("knowledge"));
        instruction = c.getString(c.getColumnIndex("instruction"));
        hint = c.getString(c.getColumnIndex("hint"));
        topic = c.getString(c.getColumnIndex("topic"));
        difficulty = c.getString(c.getColumnIndex("difficulty"));
        answer = c.getString(c.getColumnIndex("answer"));
    }

//    List<String> getStrArrayFromDB(Cursor c, String columnName){
//        List<String> strings = new ArrayList<>();
//        try{
//            JSONArray jsArr = new JSONArray(c.getString(c.getColumnIndex(columnName)));
//            for(int i = 0; i < jsArr.length(); i++){
//                strings.add(jsArr.getString(i));
//            }
//        }
//        catch (JSONException e){
//            e.printStackTrace();
//        }
//        return strings;
//    }

    protected void inflateContent(ViewGroup rootView){
        TextView knowledgeTV = rootView.findViewById(R.id.question_knowledge);
        TextView instructionTV = rootView.findViewById(R.id.question_instruction);

        TextView questionIDTV = rootView.findViewById(R.id.question_id);
        TextView topicTV = rootView.findViewById(R.id.quesiton_topic);
        TextView difficultyTV = rootView.findViewById(R.id.question_difficulty);
        questionIDTV.setText(String.format("Question Id: %d",questionID));
        topicTV.setText(String.format("Topic: %s",topic));
        difficultyTV.setText(String.format("Level: %s",difficulty));
        switch (difficulty) {
            case "Easy":
                difficultyTV.setTextColor(context.getColor(R.color.colorOrange));
                break;
            case "Medium":
                difficultyTV.setTextColor(context.getColor(R.color.colorBluePurple));
                break;
            default:
                difficultyTV.setTextColor(context.getColor(R.color.colorFireBrick));
                break;
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
        if (answer.equals(nullAnswer)){
            //arbitrary answer , check if error
            Log.d("answer","is empty");
            String[] outputStrings = output.split("(\\()");
            if (outputStrings[0].length() >= 5){
                String checkError = outputStrings[0].substring(outputStrings[0].length()-5);
                if (checkError.equals(errorString)){
                    Log.d("incorrect answer","add to incorrect list");
//                    notifyObservers(outputStrings[0]);
                    QuestionActivity.incorrectQuestionList.add(questionID);
                    notifyObservers(false);
                    currentQuestionActivity.generateNextQuestion(false);

//                    currentQuestionActivity.questionPicker.ganerateNextQuestion(false);
                }else{
                    Log.d("correct!!","increase score");
                    increaseGrade();
                    notifyObservers(true);
                    currentQuestionActivity.generateNextQuestion(true);

//                    currentQuestionActivity.questionPicker.ganerateNextQuestion(true);
                }
            }else{
                Log.d("correct!!","increase score");
                increaseGrade();
                notifyObservers(true);
//                currentQuestionActivity.questionPicker.ganerateNextQuestion(true);
                currentQuestionActivity.generateNextQuestion(true);

            }


        }else if(answer.equals(output)){
            Log.d("correct!!","increase score");
            Log.i("answer",answer);
            Log.i("output", output);
            increaseGrade();
            notifyObservers(true);
            currentQuestionActivity.generateNextQuestion(true);

//            currentQuestionActivity.questionPicker.ganerateNextQuestion(true);
        }else{
            Log.d("incorrect answer","add to incorrect list");
            Log.i("answer",answer);
            Log.i("output", output);
//            QuestionActivity.addToIncorrectList(questionID);
            QuestionActivity.incorrectQuestionList.add(questionID);
            notifyObservers(false);
            currentQuestionActivity.generateNextQuestion(false);
//            currentQuestionActivity.questionPicker.ganerateNextQuestion(false);
        }

//        Question newQuestion = currentQuestionActivity.questionPicker.getCurrentQuestion();
//        if (newQuestion!=null){
//            currentQuestionActivity.NUM_PAGES++;
//            currentQuestionActivity.notifyChange();
//        }
    }

    public void runAction(){
        setChanged();
    }

}
