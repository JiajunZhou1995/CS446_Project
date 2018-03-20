package com.mtr.codetrip.codetrip.Object;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mtr.codetrip.codetrip.CostumWidgets.RunButton;
import com.mtr.codetrip.codetrip.CostumWidgets.TextViewLineNumber;
import com.mtr.codetrip.codetrip.CostumWidgets.TextViewNormalCode;
import com.mtr.codetrip.codetrip.R;
import com.mtr.codetrip.codetrip.Utility.LayoutUtil;

import java.util.ArrayList;
import java.util.List;

import static com.mtr.codetrip.codetrip.Utility.DataBaseUtility.getStrArrayFromDB;

/**
 * Created by Catrina on 24/02/2018 at 11:55 PM.
 * Within Package: ${PACKAGE_NAME}
 */

public class QuestionMultipleChoice extends Question {

    private List<String> codeIns;
    private List<String> choices;
    private int currentSelection;
    private List<FrameLayout> choiceViews;
    private int answer;
    private RunButton doIt;


    QuestionMultipleChoice(ViewGroup viewGroup) {
        super(viewGroup);
        codeIns = new ArrayList<>();
        choices = new ArrayList<>();
        choiceViews = new ArrayList<>();
        doIt = rootView.findViewById(R.id.doit);
    }

    public QuestionMultipleChoice(){
        super();
        codeIns = new ArrayList<>();
        choices = new ArrayList<>();
        choiceViews = new ArrayList<>();
//        doIt = rootView.findViewById(R.id.doit);
    }

    @Override
    public void setRootView(ViewGroup viewGroup){
        super.setRootView(viewGroup);
        doIt = rootView.findViewById(R.id.doit);
//        doIt.setCurrentQuestion(this);
//        doIt.setCurrentQuestionActivity(currentQuestionActivity);
//        doIt.updateDoItButtonState(RunButton.RunButtonState.INVALID);
    }

    @Override
    public void populateFromDB(Cursor c) {
        super.populateFromDB(c);
        codeIns = getStrArrayFromDB(c, "code");
        choices = getStrArrayFromDB(c, "choice");
        answer = c.getInt(c.getColumnIndex("answer"));
    }


    private void updateSelection(int newSelection) {
        int lastSelection = currentSelection;
        currentSelection = newSelection;
        if (lastSelection != currentSelection) {
            TextView tv = choiceViews.get(lastSelection).findViewById(R.id.mc_item_text);
            tv.setTextColor(context.getColor(R.color.colorBlack));
            choiceViews.get(lastSelection).setBackground(context.getDrawable(R.drawable.code_area_round));
        }
    }

    @Override
    protected void inflateContent(ViewGroup rootView) {
        super.inflateContent(rootView);

        LinearLayout questionContent = rootView.findViewById(R.id.question_body);
        LayoutInflater layoutInflater = LayoutInflater.from(rootView.getContext());
        @SuppressLint("InflateParams") View multiple_choice = layoutInflater.inflate(R.layout.question_multiple_choice, null);
        questionContent.addView(multiple_choice);

        LinearLayout codeAreaLinearLayout = questionContent.findViewById(R.id.question_code_area);
        LinearLayout singleLine;
        int lineIndex = 1;
        if (codeIns.size() == 0) {
            View code_area = rootView.findViewById(R.id.question_code_area);
            code_area.setVisibility(View.GONE);
        }
        for (String codeLine : codeIns) {
            singleLine = new LinearLayout(context);
            singleLine.setOrientation(LinearLayout.HORIZONTAL);
            LayoutUtil.setup(context, LayoutUtil.LayoutType.LINEAR, singleLine, LayoutUtil.ParamType.MATCH_PARENT, LayoutUtil.ParamType.WRAP_CONTENT, 0, 0, 0, 0);


            TextViewLineNumber textViewLineNumber = new TextViewLineNumber(context, Integer.toString(lineIndex) + ".");
            LayoutUtil.setup(context, LayoutUtil.LayoutType.LINEAR, textViewLineNumber, LayoutUtil.ParamType.WRAP_CONTENT, LayoutUtil.ParamType.WRAP_CONTENT, 15, 0, 5, 0);
            singleLine.addView(textViewLineNumber);

            TextViewNormalCode normalTextView = new TextViewNormalCode(context, codeLine);
            LayoutUtil.setup(context, LayoutUtil.LayoutType.LINEAR, normalTextView, LayoutUtil.ParamType.WRAP_CONTENT, LayoutUtil.ParamType.WRAP_CONTENT, 0, 0, 0, 0);
            singleLine.addView(normalTextView);

            codeAreaLinearLayout.addView(singleLine);
            lineIndex++;
        }
        for (String choice : choices) {
            LinearLayout choiceArea = rootView.findViewById(R.id.question_choice_area);
            @SuppressLint("InflateParams") FrameLayout choiceView = (FrameLayout) layoutInflater.inflate(R.layout.mc_item_layout, null);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 10, 5, 10);
            choiceView.setLayoutParams(params);
            TextView choiceTV = choiceView.findViewById(R.id.mc_item_text);
            choiceTV.setText(choice);

            final int choiceIndex = choices.indexOf(choice);
            choiceView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateSelection(choiceIndex);
                    TextView tv = v.findViewById(R.id.mc_item_text);
                    tv.setTextColor(context.getColor(R.color.colorWhite));
                    v.setBackground(context.getDrawable(R.drawable.code_area_round_highlight));
                    doIt.updateDoItButtonState(RunButton.RunButtonState.RUN);
                    String newText = "Check";
                    doIt.setText(newText);
                }
            });
            choiceViews.add(choiceView);
            choiceArea.addView(choiceView);
        }
//        Button doIt = rootView.findViewById(R.id.doit);
//        doIt.setClickable(true);
//        doIt.setBackground(context.getDrawable(R.drawable.doit_button_invalid));
//        doIt.setText(context.getString(R.string.question_action_run));
//        doIt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (currentSelection + 1 == answer){ //off by 1
//                    status = RUN_BUTTON_STATUS.CONTINUE;
//                    TextView tv = choiceViews.get(currentSelection).findViewById(R.id.mc_item_text);
//                    updateButton();
//                }
//            }
//        });
    }


//    private void updateButton(){
//        Button doIt = rootView.findViewById(R.id.doit);
//        if (status == RUN_BUTTON_STATUS.CONTINUE){
//            doIt.setClickable(true);
//            doIt.setBackground(context.getDrawable(R.drawable.doit_button_continue));
//            doIt.setText(context.getString(R.string.question_action_continue));
//        }
//        else if (status == RUN_BUTTON_STATUS.RUN){
//            doIt.setClickable(true);
//            doIt.setBackground(context.getDrawable(R.drawable.run_button_run));
//            doIt.setText(context.getString(R.string.question_action_check));
//        }
//
//        else{
//            doIt.setClickable(true);
//            doIt.setBackground(context.getDrawable(R.drawable.doit_button_continue));
//            doIt.setText(context.getString(R.string.question_action_continue));
//        }
//    }

//    @Override
//    public void runAction() {
//        if (currentSelection + 1 == answer) { //off by 1
//            doIt.updateDoItButtonState(RunButton.RunButtonState.CONTINUE);
////            TextView tv = choiceViews.get(currentSelection).findViewById(R.id.mc_item_text);
////            updateButton();
//        }
//    }

    @Override
    public void runAction() {
        if (currentSelection + 1 == answer) { //off by 1
//            TextView tv = choiceViews.get(currentSelection).findViewById(R.id.mc_item_text);
//            updateButton();
            Log.d("correct!!","increase score");
            increaseGrade();
            currentQuestionActivity.questionPicker.ganerateNextQuestion(true);
        }else{
            Log.d("incorrect answer","add to incorrect list");
//            QuestionActivity.addToIncorrectList(questionID);
            currentQuestionActivity.questionPicker.ganerateNextQuestion(false);

        }

        Question newQuestion = currentQuestionActivity.questionPicker.getCurrentQuestion();
        if (newQuestion!=null){
            currentQuestionActivity.NUM_PAGES++;
            currentQuestionActivity.notifyChange();
        }
        doIt.updateDoItButtonState(RunButton.RunButtonState.CONTINUE);
    }
}
