package com.mtr.codetrip.codetrip.Object;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mtr.codetrip.codetrip.R;
import com.mtr.codetrip.codetrip.Utility.AsyncResponse;
import com.mtr.codetrip.codetrip.CostumWidgets.EditTextInsert;
import com.mtr.codetrip.codetrip.Utility.HttpPostAsyncTask;
import com.mtr.codetrip.codetrip.Utility.LayoutUtil;
import com.mtr.codetrip.codetrip.CostumWidgets.RunButton;
import com.mtr.codetrip.codetrip.CostumWidgets.TextViewLineNumber;
import com.mtr.codetrip.codetrip.CostumWidgets.TextViewNormalCode;

import java.util.ArrayList;
import java.util.List;

import static com.mtr.codetrip.codetrip.Utility.DataBaseUtility.getArrayFromDB;

/**
 * Created by Catrina on 24/02/2018 at 11:58 PM.
 * Within Package: ${PACKAGE_NAME}
 */

public class QuestionShortAnswer extends Question implements AsyncResponse {
    private List<String> codeArea;
    private List<List<TextView>> normalCode;
    private List<List<EditTextInsert>> editTextInsertList;
    private QuestionShortAnswer thisQuestionView;
    private String codeString;
    private RunButton runButton;





    QuestionShortAnswer(ViewGroup viewGroup){
        super(viewGroup);
        thisQuestionView = this;
        codeString = "";
        normalCode = new ArrayList<>();
        editTextInsertList = new ArrayList<>();
        runButton = rootView.findViewById(R.id.doit);
//        DropReceiveBlank receiveBlank = new DropReceiveBlank(context, runButton);

    }

    @Override
    protected void populateFromDB(Cursor c){
        super.populateFromDB(c);
        codeArea =  getArrayFromDB(c, "code");
    }
    @Override
    protected void inflateContent(ViewGroup rootView){
        super.inflateContent(rootView);
        LinearLayout questionBody = rootView.findViewById(R.id.question_body);
        LayoutInflater layoutInflater = LayoutInflater.from(rootView.getContext());
        @SuppressLint("InflateParams") View shortAnswerContent = layoutInflater.inflate(R.layout.question_short_answer,null);
        questionBody.addView(shortAnswerContent);

//        TextView console = rootView.findViewById(R.id.console);

        inflateCodeArea(shortAnswerContent);
    }


    private void inflateCodeArea(View shortAnswerContent){
        Context currentContext = shortAnswerContent.getContext();
//        LayoutInflater layoutInflater = LayoutInflater.from(currentContext);

        LinearLayout codeAreaLinearLayout = shortAnswerContent.findViewById(R.id.question_code_area);
        LinearLayout singleLine;
        List<TextView>normalCodeSingleLine;
        List<EditTextInsert>editTextInsertSingleLine;
        int lineIndex = 1;
        for (String codeLine : codeArea){
            normalCodeSingleLine = new ArrayList<>();
            editTextInsertSingleLine = new ArrayList<>();
            String[] code = codeLine.split("(\\[\\?])");
            singleLine = new LinearLayout(currentContext);
            singleLine.setOrientation(LinearLayout.HORIZONTAL);
            LayoutUtil.setup(currentContext, LayoutUtil.LayoutType.LINEAR,singleLine, LayoutUtil.ParamType.MATCH_PARENT, LayoutUtil.ParamType.WRAP_CONTENT,0,0,0,0);


            TextViewLineNumber textViewLineNumber = new TextViewLineNumber(currentContext,Integer.toString(lineIndex)+".");
            LayoutUtil.setup(currentContext, LayoutUtil.LayoutType.LINEAR, textViewLineNumber, LayoutUtil.ParamType.WRAP_CONTENT, LayoutUtil.ParamType.WRAP_CONTENT,15,0,5,0);
            singleLine.addView(textViewLineNumber);


            int index = 0;
            for (String s :code){
                TextViewNormalCode normalTextView = new TextViewNormalCode(currentContext, s);
                LayoutUtil.setup(currentContext, LayoutUtil.LayoutType.LINEAR, normalTextView, LayoutUtil.ParamType.WRAP_CONTENT, LayoutUtil.ParamType.WRAP_CONTENT,0,0,0,0);
                singleLine.addView(normalTextView);
                normalCodeSingleLine.add(normalTextView);

                if (index++ < code.length -1){
                    EditTextInsert editTextInsert = new EditTextInsert(context,this);
                    LayoutUtil.setup(currentContext, LayoutUtil.LayoutType.LINEAR, editTextInsert, LayoutUtil.ParamType.WRAP_CONTENT, LayoutUtil.ParamType.WRAP_CONTENT,5,0,5,0);
                    singleLine.addView(editTextInsert);
                    editTextInsertSingleLine.add(editTextInsert);
                }
            }
            editTextInsertList.add(editTextInsertSingleLine);
            normalCode.add(normalCodeSingleLine);
            codeAreaLinearLayout.addView(singleLine);
            lineIndex++;
        }
    }

    public void checkEditInsertList(){
        for (List<EditTextInsert> editTextInsertList : editTextInsertList){
            for (EditTextInsert editTextInsert : editTextInsertList){
                if (editTextInsert==null || editTextInsert.getText().toString().equals("")) {
                    runButton.updateDoItButtonState(RunButton.RunButtonState.INVALID);
                    return;
                }

            }
        }
        runButton.updateDoItButtonState(RunButton.RunButtonState.RUN);
    }

    @Override
    public void processFinish(String output) {
        Log.d("out put",output);
        updateConsole(output);
    }

    private void updateConsole(String output){
        TextView consoleTV = rootView.findViewById(R.id.console);
        output = prependArrow(output);
        consoleTV.setText(output);
    }

    @Override
    public void runAction(){
        for (List<EditTextInsert> editTextInserts: editTextInsertList){
            for (EditTextInsert editTextInsert : editTextInserts){
                editTextInsert.setClickable(false);
            }
        }

        for (int listIndex = 0; listIndex < normalCode.size(); listIndex++){
            int index = 0;
            List<TextView> normalCodeLine = normalCode.get(listIndex);
            List<EditTextInsert> editTextInserts = editTextInsertList.get(listIndex);
            for (TextView tv : normalCodeLine){
                codeString = codeString + tv.getText();
                if (index<editTextInserts.size())
                    codeString = codeString + editTextInserts.get(index).getText();
                index++;
            }
            codeString = String.format("%s\n", codeString);
        }

//                        console.setText(codeString);

        HttpPostAsyncTask request = new HttpPostAsyncTask(codeString);
        request.delegate = thisQuestionView;
        request.execute();
    }
}
